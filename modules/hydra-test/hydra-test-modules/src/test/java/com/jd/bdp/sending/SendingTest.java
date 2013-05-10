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

package com.jd.bdp.sending;

import com.jd.bdp.hydra.Annotation;
import com.jd.bdp.hydra.Span;
import com.jd.bdp.trigger.impl.Trigger;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: biandi
 * Date: 13-4-10
 * Time: 下午2:44
 */
public class SendingTest extends AbstractDependencyInjectionSpringContextTests {

    @Override
    protected String[] getConfigLocations() {
        String[] location = {"/dubbo-service-context.xml",
                "/hydra-config.xml",
                "/sending/test-sending.xml"
        };
        return location;
    }


    //发起4次RPC，验证所发送的Span数据是否符合Hydra业务规则
    public void testSendSpanSum4() throws InterruptedException {
        int sum = 4;
        collectSpanService.clear();
        trigger.startWork(sum);
        Thread.sleep(80 * sum);
        List<Span> list = collectSpanService.getAllSpan();
        collectSpanService.clear();

        Map<String, List<Span>> map = new HashMap<String, List<Span>>();
        for(Span span : list){
            assertNotNull(span.getTraceId());
            assertNotNull(span.getId());
            String spanName= span.getSpanName();
            if (map.containsKey(spanName)){
                List<Span> l = map.get(spanName);
                l.add(span);
                map.put(spanName, l);
            }else {
                List<Span> l = new ArrayList<Span>();
                l.add(span);
                map.put(spanName, l);
            }
        }
        assertMap(map, sum);
    }

    private void assertMap(Map<String, List<Span>> map, int sum) {
        Map<String, Integer> annMapA = new HashMap<String, Integer>();
        Map<String, Integer> annMapB = new HashMap<String, Integer>();
        Map<String, Integer> annMapC = new HashMap<String, Integer>();
        for(Map.Entry<String, List<Span>> entry : map.entrySet()){
            String key = entry.getKey();
            if (key.equals("functionA")){
                assertEquals(sum*2, entry.getValue().size());
                List<Span> listA = entry.getValue();
                for(Span spanA : listA){
                    assertNull(spanA.getParentId());
                    for (int i = 0; i < spanA.getAnnotations().size(); i++) {
                        Annotation ann = spanA.getAnnotations().get(i);
                        if (annMapA.containsKey(ann.getValue())){
                            annMapA.put(ann.getValue(), annMapA.get(ann.getValue()) + 1);
                        }else {
                            annMapA.put(ann.getValue(), 1);
                        }
                    }
                }
            }else if (key.equals("functionB")){
                assertEquals(sum*2, entry.getValue().size());
                List<Span> listB = entry.getValue();
                for(Span spanB : listB){
                    assertNotNull(spanB.getParentId());
                    for (int i = 0; i < spanB.getAnnotations().size(); i++) {
                        Annotation ann = spanB.getAnnotations().get(i);
                        if (annMapB.containsKey(ann.getValue())){
                            annMapB.put(ann.getValue(), annMapB.get(ann.getValue()) + 1);
                        }else {
                            annMapB.put(ann.getValue(), 1);
                        }
                    }
                }
            }else if (key.equals("functionC")){
                assertEquals(sum, entry.getValue().size());
                List<Span> listC = entry.getValue();
                for(Span spanC : listC){
                    assertNotNull(spanC.getParentId());
                    for (int i = 0; i < spanC.getAnnotations().size(); i++) {
                        Annotation ann = spanC.getAnnotations().get(i);
                        if (annMapC.containsKey(ann.getValue())){
                            annMapC.put(ann.getValue(), annMapC.get(ann.getValue()) + 1);
                        }else {
                            annMapC.put(ann.getValue(), 1);
                        }
                    }
                }
            }
        }
        assertEquals(sum, annMapA.get("cr").intValue());
        assertEquals(sum, annMapA.get("cs").intValue());
        assertEquals(sum, annMapA.get("sr").intValue());
        assertEquals(sum, annMapA.get("ss").intValue());
        assertEquals(sum, annMapB.get("cr").intValue());
        assertEquals(sum, annMapB.get("cs").intValue());
        assertEquals(sum, annMapB.get("sr").intValue());
        assertEquals(sum, annMapB.get("ss").intValue());
        assertEquals(sum/2, annMapC.get("cr").intValue());
        assertEquals(sum/2, annMapC.get("cs").intValue());
        assertEquals(sum/2, annMapC.get("sr").intValue());
        assertEquals(sum/2, annMapC.get("ss").intValue());
    }

    //发起1000次PRC，最终SPAN个数符合业务系统跟踪所计算出来的理论值
    //进行这个测试时需要把动态采样率关掉，保证每一条都采样
    public void testSendSpanSum1000() throws InterruptedException {
        int sum = 1000;
        collectSpanService.clear();
        trigger.startWork(sum);
        Thread.sleep(80 * sum);
        List<Span> list = collectSpanService.getAllSpan();
        collectSpanService.clear();

        Map<String, List<Span>> map = new HashMap<String, List<Span>>();
        for(Span span : list){
            assertNotNull(span.getTraceId());
            assertNotNull(span.getId());
            String spanName= span.getSpanName();
            if (map.containsKey(spanName)){
                List<Span> l = map.get(spanName);
                l.add(span);
                map.put(spanName, l);
            }else {
                List<Span> l = new ArrayList<Span>();
                l.add(span);
                map.put(spanName, l);
            }
        }
        assertMap(map, sum);
    }

    //发起10000次PRC,不做数据校验，只模拟当leaderService和hydraService阻塞或停止或重启的情况是否符合预期
    //需要停止内部的collectSpanService的注入，因为会延迟
    public void testSendSpanSum10000() throws InterruptedException {
        int sum = 1000000;
        collectSpanService.clear();
        trigger.startWorkWithSleep(sum);
    }

    private Trigger trigger;
    private TestCollectSpanService collectSpanService;

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }

    public void setCollectSpanService(TestCollectSpanService collectSpanService) {
        this.collectSpanService = collectSpanService;
    }
}
