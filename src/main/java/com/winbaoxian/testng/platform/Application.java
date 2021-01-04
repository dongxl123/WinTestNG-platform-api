package com.winbaoxian.testng.platform;

import com.winbaoxian.module.cas.annotation.EnableWinCasClient;
import com.winbaoxian.module.security.annotation.EnableWinSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.winbaoxian.testng"}, exclude = {MongoAutoConfiguration.class})
@ServletComponentScan
@EnableJpaAuditing
@EnableWinSecurity(tablePrefix = "admin", sysLog = true)
@EnableJpaRepositories(basePackages = {"com.winbaoxian.testng.platform.repository", "com.winbaoxian.module.security.repository"})
@EnableScheduling
@EntityScan(basePackages={"com.winbaoxian.testng.model.entity"})
@EnableWinCasClient
public class Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

