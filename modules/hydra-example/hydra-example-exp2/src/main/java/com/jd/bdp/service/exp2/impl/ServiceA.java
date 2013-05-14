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

package com.jd.bdp.service.exp2.impl;


import com.jd.bdp.service.exp2.inter.InterfaceA;
import com.jd.bdp.service.exp2.inter.InterfaceB;

/**
 * User: xiangkui
 * Date: 13-4-8
 * Time: 下午2:34
 */
public class ServiceA implements InterfaceA {

    @Override
    public Object functionA(Object... objects) {
        String myVoice = new String("A");
        String returnVoice = myVoice.toString();
        if (downService != null) {
            Object result = downService.functionB(objects, myVoice);
            returnVoice = returnVoice + "-><-" + result.toString();
        }
        returnVoice = "(" + returnVoice + ")";
        return returnVoice;
    }

    private InterfaceB downService;

    public void setDownService(InterfaceB downService) {
        this.downService = downService;
    }
}
