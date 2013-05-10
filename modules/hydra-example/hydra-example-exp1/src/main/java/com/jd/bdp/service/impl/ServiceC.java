package com.jd.bdp.service.impl;


import com.jd.bdp.service.impl.support.AbstractService;
import com.jd.bdp.service.inter.InterfaceC;

/**
 * User: xiangkui
 * Date: 13-4-8
 * Time: 下午2:34
 */
public class ServiceC extends AbstractService implements InterfaceC {
    /**
     * 绑定一个配置文件 ，用于启动服务
     * @return
     */
    protected String[] getConfigLocations() {
        String[] location = {
                "classpath*:dubbo-service-C.xml"
        };
        return location;
    }
    @Override
    public Object functionC(Object... objects) {
        return "C";
    }
}


