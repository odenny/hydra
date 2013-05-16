package com.jd.bdp.hydra.benchmark.startTrigger;

import com.jd.bdp.hydra.benchmark.startTrigger.support.Trigger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
/**
 * User: xiangkui
 * Date: 13-4-23
 * Time: 下午6:19
 */
public class StartTrigger {
    public static void main(String[] args)  {
        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{
                    "classpath*:trigger-context2.xml"
            });
            context.start();
            Trigger trigger=(Trigger)context.getBean("trigger");
            trigger.startWorkWithSleep(Integer.MAX_VALUE,100);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
