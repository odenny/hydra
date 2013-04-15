package com.jd.bdp.registry.client;

import com.jd.bdp.hydra.agent.Tracer;
import com.jd.bdp.registry.client.support.LeaderClientService;
import com.jd.bdp.trigger.impl.Trigger;
import org.junit.Test;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

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
                "/hydra-config.xml",
                "/registry/client/registry.xml" //客户端注册对象
        };
        return location;
    }


    //发起4次RPC，验证所发送的Span数据是否符合Hydra业务规则
    @Test
    public void testRegistry(){
        LeaderClientService leaderService = new LeaderClientService();
        Tracer.setConfiguration(null, leaderService, null);
        trigger.startWork(4);
    }

    private Trigger trigger;

    //getter and setter
    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }

    public Trigger getTrigger() {
        return trigger;
    }
}
