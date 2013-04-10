package com.jd.bdp.trigger.impl;

import org.junit.Test;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * User: xiangkui
 * Date: 13-4-1
 * Time: 下午1:27
 */
public class MockTest extends AbstractDependencyInjectionSpringContextTests {
    private Trigger trigger;
    @Override
    protected String[] getConfigLocations() {
        String[] location = {"/dubbo-service-context.xml"};
        return location;
    }
    @Test
    public void testTriggerService() throws Exception {
        trigger.startWork();
    }

    //getter and setter
    public Trigger getTrigger() {
        return trigger;
    }
    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }
}
