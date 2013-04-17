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

package com.jd.bdp.hydra.collector.service.imp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jd.bdp.hydra.Annotation;
import com.jd.bdp.hydra.Endpoint;
import com.jd.bdp.hydra.Span;
import com.jd.dd.glowworm.PB;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: biandi
 * Date: 13-4-17
 * Time: 下午1:27
 */
public class QueryServiceImpl {

    public static HTablePool POOL;
    public static Configuration conf = HBaseConfiguration.create(new Configuration());
    public static final String duration_index = "duration_index";
    public static final String duration_index_family_colume = "trace";
    public static final String ann_index = "annotation_index";
    public static final String ann_index_family_colume = "trace";
    public static final String TR_T = "trace";
    public static final String trace_family_colume="span";

    public QueryServiceImpl(){
        this.conf.set("hbase.zookeeper.quorum", "boss,emp1,emp2");//"boss,emp1,emp2"
        this.conf.set("hbase.client.retries.number", "86400");
        POOL = new HTablePool(conf, 2);
    }

    public JSONArray getTraceInfo(Long traceId){
        HTableInterface table = POOL.getTable("trace");
        try {
            Get g = new Get(traceId.toString().getBytes());
            Result rs = table.get(g);
            List<KeyValue> list =  rs.list();
            JSONArray array = new JSONArray();
            for(KeyValue kv : list){
                array.add(new String(kv.getValue()));
            }
            return array;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void setOneItem(String rowkey, String columnName,  byte[] valueParm) {
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

    public static void main(String[] args){
        QueryServiceImpl queryService = new QueryServiceImpl();
        //traceId = 1366178446534
        //span1 : 1366178470630c, 1366178470630s
        //span2 : 1366178496806c, 1366178496806s
        //span3 : 1366178559295c, 1366178559295s

//        queryService.delete("trace", "1366178446534");
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
//        annList1c.add(annCs);
//
//        Annotation annCr = new Annotation();
//        annCr.setHost(endpoint1c);
//        annCr.setTimestamp(1363910400602L);
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
//        annList1s.add(annSr);
//
//        Annotation annSs = new Annotation();
//        annSs.setHost(endpoint1s);
//        annSs.setTimestamp(1363910400582L);
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
//        annListAc.add(annACs);
//
//        Annotation annACr = new Annotation();
//        annACr.setHost(endpointAc);
//        annACr.setTimestamp(1363910400300L);
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
//        annListAs.add(annASr);
//
//        Annotation annASs = new Annotation();
//        annASs.setHost(endpointAs);
//        annASs.setTimestamp(1363910400278L);
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
//        annListBc.add(annBCs);
//
//        Annotation annBCr = new Annotation();
//        annBCr.setHost(endpointBc);
//        annBCr.setTimestamp(1363910400570L);
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
//        annListBs.add(annBSr);
//
//        Annotation annBSs = new Annotation();
//        annBSs.setHost(endpointBs);
//        annBSs.setTimestamp(1363910400560L);
//        annListBs.add(annBSs);
//
//        spanBs.setAnnotations(annListBs);
//
//        queryService.setOneItem("1366178446534", "1366178559295s", JSON.toJSONBytes(spanBs));

    }
}
