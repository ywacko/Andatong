package com.unidt.helper.cache;

import com.mongodb.BasicDBObject;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.unidt.exception.TimeShortException;
import com.unidt.helper.common.DateHelper;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.mongodb.client.model.Filters.eq;


public class P7BrandFlowRankCache extends AbstractCache {

    public P7BrandFlowRankCache(int maxcount, int timeout) throws TimeShortException {
        super(maxcount, timeout);
        startRefreshTimer(3600, 3600);
    }

    @Override
    public void refresh() throws ExecutionException {

        String[] venues = {"", "AB", "C"};
        String[] locnames = {"", "B1","1F","2F","3F","4F","5F","6F","7F","8F","9F"};

        // 先得到数据库中的年份
        MongoCollection collection = getDb().setDbname("andatong").collection("BI");
        DistinctIterable<Integer> allYears = collection.distinct("dt_year",
                eq("bi_name","get_yetai_month_10"),Integer.class);
        MongoCursor<Integer> yearCursor = allYears.iterator();

        List<Integer> years = new ArrayList<>();
        while (yearCursor.hasNext()) {
            int year = yearCursor.next();
            years.add(year);
        }

        int year = DateHelper.getYear();
        int month = DateHelper.getMonth();
        int day = DateHelper.getDay();

        collection = getDb().setDbname("andatong").collection("shop2");

        for (String venue : venues) {
            for (String locname : locnames) {

                // 两个filters 是将要进入缓存的查询条件， query是为了查出当前场馆、楼层的店铺类型的查询条件
                Document realTimeFilters = new Document("bi_name", "get_pingpai_20").append("dt_year", year)
                        .append("dt_month", month).append("dt_day", day);
                Document monthFilters = new Document("bi_name", "get_name_month_20");
                Bson query = new BasicDBObject();

                // 场馆和楼层为全部或为指定，全部则为空
                if (!StringUtils.isEmpty(venue)) {
                    realTimeFilters.append("venue", venue);
                    monthFilters.append("venue", venue);
                    ((Document) query).append("venue", venue);
                }
                if (!StringUtils.isEmpty(locname)) {
                    realTimeFilters.append("locname", locname);
                    monthFilters.append("locname", locname);
                    ((BasicDBObject) query).append("floor", locname);
                }

                // 当商店类型为全部业态时进行一次缓存
                // 实时TOP20
                getLog().info("Date: " + year + "/" + month + "/" + day);
                addCacheTask(realTimeFilters.toJson());

                // 每月TOP20
                for (int y : years) {
                    monthFilters.append("dt_year", y);
                    for (int m = 1; m <= 12; m++) {
                        monthFilters.append("dt_month", m);

                        getLog().info("Date: " + y + "/" + m);
                        addCacheTask(monthFilters.toJson());

                        monthFilters.remove("dt_month");
                    }
                    monthFilters.remove("dt_year");
                }

                //得到该场馆楼层的所有店铺类型
                DistinctIterable<String> typeList = collection.distinct("type", query, String.class);
                MongoCursor<String> typeCursor = typeList.iterator();

                // 再根据类型进行缓存
                while (typeCursor.hasNext()) {
                    String type = typeCursor.next();

                    realTimeFilters.append("type", type);
                    monthFilters.append("type", type);

                    // 实时TOP20
                    getLog().info("Date: " + year + "/" + month + "/" + day + " type: " + type);
                    addCacheTask(realTimeFilters.toJson());

                    // 每月TOP20
                    for (int y : years) {
                        monthFilters.append("dt_year", y);
                        for (int m = 1; m <= 12; m++) {
                            monthFilters.append("dt_month", m);

                            getLog().info("Date: " + y + "/" + m + " type: " + type);
                            addCacheTask(monthFilters.toJson());

                            monthFilters.remove("dt_month");
                        }
                        monthFilters.remove("dt_year");
                    }

                    realTimeFilters.remove("type");
                    monthFilters.remove("type");
                }
            }
        }
    }

}
