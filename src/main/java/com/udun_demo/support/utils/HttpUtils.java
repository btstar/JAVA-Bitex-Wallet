package com.udun_demo.support.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.udun_demo.support.common.MessageResult;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HttpUtils {

    public static MessageResult sendPost(String url, Map<String,String> param) {
        try {
            HttpRequestWithBody requestWithBody = Unirest.post(url)
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json;charset=utf8");
            JSONObject jsonObject = new JSONObject();
            for (String key : param.keySet()) {
                jsonObject.put(key,param.get(key));
            }
            requestWithBody.body(jsonObject.toJSONString());
            HttpResponse<JsonNode> response = requestWithBody.asJson();
            if (response.getStatus() == 200) {
                MessageResult messageResult = JSON.parseObject(response.getBody().toString(), MessageResult.class);
                return  messageResult;
            }
        }catch (UnirestException e) {
            log.error("请求 URL：{},body:{}失败", url,param);
        }
        return null;
    }

    //初始化请求参数
    public static Map<String ,String> wrapperParams(String key, String body) throws Exception {
        String timestamp = System.currentTimeMillis()+"" ;
        String nonce = String.valueOf(GeneratorUtil.getRandomNumber(100000, 999999));
        String sign = SignUtil.sign(key,timestamp,nonce,body);
        Map<String ,String> map = new HashMap<>();
        map.put("body",body);
        map.put("sign",sign);
        map.put("timestamp",timestamp);
        map.put("nonce",nonce);
        return map;
    }

}
