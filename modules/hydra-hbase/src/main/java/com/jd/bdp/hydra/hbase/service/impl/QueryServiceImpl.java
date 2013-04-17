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

import com.alibaba.fastjson.JSONArray;

import com.jd.bdp.hydra.hbase.service.QueryService;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Result;

import java.util.List;

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
        conf.set("hbase.client.retries.number", "86400");
        POOL = new HTablePool(conf, 2);
    }

    public JSONArray getTraceInfo(Long traceId) {
        HTableInterface table = POOL.getTable(TR_T);
        try {
            Get g = new Get(traceId.toString().getBytes());
            Result rs = table.get(g);
            List<KeyValue> list = rs.list();
            JSONArray array = new JSONArray();
            for (KeyValue kv : list) {
                array.add(new String(kv.getValue()));
            }
            return array;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    public void setOneItem(String rowkey, String columnName,  byte[] valueParm) {
//        HTableInterface table = POOL.getTable("trace");
//        table.setAutoFlush(true);//自动提交
//        try {
//            Put put = new Put(Bytes.toBytes(rowkey));
//            put.add(Bytes.toBytes(trace_family_colume), Bytes.toBytes(columnName), valueParm);
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

}
