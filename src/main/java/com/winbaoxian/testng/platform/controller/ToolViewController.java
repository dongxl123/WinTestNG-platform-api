package com.winbaoxian.testng.platform.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping(value = "/view/tool")
public class ToolViewController {

    @Value("${project.domain}")
    private String projectDomain;

    @RequestMapping(value = "freemarker")
    public String freemarker(Map<String, Object> model) {
        model.put("projectDomain", projectDomain);
        return "toolFreemarker";
    }

}
