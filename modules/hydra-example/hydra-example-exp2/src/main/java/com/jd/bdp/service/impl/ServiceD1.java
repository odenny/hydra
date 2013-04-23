package com.jd.bdp.service.impl;

import com.jd.bdp.service.inter.InterfaceA;
import com.jd.bdp.service.inter.InterfaceB;
import com.jd.bdp.service.inter.InterfaceC1;
import com.jd.bdp.service.inter.InterfaceD1;

/**
 * User: xiangkui
 * Date: 13-4-8
 * Time: 下午2:34
 */
public class ServiceD1 implements InterfaceD1 {

    @Override
    public Object functionD1(Object... objects) {
        String myVoice = new String("D1");
        String returnVoice = myVoice.toString();
        if (serviceC1 != null) {
            Object result = serviceC1.functionC1(objects, myVoice);
            returnVoice = returnVoice + "-><-" + result.toString();
        }
        returnVoice = "(" + returnVoice + ")";
        return returnVoice;
    }

    private InterfaceC1 serviceC1;

    public void setServiceC1(InterfaceC1 serviceC1) {
        this.serviceC1 = serviceC1;
    }
}
