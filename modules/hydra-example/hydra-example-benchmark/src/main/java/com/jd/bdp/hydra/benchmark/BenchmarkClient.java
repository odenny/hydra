package com.jd.bdp.hydra.benchmark;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: xiangkui
 * Date: 13-4-19
 * Time: 下午5:24
 */
public class BenchmarkClient {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{
                "/trigger-context.xml",
        });
        context.start();
        Thread.sleep(999999999);
    }
}
