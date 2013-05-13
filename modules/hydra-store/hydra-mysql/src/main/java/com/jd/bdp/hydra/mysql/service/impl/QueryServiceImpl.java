package com.jd.bdp.hydra.mysql.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jd.bdp.hydra.Annotation;
import com.jd.bdp.hydra.Span;
import com.jd.bdp.hydra.mysql.persistent.dao.AnnotationMapper;
import com.jd.bdp.hydra.mysql.persistent.dao.SpanMapper;
import com.jd.bdp.hydra.mysql.persistent.dao.TraceMapper;
import com.jd.bdp.hydra.mysql.persistent.entity.Absannotation;
import com.jd.bdp.hydra.mysql.persistent.entity.Trace;
import com.jd.bdp.hydra.mysql.service.QueryService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: biandi
 * Date: 13-5-9
 * Time: 下午4:13
 */
public class QueryServiceImpl implements QueryService {

    @Override
    public JSONObject getTraceInfo(Long traceId) {
        List<Span> spans = spanMapper.findSpanByTraceId(traceId);
        List<Absannotation> annotations = annotationMapper.getAnnotations(spans);
        return assembleTrace(spans, annotations);
    }

    private JSONObject assembleTrace(List<Span> spans, List<Absannotation> annotations) {
        JSONObject trace = new JSONObject();
        Map<Long, JSONObject> spanMap = new HashMap<Long, JSONObject>();

        for (Span span : spans) {
            spanMap.put(span.getId(), (JSONObject) JSON.toJSON(span));
        }

        for (Absannotation annotation : annotations) {
            Long spanId = annotation.getSpanId();
            JSONObject mySpan = spanMap.get(spanId);
            if (isExceptionAnn(annotation)) {
                mySpan.put("exception", createException(annotation));
            } else {
                if (mySpan.containsKey("annotations")) {
                    mySpan.getJSONArray("annotations").add(createJsonAnn(annotation));
                } else {
                    JSONArray annotationArray = new JSONArray();
                    annotationArray.add(createJsonAnn(annotation));
                    mySpan.put("annotations", annotationArray);
                }
            }
        }

        boolean isAvailable = true;
        for (Map.Entry<Long, JSONObject> entry : spanMap.entrySet()) {
            JSONObject mySpan = entry.getValue();
            if (!mySpan.containsKey("parentId") || mySpan.get("parentId") == null) {
                trace.put("rootSpan", mySpan);
                trace.put("traceId", mySpan.get("traceId"));
            } else {
                JSONObject myFather = spanMap.get(mySpan.get("parentId"));
                if (myFather.containsKey("children")) {
                    ((JSONArray) myFather.get("children")).add(mySpan);
                } else {
                    JSONArray children = new JSONArray();
                    children.add(mySpan);
                    myFather.put("children", children);
                }
            }
            setSpanDuration(mySpan);
            handleTheMinAndMaxTimestamp(trace, mySpan);
            isAvailable = isAvailable && isSpanAvailable(mySpan);
        }
        trace.put("available", isAvailable);
        return trace;
    }

    private void handleTheMinAndMaxTimestamp(JSONObject trace, JSONObject span) {
        for (Object obj : span.getJSONArray("annotations")) {
            long timestamp = Long.parseLong(((JSONObject) obj).get("timestamp").toString());
            if (trace.containsKey("minTimestamp")) {
                long min = Long.parseLong(trace.get("minTimestamp").toString());
                if (min > timestamp) {
                    trace.put("minTimestamp", timestamp);
                }
            } else {
                trace.put("minTimestamp", timestamp);
            }
            if (trace.containsKey("maxTimestamp")) {
                long max = Long.parseLong(trace.get("maxTimestamp").toString());
                if (max < timestamp) {
                    trace.put("maxTimestamp", timestamp);
                }
            } else {
                trace.put("maxTimestamp", timestamp);
            }
        }
    }

    public void setSpanDuration(JSONObject spanDuration) {
        JSONArray serverAnns = spanDuration.getJSONArray("annotations");
        Long sr = null;
        Long ss = null;
        Long cr = null;
        Long cs = null;
        for (int i = 0; i < serverAnns.size(); i++) {
            if (((JSONObject) serverAnns.get(i)).get("value").equals(Annotation.SERVER_RECEIVE)) {
                sr = Long.valueOf(((JSONObject) serverAnns.get(i)).get("timestamp").toString());
            }
            if (((JSONObject) serverAnns.get(i)).get("value").equals(Annotation.SERVER_SEND)) {
                ss = Long.valueOf(((JSONObject) serverAnns.get(i)).get("timestamp").toString());
            }
            if (((JSONObject) serverAnns.get(i)).get("value").equals(Annotation.CLIENT_RECEIVE)) {
                cr = Long.valueOf(((JSONObject) serverAnns.get(i)).get("timestamp").toString());
            }
            if (((JSONObject) serverAnns.get(i)).get("value").equals(Annotation.CLIENT_SEND)) {
                cs = Long.valueOf(((JSONObject) serverAnns.get(i)).get("timestamp").toString());
            }
        }
        if (sr != null && ss != null) {
            spanDuration.put("durationServer", ss - sr);
        }
        if (cr != null && cs != null) {
            spanDuration.put("durationClient", cr - cs);
        }
    }

    private boolean isSpanAvailable(JSONObject span) {
        return span.getJSONArray("annotations").size() == 4;
    }


    public boolean isExceptionAnn(Absannotation annotation) {
        return annotation.getKey().equalsIgnoreCase("dubbo.exception");
    }

    private JSONObject createException(Absannotation annotation) {
        JSONObject e = new JSONObject();
        e.put("key", annotation.getKey());
        e.put("value", annotation.getValue());
        return e;
    }

    private JSONObject createJsonAnn(Absannotation annotation) {
        JSONObject obj = new JSONObject();
        obj.put("value", annotation.getKey());
        obj.put("timestamp", annotation.getValue());
        JSONObject host = new JSONObject();
        host.put("ip", annotation.getIp());
        host.put("port", annotation.getPort());
        obj.put("host", host);
        return obj;
    }

    @Override
    public JSONArray getTracesByDuration(String serviceId, Long start, int sum, int durationMin, int durationMax) {
        List<Trace> list = traceMapper.findTracesByDuration(serviceId, start, durationMin, durationMax, sum);
        JSONArray array = new JSONArray();
        for (Trace trace : list) {
            JSONObject obj = new JSONObject();
            obj.put("serviceId", trace.getService());
            obj.put("timestamp", trace.getTime());
            obj.put("duration", trace.getDuration());
            obj.put("traceId", trace.getTraceId());
            array.add(obj);
        }
        return array;
    }

    @Override
    public JSONArray getTracesByEx(String serviceId, long startTime, int sum) {
        List<Trace> list = traceMapper.findTracesEx(serviceId, startTime, sum);
        JSONArray array = new JSONArray();
        for (Trace trace : list) {
            JSONObject obj = new JSONObject();
            obj.put("serviceId", trace.getService());
            obj.put("timestamp", trace.getTime());
            obj.put("exInfo", trace.getAnnValue());
            obj.put("traceId", trace.getTraceId());
            array.add(obj);
        }
        return array;
    }

    private TraceMapper traceMapper;
    private SpanMapper spanMapper;
    private AnnotationMapper annotationMapper;

    public void setTraceMapper(TraceMapper traceMapper) {
        this.traceMapper = traceMapper;
    }

    public void setSpanMapper(SpanMapper spanMapper) {
        this.spanMapper = spanMapper;
    }

    public void setAnnotationMapper(AnnotationMapper annotationMapper) {
        this.annotationMapper = annotationMapper;
    }


}
