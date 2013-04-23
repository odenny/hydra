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

import com.jd.bdp.hydra.mysql.persistent.dao.AppMapper;
import com.jd.bdp.hydra.mysql.persistent.entity.AppPara;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.List;

/**
 * User: xiangkui
 * Date: 13-4-1
 * Time: 下午1:15
 */
public class AppMapperImpl implements AppMapper {
    private SqlSessionTemplate sqlSession;

    @Override
    public void addApp(AppPara servicePara) {
        sqlSession.insert("addApp", servicePara);
    }

    @Override
    public AppPara getApp(String name) {
        return (AppPara) sqlSession.selectOne("getAppByName", name);
    }

    @Override
    public void deleteApp(AppPara servicePara) {
        sqlSession.delete("deleteApp", servicePara);
    }

    @Override
    public void updateApp(AppPara servicePara) {
        sqlSession.update("updateApp", servicePara);
    }

    @Override
    public AppPara getOneApp(Integer id) {
        return (AppPara) sqlSession.selectOne("getAppById", id);
    }

    @Override
    public void deleteAll() {
        sqlSession.delete("deleteAllApp");
    }

    @Override
    public List<AppPara> getAll() {
        return (List<AppPara>)sqlSession.selectList("getAppAll");
    }

    public void setSqlSession(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }

}
