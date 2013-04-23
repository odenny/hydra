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

package com.jd.bdp.hydra.mysql.persistent.dao;

import com.jd.bdp.hydra.mysql.persistent.entity.ServicePara;

import java.util.List;

/**
 * User: xiangkui
 * Date: 13-4-2
 * Time: 下午3:28
 */
public interface ServiceMapper {
    /*新增加一个应用,返回值为主键 ID
    * 操作失败，返回null*/
    void addService(ServicePara servicePara);

    //根据name查找ServicePara
    ServicePara getService(String name, Integer appId);

    /*删除一个应用*/
    void deleteService(ServicePara servicePara);

    /*更新应用信息*/
    void updateService(ServicePara servicePara);

    /*获得一个应用实体，
    操作失败或数据库没有相应数据，返回null*/
    ServicePara getOneService(String id);

    //删除所有
    void deleteAll();

    //根据appId查找
    List<ServicePara> get(Integer appId);
}
