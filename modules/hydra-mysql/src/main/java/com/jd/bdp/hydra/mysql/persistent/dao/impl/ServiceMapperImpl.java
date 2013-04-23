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

package com.jd.bdp.hydra.mysql.persistent.dao.impl;

import com.jd.bdp.hydra.mysql.persistent.dao.ServiceMapper;
import com.jd.bdp.hydra.mysql.persistent.entity.ServicePara;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: xiangkui
 * Date: 13-4-1
 * Time: 下午1:15
 */
public class ServiceMapperImpl implements ServiceMapper {
    private SqlSessionTemplate sqlSession;

    @Override
    public void addService(ServicePara servicePara) {
        sqlSession.insert("addService", servicePara);
    }

    @Override
    public ServicePara getService(String name, Integer appId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("appId", appId);
        return (ServicePara) sqlSession.selectOne("getServiceByName", map);
    }


    @Override
    public void deleteService(ServicePara servicePara) {
        sqlSession.delete("deleteService",servicePara);
    }

    @Override
    public void updateService(ServicePara servicePara) {
        sqlSession.update("updateService",servicePara);
    }

    @Override
    public ServicePara getOneService(String id) {
        return (ServicePara) sqlSession.selectOne("getServiceById",id);
    }

    @Override
    public void deleteAll() {
        sqlSession.delete("deleteAllService");
    }

    @Override
    public List<ServicePara> get(Integer appId) {
        return (List<ServicePara>)sqlSession.selectList("getServiceByAppId", appId);
    }

    public void setSqlSession(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }

}
