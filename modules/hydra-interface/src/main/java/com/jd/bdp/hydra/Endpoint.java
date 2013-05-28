package com.jd.bdp.hydra;

import java.io.Serializable;

/**
 * Date: 13-3-18
 * Time: 下午3:36
 */
public class Endpoint implements Serializable {
    private String ip;
    private Integer port;
//    private String serviceName;

    public Endpoint(){

    }
    public Endpoint(String ip, Integer port, String serviceName) {
        this.ip = ip;
        this.port = port;
//        this.serviceName = serviceName;
    }

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

//    public String getServiceName() {
//        return serviceName;
//    }
//
//    public void setServiceName(String serviceName) {
//        this.serviceName = serviceName;
//    }

    @Override
    public String toString() {
        return "Endpoint{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
//                ", serviceName='" + serviceName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Endpoint)) return false;

        Endpoint endpoint = (Endpoint) o;

        if (!ip.equals(endpoint.ip)) return false;
        if (!port.equals(endpoint.port)) return false;
//        if (!serviceName.equals(endpoint.serviceName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ip.hashCode();
        result = 31 * result + port.hashCode();
//        result = 31 * result + serviceName.hashCode();
        return result;
    }
}
