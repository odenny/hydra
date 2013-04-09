package com.jd.bdp.hydra.agent;

import com.jd.bdp.hydra.Span;
import com.jd.bdp.hydra.agent.support.TraceService;

import java.util.List;
import java.util.Map;

/**
 * Date: 13-3-19
 */
public interface SyncTransfer{
    public Long getTraceId();
    public Long getSpanId();
    public boolean isReady();
    public void start() throws Exception;
    public String getServiceId(String name);
    public void cancel();
    public void syncSend(Span span);
    public void setTraceService(TraceService traceService);
    public String appName();
}
