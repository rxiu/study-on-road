package com.rxiu.elasticsearch;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.rxiu.elasticsearch.model.ElasticSearchLog;
import com.rxiu.elasticsearch.common.util.DateUtils;
import com.rxiu.elasticsearch.service.ElasticSearchService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SpringBootElasticsearchApplication.class)
public class SpringBootElasticsearchApplicationTests {

    @Autowired
    ElasticSearchService searchService;

    private static final String index = "phip-ssp-collect";
    private static final String type = "apigateway";

    @Test
    public void run() {
        List<Map<String, Object>> logs = Lists.newArrayListWithExpectedSize(100);
        for (int i = 0; i < 100; i++) {
            ElasticSearchLog log = new ElasticSearchLog()
                    .setId(String.valueOf(i))
                    .setMessage("this is a spring boot elasticsearch test" + i)
                    .setCreateTime(new Date());
            logs.add(JSONObject.parseObject(JSONObject.toJSONString(log), Map.class));
        }

        Assert.assertFalse(searchService.batchAdd(index, logs));
    }

    @Test
    public void query() {

        List<ElasticSearchLog> list = searchService.query(
                index,
                Collections.singletonMap("_type", "log")
        );

        list.stream().peek(System.out::println).collect(Collectors.toList());
    }

    @Test
    public void range() {
        Date startTime = DateUtils.getBeginDayOfMonth(new Date(), 0);
        Date endTime = DateUtils.getEndDayOfMonth(startTime, 0);

        System.out.println(searchService.range(index, type, startTime, endTime));

    }

    @Test
    public void delData() {
        searchService.deleteData(
                index,
                Collections.singletonMap("_type", "log")
        );
    }

    @Test
    public void delIndex(){
        searchService.deleteIndex(index);
    }

    @Test
    public void searchScript() {
        String script = "{\n" +
                "    \"from\": \"{{from}}\", \"size\": \"{{size}}\"," +
                "    \"query\":{\n" +
                "        \"match\":{\n" +
                "            \"restfulId\":\"{{restfulId}}\"\n" +
                "        }\n" +
                "    }\n" +
                "}";

        Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
        params.put("restfulId", "85aea7e4583e48c38ac06d3e1835d9d1");
        params.put("from", 0);
        params.put("size", 20);
        List result = searchService.searchScript("ssp-odp-apigateway*", "apigateway", script, params);
        System.out.println(result);
    }

}
