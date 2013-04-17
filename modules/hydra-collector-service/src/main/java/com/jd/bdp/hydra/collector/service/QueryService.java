package com.jd.bdp.hydra.collector.service;

import com.alibaba.fastjson.JSONArray;

/**
 * User: yfliuyu
 * Date: 13-4-16
 * Time: 上午11:02
 */
public interface QueryService {

    JSONArray getTraceInfo(Long traceId);
}
