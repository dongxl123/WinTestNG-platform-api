package com.winbaoxian.testng.platform.utils;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.google.common.io.Files;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;


public class OSSClientHelper {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final String DEFAULT_ACCESS_ID = "j1TSeq91vy*KACp*";
    private static final String DEFAULT_ACCESS_KEY = "Yef8L9h7Z*C6PU7gHjH*TDwukoZzH*";
    public static final String DEFAULT_ENDPOINT = "https://oss-cn-hangzhou.aliyuncs.com";
    private static final String DEFAULT_OSS_BUCKET_NAME = "wyjhs";
    public static final String DEFAULT_OSS_BUCKET_URL = "http://media.winbaoxian.com/";

    private String accessId = DEFAULT_ACCESS_ID;
    private String accessKey = DEFAULT_ACCESS_KEY;
    private String ossBucketName = DEFAULT_OSS_BUCKET_NAME;
    private String ossBucketUrl = DEFAULT_OSS_BUCKET_URL;
    private String endpoint = DEFAULT_ENDPOINT;

    private OSSClient client;

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public void setOssBucketName(String ossBucketName) {
        this.ossBucketName = ossBucketName;
    }

    public void setOssBucketUrl(String ossBucketUrl) {
        this.ossBucketUrl = ossBucketUrl;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    private synchronized OSSClient getClient() {
        if (client == null) {
            try {
                client = new OSSClient(endpoint, accessId, accessKey);
                logger.debug("OSS|初始化OssClient");
            } catch (Exception e) {
                logger.error("OSS|初始化错误", e);
            }
        }
        return client;
    }


    public String uploadObjectFromUrl(String folderName, String fileName, String url) {
        try {
            if (StringUtils.isNotBlank(url)) {
                // 上传
                URLConnection connection = new URL(url).openConnection();
                ObjectMetadata objectMeta = new ObjectMetadata();
                if (connection != null) {
                    // 可以在metadata中标记文件类型
                    if (StringUtils.isNotBlank(connection.getContentType())) {
                        objectMeta.setContentType(connection.getContentType());
                    }
                    StringBuilder key = new StringBuilder(folderName);
                    if (!folderName.endsWith("/")) {
                        key.append("/");
                    }
                    key.append(fileName);
                    key.append(".").append(Files.getFileExtension(url));
                    try(InputStream inputStream = new URL(url).openStream()) {
                        OSSClient ossClient = getClient();
                        if (ossClient != null) {
                            ossClient.putObject(ossBucketName, key.toString(), inputStream, objectMeta);
                        }
                    }
                    return ossBucketUrl + key;
                }
            }
        } catch (Exception ex) {
            logger.error("OSS上传失败 ", ex);
        }
        return null;
    }

    public String uploadObject(String folderName, String fileName, String suffix, byte[] bytes) {
        try {
            ObjectMetadata objectMeta = new ObjectMetadata();
            objectMeta.setContentLength(bytes.length);
            // 可以在metadata中标记文件类型
            objectMeta.setContentType("image");
            StringBuilder key = new StringBuilder(folderName);
            if (!folderName.endsWith("/")) {
                key.append("/");
            }
            key.append(fileName);
            key.append(".").append(suffix);
            OSSClient ossClient = getClient();
            if (ossClient != null) {
                ossClient.putObject(ossBucketName, key.toString(), new ByteArrayInputStream(bytes), objectMeta);
            }
            return ossBucketUrl + key;
        } catch (Exception ex) {
            logger.error("OSS上传失败 ", ex);
        }
        return null;
    }


    public String uploadObject(String folderName, String fileName, File file) {
        try {
            ObjectMetadata objectMeta = new ObjectMetadata();
            objectMeta.setContentLength(file.length());
            // 可以在metadata中标记文件类型
            objectMeta.setContentType("image");
            if (file != null) {
                StringBuilder key = new StringBuilder(folderName);
                if (!folderName.endsWith("/")) {
                    key.append("/");
                }
                key.append(fileName);
                key.append(".").append(Files.getFileExtension(file.getName()));
                OSSClient ossClient = getClient();
                if (ossClient != null) {
                    ossClient.putObject(ossBucketName, key.toString(), new FileInputStream(file), objectMeta);
                }
                return ossBucketUrl + key;
            }
        } catch (Exception ex) {
            logger.error("OSS上传失败 ", ex);
        }
        return null;
    }

    public String uploadObject(String folderName, String fileName, MultipartFile file) throws Exception {
        try {

            ObjectMetadata objectMeta = new ObjectMetadata();
            objectMeta.setContentLength(file.getSize());
            // 可以在metadata中标记文件类型
            String contentType = file.getContentType();
            String originalFilename = file.getOriginalFilename();
            if (StringUtils.isNotEmpty(contentType)) {
                objectMeta.setContentType(contentType);
            }
            if (file != null) {
                StringBuilder key = new StringBuilder(folderName);
                if (!folderName.endsWith("/")) {
                    key.append("/");
                }
                key.append(fileName);
                if (StringUtils.isNotBlank(originalFilename)) {
                    key.append(".").append(Files.getFileExtension(originalFilename));
                }
                OSSClient ossClient = getClient();
                if (ossClient != null) {
                    ossClient.putObject(ossBucketName, key.toString(), file.getInputStream(), objectMeta);
                }
                return ossBucketUrl + key.toString();
            }
        } catch (Exception ex) {
            logger.error("OSS上传失败 ", ex);
        }
        return null;
    }


    /**
     * 获取OSS 文件
     *
     * @param key
     * @return 文件路径
     */
    public String getObject(String key) {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            if (StringUtils.isBlank(key)) {
                return null;
            }
            String suffix = key.split("\\.")[1];
            StringBuilder sb = new StringBuilder();
            if ("Windows_NT".equals(System.getenv().get("OS"))) {
                sb.append("E://").append(System.currentTimeMillis()).append(".").append(suffix);
            } else {
                sb.append("/tmp/").append(System.currentTimeMillis()).append(".").append(suffix);
            }
            OSSClient ossClient = getClient();
            if (ossClient != null) {
                String tempFilePath = sb.toString();
                OSSObject ossObject = ossClient.getObject(ossBucketName, key);
                File f = new File(tempFilePath);
                is = ossObject.getObjectContent();
                fos = new FileOutputStream(f);
                byte[] bf = new byte[1024];
                int readSize = is.read(bf);
                while (readSize > 0) {
                    fos.write(bf, 0, readSize);
                    readSize = is.read(bf);
                }
                fos.flush();
                return tempFilePath;
            }
            return null;
        } catch (Exception ex) {
            logger.error("getObject error", ex);
            return null;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {

            }
        }
    }
}

