package com.unidt.api.parent;

import com.unidt.helper.cache.AbstractCache;
import com.unidt.helper.cache.interf.ICache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class APIParent {
    private ICache cache = null;

    public static Logger getLog() {
        return log;
    }

    private static Logger log = LoggerFactory.getLogger(APIParent.class);

    /**
     * 构造即初始化
     * @param cache
     */
    public APIParent(ICache cache) {
        this.cache = cache;
        initAPI();
        //
        // 在此处启动单一定时器，个性化不够，容易导致后端服务器压力过大
//        ((AbstractCache)cache).startRefreshTimer();
    }

    public APIParent(){

    }

    public ICache getCache() {
        return cache;
    }

    /**
     * 在server启动，生成bean的时候初始化缓存
     */
    public void initAPI(){
        try {
            cache.refresh();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
