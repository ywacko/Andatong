package com.unidt.api;

import com.unidt.api.parent.APIParent;
import com.unidt.exception.TimeShortException;
import com.unidt.helper.cache.P5ShopTypeFlowCache;
import com.unidt.helper.common.DateHelper;
import com.unidt.helper.common.ReturnResult;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

import org.bson.Document;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@EnableAutoConfiguration
@ComponentScan
/**
 * 店铺类型客流
 */
public class P5ShopTypeFlow extends APIParent {

    public P5ShopTypeFlow() throws TimeShortException {
        super(new P5ShopTypeFlowCache(2000, 60));
    }

    @RequestMapping("/p5/history/{year}/{month}")
    @ResponseBody
    @CrossOrigin
    /**
     * 年月客流量百分比图
     */
    public String p5_history(@PathVariable int year, @PathVariable int month) throws ExecutionException {
        return getCache().get(new Document("bi_name", "get_leixing_month").append("dt_year", year).append("dt_month", month).toJson());
    }

    @RequestMapping("/p5/realTime")
    @ResponseBody
    @CrossOrigin
    /**
     * 实时客流量百分比
     */
    public String p5_realTime() throws ExecutionException {
        int year = DateHelper.getYear();
        int month = DateHelper.getMonth();

        return getCache().get(new Document("bi_name", "get_leixing_month").append("dt_year", year).append("dt_month", month).toJson());
    }

    @RequestMapping("/p5/history/total")
    @ResponseBody
    @CrossOrigin
    /**
     * 历史数据对比图
     */
    public String p5_history_total() throws Exception {
        return handleHistory().toJson();
    }

    /**
     * 处理历史数据得出格式
     * @return
     * @throws Exception
     */
    public Document handleHistory() throws Exception {
        int year = DateHelper.getYear();
        int month = DateHelper.getMonth();

        List<Integer> zhaoCai = new ArrayList<>();
        List<Integer> lianYing = new ArrayList<>();
        List<String> xaxis = new ArrayList<>();

        for (int i = 0; i < 13; i++) {
            Document filters = new Document("bi_name", "get_leixing_month")
                    .append("dt_year", year).append("dt_month", month);
            Document ret = Document.parse(getCache().get(filters.toJson()));
            List<Document> datas = (List<Document>) ret.get("data");

            // 招采和联营的value
            int zhao = 0;
            int lian = 0;

            // xaxi为当前日期，最后插入到xaxis数组
            String xaxi = null;

            for (Document data : datas) {
                if (data.get("leixing").equals("招采")) {
                    Document bi_value = (Document) data.get("bi_value");
                    zhao += bi_value.getInteger("value");
                }
                if (data.get("leixing").equals("联营")) {
                    Document bi_value = (Document) data.get("bi_value");
                    lian += bi_value.getInteger("value");
                }
            }

            zhaoCai.add(zhao);
            lianYing.add(lian);

            if (month >= 10) xaxi = year + "" + month;
            else xaxi = year + "0" + month;
            xaxis.add(xaxi);

            if (--month <= 0) {
                year--;
                month = 12;
            }
        }

        Document docZhao = new Document("name", "招采").append("value", zhaoCai);
        Document docLian = new Document("name", "联营").append("value", lianYing);

        List<Document> data = new ArrayList<>();
        data.add(docZhao);
        data.add(docLian);

        List<Document> result = new ArrayList<>();
        result.add(new Document("data", data).append("xaxis", xaxis));
        return ReturnResult.createResult(result);
    }
}
