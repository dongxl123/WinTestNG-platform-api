package com.winbaoxian.testng.platform;

import com.winbaoxian.testng.platform.service.SchedulerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    @Resource
    private SchedulerService schedulerService;

    @Test
    public void contextLoads() {
        schedulerService.analysisProjectApiInfo();
    }

}
