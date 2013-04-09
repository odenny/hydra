package com.jd.bdp.hydra.agent.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: yfliuyu
 * Date: 13-4-3
 * Time: 上午10:05
 */
public class Configuration {

    private String applicationName;
    private List<String> services;
    private Long delayTime;
    private Long flushSize;
    private Integer queueSize;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public List<String> getServices() {
        return services;
    }

    public void addService(String service){
        if(services == null){
            services = new ArrayList<String>();
        }
        services.add(service);
    }

    public void setServices(List<String> services) {
        this.services = services;
    }

    public Long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(Long delayTime) {
        this.delayTime = delayTime;
    }

    public Long getFlushSize() {
        return flushSize;
    }

    public void setFlushSize(Long flushSize) {
        this.flushSize = flushSize;
    }

    public Integer getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(Integer queueSize) {
        this.queueSize = queueSize;
    }
}
