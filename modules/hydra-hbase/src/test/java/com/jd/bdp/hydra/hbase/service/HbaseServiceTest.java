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

package com.jd.bdp.hydra.hbase.service;

import com.jd.bdp.hydra.Annotation;
import com.jd.bdp.hydra.Endpoint;
import com.jd.bdp.hydra.Span;
import com.jd.bdp.hydra.hbase.service.impl.HbaseServiceImpl;

/**
 * User: biandi
 * Date: 13-4-22
 * Time: 下午2:26
 */
public class HbaseServiceTest {

    public static void main(String[] args){
        HbaseServiceImpl hbaseService = new HbaseServiceImpl();
        for(int i = 0 ; i < 100;i++){
            if(i%2 == 0){
                Span span = new Span();
                Endpoint endpoint = new Endpoint();
                endpoint.setServiceName("22001");
                endpoint.setPort(1234);
                endpoint.setIp("127.0.0."+i);
                Annotation cs = new Annotation();
                cs.setHost(endpoint);
                cs.setTimestamp(System.currentTimeMillis());
                cs.setValue("cs");
                Annotation cr = new Annotation();
                cr.setTimestamp(System.currentTimeMillis()+(int)(Math.random() * 100));
                cr.setValue("cr");
                cr.setHost(endpoint);
                span.setId(new Long(i));
                span.setSample(true);
                span.setTraceId(1366178446534L);
                span.setSpanName("method_"+i);
                span.setParentId(new Long(i+1));
                span.addAnnotation(cr);
                span.addAnnotation(cs);
                try {
//                    hbaseService.addSpan(span);
                    hbaseService.durationIndex(span);
//                    hbaseService.annotationIndex(span);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                Span span = new Span();
                Endpoint endpoint = new Endpoint();
                endpoint.setServiceName("22001");
                endpoint.setPort(4321);
                endpoint.setIp("127.0.0."+i);
                Annotation sr = new Annotation();
                sr.setHost(endpoint);
                sr.setTimestamp(System.currentTimeMillis());
                sr.setValue("sr");
                Annotation ss = new Annotation();
                ss.setTimestamp(System.currentTimeMillis()+(int)(Math.random() * 100));
                ss.setValue("ss");
                ss.setHost(endpoint);
                span.setParentId(new Long(i+1));
                span.setId(new Long(i));
                span.setSpanName("method_"+i);
                span.setTraceId(1366178446534L);
                span.addAnnotation(sr);
                span.addAnnotation(ss);
                try{
//                    hbaseService.addSpan(span);
                    hbaseService.durationIndex(span);
//                    hbaseService.annotationIndex(span);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
