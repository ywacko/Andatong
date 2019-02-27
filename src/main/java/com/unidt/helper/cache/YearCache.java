package com.unidt.helper.cache;

import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.unidt.exception.TimeShortException;
import org.bson.conversions.Bson;
import com.mongodb.BasicDBObject;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 用了反而会变慢，所以最后就不用了，纯属好玩，写个模板。
 */
public class YearCache extends AbstractCache {

    private static YearCache instance = null;

    private YearCache(int maxcount, int timeout) throws TimeShortException {
        super(maxcount, timeout);
        startRefreshTimer(0,86400);
    }

    public static synchronized YearCache getInstance() {
        if (instance == null) {
            try {
                instance = new YearCache(1000,60);
            } catch (TimeShortException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    @Override
    public void refresh() {
        addCacheTask("findYears");
    }

    @Override
    public Document loads(String key) throws Exception {
        long start = System.currentTimeMillis();

        MongoCollection collection = getDb().setDbname("andatong").collection("BI");
        Bson query = new BasicDBObject("bi_name", "mall_static");
        DistinctIterable<Integer> allYears = collection.distinct("dt_year", query, Integer.class);
        MongoCursor<Integer> yearCursor = allYears.iterator();
        List<Integer> list = new ArrayList<>();

        while (yearCursor.hasNext()) {
            Integer year = yearCursor.next();
            list.add(year);
        }

        long end = System.currentTimeMillis();
        getLog().info("Cache:" + key + " cost:" + (end - start));
        return new Document("data", list);
    }

}
