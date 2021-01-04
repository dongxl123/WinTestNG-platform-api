package com.winbaoxian.testng.platform.service;

import com.winbaoxian.testng.platform.exception.WinTestNgPlatformException;
import com.winbaoxian.testng.platform.model.dto.VerifyTextDTO;
import com.winbaoxian.testng.platform.utils.OSSClientHelper;
import com.winbaoxian.testng.utils.DateFormatUtils;
import com.winbaoxian.testng.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author dongxuanliang252
 * @date 2019-03-20 12:01
 */
@Service
@Slf4j
public class CommonService {

    @Resource
    private OSSClientHelper ossClientHelper;

    public String uploadFile(MultipartFile file) {
        try {
            String fileName = UUIDUtil.INSTANCE.randomUUID();
            String folderName = "testPlatform/" + DateFormatUtils.INSTANCE.ymdFormat(new Date());
            String remoteUrl = ossClientHelper.uploadObject(folderName, fileName, file);
            return remoteUrl;
        } catch (Exception e) {
            log.error("ossClientHelper uploadObject error", e);
            throw new WinTestNgPlatformException("上传文件失败");
        }
    }

    public boolean verifyText(VerifyTextDTO dto) {
        return true;
    }

}
