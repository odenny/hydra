package com.jd.bdp.hydra.benchmark.startWeb;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: xiangkui
 * Date: 13-4-23
 * Time: 下午6:19
 */
public class StartWeb {
    public static void main(Exception[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{
                ""
        });
        context.start();
        Thread.sleep(999999999);
    }
}
