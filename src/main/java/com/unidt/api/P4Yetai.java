package com.unidt.api;

import com.unidt.api.parent.APIParent;
import com.unidt.exception.TimeShortException;
import com.unidt.helper.cache.P4YetaiCache;
import com.unidt.helper.common.DateHelper;
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
public class P4Yetai extends APIParent {
    public P4Yetai() throws TimeShortException {
        super(new P4YetaiCache(500,60));
    }

    @RequestMapping("/p4/yetai/rt")
    @ResponseBody
    public String rtYetai() throws ExecutionException {
        String date = DateHelper.getDate();
        return getCache().get(new Document("mall_id","sh001").append("bi_name","day_catalog").append("p_dt", date).toJson());
    }

    @RequestMapping("/p4/yetai/{year}/{month}")
    @ResponseBody
    public String oldYetai(@PathVariable int year, @PathVariable int month) throws ExecutionException {
        return getCache().get(new Document("mall_id","sh001").append("bi_name","get_yetai_month_10").append("dt_year", year).append("dt_month",month).toJson());
    }
}
