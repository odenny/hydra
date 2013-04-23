package com.jd.bdp.hydra.agent;

import com.jd.bdp.hydra.dubbomonitor.LeaderService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: yfliuyu
 * Date: 13-4-7
 * Time: 下午2:27
 */
public class TestLeaderService implements LeaderService{
    @Override
    public Map<String, String> registerClient(String name, List<String> services) {
        Map<String,String> hash = new HashMap<String,String>();
        hash.put("seed","10");
        hash.put("testMethodA","0");
        hash.put("testMethodB","1");
        hash.put("testMethodC","2");
        return null;
    }

    @Override
    public String registerClient(String name, String service) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
