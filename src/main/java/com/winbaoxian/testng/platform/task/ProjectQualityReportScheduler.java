package com.winbaoxian.testng.platform.task;

import com.winbaoxian.testng.platform.service.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class ProjectQualityReportScheduler {

    @Resource
    private SchedulerService schedulerService;

    /**
     * 每周六1点
     */
    @Scheduled(cron = "0 0 1 * * 6")
    public void weeklyRun() {
        log.info("ProjectQualityReportScheduler.weeklyRun start");
        schedulerService.autoStoreProjectQualityReportHistory();
        log.info("ProjectQualityReportScheduler.weeklyRun end");
    }

    @Scheduled(cron = "0 0 8 * * 6")
    public void sendEmailWeekly() {
        log.info("ProjectQualityReportScheduler.sendEmailWeekly start");
        schedulerService.sendProjectQualityReportWeekly();
        log.info("ProjectQualityReportScheduler.sendEmailWeekly end");
    }

}
