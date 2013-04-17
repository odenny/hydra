package com.jd.bdp.hydra.agent;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.jd.bdp.hydra.Annotation;
import com.jd.bdp.hydra.BinaryAnnotation;
import com.jd.bdp.hydra.Endpoint;
import com.jd.bdp.hydra.Span;
import com.jd.bdp.hydra.agent.support.Configuration;
import com.jd.bdp.hydra.agent.support.DefaultSyncTransfer;
import com.jd.bdp.hydra.agent.support.SampleImp;
import com.jd.bdp.hydra.agent.support.TraceService;
import com.jd.bdp.hydra.dubbomonitor.HydraService;
import com.jd.bdp.hydra.dubbomonitor.LeaderService;

/**
 * Date: 13-3-19
 * Time: 下午4:14
 */
public class Tracer {

    private static final Logger logger = LoggerFactory.getLogger(Tracer.class);

    private static Tracer tracer = null;

    private Sampler sampler = new SampleImp();

    private SyncTransfer transfer = null;

    private ThreadLocal<Span> spanThreadLocal = new ThreadLocal<Span>();

    private Tracer() {
    }


    public void removeParentSpan() {
        spanThreadLocal.remove();
    }

    public Span getParentSpan() {
        return spanThreadLocal.get();
    }

    public void setParentSpan(Span span) {
        spanThreadLocal.set(span);
    }

    public Span genSpan(Long traceId, Long pid, Long id, String spanname, boolean isSample) {
        Span span = new Span();
        span.setId(id);
        span.setParentId(pid);
        span.setSpanName(spanname);
        span.setSample(isSample);
        span.setTraceId(traceId);
        return span;
    }

    public Span newSpan(String spanname,Endpoint endpoint) {
        boolean s = isSample();
        Span span = new Span();
        span.setTraceId(s ? genTracerId() : null);
        span.setId(s ? genSpanId() : null);
        span.setSpanName(spanname);
        span.setSample(s);
        if (s) {
            BinaryAnnotation appname = new BinaryAnnotation();
            appname.setKey("dubbo.applicationName");
            appname.setValue(transfer.appName().getBytes());
            appname.setType("string");
            appname.setHost(endpoint);
            span.addBinaryAnnotation(appname);
        }
        return span;
    }

    public Endpoint newEndPoint() {
        return new Endpoint();
    }

    public static Tracer getTracer() {
        if (tracer == null) {
            synchronized (Tracer.class) {
                tracer = new Tracer();
            }
        }
        return tracer;
    }

    public void setConfig(Configuration c, LeaderService leaderService, HydraService hydraService) throws Exception {
        transfer = new DefaultSyncTransfer(c);
        TraceService traceService = new TraceService();
        traceService.setHydraService(hydraService);
        traceService.setLeaderService(leaderService);
        transfer.setTraceService(traceService);
        transfer.start();
    }

    public static void setConfiguration(Configuration configuration, LeaderService leaderService, HydraService hydraService) {
        Tracer t = getTracer();
        try {
            t.setConfig(configuration, leaderService, hydraService);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public boolean isSample() {
        return sampler.isSample() && (transfer != null && transfer.isReady());
    }

    public void addBinaryAnntation(BinaryAnnotation b) {
        Span span = spanThreadLocal.get();
        if (span != null) {
            span.addBinaryAnnotation(b);
        }
    }

    public void clientSendRecord(Span span, Endpoint endpoint, long start) {
        Annotation annotation = new Annotation();
        annotation.setValue(Annotation.CLIENT_SEND);
        annotation.setTimestamp(start);
        annotation.setHost(endpoint);
        span.addAnnotation(annotation);
    }


    public void clientReceiveRecord(Span span, Endpoint endpoint, long end) {
        Annotation annotation = new Annotation();
        annotation.setValue(Annotation.CLIENT_RECEIVE);
        annotation.setHost(endpoint);
        annotation.setTimestamp(end);
        span.addAnnotation(annotation);
        transfer.syncSend(span);
    }


    public void serverReceiveRecord(Span span, Endpoint endpoint, long start) {
        Annotation annotation = new Annotation();
        annotation.setValue(Annotation.SERVER_RECEIVE);
        annotation.setHost(endpoint);
        annotation.setTimestamp(start);
        span.addAnnotation(annotation);
        spanThreadLocal.set(span);
    }

    public void serverSendRecord(Span span, Endpoint endpoint, long end) {
        Annotation annotation = new Annotation();
        annotation.setTimestamp(end);
        annotation.setHost(endpoint);
        annotation.setValue(Annotation.SERVER_SEND);
        span.addAnnotation(annotation);
        transfer.syncSend(span);
    }


    public String getServiceId(String name) {
        String id = null;
        if(transfer != null){
            id = transfer.getServiceId(name);
        }
        return id;
    }

    public Long genTracerId() {
        return transfer.getTraceId();
    }

    public Long genSpanId() {
        return transfer.getSpanId();
    }
}

