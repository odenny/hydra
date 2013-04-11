package com.jd.bdp.registry.client;

import com.jd.bdp.hydra.Span;
import com.jd.bdp.hydra.agent.Tracer;
import com.jd.bdp.hydra.agent.support.Configuration;
import com.jd.bdp.trigger.impl.Trigger;
import org.junit.Test;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import java.util.List;

/**
 * User: xiangkui
 * Date: 13-4-10
 * Time: 下午6:59
 */
public class RegistryClientTest extends AbstractDependencyInjectionSpringContextTests {

    @Override
    protected String[] getConfigLocations() {
        String[] location = {
                "/dubbo-service-context.xml",//业务系统
        };
        return location;
    }


    //发起4次RPC，验证所发送的Span数据是否符合Hydra业务规则
    @Test
    public void testRegistry(){
        TestLeaderService leaderService = new TestLeaderService();
        Tracer.setConfiguration(null, leaderService, null);
        trigger.startWork(4);
    }

    private Trigger trigger;

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }
}
