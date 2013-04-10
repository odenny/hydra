package com.jd.bdp.service.impl;


import com.jd.bdp.service.impl.support.AbstractService;
import com.jd.bdp.service.inter.InterfaceB;
import com.jd.bdp.service.inter.InterfaceC;
import com.jd.bdp.service.inter.support.Service;

/**
 * User: xiangkui
 * Date: 13-4-8
 * Time: 下午2:34
 */
public class ServiceB implements InterfaceB {

    static int mySwitch = 1;

    /*服务调用*/
    @Override
    public Object functionB(Object... objects) {
        String myVoice = new String("Hello,Im ServiceB!");
        String returnVoice = myVoice.toString();
        if (downService != null) {
            Object result = null;
            if (mySwitch > 0) {
                result = downService.functionC(objects);
                returnVoice = returnVoice + "-><-" + result.toString();
            }
            mySwitch = -mySwitch;
        }
        returnVoice = "(" + returnVoice + ")";
        return returnVoice;
    }


    private InterfaceC downService;
    //getter and setter
    public void setDownService(InterfaceC downService) {
        this.downService = downService;
    }
}
