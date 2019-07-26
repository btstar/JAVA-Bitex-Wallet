package com.udun_demo.server.job;

import com.udun_demo.service.ICoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class InitCoinTask {

    @Autowired
    private ICoinService coinService;

    // 十分钟看下网关币种是否有变化
    @Scheduled(cron = "0 */10 * * * ?")
    private void task(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                coinService.initCoins();
            }
        }).start();
    }


}
