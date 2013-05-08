package com.jd.bdp.service.impl;

import com.jd.bdp.service.inter.InterfaceA;
import com.jd.bdp.service.inter.InterfaceB;
import com.jd.bdp.service.inter.InterfaceE;

/**
 * User: xiangkui
 * Date: 13-4-8
 * Time: 下午2:34
 */
public class ServiceE implements InterfaceE {

    @Override
    public Object functionE(Object... objects) {
        String myVoice = new String("E");
        String returnVoice = myVoice.toString();
        returnVoice = "(" + returnVoice + ")";
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return returnVoice;
    }
}
