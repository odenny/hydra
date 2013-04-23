package com.jd.bdp.service.impl;

import com.jd.bdp.service.inter.InterfaceA;
import com.jd.bdp.service.inter.InterfaceB;

/**
 * User: xiangkui
 * Date: 13-4-8
 * Time: 下午2:34
 */
public class ServiceA implements InterfaceA {

    @Override
    public Object functionA(Object... objects) {
        String myVoice = new String("Hello,Im ServiceA,you can call me SA");
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
