package com.unidt.helper.cache;

import com.mongodb.BasicDBObject;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.unidt.exception.TimeShortException;
import com.unidt.helper.common.DateHelper;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.mongodb.client.model.Filters.eq;


public class P9BrandYetaiCache extends AbstractCache {

    public P9BrandYetaiCache(int maxcount, int timeout) throws TimeShortException {
        super(maxcount, timeout);
        startRefreshTimer(3600, 3600);
    }

    @Override
    public void refresh() throws ExecutionException {

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

        // 实时时间
        int year = DateHelper.getYear();
        int month = DateHelper.getMonth();
        int day = DateHelper.getDay();
        String[] venues = {"AB", "C"};

        collection = getDb().setDbname("andatong").collection("BI");

        for (String venue : venues) {

            // 查出该场馆的全部业态
            Bson query = new BasicDBObject("bi_name", "brand_shop_prop_month").append("venue", venue);
            DistinctIterable<String> allTypes = collection.distinct("type", query, String.class);
            MongoCursor<String> typeCursor = allTypes.iterator();

            while (typeCursor.hasNext()) {
                String type = typeCursor.next();

                // 实时的业态份额
                Document realTimeFilters = new Document("bi_name", "brand_shop_prop").append("venue", venue)
                        .append("type", type).append("dt_year", year).append("dt_month", month).append("dt_day", day);
                getLog().info("Cache: " + year + "/" + month + "/" + day + " " + venue + " " + type);
                addCacheTask(realTimeFilters.toJson());

                // 每月的业态份额
                for (int y : years) {
                    for (int m = 1; m <= 12; m++) {
                        Document monthFilters = new Document("bi_name", "brand_shop_prop_month")
                                .append("venue", venue).append("type", type).append("dt_year", y).append("dt_month", m);
                        getLog().info("Cache: " + y + "/" + m + " " + venue + " " + type);
                        addCacheTask(realTimeFilters.toJson());
                    }
                }
            }
        }
    }

}
