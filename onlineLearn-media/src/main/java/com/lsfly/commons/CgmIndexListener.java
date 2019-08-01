package com.lsfly.commons;

import com.lsfly.util.FFmpegUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author huoquan
 * @date 2018/9/26.
 */
public class CgmIndexListener implements ServletContextListener {
    //开启tomcat前执行线程
    @Override
    public void contextInitialized(ServletContextEvent sce) {
    }
    //关闭tomcat前关闭线程
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("关闭tomcat前关闭所有线程");
        FFmpegUtil.getFFmpegManager().stopAll();
    }
}
