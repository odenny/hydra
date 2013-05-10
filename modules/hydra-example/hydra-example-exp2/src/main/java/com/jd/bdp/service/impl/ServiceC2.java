package com.jd.bdp.service.impl;

import com.jd.bdp.service.inter.*;

/**
 * User: xiangkui
 * Date: 13-4-8
 * Time: 下午2:34
 */
public class ServiceC2 implements InterfaceC2 {

    @Override
    public Object functionC2(Object... objects) {
        String myVoice = new String("C2");
        String returnVoice = myVoice.toString();
        if(serviceD1!=null){
            Object result=serviceD1.functionD1(objects,myVoice);
            returnVoice = returnVoice + "" + result.toString();
        }
        if(serviceD2!=null){
            Object result=serviceD2.functionD2(objects,myVoice);
            returnVoice = returnVoice + "," + result.toString();
        }
        returnVoice = "(" + returnVoice + ")";
        return returnVoice;
    }


    private InterfaceD1 serviceD1;
    private InterfaceD2 serviceD2;

    public void setServiceD1(InterfaceD1 serviceD1) {
        this.serviceD1 = serviceD1;
    }

    public void setServiceD2(InterfaceD2 serviceD2) {
        this.serviceD2 = serviceD2;
    }
}
