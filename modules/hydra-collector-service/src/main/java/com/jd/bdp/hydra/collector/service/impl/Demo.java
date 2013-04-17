package com.jd.bdp.hydra.collector.service.impl;

import com.alibaba.fastjson.JSON;
import com.jd.bdp.hydra.Annotation;
import com.jd.bdp.hydra.BinaryAnnotation;
import com.jd.bdp.hydra.Span;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;

import java.io.IOException;
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
public class Demo {
    public static HTablePool POOL;
    public static Configuration conf = HBaseConfiguration.create(new Configuration());
    public static final String duration_index = "duration_index";
    public static final String duration_index_family_colume = "trace";
    public static final String ann_index = "annotation_index";
    public static final String ann_index_family_colume = "trace";
    public static final String TR_T = "trace";
    public static final String trace_family_colume="span";

    static {
        conf.set("hbase.zookeeper.quorum","192.168.232.68:2181");
        POOL = new HTablePool(conf, 2);
    }

    public void createTable(){
        try {
            HBaseAdmin hBaseAdmin = new HBaseAdmin(conf);

            if(!hBaseAdmin.tableExists(duration_index)){
                HTableDescriptor hTableDescriptor = new HTableDescriptor(duration_index);
                hTableDescriptor.addFamily(new HColumnDescriptor(duration_index_family_colume));
                hBaseAdmin.createTable(hTableDescriptor);
            }

            if(!hBaseAdmin.tableExists(TR_T)){
                HTableDescriptor hTableDescriptor = new HTableDescriptor(TR_T);
                hTableDescriptor.addFamily(new HColumnDescriptor());
                hBaseAdmin.createTable(hTableDescriptor);
            }

            if(!hBaseAdmin.tableExists(ann_index)){
                HTableDescriptor hTableDescriptor = new HTableDescriptor(trace_family_colume);
                hTableDescriptor.addFamily(new HColumnDescriptor(ann_index_family_colume));
                hBaseAdmin.createTable(hTableDescriptor);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isTopAnntation(Span span){
        List<Annotation> alist = span.getAnnotations();
        boolean isfirst = false;
        for(Annotation a : alist){
            if(StringUtils.endsWithIgnoreCase("cs",a.getValue())){
                isfirst = true;
            }
        }
        return isfirst;
    }

    public void addSpan(Span span)throws IOException{
        String rowkey = String.valueOf(span.getTraceId());
        Put put = new Put(rowkey.getBytes());
        String jsonValue = JSON.toJSONString(span);
        String spanId = String.valueOf(span.getId());
        if(isTopAnntation(span)){
            spanId = spanId + "C";
        }else{
            spanId = spanId + "S";
        }
        put.add(trace_family_colume.getBytes(),spanId.getBytes(),jsonValue.getBytes());
        HTable htable = (HTable)POOL.getTable(TR_T);
        htable.put(put);
    }

    private Annotation getCsAnnotation(List<Annotation> alist){
        for(Annotation a : alist){
            if(StringUtils.endsWithIgnoreCase("cs",a.getValue())){
                return a;
            }
        }
        return null;
    }
    private Annotation getCrAnnotation(List<Annotation> alist){
        for(Annotation a : alist){
            if(StringUtils.endsWithIgnoreCase("cs",a.getValue())){
                return a;
            }
        }
        return null;
    }

    public void buildAnnotationIndex(Span span){
        List<Annotation> alist = span.getAnnotations();
        for(Annotation a : alist){
            String rowkey = a.getHost().getServiceName()+":"+System.currentTimeMillis()+":"+a.getValue();
            Put put = new Put();
            put.add(ann_index_family_colume.getBytes(),"traceId".getBytes(),Utils.long2ByteArray(span.getTraceId()));
        }

        for(BinaryAnnotation b : span.getBinaryAnnotations()){
            String rowkey = b.getHost().getServiceName()+":"+System.currentTimeMillis()+":"+b.getKey();
            Put put = new Put(rowkey.getBytes());
            put.add(ann_index_family_colume.getBytes(),"traceId".getBytes(),Utils.long2ByteArray(span.getTraceId()));
        }
    }

    private Annotation getSsAnnotation(List<Annotation> alist){
        for(Annotation a : alist){
            if(StringUtils.endsWithIgnoreCase("ss",a.getValue())){
                return a;
            }
        }
        return null;
    }

    private Annotation getSrAnnotation(List<Annotation> alist){
        for(Annotation a : alist){
            if(StringUtils.endsWithIgnoreCase("sr",a.getValue())){
                return a;
            }
        }
        return null;
    }

    public void buildDurationIndex(Span span){
        List<Annotation> alist = span.getAnnotations();
        Annotation cs = getCsAnnotation(alist);
        Annotation cr = getCrAnnotation(alist);
        if(cs != null){
            long duration = cs.getTimestamp()-cr.getTimestamp();
            String rowkey = cs.getHost().getServiceName()+":"+duration;
            Put put = new Put(rowkey.getBytes());
            put.add(duration_index_family_colume.getBytes(),"traceId".getBytes(),Utils.long2ByteArray(span.getTraceId()));
        }
    }


}