package com.jd.bdp.hydra;

import java.io.Serializable;

/**
 * User: yfliuyu
 * Date: 13-3-18
 * Time: 下午3:36
 */
public class Endpoint implements Serializable {
    private String ip;
    private Integer port;
    private String serviceName;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public String toString() {
        return "Endpoint{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", serviceName='" + serviceName + '\'' +
                '}';
    }
}
