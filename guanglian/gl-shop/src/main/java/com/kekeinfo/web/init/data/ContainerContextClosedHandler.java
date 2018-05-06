package com.kekeinfo.web.init.data;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.AbandonedConnectionCleanupThread;

/**
 * ServletContextListener
 * web应用容器启动或关闭时，定义创建或销毁操作，防止内存泄漏
 * 销毁数据池连接驱动
 * @author sam
 *
 */
//@WebListener // register it as you wish
public class ContainerContextClosedHandler implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(ContainerContextClosedHandler.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        // nothing to do
    	logger.debug("contextInitialized ServletContextEvent");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    	Enumeration<Driver> drivers = DriverManager.getDrivers();     
        Driver driver = null;

        // clear drivers
        while(drivers.hasMoreElements()) {
            try {
                driver = drivers.nextElement();
                DriverManager.deregisterDriver(driver);

            } catch (SQLException ex) {
                // deregistration failed, might want to do something, log at the very least
            	ex.printStackTrace();
            	logger.error("deregistration failed");
            }
        }

        // MySQL driver leaves around a thread. This static method cleans it up.
        try {
            AbandonedConnectionCleanupThread.shutdown();
        } catch (InterruptedException e) {
            // again failure, not much you can do
        	e.printStackTrace();
        }
    }

}