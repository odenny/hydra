package com.jd.bdp.service.impl;


import com.jd.bdp.service.inter.InterfaceB;
import com.jd.bdp.service.inter.InterfaceC;

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
        String myVoice = "------------>B";
        String result = "";
        if (downService != null) {
            if (mySwitch > 0) {
                result = downService.functionC(objects).toString();
            }
            mySwitch = -mySwitch;
        }
        return myVoice + result;
    }


    private InterfaceC downService;

    //getter and setter
    public void setDownService(InterfaceC downService) {
        this.downService = downService;
    }
}
