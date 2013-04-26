/*
 * Copyright jd
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.jd.bdp.hydra.benchmark.startTrigger.support;

import com.jd.bdp.service.inter.InterfaceA;
import com.jd.bdp.service.inter.support.Service;
import org.apache.log4j.lf5.viewer.categoryexplorer.TreeModelAdapter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: xiangkui
 * Date: 13-4-9
 * Time: 下午3:25
 */
public class Trigger implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws InterruptedException {
        Thread.sleep(200);//服务预热
    }
    /**
     *
     * @param num  调用次数
     * @param sleepTime  每次调用后沉默时间
     */
    public void startWorkWithSleep(int num,long sleepTime) {
        for (int i = 0; i < num; i++) {
            try {
                Object result = rootService.functionA();
                System.out.println("result:" + result);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }

    }

    //getter and setter
    private InterfaceA rootService;
    public void setRootService(InterfaceA rootService) {
        this.rootService = rootService;
    }

    public static void main(String[] args) throws InterruptedException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{
                "classpath*:trigger-context.xml",
        });
        context.start();
        Trigger trigger = (Trigger)context.getBean("trigger");
        trigger.startWorkWithSleep(5,0);
//        Thread.sleep(999999999);
    }

}
