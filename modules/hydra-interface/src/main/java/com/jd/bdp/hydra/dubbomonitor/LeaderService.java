package com.jd.bdp.hydra.dubbomonitor;


import java.util.List;
import java.util.Map;

public interface LeaderService {
    /*全局信息注册*/
    Map<String,String> registerClient(String name,List<String> services);
    /*增量注册一个服务,获得serviceId*/
    String registerClient(String name,String service);

}