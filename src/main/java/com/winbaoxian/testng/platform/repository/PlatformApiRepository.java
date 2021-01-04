package com.winbaoxian.testng.platform.repository;

import com.winbaoxian.testng.model.entity.ApiEntity;
import com.winbaoxian.testng.repository.ApiRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author dongxuanliang252
 * @date 2019-02-26 14:43
 */
public interface PlatformApiRepository extends ApiRepository {

    ApiEntity findByProjectIdAndApiUrlAndDeletedFalse(Long projectId, String apiUrl);

    ApiEntity findByProjectIdAndIdAndDeletedFalse(Long projectId, Long id);

    boolean existsByProjectIdAndApiUrlAndDeletedFalse(Long projectId, String apiUrl);

    boolean existsByProjectIdAndApiUrlAndIdNotAndDeletedFalse(Long projectId, String apiUrl, Long id);

    @Query("select count (a)>0 from ApiEntity a where a.projectId=?1 and (a.apiUrl=?2 or a.checkKey=?2)")
    boolean existsByProjectIdAndCheckKey(Long projectId, String checkKey);

}
