package com.jd.bdp.hydra.mysql.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jd.bdp.hydra.mysql.service.QueryService;

/**
 * User: biandi
 * Date: 13-5-9
 * Time: 下午4:13
 */
public class QueryServiceImpl implements QueryService{
    @Override
    public JSONObject getTraceInfo(Long traceId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public JSONArray getTracesByDuration(String serviceId, Long start, int sum, int durationMin, int durationMax) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public JSONArray getTracesByEx(String serviceId, long startTime, int sum) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
