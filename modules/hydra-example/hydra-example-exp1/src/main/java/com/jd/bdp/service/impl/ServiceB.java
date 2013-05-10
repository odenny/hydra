package com.jd.bdp.service.impl;


import com.jd.bdp.service.impl.support.AbstractService;
import com.jd.bdp.service.inter.InterfaceB;
import com.jd.bdp.service.inter.InterfaceC;

/**
 * User: xiangkui
 * Date: 13-4-8
 * Time: 下午2:34
 */
public class ServiceB extends AbstractService implements InterfaceB {
    /**
     * 绑定一个配置文件 ，用于启动服务
     *
     * @return
     */
    protected String[] getConfigLocations() {
        String[] location = {
                "classpath*:dubbo-service-B.xml"
        };
        return location;
    }

    static int mySwitch = 1;

    /*服务调用*/
    @Override
    public Object functionB(Object... objects) {
        String myVoice = "B";
        String result = "";
        if (downService != null && downService instanceof InterfaceC) {
            if (mySwitch > 0) {
                result = ((InterfaceC) downService).functionC(objects).toString();
            }
            mySwitch = -mySwitch;
        }
        return myVoice + (result==""?"":"------------>" + result);
    }
}
