/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package demo.consumer;

import demo.DemoService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Random;

public class DemoConsumer {

    public static void main(String[] args) throws Exception {

        try {

            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{
                    "dubbo-demo-consumer.xml",
                    "/hydra-config.xml"
            });
            context.start();
            DemoService service = (DemoService) context.getBean("demoService"); // 获取远程服务代理

            int i = 1000000;
            while (i-- > 0) {
                String str = service.sayHello("hello~");
                System.out.println("message:" + str);
                Random random = new Random();
                long time = 0 + random.nextInt(2000);
                Thread.sleep(time);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}