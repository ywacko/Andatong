package com.unidt.helper.cache.interf;

import org.bson.Document;

import java.util.concurrent.ExecutionException;

public interface ICache {
    /**
     * 跟据key加载缓存
     * @param key
     * @return
     * @throws Exception
     */
    Document loads(String key) throws Exception;

    /**
     * 根据key 读取缓存
     * @param key
     * @return
     * @throws ExecutionException
     */
    String get(String key) throws ExecutionException;

    /**
     * 添加缓存更新任务，通常开启新的线程来执行缓存工作，以免阻塞主线程
     * @param key
     */
    void addCacheTask(String key);

    /**
     * 刷新缓存
     * 全量刷新缓存
     * @throws Exception
     */
    void refresh() throws Exception;
}
