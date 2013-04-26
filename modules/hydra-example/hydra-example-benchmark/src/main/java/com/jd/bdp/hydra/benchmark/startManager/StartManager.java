package com.jd.bdp.hydra.benchmark.startManager;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: xiangkui
 * Date: 13-4-23
 * Time: 下午6:19
 */
public class StartManager {
    public static void main(String[] args)  {
        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{
                    "classpath*:hydra-manager.xml"
            });
            context.start();
            Thread.sleep(999999999);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
