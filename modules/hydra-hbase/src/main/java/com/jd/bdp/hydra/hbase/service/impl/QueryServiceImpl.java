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
import org.apache.hadoop.hbase.filter.*;
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

    @Override
    public JSONArray getTracesOrderByTimestamp(String serviceId, Long start, Long end, int num) {
        JSONArray array = new JSONArray();
        Scan scan = new Scan();
        scan.setStartRow(new String(serviceId + "." + start).getBytes());
        scan.setStopRow(new String(serviceId + "." + end).getBytes());
        Filter filter = new PageFilter(num);
        scan.setFilter(filter);
        HTableInterface table = null;
        ResultScanner rs = null;
        try {
            table = POOL.getTable(duration_index);
            rs = table.getScanner(scan);
            for (Result res : rs){
                List<KeyValue> list = res.list();
                for (KeyValue kv : list){
                    JSONObject obj = new JSONObject();
                    String[] key = new String(kv.getRow()).split("\\.");
                    obj.put("serviceId", key[0]);
                    obj.put("timestamp", key[1]);
                    obj.put("duration", key[2]);
                    obj.put("traceId", new String(kv.getValue()));
                    array.add(obj);
                }
            }
            return array;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            rs.close();
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
        HTableInterface table = POOL.getTable("duration_index");
        table.setAutoFlush(true);//自动提交
        try {
            Put put = new Put(Bytes.toBytes(rowkey));
            put.add(Bytes.toBytes(duration_index_family_colume), Bytes.toBytes(columnName), valueParm);
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

}
