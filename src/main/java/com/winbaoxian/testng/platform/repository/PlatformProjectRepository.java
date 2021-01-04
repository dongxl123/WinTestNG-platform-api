package com.winbaoxian.testng.platform.repository;

import com.winbaoxian.testng.model.entity.ProjectEntity;
import com.winbaoxian.testng.repository.ProjectRepository;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-02-26 14:43
 */
public interface PlatformProjectRepository extends ProjectRepository {

    List<ProjectEntity> findByDeletedFalseOrderByCreateTimeDesc();

    boolean existsByIdAndDeletedFalse(Long id);

    List<ProjectEntity> findByIdIn(List<Long> idList);

    List<ProjectEntity> findByIntegrationFlagTrueAndDeletedFalse();

}
