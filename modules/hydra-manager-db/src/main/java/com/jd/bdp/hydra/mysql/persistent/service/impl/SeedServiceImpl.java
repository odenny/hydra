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

package com.jd.bdp.hydra.mysql.persistent.service.impl;

import com.jd.bdp.hydra.mysql.persistent.dao.SeedMapper;
import com.jd.bdp.hydra.mysql.persistent.entity.SeedData;
import com.jd.bdp.hydra.mysql.persistent.service.SeedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: biandi
 * Date: 13-4-7
 * Time: 下午2:46
 */
public class SeedServiceImpl implements SeedService {

    private static final Logger log = LoggerFactory.getLogger(SeedServiceImpl.class);

    private static Integer MAX_STEP = 0xffff;

    /**
     * 派发一个seed字串
     *
     * @return seed串  null表示无效
     */
    public synchronized Long getSeed() {
        Long result = null;
        try {
            SeedData tempSeed = seedMapper.findTheSeed();
            if (tempSeed != null) {
                int plusId = 0;
                plusId = tempSeed.getValue() + 1;
                tempSeed.setValue(plusId); //持久
                seedMapper.updateSeed(tempSeed);
                result = Long.valueOf(plusId);
            } else {//对应行 数据位null 尝试插入一条数据
                SeedData insertData = new SeedData();
                insertData.setValue(1);
                seedMapper.addSeed(insertData);
                result = insertData.getValue().longValue();
            }
        } catch (Exception e) {
            log.error("seed persistent into the database occur error,will use default seed");
            result=0L;
            throw new RuntimeException("get seed accur error",e.getCause());
        } finally {
            if (result != null && result >= MAX_STEP) {
                log.error("seed has bean used over! please insure the stability of the system~");
                result = 1L;
            }
            return result;
        }


    }

    private SeedMapper seedMapper;

    public void setSeedMapper(SeedMapper seedMapper) {
        this.seedMapper = seedMapper;
    }

    public SeedMapper getSeedMapper() {
        return seedMapper;
    }
}
