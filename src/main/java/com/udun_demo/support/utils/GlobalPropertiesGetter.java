package com.udun_demo.support.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "udun.demo.global" ,ignoreUnknownFields = true)
@Data
public class GlobalPropertiesGetter {

    private String merchantId;

    private String apiKey;

    /**
     *  网关服务器地址：例如：http://127.0.0.1:8080
     */
    private String gatewayHost;

    /**
     * 接收网关服务器的http回调API接口地址
     */
    private String callbackUrl;
}
