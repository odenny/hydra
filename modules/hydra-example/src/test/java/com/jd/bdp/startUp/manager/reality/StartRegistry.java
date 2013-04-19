package com.jd.bdp.startUp.manager.reality;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: xiangkui
 * Date: 13-4-12
 * Time: 上午10:59
 */

/**
 * 开启管理端
 */
public class StartRegistry {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{
                "/hydra-manager.xml"//真实的hydra-manager系统
        });
        context.start();
        Thread.sleep(999999999);
    }
}
