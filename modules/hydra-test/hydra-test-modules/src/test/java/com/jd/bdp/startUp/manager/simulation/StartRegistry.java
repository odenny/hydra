package com.jd.bdp.startUp.manager.simulation;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: xiangkui
 * Date: 13-4-12
 * Time: 上午10:32
 */
public class StartRegistry {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{
                "/registry/manager/registry.xml"
        });
        context.start();
        Thread.sleep(999999999);
    }
}
