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

package com.jd.bdp.registry.client;

import com.jd.bdp.hydra.dubbomonitor.LeaderService;

import javax.validation.constraints.AssertTrue;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: biandi
 * Date: 13-4-10
 * Time: 下午4:12
 */
public class LeaderClientService implements LeaderService{
    @Override
    public Map<String, String> registerClient(String name, List<String> services) {
        System.out.println("开始注册~") ;
        Map<String, String> map = new HashMap<String, String>();
        map.put("seed", "1000");
        return map;
    }
}
