package com.jsl.oa.services.timese;


import com.jsl.oa.services.MessageService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MessageTimeTask {
    @Resource
    private MessageService messageService;

    // 每天0点执行一次
    @Scheduled(cron = "0 0 0 * * ?")
    public void messageRemind() {
        messageService.messageRemind();
    }
}
