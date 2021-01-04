package com.winbaoxian.testng.platform.repository;

import com.winbaoxian.testng.model.entity.ActionTemplateEntity;
import com.winbaoxian.testng.repository.ActionTemplateRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-03-20 11:48
 */
public interface PlatformActionTemplateRepository extends ActionTemplateRepository {

    Page<ActionTemplateEntity> findByNameContainsAndDeletedFalse(String name, Pageable pageable);

    List<ActionTemplateEntity> findByDeletedFalseOrderByCreateTimeDesc();
}
