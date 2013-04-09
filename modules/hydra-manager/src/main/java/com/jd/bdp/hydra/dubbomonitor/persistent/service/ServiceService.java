package com.jd.bdp.hydra.dubbomonitor.persistent.service;

/**
 * User: biandi
 * Date: 13-4-3
 * Time: 下午1:17
 */
public interface ServiceService {
    Integer getServiceId(String serviceName, String appName);
}
