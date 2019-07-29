package com.udun_demo;

import com.udun_demo.service.ICoinService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;

@EnableScheduling
@MapperScan("com.udun_demo.dao.mapper")
@SpringBootApplication
public class UdunDemoApplication {

    @Autowired
    private ICoinService coinService;

    private static final String PATH = "cfgPath";

    @PostConstruct
    void started() {
        coinService.initCoins();
    }

    public static void main(String[] args) {
        // 读取配置文件
        String location = System.getProperty(PATH);
        if(location==null){
            String os = System.getProperty("os.name").toLowerCase();
            if(os.contains("windows")){
                System.setProperty(PATH, "c:/etc/udun_demo/udun_demo.conf");
            }else{
                System.setProperty(PATH, "/etc/udun_demo/udun_demo.conf");
            }
        }
        SpringApplication.run(UdunDemoApplication.class, args);
    }

}
