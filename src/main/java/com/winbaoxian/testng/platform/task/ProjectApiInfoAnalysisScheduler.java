package com.winbaoxian.testng.platform.task;

import com.winbaoxian.testng.platform.service.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class ProjectApiInfoAnalysisScheduler {

    @Resource
    private SchedulerService schedulerService;

    @Scheduled(cron = "0 0 8 * * 1-5")
    public void workDayRun() {
        log.info("ProjectApiInfoAnalysisScheduler.analysisProjectApiInfo start");
        schedulerService.analysisProjectApiInfo();
        log.info("ProjectApiInfoAnalysisScheduler.analysisProjectApiInfo end");
    }

}
