package com.jd.bdp.hydra.collector.service;

import com.jd.bdp.hydra.Span;

import java.io.IOException;

/**
  * User: yfliuyu
 * Date: 13-4-16
 * Time: 上午11:04
  */
public interface HbaseService {
    public void addSpan(Span span)throws IOException;
    public void annotationIndex(Span span)throws IOException;
    public void durationIndex(Span span)throws  IOException;
}
