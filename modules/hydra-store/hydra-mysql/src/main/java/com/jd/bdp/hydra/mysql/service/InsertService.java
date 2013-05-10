package com.jd.bdp.hydra.mysql.service;

import com.jd.bdp.hydra.Span;

import java.io.IOException;

/**
 * User: biandi
 * Date: 13-5-9
 * Time: 下午4:12
 */
public interface InsertService {

    public void addSpan(Span span)throws IOException;
    public void annotationIndex(Span span)throws IOException;
    public void durationIndex(Span span)throws  IOException;
}
