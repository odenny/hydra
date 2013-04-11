package com.jd.bdp.registry.manager;

import com.jd.bdp.hydra.agent.Tracer;
import com.jd.bdp.hydra.agent.support.Configuration;
import com.jd.bdp.hydra.dubbomonitor.HydraService;
import com.jd.bdp.hydra.dubbomonitor.LeaderService;
import com.jd.bdp.registry.client.TestLeaderService;
import com.jd.bdp.trigger.impl.Trigger;
import org.junit.Test;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * User: xiangkui
 * Date: 13-4-10
 * Time: 下午6:59
 */
public class RegistryTest extends AbstractDependencyInjectionSpringContextTests {

    @Override
    protected String[] getConfigLocations() {
        String[] location = {
                "/dubbo-service-context.xml",//业务系统
                "/registry/registry.xml"//注册中心
        };
        return location;
    }


    //发起4次RPC，验证所发送的Span数据是否符合Hydra业务规则
    @Test
    public void testRegistry(){
        Configuration c = new Configuration();
        c.setApplicationName("bigbully");
        c.setQueueSize(500);
        LeaderService leaderService = new TestLeaderService();
        HydraService hydraService = new TestHydraService();
        Tracer.setConfiguration(c, leaderService, hydraService);
        trigger.startWork(4);
    }

    private Trigger trigger;

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }
}
