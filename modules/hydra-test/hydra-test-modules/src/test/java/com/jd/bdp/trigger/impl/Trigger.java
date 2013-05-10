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

package com.jd.bdp.trigger.impl;
import com.jd.bdp.service.inter.InterfaceA;
import org.springframework.beans.factory.InitializingBean;

/**
 * User: xiangkui
 * Date: 13-4-9
 * Time: 下午3:25
 */
public class Trigger implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        Thread.sleep(100);//服务预热
    }

    public void startWork(int num) {
        for (int i = 0; i < num; i++) {
            try {
                Object result = rootService.functionA();
                System.out.println("result:" + result);
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    //getter and setter
    private InterfaceA rootService;

    public void setRootService(InterfaceA rootService) {
        this.rootService = rootService;
    }

    public void startWorkWithSleep(int num) throws InterruptedException {
        for (int i = 0; i < num; i++) {
            System.out.println(i);
//            Thread.sleep(1000);
//            if (i == 200){
//                Thread.sleep(20000);
//            }
            Object result = rootService.functionA();
            System.out.println("result:" + result);
        }
    }
}
