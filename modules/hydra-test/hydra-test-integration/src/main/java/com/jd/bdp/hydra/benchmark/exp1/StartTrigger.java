package com.jd.bdp.hydra.benchmark.exp1;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.jd.bdp.hydra.benchmark.exp1.support.*;
/**
 * User: xiangkui
 * Date: 13-4-23
 * Time: 下午6:19
 */
public class StartTrigger {
    public static void main(String[] args)  {
        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{
                    "classpath*:exp1-trigger-context.xml"
            });
            context.start();
            Trigger trigger=(Trigger)context.getBean("trigger");
            trigger.startWorkWithSleep(Integer.MAX_VALUE,500);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
