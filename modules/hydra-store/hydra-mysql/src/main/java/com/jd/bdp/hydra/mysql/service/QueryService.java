package com.jd.bdp.hydra.mysql.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * User: biandi
 * Date: 13-5-9
 * Time: 下午4:13
 */
public interface QueryService {

    JSONObject getTraceInfo(Long traceId);

    JSONArray getTracesByDuration(String serviceId, Long start, int sum, int durationMin, int durationMax);

    JSONArray getTracesByEx(String serviceId, long startTime, int sum);
}
