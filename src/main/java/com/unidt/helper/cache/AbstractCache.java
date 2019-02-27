package com.unidt.helper.cache;

import com.google.common.cache.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.unidt.exception.TimeShortException;
import com.unidt.helper.cache.interf.ICache;
import com.unidt.helper.common.ReturnResult;
import com.unidt.helper.impl.DBFactory;
import com.unidt.helper.interf.IFraDB;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

/**
 * 缓存抽象类，在该类中封装通用的加载key，获取key方法
 */
public abstract class AbstractCache implements ICache {

    private LoadingCache<String , Document> cache = null;
    private static Logger log = LoggerFactory.getLogger(TodayFlowCache.class);
    private IFraDB<Bson, FindIterable<Document>> db = DBFactory.createMongoDB("andatong");

    /**
     * 缓存加载线程池
     */
    private ThreadPoolExecutor  threadPool = new ThreadPoolExecutor(8,100,200, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(10));

    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);

    /**
     * 默认的构造函数，提供1000条数据1分钟的缓存
     */
    AbstractCache() {
        cache = CacheBuilder.newBuilder().maximumSize(1000).expireAfterWrite(1, TimeUnit.MINUTES)
                .refreshAfterWrite(1, TimeUnit.MINUTES)
                .removalListener(new RemovalListener<String ,Document>() {
                    @Override
                    public void onRemoval(RemovalNotification<String, Document> removalNotification) {
                        log.info(removalNotification.getKey() + " 超时");
                    }
                })
                .build(new CacheLoader<String, Document>() {
                    @Override
                    public Document load(String s) throws Exception {
                        getLog().info("********************Get" + s + "***************");
                        return loads(s);
                    }
                });
    }

    /**
     * 每个缓存的最大条数和超时时间是不同的，添加控制参数，提高扩展性
     * @param maxcount
     * @param timeout
     */
    AbstractCache(int maxcount, int timeout) throws TimeShortException {
        if(timeout < 10) {
            throw new TimeShortException("Cache Time Too Short");
        }
        cache = CacheBuilder.newBuilder().maximumSize(maxcount).expireAfterWrite(timeout, TimeUnit.MINUTES)
                .removalListener(new RemovalListener<String ,Document>() {
                    @Override
                    public void onRemoval(RemovalNotification<String, Document> removalNotification) {
                        log.info(removalNotification.getKey() + " 超时");
                    }
                })
                .build(new CacheLoader<String, Document>() {
            @Override
            public Document load(String s) throws Exception {
                getLog().info("********************Get" + s + "***************");
                return loads(s);
            }
        });
    }
    //
    // getter
    private LoadingCache<String, Document> getCache() {
        return cache;
    }

    public void setCache(LoadingCache<String, Document> cache) {
        this.cache = cache;
    }

    static Logger getLog() {
        return log;
    }

    IFraDB<Bson, FindIterable<Document>> getDb() {
        return db;
    }

    /**
     * 根据键值从后端数据库中加载数据至缓存中
     * @param key
     * @return
     * @throws Exception
     */
    @Override
    public Document loads(String key) throws Exception {
        long start = System.currentTimeMillis();

        Document doc = Document.parse(key);
        log.info("Filter: " + doc.toJson());

        db.setCollection("BI");
        FindIterable<Document> ret = db.find(doc);
        MongoCursor<Document> cursor = ret.iterator();
        List<Document> lst = new ArrayList<>();

        while(cursor.hasNext()) {
            Document tmp = cursor.next();
            tmp.remove("_id");
            lst.add(tmp);
        }

        Document result = ReturnResult.createResult(lst);
        long end = System.currentTimeMillis();
        log.info("Cache:" + key + " cost:" + (end - start));
        return result;
    }

    /**
     * 从缓存中加载数据
     * @param key
     * @return
     * @throws ExecutionException
     */
    public String get(String key) throws ExecutionException {
        return cache.get(key).toJson();
    }

    /**
     * 用于系统初始化时开启的缓存加载线程
     */
    class RefreshThread implements Runnable{
        private String key;

        RefreshThread(String key) {
            this.key = key;
        }
        @Override
        public void run() {
            getLog().info("Cache: " + key);
            getCache().refresh(key);
        }
    }

    /**
     * 定时刷新缓存
     */
    class RefreshTimer implements Runnable {

        @Override
        public void run() {
            log.info("Start refresh cache");
            Set<String> keys = cache.asMap().keySet();
            for (String key:keys) {
                log.info("Refresh: " + key);
                cache.refresh(key);
            }
        }
    }

    @Override
    public void addCacheTask(String key){
        threadPool.execute(new RefreshThread(key));
    }

    public void startRefreshTimer(int delay, int period) {
        scheduledExecutorService.scheduleAtFixedRate(new RefreshTimer(),delay,period, TimeUnit.SECONDS);
    }

}
