package com.winbaoxian.testng.platform.repository;

import com.winbaoxian.testng.model.entity.ResourceConfigEntity;
import com.winbaoxian.testng.repository.ResourceConfigRepository;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-02-26 14:43
 */
public interface PlatformResourceConfigRepository extends ResourceConfigRepository {

    List<ResourceConfigEntity> findByDeletedFalseOrderByCreateTimeDesc();

}
