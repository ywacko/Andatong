package com.unidt.api;

import com.unidt.api.parent.APIParent;
import com.unidt.bean.VenueFloorBean;
import com.unidt.exception.TimeShortException;
import com.unidt.helper.cache.P9BrandYetaiCache;
import com.unidt.helper.common.DateHelper;
import com.unidt.helper.common.ReturnResult;
import org.bson.Document;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@EnableAutoConfiguration
@ComponentScan
public class P9BrandYetai extends APIParent {

    public P9BrandYetai() throws TimeShortException {
        super(new P9BrandYetaiCache(2000, 60));
    }

    @RequestMapping(value = "/p9/realTime", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    /**
     * 实时查询业态份额
     */
    public String p9_realtime(@RequestBody VenueFloorBean bean) throws ExecutionException {
        Document filters = new Document("bi_name", "brand_shop_prop").append("venue", bean.getVenue())
                .append("type", bean.getType()).append("dt_year", bean.getDt_year())
                .append("dt_month", bean.getDt_month()).append("dt_day", bean.getDt_day());
        return getCache().get(filters.toJson());
    }

    @RequestMapping(value = "/p9/month", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    /**
     * 按月份查询业态份额
     */
    public String p9_month(@RequestBody VenueFloorBean bean) throws ExecutionException {
        Document filters = new Document("bi_name", "brand_shop_prop_month").append("venue", bean.getVenue())
                .append("type", bean.getType()).append("dt_year", bean.getDt_year())
                .append("dt_month", bean.getDt_month());
        return getCache().get(filters.toJson());
    }

    @RequestMapping(value = "/p9/total", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    /**
     * 13个月的业态份额
     */
    public String p9_total(@RequestBody VenueFloorBean bean) throws ExecutionException {
        return handleTotal(bean).toJson();
    }

    /**
     * 把返回处理成理想状态
     * @param bean
     * @return
     * @throws ExecutionException
     */
    public Document handleTotal(VenueFloorBean bean) throws ExecutionException {
        int year = DateHelper.getYear();
        int month = DateHelper.getMonth();

        List<Double> series = new ArrayList<>();
        List<String> xaxis = new ArrayList<>();

        for (int i = 0 ; i < 13; i++) {
            Document filters = new Document("bi_name", "brand_shop_prop_month").append("venue", bean.getVenue())
                    .append("type", bean.getType()).append("dt_year", year).append("dt_month", month);
            Document ret = Document.parse(getCache().get(filters.toJson()));
            List<Document> datas = (List<Document>) ret.get("data");

            // keyValue的值为当前查询的店铺价值, totalValue为所有商店总价值
            double totalValue = 0;
            int keyValue = 0;

            // xaxi为当前日期，最后插入到xaxis数组
            String xaxi = null;

            for (Document data : datas) {
                Document bi_value = (Document) data.get("bi_value");
                if (bi_value.get("shop_name").equals(bean.getName())) {
                    keyValue = bi_value.getInteger("value");
                }
                totalValue += bi_value.getInteger("value");
            }

            if (totalValue > 0) series.add(keyValue/totalValue);
            else series.add(0.0);

            if (month >= 10) xaxi = year + "" + month;
            else xaxi = year + "0" + month;
            xaxis.add(xaxi);

            if (--month <= 0) {
                year--;
                month = 12;
            }
        }

        Document data = new Document("name", bean.getName()).append("series", series).append("xaxis", xaxis);
        List<Document> result = new ArrayList<>();
        result.add(data);

        return ReturnResult.createResult(result);
    }

}
