package com.unidt.helper.cache;

import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.unidt.exception.TimeShortException;
import com.unidt.helper.common.DateHelper;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class P4YetaiCache extends AbstractCache {


    public P4YetaiCache(int maxcount, int timeout) throws TimeShortException {
        super(maxcount, timeout);
        startRefreshTimer(300,300);
    }

    /**
     * 缓存业态客流份额数据
     * @throws Exception
     */
    @Override
    public void refresh() throws Exception {


        //
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

        for (int year:years) {
            // 固定12个月份
            for (int month = 1; month <= 12; month++) {
                Document filters =  new Document("mall_id","sh001").append("bi_name","get_yetai_month_10").append("dt_year", year).append("dt_month",month);
                addCacheTask(filters.toJson());
            }
        }
        //
        // 缓存实时业态份额
        String date = DateHelper.getDate();
        Document filter = new Document("mall_id","sh001").append("bi_name","day_catalog").append("p_dt", date);
        addCacheTask(filter.toJson());
    }
}
