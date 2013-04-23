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
        return returnVoice;
    }
}
