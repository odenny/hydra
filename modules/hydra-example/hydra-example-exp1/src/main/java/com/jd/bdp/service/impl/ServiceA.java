package com.jd.bdp.service.impl;

import com.jd.bdp.service.impl.support.AbstractService;
import com.jd.bdp.service.inter.InterfaceA;
import com.jd.bdp.service.inter.InterfaceB;
/**
 * User: xiangkui
 * Date: 13-4-8
 * Time: 下午2:34
 */
public class ServiceA extends AbstractService implements InterfaceA {
    /**
     * 绑定一个配置文件 ，用于启动服务
     * @return
     */
    protected String[] getConfigLocations() {
        String[] location = {
                "classpath*:dubbo-service-A.xml"
        };
        return location;
    }
    @Override
    public Object functionA(Object... objects) {
        String myVoice = "A";
        String result = "";
        if (downService != null && downService instanceof InterfaceB) {
            result = ((InterfaceB)downService).functionB(objects, myVoice).toString();
        }
        return myVoice + "--------->"+result;
    }


}
