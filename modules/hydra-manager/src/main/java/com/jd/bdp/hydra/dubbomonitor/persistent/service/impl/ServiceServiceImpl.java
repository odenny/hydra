package com.jd.bdp.hydra.dubbomonitor.persistent.service.impl;

import com.jd.bdp.hydra.dubbomonitor.persistent.dao.AppMapper;
import com.jd.bdp.hydra.dubbomonitor.persistent.dao.ServiceMapper;
import com.jd.bdp.hydra.dubbomonitor.persistent.entity.AppPara;
import com.jd.bdp.hydra.dubbomonitor.persistent.entity.ServicePara;
import com.jd.bdp.hydra.dubbomonitor.persistent.service.ServiceIdGenService;
import com.jd.bdp.hydra.dubbomonitor.persistent.service.ServiceService;

/**
 * User: biandi
 * Date: 13-4-3
 * Time: 下午1:17
 */
public class ServiceServiceImpl implements ServiceService {

    @Override
    public synchronized Integer getServiceId(String serviceName, String appName) {
        AppPara appPara = appMapper.getApp(appName);
        if (appPara == null) {
            throw new RuntimeException("在获取service标识之前不可能没有对应的App!");
        } else {
            ServicePara service = serviceMapper.getService(serviceName, appPara.getId());
            if (service == null) {
                service = new ServicePara();
                service.setId(serviceIdGenService.getNewServiceId());
                service.setName(serviceName);
                service.setAppId(appPara.getId());
                serviceMapper.addService(service);
                return service.getId();
            } else {
                return service.getId();
            }
        }
    }

    private ServiceMapper serviceMapper;
    private AppMapper appMapper;
    private ServiceIdGenService serviceIdGenService;

    public void setServiceMapper(ServiceMapper serviceMapper) {
        this.serviceMapper = serviceMapper;
    }

    public void setAppMapper(AppMapper appMapper) {
        this.appMapper = appMapper;
    }

    public void setServiceIdGenService(ServiceIdGenService serviceIdGenService) {
        this.serviceIdGenService = serviceIdGenService;
    }
}
