package com.unidt.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class APIException extends Exception{
    private Logger log = LoggerFactory.getLogger(MongoException.class);

    public APIException(){
        super();
        log.error("API 相关异常发生");
    }
    public APIException(String msg) {
        super(msg);
        log.error(msg);
    }
}
