package com.unidt.api;

import com.unidt.api.parent.APIParent;
import com.unidt.exception.TimeShortException;
import com.unidt.helper.cache.P3HeatMapCache;
import org.bson.Document;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@EnableAutoConfiguration
@ComponentScan

public class P3HeatMap extends APIParent {
    public P3HeatMap() throws TimeShortException {
        super(new P3HeatMapCache(2000,60));
    }

    /**
     * 获取制定场馆、制定楼层的热力图
     * @param venue
     * @param floor
     * @return
     * @throws ExecutionException
     */
    @RequestMapping("/p3/heat/{venue}/{floor}")
    @ResponseBody
    public String heat(@PathVariable String venue, @PathVariable String floor) throws ExecutionException {
        return getCache().get(new Document("mall_id","sh001").append("bi_name","heat_map")
                                .append("venue",venue).append("bi_value.floor",floor).toJson());
    }

    /**
     * 获取指定场馆、楼层的今日客流
     * @return
     */
    @RequestMapping("/p3/flow/{venue}/{floor}")
    @ResponseBody
    public String floor_flow(@PathVariable String venue, @PathVariable String floor) throws ExecutionException {
        return getCache().get(new Document("mall_id","sh001").append("bi_name","floor_day_flow")
                .append("venue",venue).append("bi_value.floor",floor).toJson());
    }

}
