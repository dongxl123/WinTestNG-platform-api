package com.winbaoxian.testng.platform.task;

import com.winbaoxian.testng.platform.service.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class IntegrationProjectRunScheduler {

    @Resource
    private SchedulerService schedulerService;

    @Scheduled(cron = "0 15 8 * * 1-5")
    public void workDayRun() {
        log.info("IntegrationProjectRunScheduler.workDayRun start");
        schedulerService.integrationProjectRunForAutomation();
        log.info("IntegrationProjectRunScheduler.workDayRun end");
    }

}