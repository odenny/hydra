package com.jd.bdp.hydra.agent.support;


import com.jd.bdp.hydra.Span;
import com.jd.bdp.hydra.agent.CollectorService;
import com.jd.bdp.hydra.agent.RegisterService;
import com.jd.bdp.hydra.dubbomonitor.HydraService;
import com.jd.bdp.hydra.dubbomonitor.LeaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        //fixme try-catch性能影响？
        try {
            hydraService.push(spanList);
        }   catch (Exception e){
            logger.warn("跟踪数据推送失败~");
        }
    }

    @Override
    public boolean registerService(String name, List<String> services) {
        logger.info(name+" "+services);
        try {
            this.registerInfo = leaderService.registerClient(name, services);
        }   catch (Exception e){
             logger.warn("client cannot regist into the hydra system,will not to trace ..");
        }
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
