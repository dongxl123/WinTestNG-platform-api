package com.winbaoxian.testng.platform.component.holder;

import com.winbaoxian.testng.platform.utils.OSSClientHelper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author DongXL
 * @Create 2018-03-21 14:11
 */

@Configuration
public class HolderFactory {

    @Bean
    @ConfigurationProperties(prefix = "aliyun.oss")
    public OSSClientHelper getOSSClientHelper() {
        return new OSSClientHelper();
    }


}
