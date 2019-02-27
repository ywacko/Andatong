package com.unidt.helper.cache;

import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.unidt.exception.TimeShortException;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

/**
 * 月度客流部分指标缓存
 */
public class P2MonthFlowCache extends AbstractCache {

    public P2MonthFlowCache (int maxcount, int timeout) throws TimeShortException {
        super(maxcount, timeout);
        startRefreshTimer(3600,3600);
    }

    @Override
    public void refresh() throws Exception {
        //
        // 先得到数据库中的年份
        MongoCollection collection = getDb().setDbname("andatong").collection("BI");
        DistinctIterable<Integer> allYears = collection.distinct("dt_year",
                eq("bi_name","mall_static"),Integer.class);
        MongoCursor<Integer> yearCursor = allYears.iterator();

        List<Integer> years = new ArrayList<>();
        while (yearCursor.hasNext()) {
            int year = yearCursor.next();
            years.add(year);
        }

        for (int year : years) {
            //
            // 简单处理，月份从1 ~ 12
            for( int month = 1; month <= 12; month++) {
                Document filters = new Document("bi_name","mall_static").append("dt_year", year).append("dt_month",month);
                getLog().info("Cache :" + year + "-" + month);
                addCacheTask(filters.toJson());
            }
        }

    }

}
