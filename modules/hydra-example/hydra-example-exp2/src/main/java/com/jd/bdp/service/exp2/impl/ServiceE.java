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


import com.jd.bdp.service.exp2.inter.InterfaceE;

/**
 * User: xiangkui
 * Date: 13-4-8
 * Time: 下午2:34
 */
public class ServiceE implements InterfaceE {

    static int mySwitch = 1;

    @Override
    public Object functionE(Object... objects) {
        String myVoice = new String("E");
        String returnVoice = myVoice.toString();
        returnVoice = "(" + returnVoice + ")";
//        if (mySwitch > 0){
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
//        }
        mySwitch = -mySwitch;
        return returnVoice;
    }
}
