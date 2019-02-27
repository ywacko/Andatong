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


public class P6BrandFlowCache extends AbstractCache {

    public P6BrandFlowCache(int maxcount, int timeout) throws TimeShortException {
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
                for( int month = 1; month <= 12; month++) {

                    //整月的总客流
                    filters = new Document("bi_name", "get_month_total_name").append("dt_year", year)
                            .append("dt_month",month).append("venue", venue).append("name", name);
                    getLog().info("Cache :" + year + "-" + month + "-" + name);
                    addCacheTask(filters.toJson());

                    //整月分天的客流情况和天气情况
                    filters = new Document("bi_name", "mall_brand_static").append("dt_year", year)
                            .append("dt_month",month).append("venue", venue).append("name", name);
                    getLog().info("Cache :" + year + "-" + month + "-" + name);
                    addCacheTask(filters.toJson());

                }
            }

            //实时客流
            filters = new Document("bi_name", "today_flow_name").append("venue", venue).append("name", name);
            getLog().info("Cache - " + name);
            addCacheTask(filters.toJson());
        }
    }

}
