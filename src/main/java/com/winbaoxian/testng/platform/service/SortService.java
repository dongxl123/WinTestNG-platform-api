package com.winbaoxian.testng.platform.service;

import com.winbaoxian.testng.platform.model.dto.IdCountDTO;
import com.winbaoxian.testng.platform.model.dto.ProjectSelectDTO;
import com.winbaoxian.testng.platform.model.dto.UserSelectDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SortService {

    @Resource
    private StatisticService statisticService;


    public void sortSelectUserList(List<UserSelectDTO> userList) {
        if (CollectionUtils.isEmpty(userList)) {
            return;
        }
        List<IdCountDTO> idCountDTOList = statisticService.getTestCaseCountListGroupByUserId();
        if (CollectionUtils.isEmpty(idCountDTOList)) {
            return;
        }
        Map<Long, Long> idCountMap = idCountDTOList.stream().collect(Collectors.toMap(o -> o.getId(), o -> o.getCount()));
        userList.sort((o1, o2) -> {
            Long count1 = idCountMap.getOrDefault(o1.getId(), 0L);
            Long count2 = idCountMap.getOrDefault(o2.getId(), 0L);
            if (count1 > count2) {
                return -1;
            } else if (count1.equals(count2)) {
                return BooleanUtils.toInteger(o1.getId() > o2.getId(), 1, -1);
            } else {
                return 1;
            }
        });
    }

    public void sortSelectProjectList(List<ProjectSelectDTO> projectList) {
        if (CollectionUtils.isEmpty(projectList)) {
            return;
        }
        List<IdCountDTO> idCountDTOList = statisticService.getTestCaseCountListGroupByProductId();
        if (CollectionUtils.isEmpty(idCountDTOList)) {
            return;
        }
        Map<Long, Long> idCountMap = idCountDTOList.stream().collect(Collectors.toMap(o -> o.getId(), o -> o.getCount()));
        projectList.sort((o1, o2) -> {
            Long count1 = idCountMap.getOrDefault(o1.getId(), 0L);
            Long count2 = idCountMap.getOrDefault(o2.getId(), 0L);
            if (count1 > count2) {
                return -1;
            } else if (count1.equals(count2)) {
                return BooleanUtils.toInteger(o1.getId() > o2.getId(), 1, -1);
            } else {
                return 1;
            }
        });
    }


}
