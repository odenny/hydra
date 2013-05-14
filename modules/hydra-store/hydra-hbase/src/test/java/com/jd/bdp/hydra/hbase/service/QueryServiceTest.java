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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jd.bdp.hydra.Annotation;
import com.jd.bdp.hydra.BinaryAnnotation;
import com.jd.bdp.hydra.Endpoint;
import com.jd.bdp.hydra.Span;
import com.jd.bdp.hydra.hbase.service.impl.HbaseUtils;
import com.jd.bdp.hydra.store.inter.InsertService;
import com.jd.bdp.hydra.store.inter.QueryService;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.junit.Test;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: biandi
 * Date: 13-4-18
 * Time: 下午2:13
 */
public class QueryServiceTest extends AbstractDependencyInjectionSpringContextTests {

    @Override
    protected String[] getConfigLocations() {
        this.setAutowireMode(AUTOWIRE_BY_NAME);
        String[] location = {"classpath:hydra-hbase-test.xml"};
        return location;
    }


    @Test
    public void testAll() {
        try {
            truncateAllTables();
            prepareTestData();
            //测试duration表查询
            JSONArray array1 = queryService.getTracesByDuration("10101", 1363910400124L, 2, 10, 15);
            assertEquals(1, array1.size());
            JSONObject obj = array1.getJSONObject(0);
            assertEquals("10101", obj.get("serviceId"));
            assertEquals(1363910400133L, obj.getLong("timestamp").longValue());
            assertEquals(11, obj.getInteger("duration").intValue());
            assertEquals(1366178446536L, obj.getLong("traceId").longValue());

            //测试异常信息
            array1 = queryService.getTracesByEx("10101", 1363910400124L, 3);
            assertEquals(1, array1.size());
            obj = array1.getJSONObject(0);
            assertEquals("10101", obj.get("serviceId"));
            assertEquals(1363910400136L, obj.getLong("timestamp").longValue());
            assertEquals(1366178446536L, obj.getLong("traceId").longValue());
            assertEquals("abc", obj.getString("exInfo"));

            //测试跟踪详细信息
            obj = queryService.getTraceInfo(1366178446534L);
            assertEquals("1366178446534", obj.getString("traceId"));
            assertEquals("1363910400123", obj.getString("minTimestamp"));
            assertEquals("1363910400602", obj.getString("maxTimestamp"));
            assertTrue(obj.getBoolean("available"));
            assertNotNull(obj.get("rootSpan"));
            JSONObject rootSpan = obj.getJSONObject("rootSpan");
            assertEquals("1366178470630", rootSpan.getString("id"));
            assertEquals("479", rootSpan.getString("durationClient"));
            assertEquals("412", rootSpan.getString("durationServer"));
            assertEquals(2, rootSpan.getJSONArray("children").size());
            JSONObject span1 = null;
            JSONObject span2 = null;
            for (Object temp : rootSpan.getJSONArray("children")) {
                if (((JSONObject) temp).getString("id").equals("1366178496806")) {
                    span1 = (JSONObject) temp;
                }
                if (((JSONObject) temp).getString("id").equals("1366178559295")) {
                    span2 = (JSONObject) temp;
                }
            }
            assertNotNull(span1);
            assertNotNull(span2);
            assertEquals("150", span1.getString("durationClient"));
            assertEquals("123", span1.getString("durationServer"));

            assertEquals("260", span2.getString("durationClient"));
            assertEquals("225", span2.getString("durationServer"));
            assertNotNull(span2.get("exception"));
            JSONObject e = span2.getJSONObject("exception");
            assertEquals("dubbo.exception", e.getString("key"));
            assertEquals("abc", e.getString("value"));
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        } finally {
            try {
                truncateAllTables();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void prepareTestData() throws IOException {
        //duration测试数据
        Endpoint endpointD1 = new Endpoint();
        endpointD1.setIp("127.0.0.1");
        endpointD1.setPort(1235);

        Span spanD1 = new Span();
        spanD1.setTraceId(1366178446535L);
        spanD1.setId(1366178470730L);
        spanD1.setSpanName("getSpanD1");
        spanD1.setServiceId("10101");
        List<Annotation> annListD1 = new ArrayList<Annotation>();
        Annotation annD1cs = new Annotation();
        annD1cs.setHost(endpointD1);
        annD1cs.setTimestamp(1363910400128L);
        annD1cs.setValue(Annotation.CLIENT_SEND);
        annListD1.add(annD1cs);

        Annotation annD1cr = new Annotation();
        annD1cr.setHost(endpointD1);
        annD1cr.setTimestamp(1363910400602L);
        annD1cr.setValue(Annotation.CLIENT_RECEIVE);
        annListD1.add(annD1cr);

        spanD1.setAnnotations(annListD1);

        Endpoint endpointD2 = new Endpoint();
        endpointD2.setIp("127.0.0.1");
        endpointD2.setPort(1235);


        //这条应该在duration测试中被查出
        Span spanD2 = new Span();
        spanD2.setTraceId(1366178446536L);
        spanD2.setId(1366178470731L);
        spanD2.setSpanName("getSpanD1");
        spanD2.setServiceId("10101");
        List<Annotation> annListD2 = new ArrayList<Annotation>();
        Annotation annD2cs = new Annotation();
        annD2cs.setHost(endpointD2);
        annD2cs.setTimestamp(1363910400133L);
        annD2cs.setValue(Annotation.CLIENT_SEND);
        annListD2.add(annD2cs);

        Annotation annD2cr = new Annotation();
        annD2cr.setHost(endpointD2);
        annD2cr.setTimestamp(1363910400144L);
        annD2cr.setValue(Annotation.CLIENT_RECEIVE);
        annListD2.add(annD2cr);

        spanD2.setAnnotations(annListD2);


        //应该在异常信息中被查出
        Span spanD3 = new Span();
        spanD3.setTraceId(1366178446536L);
        spanD3.setId(1366178470731L);
        spanD3.setSpanName("getSpanD1");
        spanD3.setServiceId("10101");
        List<Annotation> annListD3 = new ArrayList<Annotation>();
        Annotation annD3cs = new Annotation();
        annD3cs.setHost(endpointD2);
        annD3cs.setTimestamp(1363910400136L);
        annD3cs.setValue(Annotation.CLIENT_SEND);
        annListD3.add(annD3cs);

        Annotation annD3cr = new Annotation();
        annD3cr.setHost(endpointD2);
        annD3cr.setTimestamp(1363910400602L);
        annD3cr.setValue(Annotation.CLIENT_RECEIVE);
        annListD3.add(annD3cr);

        spanD3.setAnnotations(annListD3);

        BinaryAnnotation ba = new BinaryAnnotation();
        ba.setKey(HbaseUtils.DUBBO_EXCEPTION);
        ba.setValue("abc");
        List<BinaryAnnotation> baList = new ArrayList<BinaryAnnotation>();
        baList.add(ba);
        spanD3.setBinaryAnnotations(baList);

        insertService.addTrace(spanD1);
        insertService.addAnnotation(spanD1);
        insertService.addTrace(spanD2);
        insertService.addAnnotation(spanD2);
        insertService.addTrace(spanD3);
        insertService.addAnnotation(spanD3);

        //以下是一个完整trace
//        traceId = 1366178446534
//        span1 : 1366178470630c, 1366178470630s
//        span2 : 1366178496806c, 1366178496806s
//        span3 : 1366178559295c, 1366178559295s
        Endpoint endpoint1c = new Endpoint();
        endpoint1c.setIp("127.0.0.1");
        endpoint1c.setPort(1235);

        Span span1c = new Span();
        span1c.setTraceId(1366178446534L);
        span1c.setId(1366178470630L);
        span1c.setSpanName("getSpan1");
        span1c.setServiceId("10101");
        List<Annotation> annList1c = new ArrayList<Annotation>();
        Annotation annCs = new Annotation();
        annCs.setHost(endpoint1c);
        annCs.setTimestamp(1363910400123L);
        annCs.setValue(Annotation.CLIENT_SEND);
        annList1c.add(annCs);

        Annotation annCr = new Annotation();
        annCr.setHost(endpoint1c);
        annCr.setTimestamp(1363910400602L);
        annCr.setValue(Annotation.CLIENT_RECEIVE);
        annList1c.add(annCr);

        span1c.setAnnotations(annList1c);


        Endpoint endpoint1s = new Endpoint();
        endpoint1s.setIp("127.0.0.1");
        endpoint1s.setPort(1235);

        Span span1s = new Span();
        span1s.setTraceId(1366178446534L);
        span1s.setId(1366178470630L);
        span1s.setSpanName("getSpan1");
        span1s.setServiceId("10101");
        List<Annotation> annList1s = new ArrayList<Annotation>();
        Annotation annSr = new Annotation();
        annSr.setHost(endpoint1s);
        annSr.setTimestamp(1363910400170L);
        annSr.setValue(Annotation.SERVER_RECEIVE);
        annList1s.add(annSr);

        Annotation annSs = new Annotation();
        annSs.setHost(endpoint1s);
        annSs.setTimestamp(1363910400582L);
        annSs.setValue(Annotation.SERVER_SEND);
        annList1s.add(annSs);

        span1s.setAnnotations(annList1s);

        insertService.addSpan(span1c);
        insertService.addSpan(span1s);
        insertService.addAnnotation(span1c);
        insertService.addAnnotation(span1s);
        insertService.addTrace(span1c);
        insertService.addTrace(span1s);

        Endpoint endpointAc = new Endpoint();
        endpointAc.setIp("127.0.0.1");
        endpointAc.setPort(1235);

        Span spanAc = new Span();
        spanAc.setTraceId(1366178446534L);
        spanAc.setId(1366178496806L);
        spanAc.setSpanName("getSpanA");
        spanAc.setParentId(1366178470630L);
        spanAc.setServiceId("10102");
        List<Annotation> annListAc = new ArrayList<Annotation>();
        Annotation annACs = new Annotation();
        annACs.setHost(endpointAc);
        annACs.setTimestamp(1363910400150L);
        annACs.setValue(Annotation.CLIENT_SEND);
        annListAc.add(annACs);

        Annotation annACr = new Annotation();
        annACr.setHost(endpointAc);
        annACr.setTimestamp(1363910400300L);
        annACr.setValue(Annotation.CLIENT_RECEIVE);
        annListAc.add(annACr);

        spanAc.setAnnotations(annListAc);

        Endpoint endpointAs = new Endpoint();
        endpointAs.setIp("127.0.0.1");
        endpointAs.setPort(1235);

        Span spanAs = new Span();
        spanAs.setTraceId(1366178446534L);
        spanAs.setId(1366178496806L);
        spanAs.setSpanName("getSpanA");
        spanAs.setParentId(1366178470630L);
        spanAs.setServiceId("10102");
        List<Annotation> annListAs = new ArrayList<Annotation>();
        Annotation annASr = new Annotation();
        annASr.setHost(endpointAs);
        annASr.setTimestamp(1363910400155L);
        annASr.setValue(Annotation.SERVER_RECEIVE);
        annListAs.add(annASr);

        Annotation annASs = new Annotation();
        annASs.setHost(endpointAs);
        annASs.setTimestamp(1363910400278L);
        annASs.setValue(Annotation.SERVER_SEND);
        annListAs.add(annASs);

        spanAs.setAnnotations(annListAs);

        insertService.addSpan(spanAc);
        insertService.addSpan(spanAs);
        insertService.addAnnotation(spanAc);
        insertService.addAnnotation(spanAs);
        insertService.addTrace(spanAc);
        insertService.addTrace(spanAs);

        //spanBc
        Endpoint endpointBc = new Endpoint();
        endpointBc.setIp("127.0.0.1");
        endpointBc.setPort(1235);

        Span spanBc = new Span();
        spanBc.setTraceId(1366178446534L);
        spanBc.setId(1366178559295L);
        spanBc.setSpanName("getSpanB");
        spanBc.setParentId(1366178470630L);
        spanBc.setServiceId("10103");
        List<Annotation> annListBc = new ArrayList<Annotation>();
        Annotation annBCs = new Annotation();
        annBCs.setHost(endpointBc);
        annBCs.setTimestamp(1363910400310L);
        annBCs.setValue(Annotation.CLIENT_SEND);
        annListBc.add(annBCs);

        Annotation annBCr = new Annotation();
        annBCr.setHost(endpointBc);
        annBCr.setTimestamp(1363910400570L);
        annBCr.setValue(Annotation.CLIENT_RECEIVE);
        annListBc.add(annBCr);

        spanBc.setAnnotations(annListBc);

        //spanBs
        Endpoint endpointBs = new Endpoint();
        endpointBs.setIp("127.0.0.1");
        endpointBs.setPort(1235);

        Span spanBs = new Span();
        spanBs.setTraceId(1366178446534L);
        spanBs.setId(1366178559295L);
        spanBs.setSpanName("getSpanB");
        spanBs.setParentId(1366178470630L);
        spanBs.setServiceId("10103");

        List<Annotation> annListBs = new ArrayList<Annotation>();
        Annotation annBSr = new Annotation();
        annBSr.setHost(endpointBs);
        annBSr.setTimestamp(1363910400335L);
        annBSr.setValue(Annotation.SERVER_RECEIVE);
        annListBs.add(annBSr);

        Annotation annBSs = new Annotation();
        annBSs.setHost(endpointBs);
        annBSs.setTimestamp(1363910400560L);
        annBSs.setValue(Annotation.SERVER_SEND);
        annListBs.add(annBSs);

        spanBs.setAnnotations(annListBs);

        BinaryAnnotation ba1 = new BinaryAnnotation();
        ba1.setKey(HbaseUtils.DUBBO_EXCEPTION);
        ba1.setValue("abc");
        List<BinaryAnnotation> baList1 = new ArrayList<BinaryAnnotation>();
        baList1.add(ba1);
        spanBs.setBinaryAnnotations(baList1);

        insertService.addSpan(spanBc);
        insertService.addSpan(spanBs);
        insertService.addAnnotation(spanAc);
        insertService.addAnnotation(spanAs);
        insertService.addTrace(spanAc);
        insertService.addTrace(spanAs);
    }

    private void truncateAllTables() throws IOException {
        HBaseAdmin hBaseAdmin = new HBaseAdmin(HbaseUtils.conf);
        HTableDescriptor hTableDescriptor;
        if (hBaseAdmin.tableExists(hbaseUtils.getTR_T())) {
            if (hBaseAdmin.isTableEnabled(hbaseUtils.getTR_T())) {
                hBaseAdmin.disableTable(hbaseUtils.getTR_T());
            }
            hBaseAdmin.deleteTable(hbaseUtils.getTR_T());
            hTableDescriptor = new HTableDescriptor(hbaseUtils.getTR_T());
            hTableDescriptor.addFamily(new HColumnDescriptor(HbaseUtils.trace_family_column));
            hBaseAdmin.createTable(hTableDescriptor);
        }
        if (hBaseAdmin.tableExists(hbaseUtils.getDuration_index())) {
            if (hBaseAdmin.isTableEnabled(hbaseUtils.getDuration_index())) {
                hBaseAdmin.disableTable(hbaseUtils.getDuration_index());
            }
            hBaseAdmin.deleteTable(hbaseUtils.getDuration_index());
            hTableDescriptor = new HTableDescriptor(hbaseUtils.getDuration_index());
            hTableDescriptor.addFamily(new HColumnDescriptor(HbaseUtils.duration_index_family_column));
            hBaseAdmin.createTable(hTableDescriptor);
        }
        if (hBaseAdmin.tableExists(hbaseUtils.getAnn_index())) {
            if (hBaseAdmin.isTableEnabled(hbaseUtils.getAnn_index())) {
                hBaseAdmin.disableTable(hbaseUtils.getAnn_index());
            }
            hBaseAdmin.deleteTable(hbaseUtils.getAnn_index());
            hTableDescriptor = new HTableDescriptor(hbaseUtils.getAnn_index());
            hTableDescriptor.addFamily(new HColumnDescriptor(HbaseUtils.ann_index_family_column));
            hBaseAdmin.createTable(hTableDescriptor);
        }
    }

    private InsertService insertService;
    private HbaseUtils hbaseUtils;
    private QueryService queryService;

    public void setInsertService(InsertService insertService) {
        this.insertService = insertService;
    }

    public void setHbaseUtils(HbaseUtils hbaseUtils) {
        this.hbaseUtils = hbaseUtils;
    }

    public void setQueryService(QueryService queryService) {
        this.queryService = queryService;
    }
}
