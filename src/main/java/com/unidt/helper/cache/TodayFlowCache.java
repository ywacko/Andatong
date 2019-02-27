package com.unidt.helper.cache;

import com.unidt.exception.TimeShortException;
import com.unidt.helper.cache.interf.ICache;
import com.unidt.helper.common.DateHelper;
import org.bson.Document;


/**
 * 针对的bi_name
 * today_flow
 * today_total
 * floor_day_flow
 */
public class TodayFlowCache extends AbstractCache {

    public TodayFlowCache (int maxcount, int timeout) throws TimeShortException {
        super(maxcount,timeout);
        startRefreshTimer(120,120);
    }

    /**
     * 全部刷新缓存,构造顺序:
     * bi_name -> mall_id -> p_dt
     * @throws Exception
     */
    @Override
    public void refresh() throws Exception {

        long start = System.currentTimeMillis();
        //today_total
        String p_dt = DateHelper.getDate();
        Document filter = new Document("bi_name","today_total").append("mall_id","sh001").append("p_dt",p_dt);

        addCacheTask(filter.toJson());
        //today_flow
        filter = new Document("bi_name","today_flow").append("mall_id","sh001").append("p_dt",p_dt);

        addCacheTask(filter.toJson());
        //floor_day_flow
        filter = new Document("bi_name","floor_day_flow").append("mall_id","sh001").append("p_dt", p_dt);

        addCacheTask(filter.toJson());

        long end = System.currentTimeMillis();
        getLog().info("Cache Finished, Cost:" + (end - start));

    }


    /**
     * Just for test
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        ICache cache = new TodayFlowCache(1000,2);
        cache.refresh();
        long start = System.currentTimeMillis();
        String doc = cache.get(new Document("bi_name","today_flow").append("mall_id","sh001").append("p_dt","20190215").toJson());
        long end = System.currentTimeMillis();
        getLog().info("TimeEllapsed:" + (end - start));
        getLog().info(doc);
    }
}
