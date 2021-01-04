package com.winbaoxian.testng.platform.service.helper;

import com.winbaoxian.testng.core.WinTestNGExecutor;
import com.winbaoxian.testng.enums.TriggerMode;
import com.winbaoxian.testng.model.core.log.TestReportDataSummaryDTO;
import com.winbaoxian.testng.model.core.log.TestReportDataTestCaseDTO;
import com.winbaoxian.testng.model.dto.TestCasesDTO;
import com.winbaoxian.testng.model.entity.ProjectEntity;
import com.winbaoxian.testng.platform.exception.WinTestNgPlatformException;
import com.winbaoxian.testng.platform.model.constant.WinTestNGPlatformConstant;
import com.winbaoxian.testng.platform.model.dto.ConsoleLogDTO;
import com.winbaoxian.testng.platform.repository.PlatformProjectRepository;
import com.winbaoxian.testng.platform.repository.PlatformTestCasesRepository;
import com.winbaoxian.testng.platform.utils.SpringContextHolder;
import com.winbaoxian.testng.utils.ConsoleLogUtils;
import com.winbaoxian.testng.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestNG;
import org.testng.annotations.Optional;
import org.testng.annotations.*;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author dongxuanliang252
 * @date 2019-03-20 11:28
 */
@Service
@Slf4j
public class PlatformTestNGHelper {

    @Resource
    private WinTestNGExecutor winTestNGExecutor;
    @Resource
    private PlatformProjectRepository platformProjectRepository;
    @Resource
    private PlatformTestCasesRepository platformTestCasesRepository;

    private ExecutorService actionTemplatePool;
    private ExecutorService testCasesPool;
    private ExecutorService schedulerPool;

    @PostConstruct
    public void init() {
        actionTemplatePool = Executors.newFixedThreadPool(10);
        testCasesPool = getThreadPoolExecutorWithInterruptIdle(10);
        schedulerPool = getThreadPoolExecutorWithInterruptIdle(3);
    }

    private ExecutorService getThreadPoolExecutorWithInterruptIdle(int poolSize) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(poolSize, poolSize,
                1L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }

    /**
     * 返回本次测试的UUID
     *
     * @param testCaseIdList
     * @return
     */
    public String executeTestCases(List<Long> testCaseIdList) {
        if (CollectionUtils.isEmpty(testCaseIdList)) {
            throw new WinTestNgPlatformException("请选择执行的测试任务");
        }
        String uuid = UUIDUtil.INSTANCE.randomUUID();
        testCasesPool.execute(() -> {
            XmlTest xmlTest = new XmlTest();
            xmlTest.setName("executeTestCases");
            XmlSuite xmlSuite = new XmlSuite();
            xmlSuite.setName("TestNG Suite");
            xmlSuite.setTests(Arrays.asList(xmlTest));
            Map<String, String> parameters = new HashMap<>();
            parameters.put("sql", String.format("select * from test_cases where id in (%s)", StringUtils.join(testCaseIdList, WinTestNGPlatformConstant.CHAR_COMMA)));
            parameters.put("uuid", uuid);
            xmlTest.setParameters(parameters);
            xmlTest.setClasses(Arrays.asList(new XmlClass(WinTestNGRunner.class)));
            xmlTest.setXmlSuite(xmlSuite);
            TestNG testNG = new TestNG();
            testNG.setXmlSuites(Arrays.asList(xmlSuite));
            testNG.setUseDefaultListeners(false);
            testNG.run();
        });
        return uuid;
    }


    /**
     * 返回本次测试的UUID
     *
     * @param projectId
     * @return
     */
    public String executeOnlineTestCasesByProjectId(Long projectId) {
        if (projectId == null) {
            throw new WinTestNgPlatformException("请选择执行的项目");
        }
        ProjectEntity projectEntity = platformProjectRepository.findOne(projectId);
        if (projectEntity == null || BooleanUtils.isTrue(projectEntity.getDeleted())) {
            throw new WinTestNgPlatformException("项目不存在或已删除");
        }
        if (BooleanUtils.isNotTrue(projectEntity.getIntegrationFlag())) {
            throw new WinTestNgPlatformException("项目未集成");
        }
        if (!platformTestCasesRepository.existsByProjectIdAndCiFlagTrueAndDeletedFalse(projectId)) {
            throw new WinTestNgPlatformException("项目不存在上线的测试任务");
        }
        String uuid = UUIDUtil.INSTANCE.randomUUID();
        schedulerPool.execute(() -> {
            XmlTest xmlTest = new XmlTest();
            xmlTest.setName("executeOnlineTestCasesByProjectId");
            XmlSuite xmlSuite = new XmlSuite();
            xmlSuite.setName("TestNG Suite");
            xmlSuite.setTests(Arrays.asList(xmlTest));
            Map<String, String> parameters = new HashMap<>();
            parameters.put("sql", String.format("SELECT * FROM test_cases where ci_flag = TRUE AND deleted = FALSE AND project_id = %s", projectId));
            parameters.put("uuid", uuid);
            parameters.put("triggerMode", TriggerMode.CRON.name());
            parameters.put("projectId", String.valueOf(projectId));
            xmlTest.setParameters(parameters);
            xmlTest.setClasses(Arrays.asList(new XmlClass(WinTestNGRunner.class)));
            xmlTest.setXmlSuite(xmlSuite);
            TestNG testNG = new TestNG();
            testNG.setXmlSuites(Arrays.asList(xmlSuite));
            testNG.setUseDefaultListeners(false);
            testNG.run();
        });
        return uuid;
    }


    public String executeActionTemplate(Long templateId, Map<String, Object> mappings) {
        String uuid = UUIDUtil.INSTANCE.randomUUID();
        actionTemplatePool.execute(() -> {
            try {
                winTestNGExecutor.executeActionTemplate(templateId, mappings, uuid);
            } catch (Exception e) {
                log.error("winTestNGExecutor.executeActionTemplate error", e);
            } finally {
                ConsoleLogUtils.INSTANCE.log(uuid, WinTestNGPlatformConstant.CONSOLE_LOG_END_STRING);
            }
        });
        return uuid;
    }

    public ConsoleLogDTO getAppendLog(String uuid, Long offset) {
        if (StringUtils.isBlank(uuid)) {
            return null;
        }
        if (offset == null) {
            offset = 0L;
        }
        String content = ConsoleLogUtils.INSTANCE.read(uuid, offset);
        ConsoleLogDTO logDTO = new ConsoleLogDTO();
        logDTO.setOffset(offset);
        logDTO.setEndFlag(false);
        if (StringUtils.isNotBlank(content)) {
            logDTO.setContent(content);
            logDTO.setOffset(offset + content.length());
            if (StringUtils.containsIgnoreCase(content, WinTestNGPlatformConstant.CONSOLE_LOG_END_STRING)) {
                logDTO.setEndFlag(true);
            }
        }
        return logDTO;
    }

    private class WinTestNGRunner {

        private String sql;
        private TestReportDataSummaryDTO summaryData;

        @Parameters({"sql", "uuid", "triggerMode", "projectId"})
        @BeforeClass
        public void beforeClass(String sql, String uuid, @Optional() TriggerMode triggerMode, @Optional() Long projectId) {
            this.sql = sql;
            summaryData = new TestReportDataSummaryDTO();
            if (triggerMode != null) {
                summaryData.setTriggerMode(triggerMode);
                if (TriggerMode.CRON.equals(triggerMode)) {
                    summaryData.setAnalysisFlag(Boolean.TRUE);
                }
            } else {
                summaryData.setTriggerMode(TriggerMode.MANUAL);
            }
            summaryData.setReportUuid(uuid);
            summaryData.setStartTime(new Date());
            summaryData.setProjectId(projectId);
        }

        /**
         * 查询测试任务
         * parallel = true
         *
         * @return
         * @throws SQLException
         * @throws ClassNotFoundException
         */
        @DataProvider(name = "testData")
        public Object[] getData() throws SQLException, ClassNotFoundException {
            TestCasesDTO[] dataArray = getWinTestNGExecutor().getTestCasesList(sql);
            summaryData.setTotalCount((long) dataArray.length);
            return dataArray;
        }

        @BeforeMethod
        public void beforeMethod() {
        }

        @Test(dataProvider = "testData")
        public void execute(TestCasesDTO testCasesDTO) throws Throwable {
            TestReportDataTestCaseDTO reportDataContext = new TestReportDataTestCaseDTO();
            reportDataContext.setReportUuid(summaryData.getReportUuid());
            reportDataContext.setAnalysisFlag(summaryData.getAnalysisFlag());
            summaryData.addTestCaseData(reportDataContext);
            try {
                getWinTestNGExecutor().executeTestCase(testCasesDTO, reportDataContext);
                summaryData.incSuccessCount();
            } catch (Throwable e) {
                summaryData.incFailCount();
                throw e;
            }
        }

        @AfterMethod
        public void afterMethod(ITestContext a, XmlTest b, Method c, Object[] d, ITestResult e) {
        }

        @AfterClass
        public void afterClass() {
            summaryData.setEndTime(new Date());
            //保存测试报告数据
            try {
                getWinTestNGExecutor().doWorkAfterFinished(summaryData);
            } catch (Exception e) {
                log.error("saveTestReportData failed", e);
            } finally {
                ConsoleLogUtils.INSTANCE.log(summaryData.getReportUuid(), WinTestNGPlatformConstant.CONSOLE_LOG_END_STRING);
            }
        }

        private WinTestNGExecutor getWinTestNGExecutor() {
            return SpringContextHolder.INSTANCE.getBean(WinTestNGExecutor.class);
        }
    }

}
