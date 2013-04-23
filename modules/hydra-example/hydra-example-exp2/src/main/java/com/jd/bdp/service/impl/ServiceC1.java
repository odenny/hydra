package com.jd.bdp.service.impl;

import com.jd.bdp.service.inter.InterfaceA;
import com.jd.bdp.service.inter.InterfaceB;
import com.jd.bdp.service.inter.InterfaceC1;
import com.jd.bdp.service.inter.InterfaceE;

/**
 * User: xiangkui
 * Date: 13-4-8
 * Time: 下午2:34
 */
public class ServiceC1 implements InterfaceC1 {

    @Override
    public Object functionC1(Object... objects) {
        String myVoice = new String("C1");
        String returnVoice = myVoice.toString();
        if (serviceE != null) {
            Object result = serviceE.functionE(objects, myVoice);
            returnVoice = returnVoice + "-><-" + result.toString();
        }
        returnVoice = "(" + returnVoice + ")";
        return returnVoice;
    }

    private InterfaceE serviceE;

    public InterfaceE getServiceE() {
        return serviceE;
    }
}
