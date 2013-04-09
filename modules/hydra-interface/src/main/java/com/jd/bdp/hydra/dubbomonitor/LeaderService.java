package com.jd.bdp.hydra.dubbomonitor;


import java.util.List;
import java.util.Map;

public interface LeaderService {
    Map<String,String> registerClient(String name,List<String> services);
}