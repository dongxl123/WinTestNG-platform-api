package com.winbaoxian.testng.platform.component.jpa;

import com.winbaoxian.module.security.model.dto.WinSecurityPrincipal;
import com.winbaoxian.module.security.service.WinSecurityAccessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * jpa @CreateBy
 *
 * @author dongxuanliang252
 * @date 2019-03-26 13:48
 */
@Component
@Slf4j
public class AuditorProvider implements AuditorAware<Long> {

    @Resource
    private WinSecurityAccessService winSecurityAccessService;

    @Override
    public Long getCurrentAuditor() {
        WinSecurityPrincipal principal = null;
        try {
            principal = winSecurityAccessService.getWinSecurityPrincipal();
        } catch (Exception e) {
            log.info("getCurrentAuditor, {}", e.getMessage());
        }
        if (principal != null) {
            return principal.getId();
        }
        return null;
    }

}
