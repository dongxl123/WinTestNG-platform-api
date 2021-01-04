package com.winbaoxian.testng.platform.service;

import com.winbaoxian.testng.platform.model.constant.WinTestNGPlatformConstant;
import com.winbaoxian.testng.platform.model.dto.IdCountDTO;
import com.winbaoxian.testng.platform.repository.PlatformTestCasesRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class StatisticService {

    @Resource
    private PlatformTestCasesRepository platformTestCasesRepository;

    /**
     * 获取项目的测试任务数量汇总列表数据
     *
     * @return
     */
    @Cacheable(cacheNames = WinTestNGPlatformConstant.COMMON_CACHE_KEY_PREFIX, key = "#root.methodName")
    public List<IdCountDTO> getTestCaseCountListGroupByProductId() {
        return platformTestCasesRepository.getTestCaseCountListGroupByProductId();
    }

    /**
     * 获取用户的测试任务数量汇总列表数据
     *
     * @return
     */
    @Cacheable(cacheNames = WinTestNGPlatformConstant.COMMON_CACHE_KEY_PREFIX, key = "#root.methodName")
    public List<IdCountDTO> getTestCaseCountListGroupByUserId() {
        return platformTestCasesRepository.getTestCaseCountListGroupByUserId();
    }


}
