package com.jd.bdp.hydra.agent;

import java.util.List;
import java.util.Map;

/**
 * User: yfliuyu
 * Date: 13-4-3
 * Time: 下午4:00
 */
public interface RegisterService {
    public boolean registerService(String name,List<String> services);

    /*更新注册信息*/
    boolean registerService(String appName, String serviceName);
}
