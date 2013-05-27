package com.jd.bdp.hydra.agent;

import com.jd.bdp.hydra.Span;

import java.util.List;

/**
 * Date: 13-4-3
 * Time: 下午4:15
 * dubbo收集服务接口
  */
public interface CollectorService {
    public void sendSpan(List<Span> spanList);
}
