package com.jd.bdp.hydra.benchmark.exp1;

import com.jd.bdp.service.impl.ServiceB;

/**
 * User: xiangkui
 * Date: 13-4-11
 * Time: 下午2:57
 */
public class StartB {
    public static void main(String[] args) throws Exception {
        new ServiceB().startUp(args);
        Thread.sleep(Long.MAX_VALUE);
    }
}
