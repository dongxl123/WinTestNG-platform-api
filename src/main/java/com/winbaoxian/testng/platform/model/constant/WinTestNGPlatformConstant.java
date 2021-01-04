package com.winbaoxian.testng.platform.model.constant;

import com.winbaoxian.testng.constant.WinTestNGConstant;

/**
 * @author dongxuanliang252
 * @date 2019-03-06 18:10
 */
public interface WinTestNGPlatformConstant extends WinTestNGConstant {
    String SORT_COLUMN_CREATE_TIME = "createTime";
    String SORT_COLUMN_START_TIME = "startTime";
    String STRING_FREEMARKER = "freemarker";
    String DASHBOARD_CACHE_KEY_PREFIX = "wintestng-platform-dashboard";
    String COMMON_CACHE_KEY_PREFIX = "wintestng-platform-common";
    String GIT_SUFFIX = ".git";
    String APIDOC_URL_PATTERN = "http://docs.winbaoxian.cn/api/%s/api_data.json";
    String SERVICEDOC_URL_PATTERN = "http://docs.winbaoxian.cn/service/%s/service_data.json";
}
