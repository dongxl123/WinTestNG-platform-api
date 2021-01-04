package com.winbaoxian.testng.platform.repository;

import com.winbaoxian.testng.model.entity.ModuleEntity;
import com.winbaoxian.testng.model.entity.ProjectEntity;
import com.winbaoxian.testng.repository.ModuleRepository;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-03-20 11:50
 */
public interface PlatformModuleRepository extends ModuleRepository {

    List<ModuleEntity> findByProjectIdAndDeletedFalse(Long projectId, Sort sort);

    List<ModuleEntity> findByIdIn(List<Long> idList);
}
