package com.jd.bdp.hydra.benchmark.startABC;

import com.jd.bdp.service.impl.ServiceA;
/**
 * User: xiangkui
 * Date: 13-4-11
 * Time: 下午2:57
 */
public class StartA {
    public static void main(String[] args) throws Exception {
        new ServiceA().startUp(args);
        Thread.sleep(Long.MAX_VALUE);
    }
}
