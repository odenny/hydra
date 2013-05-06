package com.jd.bdp.service.impl;


import com.jd.bdp.service.inter.InterfaceC;

/**
 * User: xiangkui
 * Date: 13-4-8
 * Time: 下午2:34
 */
public class ServiceC implements InterfaceC {

    static int mySwitch = 1;

    @Override
    public Object functionC(Object... objects) {
        mySwitch = -mySwitch;
        if (mySwitch > 0) {
            return "------------------>C";
        } else {
//            Integer.valueOf("abc");//制造业务异常
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            return "------------------>C(ex)";
        }
    }
}


