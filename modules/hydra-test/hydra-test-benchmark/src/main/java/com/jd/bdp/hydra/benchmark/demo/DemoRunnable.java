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

package com.jd.bdp.hydra.benchmark.demo;

import com.jd.bdp.hydra.benchmark.support.AbstractClientRunnable;
import com.jd.bdp.hydra.benchmark.support.ServiceFactory;
import com.jd.bdp.service.inter.InterfaceA;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
/**
 * User: xiangkui
 * Date: 13-4-9
 * Time: 下午3:25
 */
public class DemoRunnable extends AbstractClientRunnable {

    public DemoRunnable(String targetIP, int targetPort, int clientNums, int rpcTimeout, CyclicBarrier barrier, CountDownLatch latch, long startTime, long endTime) {
        super(targetIP, targetPort, clientNums, rpcTimeout, barrier, latch, startTime, endTime);
    }

    @Override
    public Object invoke(ServiceFactory serviceFactory) {
        InterfaceA rootService = (InterfaceA) serviceFactory.get(InterfaceA.class);
        return rootService.functionA();
    }
}
