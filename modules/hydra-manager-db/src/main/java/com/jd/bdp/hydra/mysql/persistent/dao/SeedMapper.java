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


import com.jd.bdp.hydra.mysql.persistent.entity.SeedData;

/**
 * User: xiangkui
 * Date: 13-4-2
 * Time: 下午3:28
 */
public interface SeedMapper {
    /*判定是否有该种子值*/
    public boolean hasSeed(Integer value) throws Exception;

    /*获取数据库中最大的种子值，返回最大的种子值*/
    public Integer getMaxSeedValue() throws Exception;
    //--------------------------------------//

    /*新增加一个种子*/
    public void addSeed(SeedData seedData) throws Exception;

    /*删除一个种子*/
    public void deleteSeed(SeedData seedData) throws Exception;

    /*更新种子信息*/
    public void updateSeed(SeedData seedData) throws Exception;

    /*获得一个种子实体*/
    public SeedData getOneSeed(Integer id) throws Exception;

    //---* 版本2的种子逻辑-数据库中只存一行数据（即Max_VALUE）*-----------------------------
    public SeedData findTheSeed() throws Exception;

}
