package com.unidt.helper.common;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 返回结果json结构
 * {"code":200, "msg":"ok", "data":{}}
 */
public class ReturnResult {

    public static Document createResult() {
        return new Document("code",200).append("msg", "ok").append("data", new ArrayList<Document>()).append("total_count",0)
                .append("total_page",0).append("count",0).append("page",0).append("pagesize",0);
    }

    public static Document createResult(int code, String msg) {
        return new Document("code",code).append("msg", msg).append("data",  new ArrayList<Document>()).append("total_count",0)
                .append("total_page",0).append("count",0).append("page",0).append("pagesize",0);
    }


    public static Document createResult(List<?> data) {
        return new Document("code",200).append("msg", "ok").append("data", data).append("total_count",data.size())
                .append("total_page",1).append("count",data.size()).append("page",1).append("pagesize",50000);
    }
}
