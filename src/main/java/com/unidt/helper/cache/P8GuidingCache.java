package com.unidt.helper.cache;

import com.mongodb.client.DistinctIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.unidt.exception.TimeShortException;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.mongodb.client.model.Filters.eq;


public class P8GuidingCache extends AbstractCache {

    public P8GuidingCache(int maxcount, int timeout) throws TimeShortException {
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

        //得到所有商店名
        collection = getDb().setDbname("andatong").collection("shop2");
        FindIterable<Document> shopName = collection.find();
        MongoCursor<Document> shopCursor = shopName.iterator();

        Document filters = null;

        while (shopCursor.hasNext()) {
            Document document = shopCursor.next();
            String venue = (String) document.get("venue");
            String name = (String) document.get("name");

            for (int year : years) {
                //月份就按1到12月，不作处理
                for (int month = 1; month <= 12; month++) {
                    filters = new Document("bi_name", "brand_link").append("dt_year", year)
                            .append("dt_month", month).append("venue", venue).append("bi_value.from_name", name);

                    getLog().info("Cache " + year + "/" + month + " " + name);
                    addCacheTask(filters.toJson());

                }
            }
        }
    }

}
