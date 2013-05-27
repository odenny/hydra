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


import com.jd.bdp.hydra.mysql.persistent.dao.SeedMapper;
import com.jd.bdp.hydra.mysql.persistent.entity.SeedData;
import org.mybatis.spring.SqlSessionTemplate;

/**
 * User: xiangkui
 * Date: 13-4-1
 * Time: 下午1:15
 */
public class SeedMapperImpl implements SeedMapper {
    private SqlSessionTemplate sqlSession;

    /**
     * 验证该种子是否已经派发过
     * 内部逻辑：
     * 1：找最大索引Id行，获取种子value值 为V（有记录行）
     * 2：若没有记录行，则返回false （无记录行）
     * 2：若value>V,则返回false，否则返回true
     * @param value 种子值
     * @return 是否派发过
     * @throws Exception 持久层异常
     * @since 2.0
     */
    @Override
    public boolean hasSeed(Integer value) throws Exception {
        Integer result_count = null;
        boolean hasSeed=false;
        try {
            result_count = (Integer) sqlSession.selectOne("findSeedByValue",value);
            hasSeed=(result_count==0?false:true);
        } catch (Exception e) {
            throw e;
        }
        return hasSeed;
    }
    /**
     * 找到种子标示值
     * 内部逻辑：
     * 1：找最大索引Id行，获取种子value值 为V
     * 1.1返回记录行（有记录）
     * 1.2返回null （无记录）
     * @return 种子标识
     * @throws Exception 持久层异常
     * @since 2.0
     */
    @Override
    public SeedData findTheSeed() throws Exception {
        SeedData seedData = null;
        try {
            seedData = (SeedData) sqlSession.selectOne("findTheSeed");
        } catch (Exception e) {
            throw e;
        }
        return seedData;
    }

    @Override
    public Integer getMaxSeedValue() throws Exception {
        Integer max_value = null;
        try {
            max_value = (Integer) sqlSession.selectOne("getMaxSeedValue");
        } catch (Exception e) {
            throw e;
        }
        return max_value;
    }
    @Override
    public void addSeed(SeedData servicePara) throws Exception {
        Integer id=null;
        boolean flag = false;
        try {
            flag=sqlSession.insert("addSeed",servicePara)>0?true:false;
            id=servicePara.getId();
        } catch (Exception e) {
            flag=false;
            throw e;
        }
    }

    @Deprecated
    @Override
    public void deleteSeed(SeedData servicePara) throws Exception {
        boolean flag = false;
        try {
            flag=sqlSession.delete("deleteSeed",servicePara)>0?true:false;
        } catch (Exception e) {
            flag=false;
            throw e;
        }
    }

    @Override
    public void updateSeed(SeedData servicePara) throws Exception {
        boolean flag = false;
        try {
            flag=sqlSession.update("updateSeed",servicePara)>0?true:false;
        } catch (Exception e) {
            flag=false;
            throw e;
        }
    }

    @Deprecated
    @Override
    public SeedData getOneSeed(Integer id) throws Exception {
        SeedData seedData = null;
        try {
            seedData = (SeedData) sqlSession.selectOne("getSeedById",id);
        } catch (Exception e) {
            throw e;
        }
        return seedData;
    }



    //getter and setter
    public SqlSessionTemplate getSqlSession() {
        return sqlSession;
    }

    public void setSqlSession(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }


}
