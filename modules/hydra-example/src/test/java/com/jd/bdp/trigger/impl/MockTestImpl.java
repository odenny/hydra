package com.jd.bdp.trigger.impl;

import com.jd.bdp.service.inter.support.Service;
import org.junit.Test;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * User: xiangkui
 * Date: 13-4-1
 * Time: 下午1:27
 */
public class MockTestImpl extends AbstractDependencyInjectionSpringContextTests {
    private Service rootService;
    @Override
    protected String[] getConfigLocations() {
        String[] location = {"/dubbo-service-context.xml"};
        return location;
    }
    @Test
    public void testTriggerService() throws Exception {
        Object result=rootService.function();
        System.out.println("调用结束，获得调用结果:"+result);
    }

    //getter and setter

    public Service getRootService() {
        return rootService;
    }

    public void setRootService(Service rootService) {
        this.rootService = rootService;
    }
}
