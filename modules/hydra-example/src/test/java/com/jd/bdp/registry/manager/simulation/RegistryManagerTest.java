package com.jd.bdp.registry.manager.simulation;

import com.jd.bdp.trigger.impl.Trigger;
import org.junit.Test;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * User: xiangkui
 * Date: 13-4-10
 * Time: 下午6:59
 */
public class RegistryManagerTest extends AbstractDependencyInjectionSpringContextTests {

    @Override
    protected String[] getConfigLocations() {
        String[] location = {
                "/registry/manager/registry.xml",//注册中心
                "/dubbo-service-context.xml",//业务系统
                "/hydra-config.xml",//hydra配置
        };
        return location;
    }


    //发起4次RPC，验证注册中心是否收到注册请求
    @Test
    public void testRegistry(){
        trigger.startWork(4);
    }

    private Trigger trigger;

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }
}
