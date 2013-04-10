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

package com.jd.bdp.sending;

import com.jd.bdp.hydra.Span;
import com.jd.bdp.hydra.agent.Tracer;
import com.jd.bdp.hydra.agent.support.Configuration;
import com.jd.bdp.hydra.dubbomonitor.HydraService;
import com.jd.bdp.hydra.dubbomonitor.LeaderService;
import com.jd.bdp.service.impl.ServiceA;
import com.jd.bdp.trigger.impl.Trigger;
import org.junit.Test;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import java.util.List;

/**
 * User: biandi
 * Date: 13-4-10
 * Time: 下午2:44
 */
public class SendingTest extends AbstractDependencyInjectionSpringContextTests {

    @Override
    protected String[] getConfigLocations() {
        String[] location = {"/dubbo-service-context.xml"};
        return location;
    }


    //发起4次RPC，验证所发送的Span数据是否符合Hydra业务规则
    @Test
    public void testSendSpanSum(){
        Configuration c = new Configuration();
        c.setApplicationName("bigbully");
        c.setQueueSize(500);
        LeaderService leaderService = new TestLeaderService();
        HydraService hydraService = new TestHydraService();
        Tracer.setConfiguration(c, leaderService, hydraService);
        trigger.startWork(4);
        List<Span> results = ((TestHydraService)hydraService).getResults();
        System.out.println(results.size());
    }

    private Trigger trigger;

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }
}
