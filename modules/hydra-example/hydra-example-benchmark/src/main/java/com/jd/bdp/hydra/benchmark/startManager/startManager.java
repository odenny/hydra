package com.jd.bdp.hydra.benchmark.startManager;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: xiangkui
 * Date: 13-4-23
 * Time: 下午6:19
 */
public class startManager {
    public static void main(Exception[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{
                "/hydra-manager.xml"
        });
        context.start();
        Thread.sleep(999999999);
    }
}
