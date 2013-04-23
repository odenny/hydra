package com.jd.bdp.service.impl;

import com.jd.bdp.service.inter.InterfaceA;
import com.jd.bdp.service.inter.InterfaceB;
import com.jd.bdp.service.inter.InterfaceD2;

/**
 * User: xiangkui
 * Date: 13-4-8
 * Time: 下午2:34
 */
public class ServiceD2 implements InterfaceD2 {

    @Override
    public Object functionD2(Object... objects) {
        String myVoice = new String("D2");
        String returnVoice = myVoice.toString();
        returnVoice = "(" + returnVoice + ")";
        return returnVoice;
    }
}
