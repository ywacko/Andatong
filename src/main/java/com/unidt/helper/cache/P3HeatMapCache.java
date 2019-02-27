package com.unidt.helper.cache;

import com.mongodb.client.MongoCollection;
import com.unidt.exception.TimeShortException;
import org.bson.Document;

public class P3HeatMapCache extends AbstractCache{
    public P3HeatMapCache (int maxcount, int timeout) throws TimeShortException {
        super(maxcount, timeout);
        startRefreshTimer(300,300);
    }

    @Override
    public void refresh() throws Exception {
        //
        // 获取热力数据，因为数据总量大，该查询相对耗时
        // mall -> bi_name -> venue -> floor
        // 热力图只保留当天，故不需要传日期参数
        MongoCollection collection = getDb().setDbname("andatong").collection("BI");
        String[] venues = {"AB","C"};
        String[] floors = {"B1","1F","2F","3F","4F","5F","6F","7F","8F","9F"};
        for(String venue: venues) {
            for (String floor: floors) {
                //
                // 热力图
                Document filter = new Document("mall_id","sh001").append("bi_name","heat_map")
                        .append("venue",venue).append("bi_value.floor",floor);
                addCacheTask(filter.toJson());
                //
                // 楼层客流
                filter = new Document("mall_id","sh001").append("bi_name","floor_day_flow")
                        .append("venue",venue).append("bi_value.floor",floor);
                addCacheTask(filter.toJson());
            }
        }

    }
}
