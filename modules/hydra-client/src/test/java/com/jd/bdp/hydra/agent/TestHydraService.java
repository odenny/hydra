package com.jd.bdp.hydra.agent;

import com.jd.bdp.hydra.Span;
import com.jd.bdp.hydra.dubbomonitor.HydraService;

import java.util.List;

/**
 * User: yfliuyu
 * Date: 13-4-7
 * Time: 下午3:22
 */
public class TestHydraService implements HydraService{
    @Override
    public boolean push(List<Span> span) {
        System.out.println("span size:"+span.size());
        return false;
    }
}
