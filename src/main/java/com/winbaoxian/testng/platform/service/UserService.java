package com.winbaoxian.testng.platform.service;

import com.winbaoxian.module.security.model.dto.WinSecurityBaseUserDTO;
import com.winbaoxian.module.security.model.entity.WinSecurityBaseUserEntity;
import com.winbaoxian.module.security.service.WinSecurityUserService;
import com.winbaoxian.testng.platform.model.dto.UserSelectDTO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-03-21 18:24
 */
@Service
public class UserService {

    @Resource
    private WinSecurityUserService winSecurityUserService;
    @Resource
    private SortService sortService;

    public List<UserSelectDTO> getSelectUserList() {
        WinSecurityBaseUserDTO params = new WinSecurityBaseUserDTO();
        List<WinSecurityBaseUserDTO> userList = winSecurityUserService.getUserList(params);
        if (CollectionUtils.isEmpty(userList)) {
            return null;
        }
        List<UserSelectDTO> retList = new ArrayList<>();
        for (WinSecurityBaseUserDTO userDTO : userList) {
            UserSelectDTO retDTO = new UserSelectDTO();
            retDTO.setId(userDTO.getId());
            retDTO.setName(userDTO.getName());
            retDTO.setUserName(userDTO.getUserName());
            retList.add(retDTO);
        }
        sortService.sortSelectUserList(retList);
        return retList;
    }

    public List<WinSecurityBaseUserDTO> getByIdList(List<Long> userIdList) {
        Specification<WinSecurityBaseUserEntity> specification = (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(userIdList)) {
                predicateList.add(root.get("id").in(userIdList));
            }
            return query.where(predicateList.toArray(new Predicate[predicateList.size()])).getRestriction();
        };
        return winSecurityUserService.getUserList(specification);
    }
}
