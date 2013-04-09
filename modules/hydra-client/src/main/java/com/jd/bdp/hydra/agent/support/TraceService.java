package com.jd.bdp.hydra.agent.support;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.jd.bdp.hydra.Span;
import com.jd.bdp.hydra.agent.CollectorService;
import com.jd.bdp.hydra.agent.RegisterService;
import com.jd.bdp.hydra.dubbomonitor.HydraService;
import com.jd.bdp.hydra.dubbomonitor.LeaderService;

import java.util.List;
import java.util.Map;

/**
 * Date: 13-3-27
 * Time: 上午10:57
 */
public class TraceService implements RegisterService, CollectorService {

    private static final Logger logger = LoggerFactory.getLogger(TraceService.class);

    private LeaderService leaderService;
    private HydraService hydraService;
    private Map<String, String> registerInfo;
    public static final String APP_NAME = "applicationName";
    public static final String SEED = "seed";
    private boolean isRegister = false;

    public boolean isRegister() {
        return isRegister;
    }

    @Override
    public void sendSpan(List<Span> spanList) {
        hydraService.push(spanList);
    }

    @Override
    public boolean registerService(String name, List<String> services) {
        logger.info(name+" "+services);
        this.registerInfo = leaderService.registerClient(name, services);
        if (registerInfo != null) {
            isRegister = true;
        }
        return isRegister;
    }

    public LeaderService getLeaderService() {
        return leaderService;
    }

    public void setLeaderService(LeaderService leaderService) {
        this.leaderService = leaderService;
    }

    public HydraService getHydraService() {
        return hydraService;
    }

    public void setHydraService(HydraService hydraService) {
        this.hydraService = hydraService;
    }

    public String getServiceId(String service) {
        if (isRegister) {
            return registerInfo.get(service);
        }
        return null;
    }

    public Long getSeed() {
        String s = null;
        if (isRegister) {
            s = registerInfo.get(SEED);
            return Long.valueOf(s);
        }
        return null;
    }
}
