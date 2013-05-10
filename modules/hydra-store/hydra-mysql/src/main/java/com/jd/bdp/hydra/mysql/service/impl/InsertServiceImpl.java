package com.jd.bdp.hydra.mysql.service.impl;

import com.jd.bdp.hydra.Annotation;
import com.jd.bdp.hydra.Span;
import com.jd.bdp.hydra.mysql.persistent.dao.AnnotationMapper;
import com.jd.bdp.hydra.mysql.persistent.dao.SpanMapper;
import com.jd.bdp.hydra.mysql.persistent.dao.TraceMapper;
import com.jd.bdp.hydra.mysql.persistent.entity.Trace;
import com.jd.bdp.hydra.mysql.service.InsertService;

import java.io.IOException;
import java.sql.Timestamp;

/**
 * User: biandi
 * Date: 13-5-9
 * Time: 下午4:13
 */
public class InsertServiceImpl implements InsertService {

    private AnnotationMapper annotationMapper;
    private SpanMapper spanMapper;
    private TraceMapper traceMapper;

    @Override
    public void addSpan(Span span) throws IOException {
        spanMapper.addSpan(span);
    }


    @Override
    public void addTrace(Span span) throws IOException {
        if (Utils.isTopAnntation(span)) {
            Annotation annotation = Utils.getCrAnnotation(span.getAnnotations());
            Annotation annotation1 = Utils.getCsAnnotation(span.getAnnotations())
            Trace t = new Trace();
            t.setTraceId(span.getTraceId());
            t.setAnnValue(annotation1.getValue());
            t.setDuration((int)(annotation1.getTimestamp() - annotation.getTimestamp()));
            t.setService(span.getServiceId());
            t.setTime(annotation1.getTimestamp());
            traceMapper.addTrace();
        }
    }

    @Override
    public void addAnnotation(Span span) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
