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

import com.jd.bdp.service.exp2.inter.InterfaceB;
import com.jd.bdp.service.exp2.inter.InterfaceC1;
import com.jd.bdp.service.exp2.inter.InterfaceC2;

/**
 * User: xiangkui
 * Date: 13-4-8
 * Time: 下午2:34
 */
public class ServiceB implements InterfaceB {

    @Override
    public Object functionB(Object... objects) {
        String myVoice = new String("B");
        String returnVoice = myVoice.toString();
        returnVoice = returnVoice + "-><-";
        if (serviceC1 != null) {
            Object result = serviceC1.functionC1(objects, myVoice);
            returnVoice = returnVoice + "" + result.toString();
        }
        if (serviceC2 != null) {
            Object result = serviceC2.functionC2(objects, myVoice);
            returnVoice = returnVoice + "," + result.toString();
        }
        returnVoice = "(" + returnVoice + ")";
        return returnVoice;
    }

    private InterfaceC1 serviceC1;
    private InterfaceC2 serviceC2;

    public void setServiceC1(InterfaceC1 serviceC1) {
        this.serviceC1 = serviceC1;
    }

    public void setServiceC2(InterfaceC2 serviceC2) {
        this.serviceC2 = serviceC2;
    }
}
