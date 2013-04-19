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

package com.jd.bdp.startUp.manager.simulation.support;

import com.jd.bdp.hydra.dubbomonitor.LeaderService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: 项奎
 * Date: 13-4-12
 * Time: 下午1:10
 */

/**
 * 服务需要暴露，接收来自不同应用点的注册请求
 */
public class LeaderManagerService implements LeaderService{
    @Override
    public Map<String, String> registerClient(String name, List<String> services) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("seed", "1000");
        map.put("com.jd.bdp.service.inter.InterfaceA", "1");
        map.put("com.jd.bdp.service.inter.InterfaceB", "2");
        map.put("com.jd.bdp.service.inter.InterfaceC", "3");
        return map;
    }
}
