package com.jd.bdp.hydra.dubbomonitor.persistent.dao;

import com.jd.bdp.hydra.dubbomonitor.persistent.entity.AppPara;

/**
 * User: xiangkui
 * Date: 13-4-2
 * Time: 下午3:28
 */
public interface AppMapper {
    /*新增加一个应用*/
    void addApp(AppPara appPara);

    //根据name查找AppPara
    AppPara getApp(String name);

    /*删除一个应用*/
    void deleteApp(AppPara appPara);

    /*更新应用信息*/
    void updateApp(AppPara appPara);

    /*获得一个应用实体*/
    AppPara getOneApp(Integer id);

    //删除所有
    void deleteAll();
}
