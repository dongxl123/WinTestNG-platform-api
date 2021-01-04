package com.winbaoxian.testng.platform.repository;

import com.winbaoxian.testng.model.entity.GlobalParamsEntity;
import com.winbaoxian.testng.repository.GlobalParamsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author dongxuanliang252
 * @date 2019-03-20 11:50
 */
public interface PlatformGlobalParamsRepository extends GlobalParamsRepository {

    Page<GlobalParamsEntity> findByKeyContainsAndDeletedFalse(String key, Pageable pageable);

    boolean existsByKeyAndDeletedFalse(String key);

    boolean existsByKeyAndIdNotAndDeletedFalse(String key, Long id);

}
