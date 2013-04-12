package com.jd.bdp.hydra.dubbomonitor.provider.impl;

import com.jd.bdp.hydra.dubbomonitor.LeaderService;
import com.jd.bdp.hydra.dubbomonitor.persistent.service.AppService;
import com.jd.bdp.hydra.dubbomonitor.persistent.service.SeedService;
import com.jd.bdp.hydra.dubbomonitor.persistent.service.ServiceService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: biandi
 * Date: 13-4-7
 * Time: 下午3:02
 */
public class LeaderServiceImpl implements LeaderService {

    @Override
    public Map<String, String> registerClient(String name, List<String> services) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("seed", seedService.getSeed().toString());
        map.put(name, appService.getAppId(name).toString());
        for (String serviceName : services) {
            map.put("serviceName", serviceService.getServiceId(serviceName, name).toString());
        }
        return map;
    }

    private ServiceService serviceService;
    private SeedService seedService;
    private AppService appService;

    public void setServiceService(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    public void setSeedService(SeedService seedService) {
        this.seedService = seedService;
    }

    public void setAppService(AppService appService) {
        this.appService = appService;
    }
}
