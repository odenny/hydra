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

package com.jd.bdp.hydra.hbase.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jd.bdp.hydra.Annotation;
import com.jd.bdp.hydra.hbase.service.QueryService;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: biandi
 * Date: 13-4-17
 * Time: 下午1:27
 */
public class QueryServiceImpl implements QueryService {

    public static HTablePool POOL;
    public static Configuration conf = HBaseConfiguration.create(new Configuration());
    public static final String duration_index = "duration_index";
    public static final String duration_index_family_colume = "trace";
    public static final String ann_index = "annotation_index";
    public static final String ann_index_family_colume = "trace";
    public static final String TR_T = "trace";
    public static final String trace_family_colume = "span";

    static {
        conf.set("hbase.zookeeper.quorum", "boss,emp1,emp2");//"boss,emp1,emp2"
        conf.set("hbase.client.retries.number", "3");
        POOL = new HTablePool(conf, 2);
    }

    public JSONObject getTraceInfo(Long traceId) {
        HTableInterface table = null;
        try {
            table = POOL.getTable(TR_T);
            Get g = new Get(traceId.toString().getBytes());
            Result rs = table.get(g);
            List<KeyValue> list = rs.list();
            return assembleTrace(list);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                table.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    private JSONObject assembleTrace(List<KeyValue> list) {
        JSONObject trace = new JSONObject();
        Map<String, JSONObject> map = new HashMap<String, JSONObject>();
        for (KeyValue kv : list) {
            JSONObject content = JSON.parseObject(new String(kv.getValue()));
            JSONObject spanAleadyExist;
            if (map.containsKey(content.get("id").toString())) {//凑齐第二个半个span后
                spanAleadyExist = map.get(content.get("id").toString());
                if (isClientSpan(kv)) {
                    spanAleadyExist.put("durationClient", getDurationClient(content));
                } else {
                    spanAleadyExist.put("durationServer", getDurationServer(content));
                }
                ((JSONArray) spanAleadyExist.get("annotations")).addAll((JSONArray) content.get("annotations"));
            } else {
                spanAleadyExist = content;
                if (isClientSpan(kv)) {
                    spanAleadyExist.put("durationClient", getDurationClient(content));
                } else {
                    spanAleadyExist.put("durationServer", getDurationServer(content));
                }
                map.put(content.get("id").toString(), spanAleadyExist);
            }
        }
        for (Map.Entry<String, JSONObject> entry : map.entrySet()) {
            if (!entry.getValue().containsKey("parentId")) {
                trace.put("rootSpan", entry.getValue());
                trace.put("traceId", entry.getValue().get("traceId"));
            } else {
                JSONObject myFather = map.get(entry.getValue().get("parentId").toString());
                if (myFather.containsKey("children")) {
                    ((JSONArray) myFather.get("children")).add(entry.getValue());
                } else {
                    JSONArray children = new JSONArray();
                    children.add(entry.getValue());
                    myFather.put("children", children);
                }
            }
        }
        return trace;
    }

    private Long getDurationServer(JSONObject content) {
        JSONArray serverAnns = ((JSONArray) content.get("annotations"));
        Long sr = null;
        Long ss = null;
        for (int i = 0; i < serverAnns.size(); i++) {
            if (((JSONObject) serverAnns.get(i)).get("value").equals(Annotation.SERVER_RECEIVE)) {
                sr = Long.valueOf(((JSONObject) serverAnns.get(i)).get("timestamp").toString());
            }
            if (((JSONObject) serverAnns.get(i)).get("value").equals(Annotation.SERVER_SEND)) {
                ss = Long.valueOf(((JSONObject) serverAnns.get(i)).get("timestamp").toString());
            }
        }
        return ss - sr;
    }

    private boolean isClientSpan(KeyValue kv) {
        return StringUtils.endsWithIgnoreCase(new String(kv.getQualifier()), "c");
    }

    private Long getDurationClient(JSONObject content) {
        JSONArray clientAnns = ((JSONArray) content.get("annotations"));
        Long cr = null;
        Long cs = null;
        for (int i = 0; i < clientAnns.size(); i++) {
            if (((JSONObject) clientAnns.get(i)).get("value").equals(Annotation.CLIENT_RECEIVE)) {
                cr = Long.valueOf(((JSONObject) clientAnns.get(i)).get("timestamp").toString());
            }
            if (((JSONObject) clientAnns.get(i)).get("value").equals(Annotation.CLIENT_SEND)) {
                cs = Long.valueOf(((JSONObject) clientAnns.get(i)).get("timestamp").toString());
            }
        }
        return cr - cs;
    }


    public void setOneItem(String rowkey, String columnName, byte[] valueParm) {
        HTableInterface table = POOL.getTable("trace");
        table.setAutoFlush(true);//自动提交
        try {
            Put put = new Put(Bytes.toBytes(rowkey));
            put.add(Bytes.toBytes(trace_family_colume), Bytes.toBytes(columnName), valueParm);
            table.put(put);
//            table.flushCommits();//手动提交，最好每次close之前手动提交...
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                table.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除指定表名的rowKey下某时间戳的数据。
     */
    public boolean delete(String tableName, String rowKey) {
        boolean result = false;
        HTableInterface hTable = null;
        try {
            hTable = POOL.getTable(Bytes.toBytes(tableName));

            if (hTable == null) {
                return result;
            }

            byte[] rowKeys = Bytes.toBytes(rowKey);
            Delete delete = new Delete(rowKeys);
            hTable.delete(delete);
            result = true;
        } catch (Exception e) {


        } finally {
            try {
                hTable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static void main(String[] args) {
        QueryServiceImpl queryService = new QueryServiceImpl();
        queryService.getTraceInfo(1366178446534L);
//        //traceId = 1366178446534
//        //span1 : 1366178470630c, 1366178470630s
//        //span2 : 1366178496806c, 1366178496806s
//        //span3 : 1366178559295c, 1366178559295s
//
////        queryService.delete("trace", "1366178446534");
//
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
//        queryService.setOneItem("1366178446534", "1366178470630c", JSON.toJSONBytes(span1c));
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
//        queryService.setOneItem("1366178446534", "1366178470630s", JSON.toJSONBytes(span1s));
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
//        queryService.setOneItem("1366178446534", "1366178496806c", JSON.toJSONBytes(spanAc));
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
//        queryService.setOneItem("1366178446534", "1366178496806s", JSON.toJSONBytes(spanAs));
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
//        queryService.setOneItem("1366178446534", "1366178559295c", JSON.toJSONBytes(spanBc));
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
//        queryService.setOneItem("1366178446534", "1366178559295s", JSON.toJSONBytes(spanBs));

//        queryService.delete("trace", "1366178446534");
    }

}
