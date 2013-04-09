package com.jd.bdp.hydra.dubbomonitor.persistent.service.impl;

import com.jd.bdp.hydra.dubbomonitor.persistent.dao.SeedMapper;
import com.jd.bdp.hydra.dubbomonitor.persistent.entity.SeedData;
import com.jd.bdp.hydra.dubbomonitor.persistent.service.SeedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: biandi
 * Date: 13-4-7
 * Time: 下午2:46
 */
public class SeedServiceImpl implements SeedService{

    private static final Logger log = LoggerFactory.getLogger(SeedServiceImpl.class);

    private int plusId =0;
    private static Integer MAX_STEP = 0xffff;

    /**
     * 派发一个seed字串
     * @return seed串  null表示无效
     */
    public synchronized Long getSeed() {
        try {
            SeedData seedData=seedMapper.findTheSeed();
            plusId= seedData.getValue()+1;
            seedData.setValue(plusId); //持久
            seedMapper.updateSeed(seedData);
        } catch (Exception e) {
            log.error("seed persistent into the database occur error,will use default seed");
            plusId=0;
        }
        if (plusId>= MAX_STEP) {
            log.warn("seed has bean used over! please insure the stability of the system~");
            plusId=0;
        }
        return 1L*plusId;
    }
    private SeedMapper seedMapper;

    public void setSeedMapper(SeedMapper seedMapper) {
        this.seedMapper = seedMapper;
    }
}
