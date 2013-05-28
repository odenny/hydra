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
import com.jd.bdp.hydra.store.inter.QueryService;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.PageFilter;
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
public class QueryServiceImpl extends HbaseUtils implements QueryService {

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
    public JSONArray getTracesByDuration(String serviceId, Long startTime, int sum, int durationMin, int durationMax) {
        JSONArray array = new JSONArray();
        Scan scan = new Scan();
        scan.setStartRow(new String(serviceId + ":" + startTime).getBytes());
        scan.setStopRow(new String(serviceId + ":" + Long.MAX_VALUE).getBytes());
        Filter filter = new PageFilter(sum);
        scan.setFilter(filter);
        try {
            scan.setTimeRange(durationMin, durationMax);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HTableInterface table = null;
        ResultScanner rs = null;
        try {
            table = POOL.getTable(duration_index);
            rs = table.getScanner(scan);
            int resultSum = 0;
            for (Result res : rs) {
                List<KeyValue> list = res.list();
                for (KeyValue kv : list) {
                    if (resultSum == sum) {//因为每行有可能有多条数据，所以要手动过滤条数
                        break;
                    }
                    JSONObject obj = new JSONObject();
                    String[] key = new String(kv.getRow()).split(":");
                    obj.put("serviceId", key[0]);
                    obj.put("timestamp", Long.parseLong(key[1]));
                    obj.put("duration", kv.getTimestamp());
                    obj.put("traceId", byteArray2Long(kv.getQualifier()));
                    array.add(obj);
                    resultSum++;
                }
            }
            return array;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                table.close();
                rs.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    @Override
    public JSONArray getTracesByEx(String serviceId, Long startTime, int sum) {
        JSONArray array = new JSONArray();
        Scan scan = new Scan();
        scan.setStartRow(new String(serviceId + ":" + startTime + ":dubbo.exception".getBytes()).getBytes());
        scan.setStopRow(new String(serviceId + ":" + Long.MAX_VALUE + ":dubbo.exception".getBytes()).getBytes());
        Filter filter = new PageFilter(sum);
        scan.setFilter(filter);
        HTableInterface table = null;
        ResultScanner rs = null;
        try {
            table = POOL.getTable(ann_index);
            rs = table.getScanner(scan);
            int resultSum = 0;
            for (Result res : rs) {
                List<KeyValue> list = res.list();
                for (KeyValue kv : list) {
                    if (resultSum == sum) {//因为每行有可能有多条数据，所以要手动过滤条数
                        break;
                    }
                    JSONObject obj = new JSONObject();
                    String[] key = new String(kv.getRow()).split(":");
                    obj.put("serviceId", key[0]);
                    obj.put("timestamp", Long.parseLong(key[1]));
                    obj.put("traceId", byteArray2Long(kv.getQualifier()));
                    obj.put("exInfo", new String(kv.getValue()));
                    array.add(obj);
                    resultSum++;
                }
            }
            return array;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                table.close();
                rs.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    private JSONObject assembleTrace(List<KeyValue> list) {
        JSONObject trace = new JSONObject();
        Map<String, JSONObject> map = new HashMap<String, JSONObject>();
        if (list != null){
            for (KeyValue kv : list) {
                JSONObject content = JSON.parseObject(new String(kv.getValue()));
                JSONObject spanAleadyExist;
                if (map.containsKey(content.get("id").toString())) {//凑齐第二个半个span后
                    spanAleadyExist = map.get(content.get("id").toString());
                    if (!spanAleadyExist.containsKey("serviceId")) {
                        spanAleadyExist.put("serviceId", content.get("serviceId"));
                    }
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
                if (!spanAleadyExist.containsKey("exception")) {
                    handleException(spanAleadyExist, content);
                }
            }
            boolean isAvailable = true;
            for (Map.Entry<String, JSONObject> entry : map.entrySet()) {
                JSONObject mySpan = entry.getValue();
                if (!mySpan.containsKey("parentId")) {
                    trace.put("rootSpan", mySpan);
                    trace.put("traceId", mySpan.get("traceId"));
                } else {
                    JSONObject myFather = map.get(mySpan.get("parentId").toString());
                    if (myFather.containsKey("children")) {
                        ((JSONArray) myFather.get("children")).add(mySpan);
                    } else {
                        JSONArray children = new JSONArray();
                        children.add(mySpan);
                        myFather.put("children", children);
                    }
                }
                isAvailable = isAvailable && isSpanAvailable(mySpan);
            }
            trace.put("available", isAvailable);
        }
        return trace;
    }

    //这里判断如果某个span没有收集全4个annotation，则判定为不可用，页面不展示图
    private boolean isSpanAvailable(JSONObject span) {
        return span.getJSONArray("annotations").size() == 4;
    }

    private void handleException(JSONObject spanAleadyExist, JSONObject content) {
        JSONObject e = null;
        for (Object obj : (JSONArray) content.get("binaryAnnotations")) {
            if (((JSONObject) obj).get("key").toString().equalsIgnoreCase(DUBBO_EXCEPTION)) {
                e = (JSONObject) obj;
                break;
            }
        }
        if (e != null) {
            spanAleadyExist.put("exception", e);
        }
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


//    public void setOneItem(String tableName, String familyColumnName, String rowkey, String columnName, byte[] valueParm) {
//        HTableInterface table = POOL.getTable(tableName);
//        table.setAutoFlush(true);//自动提交
//        try {
//            Put put = new Put(Bytes.toBytes(rowkey));
//            put.add(Bytes.toBytes(familyColumnName), Bytes.toBytes(columnName), valueParm);
//            table.put(put);
////            table.flushCommits();//手动提交，最好每次close之前手动提交...
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                table.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    /**
//     * 删除指定表名的rowKey下某时间戳的数据。
//     */
//    public boolean delete(String tableName, String rowKey) {
//        boolean result = false;
//        HTableInterface hTable = null;
//        try {
//            hTable = POOL.getTable(Bytes.toBytes(tableName));
//
//            if (hTable == null) {
//                return result;
//            }
//
//            byte[] rowKeys = Bytes.toBytes(rowKey);
//            Delete delete = new Delete(rowKeys);
//            hTable.delete(delete);
//            result = true;
//        } catch (Exception e) {
//
//
//        } finally {
//            try {
//                hTable.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return result;
//    }
//
//
//    public void createTable(String tableName, String familyColumnName) {
//        try {
//            HBaseAdmin hBaseAdmin = new HBaseAdmin(conf);
//            if (!hBaseAdmin.tableExists(tableName)) {
//                HTableDescriptor hTableDescriptor = new HTableDescriptor(tableName);
//                hTableDescriptor.addFamily(new HColumnDescriptor(familyColumnName));
//                hBaseAdmin.createTable(hTableDescriptor);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    public static void main(String[] args){
//        QueryServiceImpl queryService =
//    }
}
