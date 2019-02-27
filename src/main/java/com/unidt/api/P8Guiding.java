package com.unidt.api;

import com.unidt.api.parent.APIParent;
import com.unidt.bean.VenueFloorBean;
import com.unidt.exception.TimeShortException;
import com.unidt.helper.cache.P8GuidingCache;
import org.bson.Document;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@EnableAutoConfiguration
@ComponentScan
public class P8Guiding extends APIParent {

    public P8Guiding() throws TimeShortException {
        super(new P8GuidingCache(2000, 60));
    }

    @RequestMapping(value = "/p8/guiding", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    /**
     * 按时间查询
     */
    public String p8_guiding(@RequestBody VenueFloorBean bean) throws ExecutionException {
        Document filters = new Document("bi_name", "brand_link").append("dt_year", bean.getDt_year())
                .append("dt_month", bean.getDt_month()).append("venue", bean.getVenue())
                .append("bi_value.from_name", bean.getName());
        return getCache().get(filters.toJson());
    }


}
