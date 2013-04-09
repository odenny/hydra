package com.jd.bdp.hydra.dubbomonitor.persistent.dao.impl;

import com.jd.bdp.hydra.dubbomonitor.persistent.dao.AppMapper;
import com.jd.bdp.hydra.dubbomonitor.persistent.entity.AppPara;
import org.mybatis.spring.SqlSessionTemplate;

/**
 * User: xiangkui
 * Date: 13-4-1
 * Time: 下午1:15
 */
public class AppMapperImpl implements AppMapper {
    private SqlSessionTemplate sqlSession;

    @Override
    public void addApp(AppPara servicePara) {
        sqlSession.insert("addApp",servicePara);
    }

    @Override
    public AppPara getApp(String name) {
        return (AppPara) sqlSession.selectOne("getAppByName",name);
    }

    @Override
    public void deleteApp(AppPara servicePara) {
        sqlSession.delete("deleteApp",servicePara);
    }

    @Override
    public void updateApp(AppPara servicePara) {
        sqlSession.update("updateApp",servicePara);
    }

    @Override
    public AppPara getOneApp(Integer id) {
        return (AppPara) sqlSession.selectOne("getAppById",id);
    }

    @Override
    public void deleteAll() {
        sqlSession.delete("deleteAllApp");
    }

    public void setSqlSession(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }

}
