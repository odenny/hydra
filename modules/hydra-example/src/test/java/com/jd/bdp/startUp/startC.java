package com.jd.bdp.startUp;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: xiangkui
 * Date: 13-4-11
 * Time: 下午2:57
 */
public class StartC {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{
                "/dubbo-service-C.xml",
                "/hydra-config.xml"});
        context.start();
        Thread.sleep(999999999);
    }
}
