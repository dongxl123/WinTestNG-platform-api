package com.winbaoxian.testng.platform.component.listener;

import com.winbaoxian.testng.platform.utils.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

@WebListener
public class SpringContextListener implements ServletContextListener {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        SpringContextHolder.INSTANCE.setApplicationContext(WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext()));
        setupCurrentAppServer(servletContextEvent.getServletContext()); //设置当前应用服务器是哪台
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // ignore
    }

    /**
     * 用于定位当前页面是哪台应用服务器
     *
     * @param servletContext
     */
    private void setupCurrentAppServer(ServletContext servletContext) {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            if (inetAddress != null) {
                String serverIP = inetAddress.getHostAddress();
                String serverName = inetAddress.getHostName();
                servletContext.setAttribute("appServerIPName", serverName + serverIP.substring(serverIP.lastIndexOf(".")));
            }
        } catch (UnknownHostException e) {
            logger.error("SpringContextListener.setupCurrentAppServer", e);
        }
    }
}


