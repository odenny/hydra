package com.jd.bdp.service.impl;


import com.jd.bdp.service.impl.support.AbstractService;
import com.jd.bdp.service.inter.InterfaceC;

/**
 * User: xiangkui
 * Date: 13-4-8
 * Time: 下午2:34
 */
public class ServiceC extends AbstractService implements InterfaceC {
    @Override
    public Object function(Object... objects) {
        return "wa~ it seems all body come here";
    }
}
