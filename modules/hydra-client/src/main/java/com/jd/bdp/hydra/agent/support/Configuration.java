package com.jd.bdp.hydra.agent.support;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 13-4-3
 * Time: 上午10:05
 */
public class Configuration {

    private String applicationName;
    private List<String> services;
    private Long delayTime; //保留字段，延迟多久发送一批到收集端
    private Long flushSize;//保留字段，一批发送多少条消息到收集端
    private Integer queueSize;//缓冲队列大小，可以访问量设置。

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
