package com.jd.bdp.hydra.benchmark.startABC;

import com.jd.bdp.service.impl.ServiceC;

/**
 * User: xiangkui
 * Date: 13-4-11
 * Time: 下午2:57
 */
public class StartC {
    public static void main(String[] args) throws Exception {
        new ServiceC().startUp(args);
        Thread.sleep(Long.MAX_VALUE);
    }
}
