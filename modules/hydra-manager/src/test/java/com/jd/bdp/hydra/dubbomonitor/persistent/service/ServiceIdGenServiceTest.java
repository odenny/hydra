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

package com.jd.bdp.hydra.dubbomonitor.persistent.service;

import com.jd.bdp.hydra.dubbomonitor.persistent.dao.ServiceIdGenMapper;
import com.jd.bdp.hydra.dubbomonitor.persistent.entity.ServiceIdGen;
import org.junit.Test;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import java.util.HashMap;
import java.util.Map;

/**
 * User: biandi
 * Date: 13-4-16
 * Time: 上午11:05
 */
public class ServiceIdGenServiceTest extends AbstractDependencyInjectionSpringContextTests {

    @Test
    public void testGetNewId(){
        int id1 = serviceIdGenService.getNewServiceId();
        int id2 = serviceIdGenService.getNewServiceId();
        ServiceIdGen serviceIdGen = serviceIdGenMapper.getServiceIdGen();
        int idScope = serviceIdGen.getIdScope();
        int headLength = serviceIdGen.getMaxHead().toString().length();

        String id1Head = null;
        String id2Head = null;
        if (headLength == 1){//如果head最大值为1位
            id1Head = String.valueOf(id1).substring(0, headLength);
            id2Head = String.valueOf(id2).substring(0, headLength);
        }else if (headLength == 2){//如果head最大值为1位
            int idScopeLength = String.valueOf(idScope).length();
            if (String.valueOf(id1).length() < 1 + idScopeLength){
                id1Head = String.valueOf(id1).substring(0, 1);
            }else {
                id1Head = String.valueOf(id1).substring(0, headLength);
            }

            if (String.valueOf(id2).length() < 1 + idScopeLength){
                id2Head = String.valueOf(id2).substring(0, 1);
            }else {
                id2Head = String.valueOf(id2).substring(0, headLength);
            }
        }else {//暂不支持超过3位的head
            assertTrue(false);
        }
        //首先比较head是否不同，且在遭遇最大值前自增
        if (Integer.parseInt(id2Head) > Integer.parseInt(id1Head)){
            assertTrue(true);
        }else if (Integer.parseInt(id2Head) < Integer.parseInt(id1Head)){
            assertEquals(id1Head, serviceIdGen.getMaxHead());
        }else {
            assertTrue(false);
        }
        //比较之后的几位不同
        int trueId1 = Integer.parseInt(String.valueOf(id1).substring(id1Head.length()));
        int trueId2 = Integer.parseInt(String.valueOf(id2).substring(id2Head.length()));
        assertTrue(trueId2 > trueId1);
    }


    @Test
    public void testServiceIdDifferent(){
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        new Thread(new TestThread(map, serviceIdGenService)).run();
        new Thread(new TestThread(map, serviceIdGenService)).run();
        new Thread(new TestThread(map, serviceIdGenService)).run();
        assertEquals(150, map.size());
    }

    private class TestThread implements Runnable{

        private Map<Integer, Integer> map;
        private ServiceIdGenService serviceIdGenService;

        private TestThread(Map<Integer, Integer> map, ServiceIdGenService serviceIdGenService){
            this.map = map;
            this.serviceIdGenService = serviceIdGenService;
        }

        @Override
        public void run() {
            for (int i = 0; i < 50; i++) {
                int id = serviceIdGenService.getNewServiceId();
                map.put(id, id);
            }
        }
    }


    @Override
    protected String[] getConfigLocations() {
        String[] location = {"classpath:hydra-manager.xml"};
        return location;
    }

    private ServiceIdGenMapper serviceIdGenMapper;
    private ServiceIdGenService serviceIdGenService;

    public void setServiceIdGenService(ServiceIdGenService serviceIdGenService) {
        this.serviceIdGenService = serviceIdGenService;
    }

    public void setServiceIdGenMapper(ServiceIdGenMapper serviceIdGenMapper) {
        this.serviceIdGenMapper = serviceIdGenMapper;
    }
}
