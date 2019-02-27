package com.unidt.api;

import com.unidt.api.parent.APIParent;
import com.unidt.bean.TimeSpaneBean;
import com.unidt.exception.TimeShortException;
import com.unidt.helper.cache.P2MonthFlowCache;
import com.unidt.helper.common.DateHelper;
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

public class P2MonthFlow extends APIParent {

    public P2MonthFlow() throws TimeShortException {
        super(new P2MonthFlowCache(1000,60));
    }

    @RequestMapping("/p2/month/{year}/{month}")
    @ResponseBody
    /**
     * 获取指定年月的客流情况
     */
    public String p2_month_flow(@PathVariable int year, @PathVariable int month) throws ExecutionException {
        String p_dt = DateHelper.getDate();
        return getCache().get(new Document("bi_name","mall_static").append("dt_year",year).append("dt_month",month).toJson());
    }

    /**
     * 指定年月的客流总数情况
     * @param year
     * @param month
     * @return
     * @throws ExecutionException
     */
    @RequestMapping("/p2/month/total/{year}/{month}")
    @ResponseBody
    public String p2_month_total(@PathVariable int year, @PathVariable int month) throws ExecutionException {
        String p_dt = DateHelper.getDate();
        return getCache().get(new Document("bi_name","get_month_total").append("dt_year",year).append("dt_month",month).toJson());
    }

    /**
     *  单双日客流情况
     * @param odd
     * @param year
     * @param month
     * @return
     * @throws ExecutionException
     */
    @RequestMapping("/p2/month/odd/{odd}/{year}/{month}")
    @ResponseBody
    public String p2_month_odd(@PathVariable String odd,@PathVariable int year, @PathVariable int month) throws ExecutionException {
        String ret = getCache().get(new Document("bi_name","mall_static").append("dt_year",year).append("dt_month",month).toJson());
        Document docs = Document.parse(ret);

        List<Document> real_data = new ArrayList<>();

        List<Document> datas = (List<Document>) docs.get("data");

        for (Document data:datas) {
            if(data.getString("isodd").compareToIgnoreCase(odd) == 0) {
                real_data.add(data);
            }
        }
        docs.remove("data");
        docs = docs.append("data", real_data);

        return docs.toJson();
    }

    /**
     * 获取指定时间范围内的数据
     * @param api
     * @param bean
     * @return
     * @throws ExecutionException
     */
    public static Document get_month_span(APIParent api, TimeSpaneBean bean) throws ExecutionException {
        String ret = api.getCache().get(new Document("bi_name","mall_static").append("dt_year",bean.getDt_year()).append("dt_month",bean.getDt_month()).toJson());
        Document docs = Document.parse(ret);

        if(bean.getBegin_date() == null ||  bean.getBegin_date().isEmpty()) {
            getLog().info("未指定时间范围");
            return docs;
        }

        List<Document> real_data = new ArrayList<>();

        List<Document> datas = (List<Document>) docs.get("data");

        for (Document data:datas) {
            if(data.getString("p_dt").compareToIgnoreCase(bean.getBegin_date()) >= 0
                    && data.getString("p_dt").compareToIgnoreCase(bean.getEnd_date()) <= 0) {
                data.append("color",1);
            }else {
                data.append("color",0);
            }
            real_data.add(data);
        }
        docs.remove("data");
        docs = docs.append("data", real_data);
        return docs;
    }

    /**
     *
     * @param type
     * @return
     */
    public static Integer getOdd(int type) {
        if (type == 1) {
            return 1;
        }else if(type == 2){
            return 0;
        }
        return null;
    }

    /**
     *
     * @param type
     * @return
     */
    public static Integer getWeekend(int type) {
        if (type == 3) {
            return 1;
        }else if(type == 4){
            return 0;
        }
        return null;
    }

    /**
     * 带过滤地获取数据，过滤条件包括： 时间范围、单双日、工作日
     * @param api
     * @param bean
     * @return
     * @throws ExecutionException
     */
    public static Document getWithFilter(APIParent api, TimeSpaneBean bean) throws ExecutionException {
        Integer odd = getOdd(bean.getType());
        Integer weekend = getWeekend(bean.getType());
        //
        // 先根据时间范围过滤
        Document docs = get_month_span(api,bean);

        if(bean.getType() == 0) {
            getLog().info("获取全部数据");
            return docs;
        }

        List<Document> datas = (List<Document>) docs.get("data");

        List<Document> real_data = new ArrayList<>();
        //
        // 根据单双日过滤
        for(Document data:datas) {
            if (odd != null) {
                if (data.getInteger("isodd").compareTo(odd) == 0) {
                    real_data.add(data);
                }
            }else if (weekend != null) {
                if (data.getInteger("isweekend").compareTo(weekend) == 0) {
                    real_data.add(data);
                }
            }
        }
        docs.remove("data");
        docs = docs.append("data", real_data);
        return docs;
    }
    /**
     * 周末\工作日客流情况
     * @param weekend
     * @param year
     * @param month
     * @return
     * @throws ExecutionException
     */
    @RequestMapping("/p2/month/weekend/{weekend}/{year}/{month}")
    @ResponseBody
    public String p2_month_weekend(@PathVariable int weekend,@PathVariable int year, @PathVariable int month) throws ExecutionException {
        String p_dt = DateHelper.getDate();
        String ret = getCache().get(new Document("bi_name","mall_static").append("dt_year",year).append("dt_month",month).toJson());
        Document docs = Document.parse(ret);

        List<Document> real_data = new ArrayList<>();

        List<Document> datas = (List<Document>) docs.get("data");
        for (Document data:datas) {
            if(data.getInteger("isweekend") == weekend) {
                real_data.add(data);
            }
        }
        docs.remove("data");
        docs = docs.append("data", real_data);

        return docs.toJson();
    }

    /**
     *
     * @param bean
     * @return
     * @throws ExecutionException
     */
    @RequestMapping(value = "/p2/month/zone", method = RequestMethod.POST)
    @ResponseBody
    public String p2_month_zone(@RequestBody TimeSpaneBean bean) throws ExecutionException {

        getLog().info("month zone");
        getLog().info(bean.getBegin_date());
        return getWithFilter(this,bean).toJson();
    }
}
