package com.winbaoxian.testng.platform.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum FileReaderUtils {

    INSTANCE;

    public String getResourceFileContent(String path) {
        try {
            File file = ResourceUtils.getFile("classpath:" + path);
            if (!file.exists()) {
                return null;
            }
            return readFile(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    private String readFile(File file){
        BufferedReader sbr = null;
        try {
            sbr = new BufferedReader(new FileReader(file));
            Stream<String> lineStreams = sbr.lines();
            List<String> lines = lineStreams.collect(Collectors.toList());
            return StringUtils.join(lines, "\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (sbr != null) {
                try {
                    sbr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
