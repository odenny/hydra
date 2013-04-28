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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jd.bdp.hydra.Annotation;
import com.jd.bdp.hydra.Endpoint;
import com.jd.bdp.hydra.Span;
import com.jd.bdp.hydra.hbase.service.impl.QueryServiceImpl;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: biandi
 * Date: 13-4-18
 * Time: 下午2:13
 */
public class QueryServiceTest {

    public void testPutAndGet() {
        QueryServiceImpl queryService = new QueryServiceImpl();
//        Date date1 = new Date(1366264570484L);
//        System.out.println(date1);
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd   HH:00:00");
//        String dateStr = formatter.format(date1);
//        ParsePosition pos = new ParsePosition(0);
//        Date date = formatter.parse(dateStr, pos);
//        System.out.println(date);
//        System.out.println(date1.getTime());
//        System.out.println(date.getTime());
//        queryService.getTraces("230630", 1366261200000L, 1366261200000L, 5, 52);



//        JSONObject trace = queryService.getTraceInfo(1366178446534L);
//        assertNull(trace);
        //traceId = 1366178446534
        //span1 : 1366178470630c, 1366178470630s
        //span2 : 1366178496806c, 1366178496806s
        //span3 : 1366178559295c, 1366178559295s

//        queryService.delete("trace", "1366178446534");

//        Endpoint endpoint1c = new Endpoint();
//        endpoint1c.setIp("127.0.0.1");
//        endpoint1c.setPort(1235);
//        endpoint1c.setServiceName("app1");
//
//        //spanBc
//        Span span1c = new Span();
//        span1c.setTraceId(1366178446534L);
//        span1c.setId(1366178470630L);
//        span1c.setSpanName("getSpan1");
//        List<Annotation> annList1c = new ArrayList<Annotation>();
//        Annotation annCs = new Annotation();
//        annCs.setHost(endpoint1c);
//        annCs.setTimestamp(1363910400123L);
//        annCs.setValue(Annotation.CLIENT_SEND);
//        annList1c.add(annCs);
//
//        Annotation annCr = new Annotation();
//        annCr.setHost(endpoint1c);
//        annCr.setTimestamp(1363910400602L);
//        annCr.setValue(Annotation.CLIENT_RECEIVE);
//        annList1c.add(annCr);
//
//        span1c.setAnnotations(annList1c);
//
//        queryService.setOneItem("trace", "span", "1366178446534", "1366178470630c", JSON.toJSONBytes(span1c));
//
//        //spanBs
//        Endpoint endpoint1s = new Endpoint();
//        endpoint1s.setIp("127.0.0.1");
//        endpoint1s.setPort(1235);
//        endpoint1s.setServiceName("app1");
//
//        Span span1s = new Span();
//        span1s.setTraceId(1366178446534L);
//        span1s.setId(1366178470630L);
//        span1s.setSpanName("getSpan1");
//        List<Annotation> annList1s = new ArrayList<Annotation>();
//        Annotation annSr = new Annotation();
//        annSr.setHost(endpoint1s);
//        annSr.setTimestamp(1363910400170L);
//        annSr.setValue(Annotation.SERVER_RECEIVE);
//        annList1s.add(annSr);
//
//        Annotation annSs = new Annotation();
//        annSs.setHost(endpoint1s);
//        annSs.setTimestamp(1363910400582L);
//        annSs.setValue(Annotation.SERVER_SEND);
//        annList1s.add(annSs);
//
//        span1s.setAnnotations(annList1s);
//
//        queryService.setOneItem("trace", "span", "1366178446534", "1366178470630s", JSON.toJSONBytes(span1s));
//
//        //spanBc
//        Endpoint endpointAc = new Endpoint();
//        endpointAc.setIp("127.0.0.1");
//        endpointAc.setPort(1235);
//        endpointAc.setServiceName("app1");
//        Span spanAc = new Span();
//        spanAc.setTraceId(1366178446534L);
//        spanAc.setId(1366178496806L);
//        spanAc.setSpanName("getSpanA");
//        spanAc.setParentId(1366178470630L);
//        List<Annotation> annListAc = new ArrayList<Annotation>();
//        Annotation annACs = new Annotation();
//        annACs.setHost(endpointAc);
//        annACs.setTimestamp(1363910400150L);
//        annACs.setValue(Annotation.CLIENT_SEND);
//        annListAc.add(annACs);
//
//        Annotation annACr = new Annotation();
//        annACr.setHost(endpointAc);
//        annACr.setTimestamp(1363910400300L);
//        annACr.setValue(Annotation.CLIENT_RECEIVE);
//        annListAc.add(annACr);
//
//        spanAc.setAnnotations(annListAc);
//
//        queryService.setOneItem("trace", "span", "1366178446534", "1366178496806c", JSON.toJSONBytes(spanAc));
//
//        //spanBs
//        Endpoint endpointAs = new Endpoint();
//        endpointAs.setIp("127.0.0.1");
//        endpointAs.setPort(1235);
//        endpointAs.setServiceName("app1");
//
//        Span spanAs = new Span();
//        spanAs.setTraceId(1366178446534L);
//        spanAs.setId(1366178496806L);
//        spanAs.setSpanName("getSpanA");
//        spanAc.setParentId(1366178470630L);
//        List<Annotation> annListAs = new ArrayList<Annotation>();
//        Annotation annASr = new Annotation();
//        annASr.setHost(endpointAs);
//        annASr.setTimestamp(1363910400155L);
//        annASr.setValue(Annotation.SERVER_RECEIVE);
//        annListAs.add(annASr);
//
//        Annotation annASs = new Annotation();
//        annASs.setHost(endpointAs);
//        annASs.setTimestamp(1363910400278L);
//        annASs.setValue(Annotation.SERVER_SEND);
//        annListAs.add(annASs);
//
//        spanAs.setAnnotations(annListAs);
//
//        queryService.setOneItem("trace", "span", "1366178446534", "1366178496806s", JSON.toJSONBytes(spanAs));
//
//        //spanBc
//        Endpoint endpointBc = new Endpoint();
//        endpointBc.setIp("127.0.0.1");
//        endpointBc.setPort(1235);
//        endpointBc.setServiceName("app1");
//        Span spanBc = new Span();
//        spanBc.setTraceId(1366178446534L);
//        spanBc.setId(1366178559295L);
//        spanBc.setSpanName("getSpanB");
//        spanBc.setParentId(1366178470630L);
//        List<Annotation> annListBc = new ArrayList<Annotation>();
//        Annotation annBCs = new Annotation();
//        annBCs.setHost(endpointBc);
//        annBCs.setTimestamp(1363910400310L);
//        annBCs.setValue(Annotation.CLIENT_SEND);
//        annListBc.add(annBCs);
//
//        Annotation annBCr = new Annotation();
//        annBCr.setHost(endpointBc);
//        annBCr.setTimestamp(1363910400570L);
//        annBCr.setValue(Annotation.CLIENT_RECEIVE);
//        annListBc.add(annBCr);
//
//        spanBc.setAnnotations(annListBc);
//
//        queryService.setOneItem("trace", "span", "1366178446534", "1366178559295c", JSON.toJSONBytes(spanBc));
//
//        //spanBs
//        Endpoint endpointBs = new Endpoint();
//        endpointBs.setIp("127.0.0.1");
//        endpointBs.setPort(1235);
//        endpointBs.setServiceName("app1");
//
//        Span spanBs = new Span();
//        spanBs.setTraceId(1366178446534L);
//        spanBs.setId(1366178559295L);
//        spanBs.setSpanName("getSpanB");
//        spanBc.setParentId(1366178470630L);
//        List<Annotation> annListBs = new ArrayList<Annotation>();
//        Annotation annBSr = new Annotation();
//        annBSr.setHost(endpointBs);
//        annBSr.setTimestamp(1363910400335L);
//        annBSr.setValue(Annotation.SERVER_RECEIVE);
//        annListBs.add(annBSr);
//
//        Annotation annBSs = new Annotation();
//        annBSs.setHost(endpointBs);
//        annBSs.setTimestamp(1363910400560L);
//        annBSs.setValue(Annotation.SERVER_SEND);
//        annListBs.add(annBSs);
//
//        spanBs.setAnnotations(annListBs);
//
//        queryService.setOneItem("trace", "span", "1366178446534", "1366178559295s", JSON.toJSONBytes(spanBs));

//        queryService.delete("trace", "1366178446534");
    }


    public static void main(String[] args) throws InterruptedException, IOException {
//        QueryServiceImpl queryService = new QueryServiceImpl();
//        JSONArray array = queryService.getTracesByDuration("22001", 1366614281241L, 20, 60, 500);
//        System.out.println(array.size());
//        System.out.println(array);
        QueryServiceTest q = new QueryServiceTest();
        q.testPutAndGet();
    }


}
