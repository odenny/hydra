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

import com.jd.bdp.hydra.mysql.persistent.dao.ServiceIdGenMapper;
import com.jd.bdp.hydra.mysql.persistent.entity.ServiceIdGen;
import com.jd.bdp.hydra.mysql.persistent.service.ServiceIdGenService;

/**
 * User: biandi
 * Date: 13-4-16
 * Time: 上午10:04
 */
public class ServiceIdGenServiceImpl implements ServiceIdGenService {

    @Override
    public synchronized String getNewServiceId() {
        ServiceIdGen serviceIdGen = serviceIdGenMapper.getServiceIdGen();
        int newTrueId;
        if (serviceIdGen.getMaxId() == serviceIdGen.getIdScope() - 1){
            newTrueId = 0;
        }else {
            newTrueId = serviceIdGen.getMaxId() + 1;
        }
        int oldHeadId = serviceIdGen.getHead();
        int newHeadId;
        if (oldHeadId == serviceIdGen.getMaxHead()){
            newHeadId = 1;
        }else {
            newHeadId = oldHeadId + 1;
        }
        int serviceId = newHeadId * serviceIdGen.getIdScope() + newTrueId;
        serviceIdGen.setHead(newHeadId);
        serviceIdGen.setMaxId(newTrueId);
        serviceIdGenMapper.updateServiceIdGen(serviceIdGen);
        int fullLength = String.valueOf(serviceIdGen.getMaxHead() * serviceIdGen.getIdScope()).length();
        if (String.valueOf(serviceId).length() < fullLength){
            return "0" + String.valueOf(serviceId);//只限head为2位的
        }else {
            return String.valueOf(serviceId);
        }
    }

    private ServiceIdGenMapper serviceIdGenMapper;

    public void setServiceIdGenMapper(ServiceIdGenMapper serviceIdGenMapper) {
        this.serviceIdGenMapper = serviceIdGenMapper;
    }

}
