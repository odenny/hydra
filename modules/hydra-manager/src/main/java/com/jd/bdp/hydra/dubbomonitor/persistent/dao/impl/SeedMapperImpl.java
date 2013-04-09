package com.jd.bdp.hydra.dubbomonitor.persistent.dao.impl;

import com.jd.bdp.hydra.dubbomonitor.persistent.dao.SeedMapper;
import com.jd.bdp.hydra.dubbomonitor.persistent.entity.SeedData;
import org.mybatis.spring.SqlSessionTemplate;

/**
 * User: xiangkui
 * Date: 13-4-1
 * Time: 下午1:15
 */
public class SeedMapperImpl implements SeedMapper {
    private SqlSessionTemplate sqlSession;

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

    //TODO
    @Override
    public void addSeed(SeedData servicePara) throws Exception {
        Integer id=null;
        boolean flag = false;
        try {
            flag=sqlSession.insert("addSeed",servicePara)>0?true:false;
            id=servicePara.getId();
        } catch (Exception e) {
            //e.printStackTrace();
            flag=false;
            throw e;
        }
    }

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

    //getter and setter
    public SqlSessionTemplate getSqlSession() {
        return sqlSession;
    }

    public void setSqlSession(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }


}
