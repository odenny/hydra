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

import com.jd.bdp.hydra.agent.support.Configuration;
import com.jd.bdp.hydra.dubbo.HydraConfiger;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
/**
 * User: xiangkui
 * Date: 13-4-1
 * Time: 下午1:27
 */
public class MockTest extends AbstractDependencyInjectionSpringContextTests {
    private Trigger trigger;
    private HydraConfiger configer;
    @Override
    protected String[] getConfigLocations() {
        String[] location = {
                "/dubbo-service-context.xml",//业务spring上下文
                "/hydra-config.xml" //hydra标配spring上下文
        };
        return location;
    }

    public void TtestConfiger() throws Exception {
        //1：获取 Hydra感知的配置信息
        Configuration envData=configer.getConfig();
        String appEnvName=envData.getApplicationName();
        List<String> serviceEnvList=envData.getServices();
        //2：获取用户定义的配置的信息
        //2.1 服务配置
        List<String> serviceConfigList = new ArrayList<String>();
        String packageName="com.jd.bdp.service.inter";
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources(packageName.replace('.', '/'));
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        for (File file : dirs.get(0).listFiles()) {
            if (file.getName().endsWith(".class"))
                serviceConfigList.add(packageName + "." + file.getName().substring(0,file.getName().length() - 6));
        }
        //2.2 AppName配置
        String appConfigName="";
        try{
            Properties prop = new Properties();
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("dubbo.properties");
            prop.load(in);
            in.close();
            appConfigName=prop.getProperty("dubbo.application.name").trim();
        }   catch (Exception e){
            e.printStackTrace();
        }
        //3 判断逻辑
        for(String config:serviceConfigList){
            Assert.assertTrue(serviceEnvList.contains(config));
        }
        for(String config:serviceEnvList){
            Assert.assertTrue(serviceConfigList.contains(config));
        }
        Assert.assertEquals(appConfigName, appEnvName);
        Thread.sleep(10);
    }
    @Test
    public void testTriggerService() throws Exception {
        trigger.startWork(10);
    }

    //getter and setter
    public Trigger getTrigger() {
        return trigger;
    }
    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }
    public HydraConfiger getConfiger() {
        return configer;
    }
    public void setConfiger(HydraConfiger configer) {
        this.configer = configer;
    }
}
