package com.unidt.api;


import com.unidt.api.parent.APIParent;
import com.unidt.exception.TimeShortException;
import com.unidt.helper.cache.TodayFlowCache;
import com.unidt.helper.common.DateHelper;
import org.bson.Document;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;


@RestController
@EnableAutoConfiguration
@ComponentScan
public class TodayFlow extends APIParent {

    public TodayFlow() throws TimeShortException {
        super(new TodayFlowCache(1000,60));
    }
    @RequestMapping("/p1/today/rt_flow")
    @ResponseBody
    /**
     * 实时客流曲线
     */
    public String p1_rt_flow() throws ExecutionException {
        String p_dt = DateHelper.getDate();
        return getCache().get(new Document("bi_name","today_flow").append("mall_id","sh001").append("p_dt",p_dt).toJson());
    }

    @RequestMapping("/p1/today/flow_total")
    @ResponseBody
    /**
     * 实时客流曲线
     */
    public String p1_total_flow() throws ExecutionException {
        String p_dt = DateHelper.getDate();
        return getCache().get(new Document("bi_name","today_total").append("mall_id","sh001").append("p_dt",p_dt).toJson());
    }

}
