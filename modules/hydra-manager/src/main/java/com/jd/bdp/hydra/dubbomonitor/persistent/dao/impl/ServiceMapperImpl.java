package com.jd.bdp.hydra.dubbomonitor.persistent.dao.impl;

import com.jd.bdp.hydra.dubbomonitor.persistent.dao.ServiceMapper;
import com.jd.bdp.hydra.dubbomonitor.persistent.entity.ServicePara;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.HashMap;
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
    public ServicePara getOneService(Integer id) {
        return (ServicePara) sqlSession.selectOne("getServiceById",id);
    }

    @Override
    public void deleteAll() {
        sqlSession.delete("deleteAllService");
    }

    public void setSqlSession(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }

}
