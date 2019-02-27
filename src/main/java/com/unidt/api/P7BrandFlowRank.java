package com.unidt.api;

import com.unidt.api.parent.APIParent;
import com.unidt.bean.VenueFloorBean;
import com.unidt.exception.TimeShortException;
import com.unidt.helper.cache.P7BrandFlowRankCache;
import com.unidt.helper.common.DateHelper;
import org.bson.Document;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@EnableAutoConfiguration
@ComponentScan
/**
 * 品牌客流排行
 */
public class P7BrandFlowRank extends APIParent {

    public P7BrandFlowRank() throws TimeShortException {
        super(new P7BrandFlowRankCache(2000, 60));

    }

    @RequestMapping(value = "/p7/realtime", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    /**
     * 实时品牌排行
     */
    public String p7_real_time_rank(@RequestBody VenueFloorBean bean) throws ExecutionException {
        Document filters = new Document("bi_name", "get_pingpai_20").append("dt_year", DateHelper.getYear())
                .append("dt_month", DateHelper.getMonth()).append("dt_day", DateHelper.getDay());
        if (!StringUtils.isEmpty(bean.getVenue())) filters.append("venue", bean.getVenue());
        if (!StringUtils.isEmpty(bean.getLocname())) filters.append("locname", bean.getLocname());
        if (!StringUtils.isEmpty(bean.getType())) filters.append("type", bean.getType());
        return getCache().get(filters.toJson());
    }

    @RequestMapping(value = "/p7/month", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    /**
     * 月品牌排行
     */
    public String p7_month_rank(@RequestBody VenueFloorBean bean) throws ExecutionException {
        Document filters = new Document("bi_name", "get_name_month_20");
        if (!StringUtils.isEmpty(bean.getVenue())) filters.append("venue", bean.getVenue());
        if (!StringUtils.isEmpty(bean.getLocname())) filters.append("locname", bean.getLocname());
        if (!StringUtils.isEmpty(bean.getType())) filters.append("type", bean.getType());
        filters.append("dt_year", bean.getDt_year()).append("dt_month", bean.getDt_month());
        return getCache().get(filters.toJson());
    }


}
