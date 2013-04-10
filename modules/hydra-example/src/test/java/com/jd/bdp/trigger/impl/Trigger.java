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

import com.jd.bdp.service.inter.InterfaceA;
import com.jd.bdp.service.inter.support.Service;
/**
 * User: xiangkui
 * Date: 13-4-9
 * Time: 下午3:25
 */
public class Trigger {
    Service rootService;
    public void startWork(int num){
        for(int i=0;i<num;i++){
            Object result=rootService.function();
            System.out.println("result:"+result);
        }

    }
    //getter and setter
    public Service getRootService() {
        return rootService;
    }

    public void setRootService(Service rootService) {
        this.rootService = rootService;
    }
}
