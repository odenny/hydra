package com.jd.bdp.service.impl;


import com.jd.bdp.service.impl.support.AbstractService;
import com.jd.bdp.service.inter.InterfaceB;
import com.jd.bdp.service.inter.support.Service;

/**
 * User: xiangkui
 * Date: 13-4-8
 * Time: 下午2:34
 */
public class ServiceB extends AbstractService implements InterfaceB {

    static int mySwitch = 1;
    /*服务调用*/
    @Override
    public Object function(Object... objects) {
        String myVoice=new String("Hello,Im ServiceB!");
        String returnVoice=myVoice.toString();
        if(downService!=null){
            Object result=null;
            if(mySwitch>0){
                result = downService.function(objects);
                returnVoice=returnVoice+"-><-"+result.toString();
            }
            mySwitch=-mySwitch;
        }
        returnVoice="("+returnVoice+")";
        return returnVoice;
    }

    //getter and setter
    public Service getDownService() {
        return downService;
    }

    public void setDownService(Service downService) {
        this.downService = downService;
    }
}
