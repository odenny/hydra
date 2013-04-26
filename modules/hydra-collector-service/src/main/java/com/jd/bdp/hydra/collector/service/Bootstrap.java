package com.jd.bdp.hydra.collector.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: yfliuyu
 * Date: 13-4-16
 * Time: 上午11:01
 */
public class Bootstrap {
    private CollectorService collectorService;
    private final String topic = "hydra_test";

    public static void main(String[] strings){
        try {
            ApplicationContext context = new ClassPathXmlApplicationContext("hydra-collector-service.xml");
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.collectorService = (CollectorService)context.getBean("collectorService");
            bootstrap.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start()throws Exception{
        collectorService.subscribe(topic);
    }

}
