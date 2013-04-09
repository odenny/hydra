package com.jd.bdp.trigger.impl;

import com.jd.bdp.service.inter.InterfaceA;
import com.jd.bdp.service.inter.support.Service;
/**
 * User: xiangkui
 * Date: 13-4-9
 * Time: 下午3:25
 */
public class Trigger {
    Service rootService;
    public void startWork(){
        for(int i=0;i<10;i++){
            Object result=rootService.function();
            System.out.println("result:"+result);
        }

    }
    //getter and setter
    public Service getRootService() {
        return rootService;
    }

    public void setRootService(Service rootService) {
        this.rootService = rootService;
    }
}
