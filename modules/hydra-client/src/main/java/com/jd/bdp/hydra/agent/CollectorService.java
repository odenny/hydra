package com.jd.bdp.hydra.agent;

import com.jd.bdp.hydra.Span;

import java.util.List;

/**
  * User: yfliuyu
 * Date: 13-4-3
 * Time: 下午4:15
  */
public interface CollectorService {
    public void sendSpan(List<Span> spanList);
}
