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
import com.jd.bdp.hydra.Annotation;
import com.jd.bdp.hydra.BinaryAnnotation;
import com.jd.bdp.hydra.Endpoint;
import com.jd.bdp.hydra.Span;
import com.jd.bdp.hydra.hbase.service.HbaseService;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
trace:
rowkey:serviceId+traceId//
familColume:Span
Qualifier:{
	spanId1+0:
	spanId2+1:
	spanId3+0:
	spanId4+1:
}

durationIndex
rowkey:serviceId+duration
familColume:trace
Qualifier:{
	traceId
}

annotationIndex
rowkey:serviceId+key+value
familColume:trace
Qualifier:{
	traceId
}
 */
public class HbaseServiceImpl implements HbaseService {
    public static HTablePool POOL;
    public static Configuration conf = HBaseConfiguration.create(new Configuration());
    public static final String duration_index = "duration_index";
    public static final String duration_index_family_colume = "trace";
    public static final String ann_index = "annotation_index";
    public static final String ann_index_family_colume = "trace";
    public static final String TR_T = "trace";
    public static final String trace_family_colume = "span";

    static {
        conf.set("hbase.zookeeper.quorum", "boss,emp1,emp2");
        conf.set("hbase.client.retries.number", "3");
        POOL = new HTablePool(conf, 2);
    }

    public void createTable() {
        try {
            HBaseAdmin hBaseAdmin = new HBaseAdmin(conf);
            if (!hBaseAdmin.tableExists(duration_index)) {
                HTableDescriptor hTableDescriptor = new HTableDescriptor(duration_index);
                hTableDescriptor.addFamily(new HColumnDescriptor(duration_index_family_colume));
                hBaseAdmin.createTable(hTableDescriptor);
            }
            if (!hBaseAdmin.tableExists(TR_T)) {
                HTableDescriptor hTableDescriptor = new HTableDescriptor(TR_T);
                hTableDescriptor.addFamily(new HColumnDescriptor());
                hBaseAdmin.createTable(hTableDescriptor);
            }
            if (!hBaseAdmin.tableExists(ann_index)) {
                HTableDescriptor hTableDescriptor = new HTableDescriptor(trace_family_colume);
                hTableDescriptor.addFamily(new HColumnDescriptor(ann_index_family_colume));
                hBaseAdmin.createTable(hTableDescriptor);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void addSpan(Span span) throws IOException {
        String rowkey = String.valueOf(span.getTraceId());
        Put put = new Put(rowkey.getBytes());
        String jsonValue = JSON.toJSONString(span);
        String spanId = String.valueOf(span.getId());
        if (HbaseUtils.isTopAnntation(span)) {
            spanId = spanId + "C";
        } else {
            spanId = spanId + "S";
        }
        HTableInterface htable = null;
        try {
            put.add(trace_family_colume.getBytes(), spanId.getBytes(), jsonValue.getBytes());
            htable = POOL.getTable(TR_T);
            htable.put(put);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(htable != null){
                htable.close();
            }
        }
    }


    public void annotationIndex(Span span) {
        List<Annotation> alist = span.getAnnotations();
        List<Put> putlist = new ArrayList<Put>();
        for (Annotation a : alist) {
            String rowkey = a.getHost().getServiceName() + ":" + System.currentTimeMillis() + ":" + a.getValue();
            Put put = new Put(rowkey.getBytes());
            put.add(ann_index_family_colume.getBytes(), "traceId".getBytes(), HbaseUtils.long2ByteArray(span.getTraceId()));
            putlist.add(put);
        }

        for (BinaryAnnotation b : span.getBinaryAnnotations()) {
            String rowkey = b.getHost().getServiceName() + ":" + System.currentTimeMillis() + ":" + b.getKey();
            Put put = new Put(rowkey.getBytes());
            put.add(ann_index_family_colume.getBytes(), "traceId".getBytes(), HbaseUtils.long2ByteArray(span.getTraceId()));
            putlist.add(put);
        }

        HTableInterface htable = null;
        try {
            htable = POOL.getTable(ann_index);
            htable.put(putlist);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(htable != null){
                try {
                    htable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void durationIndex(Span span) {
        List<Annotation> alist = span.getAnnotations();
        Annotation cs = HbaseUtils.getCsAnnotation(alist);
        Annotation cr = HbaseUtils.getCrAnnotation(alist);
        if (cs != null) {
            long duration = cs.getTimestamp() - cr.getTimestamp();
            String rowkey = cs.getHost().getServiceName() + ":" + duration;
            System.out.println(rowkey);
            Put put = new Put(rowkey.getBytes());
            put.add(duration_index_family_colume.getBytes(), "traceId".getBytes(), HbaseUtils.long2ByteArray(span.getTraceId()));
            HTableInterface htable = null;
            try {
                htable = POOL.getTable(duration_index);
                htable.put(put);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(htable != null){
                    try {
                        htable.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] strings){
        HbaseServiceImpl hbaseService = new HbaseServiceImpl();
        for(int i = 0 ; i < 100;i++){
            if(i%2 == 0){
                Span span = new Span();
                Endpoint endpoint = new Endpoint();
                endpoint.setServiceName(i+"");
                endpoint.setPort(1234);
                endpoint.setIp("127.0.0."+i);
                Annotation cs = new Annotation();
                cs.setHost(endpoint);
                cs.setTimestamp(System.currentTimeMillis());
                cs.setValue("cs");
                Annotation cr = new Annotation();
                cr.setTimestamp(System.currentTimeMillis()-100);
                cr.setValue("cr");
                cr.setHost(endpoint);
                span.setId(new Long(i));
                span.setSample(true);
                span.setTraceId(new Long(i+i));
                span.setSpanName("method_"+i);
                span.setParentId(new Long(i+1));
                span.addAnnotation(cr);
                span.addAnnotation(cs);
                try {
                    hbaseService.addSpan(span);
                    hbaseService.durationIndex(span);
                    hbaseService.annotationIndex(span);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                Span span = new Span();
                Endpoint endpoint = new Endpoint();
                endpoint.setServiceName(i+"");
                endpoint.setPort(4321);
                endpoint.setIp("127.0.0."+i);
                Annotation sr = new Annotation();
                sr.setHost(endpoint);
                sr.setTimestamp(System.currentTimeMillis());
                sr.setValue("ss");
                Annotation ss = new Annotation();
                ss.setTimestamp(System.currentTimeMillis()-100);
                ss.setValue("sr");
                ss.setHost(endpoint);
                span.setParentId(new Long(i+1));
                span.setId(new Long(i));
                span.setSpanName("method_"+i);
                span.setTraceId(new Long(i+i));
                span.addAnnotation(sr);
                span.addAnnotation(ss);
                try{
                    hbaseService.addSpan(span);
                    hbaseService.durationIndex(span);
                    hbaseService.annotationIndex(span);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}