package com.jd.bdp.hydra.agent.dubbo.tracker;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.monitor.MonitorService;
import com.jd.bdp.hydra.agent.dubbo.DubboTracerService;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: xiangkui
 * Date: 13-3-19
 * Time: 上午10:13
 * To change this template use File | Settings | File Templates.
 */
public class MockDubboTracerService implements DubboTracerService {
    private URL statistics;

    public void collect(URL statistics) {
        System.out.println("collect....................");
        this.statistics = statistics;
    }

    public URL getStatistics() {
        return statistics;
    }

    public List<URL> lookup(URL query) {
        return Arrays.asList(statistics);
    }

}
