/*
 * Copyright jd
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.jd.bdp.hydra.mysql.persistent.service.impl;

import com.jd.bdp.hydra.mysql.persistent.dao.AppMapper;
import com.jd.bdp.hydra.mysql.persistent.dao.ServiceMapper;
import com.jd.bdp.hydra.mysql.persistent.entity.AppPara;
import com.jd.bdp.hydra.mysql.persistent.entity.ServicePara;
import com.jd.bdp.hydra.mysql.persistent.service.ServiceIdGenService;
import com.jd.bdp.hydra.mysql.persistent.service.ServiceService;

import java.util.List;

/**
 * User: biandi
 * Date: 13-4-3
 * Time: 下午1:17
 */
public class ServiceServiceImpl implements ServiceService {

    @Override
    public synchronized String getServiceId(String serviceName, String appName) {
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

    @Override
    public List<ServicePara> get(Integer appId) {
        return serviceMapper.get(appId);
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
