package com.jd.bdp.hydra.tempTest.startCollect;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: xiangkui
 * Date: 13-4-23
 * Time: 下午6:19
 */
public class StartCollectorMysql {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{
                "classpath*:/hydra-collector/hydra-collector-integrationTest.xml"
        });
        context.start();
        Thread.sleep(999999999);
    }
}
