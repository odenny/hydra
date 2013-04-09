package com.jd.bdp.service.impl;

import com.jd.bdp.service.impl.support.AbstractService;
import com.jd.bdp.service.inter.InterfaceA;
import com.jd.bdp.service.inter.support.Service;

/**
 * User: xiangkui
 * Date: 13-4-8
 * Time: 下午2:34
 */
public class ServiceA extends AbstractService implements InterfaceA{

    @Override
    public Object function(Object... objects) {
        String myVoice=new String("Hello,Im ServiceA,you can call me SA");
        String returnVoice=myVoice.toString();
        if(downService!=null){
            Object result=downService.function(objects,myVoice);
            returnVoice=returnVoice+"-><-"+result.toString();
        }
        returnVoice="("+returnVoice+")";
        return returnVoice;
    }

}
