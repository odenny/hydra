/*
 * Copyright jd
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
        trigger.startWork(10);
    }

    //getter and setter
    public Trigger getTrigger() {
        return trigger;
    }
    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }
}
