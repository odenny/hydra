package com.jd.bdp.service.impl;

import com.jd.bdp.service.inter.InterfaceA;
import com.jd.bdp.service.inter.InterfaceB;
import com.jd.bdp.service.inter.InterfaceC1;
import com.jd.bdp.service.inter.InterfaceC2;

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
        if(serviceC1!=null){
            Object result=serviceC1.functionC1(objects,myVoice);
            returnVoice = returnVoice + "" + result.toString();
        }
        if(serviceC2!=null){
            Object result=serviceC2.functionC2(objects,myVoice);
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
