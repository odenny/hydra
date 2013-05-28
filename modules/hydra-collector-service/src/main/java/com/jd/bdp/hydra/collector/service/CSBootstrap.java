package com.jd.bdp.hydra.collector.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Date: 13-4-16
 * Time: 上午11:01
 */
public class CSBootstrap {

    public static void main(String[] strings){
        try {
            ApplicationContext context = new ClassPathXmlApplicationContext("hydra-collector-service.xml");
            CollectorSerService collectorSerService = (CollectorSerService)context.getBean("collectorService");
            collectorSerService.subscribe();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
