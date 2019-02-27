package com.unidt.helper.cache;

import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.unidt.exception.TimeShortException;
import com.unidt.helper.common.DateHelper;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.mongodb.client.model.Filters.eq;

public class P5ShopTypeFlowCache extends AbstractCache {

    public P5ShopTypeFlowCache(int maxcount, int timeout) throws TimeShortException {
        super(maxcount, timeout);
        startRefreshTimer(3600, 3600);
    }

    @Override
    public void refresh() throws ExecutionException {

//        YearCache yearCache = YearCache.getInstance();
//        List<Integer> years = (List<Integer>) Document.parse(yearCache.get("findYears")).get("data");

        // 得到数据库中所有年份
        MongoCollection collection = getDb().setDbname("andatong").collection("BI");
        DistinctIterable<Integer> allYears = collection.distinct("dt_year",
                eq("bi_name","get_yetai_month_10"),Integer.class);
        MongoCursor<Integer> yearCursor = allYears.iterator();

        List<Integer> years = new ArrayList<>();
        while (yearCursor.hasNext()) {
            int year = yearCursor.next();
            years.add(year);
        }

        for (int year : years) {

            //月份就按1到12月，不作处理
            for( int month = 1; month <= 12; month++) {
                Document filters = new Document("bi_name","get_leixing_month")
                        .append("dt_year", year).append("dt_month",month);
                getLog().info("Cache :" + year + "-" + month);
                addCacheTask(filters.toJson());
            }
        }
    }

}
