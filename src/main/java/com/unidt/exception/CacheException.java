package com.unidt.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

public class CacheException extends ExecutionException {
    private Logger log = LoggerFactory.getLogger(MongoException.class);

    public CacheException(){
        super();
        log.error("Cache 相关异常发生");
    }
    public CacheException(String msg) {
        super(msg);
        log.error(msg);
    }
}
