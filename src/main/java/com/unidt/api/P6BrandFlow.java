package com.unidt.api;

import com.unidt.api.parent.APIParent;
import com.unidt.bean.TimeSpaneBean;
import com.unidt.exception.TimeShortException;
import com.unidt.helper.cache.P6BrandFlowCache;
import org.bson.Document;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@EnableAutoConfiguration
@ComponentScan
public class P6BrandFlow extends APIParent {

    public P6BrandFlow() throws TimeShortException {
        super(new P6BrandFlowCache(2000, 60));

    }

    @RequestMapping("/p6/brand/{venue}/{name}")
    @ResponseBody
    @CrossOrigin
    /**
     * 实时客流
     */
    public String p6_brand_flow(@PathVariable String venue, @PathVariable String name) throws ExecutionException {
        return getCache().get(new Document("bi_name", "today_flow_name").append("venue", venue).append("name", name).toJson());
    }

    @RequestMapping("/p6/monthflow/{year}/{month}/{venue}/{name}")
    @ResponseBody
    @CrossOrigin
    /**
     * 单月总客流
     */
    public String p6_month_flow(@PathVariable int year, @PathVariable int month, @PathVariable String venue, @PathVariable String name)
        throws ExecutionException {
        return getCache().get(new Document("bi_name","get_month_total_name").append("dt_year", year).append("dt_month", month)
                .append("venue", venue).append("name", name).toJson());
    }

    @RequestMapping(value = "/p6/dayflow", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    /**
     * 单月按天的客流
     */
    public String p6_day_flow(@RequestBody TimeSpaneBean bean) throws ExecutionException{
        return handles_day_flow(bean).toJson();
    }

    /**
     * 处理带有起始和终止时间的请求
     * @param bean
     * @return
     * @throws ExecutionException
     */
    private Document handles_day_flow(TimeSpaneBean bean) throws ExecutionException {
        String ret = getCache().get(new Document("bi_name", "mall_brand_static").append("dt_year", bean.getDt_year())
                .append("dt_month", bean.getDt_month()).append("venue", bean.getVenue()).append("name", bean.getName()).toJson());

        Document document = Document.parse(ret);

        if (StringUtils.isEmpty(bean.getBegin_date())) {
            return document;
        }

        List<Document> newdData = (List<Document>) document.get("data");

        for (Document data : newdData) {
            if(data.getString("p_dt").compareToIgnoreCase(bean.getBegin_date()) >= 0
                    && data.getString("p_dt").compareToIgnoreCase(bean.getEnd_date()) <= 0) {
                data.append("color",1);
            } else {
                data.append("color",0);
            }
        }

        document.remove("data");
        document.append("data", newdData);
        return document;
    }
}
