package com.jd.bdp.hydra.collector.service;

import com.alibaba.fastjson.JSONArray;
import com.jd.bdp.hydra.Span;

import java.util.List;

/**
  * User: yfliuyu
 * Date: 13-4-16
 * Time: 上午11:02
  */
public interface QueryService{

    JSONArray getTraceInfo(Long traceId);
}
