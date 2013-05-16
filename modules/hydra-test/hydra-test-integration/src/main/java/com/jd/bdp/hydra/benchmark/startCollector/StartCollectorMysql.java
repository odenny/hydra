package com.jd.bdp.hydra.benchmark.startCollector;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: xiangkui
 * Date: 13-4-23
 * Time: 下午6:19
 */
public class StartCollectorMysql {

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{
                "classpath*:dubbo-hydra-provider-mysql.xml"
        });
        context.start();
        Thread.sleep(999999999);
    }
}
