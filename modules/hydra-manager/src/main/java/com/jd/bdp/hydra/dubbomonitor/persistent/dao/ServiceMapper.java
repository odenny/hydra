package com.jd.bdp.hydra.dubbomonitor.persistent.dao;

import com.jd.bdp.hydra.dubbomonitor.persistent.entity.ServicePara;

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
    ServicePara getOneService(Integer id);

    //删除所有
    void deleteAll();
}
