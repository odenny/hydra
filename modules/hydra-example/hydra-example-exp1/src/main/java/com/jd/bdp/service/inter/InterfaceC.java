package com.jd.bdp.service.inter;

/**
 * User: xiangkui
 * Date: 13-4-9
 * Time: 下午1:24
 */

import com.jd.bdp.service.inter.support.Service;

/**
 * 某个Dubbo服务
 */
public interface InterfaceC extends Service {

    Object functionC(Object... objects);
}
