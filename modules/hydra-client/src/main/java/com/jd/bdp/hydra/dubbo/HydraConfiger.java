package com.jd.bdp.hydra.dubbo;

import com.alibaba.dubbo.common.utils.ConfigUtils;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.alibaba.dubbo.registry.Registry;
import com.jd.bdp.hydra.agent.Tracer;
import com.jd.bdp.hydra.agent.support.Configuration;
import com.jd.bdp.hydra.dubbomonitor.HydraService;
import com.jd.bdp.hydra.dubbomonitor.LeaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: xiangkui
 * Date: 13-4-7
 * Time: 下午5:32
 */
public class HydraConfiger implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(HydraConfiger.class);
    Configuration config;
    HydraService hydraService;
    LeaderService leaderService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, ApplicationConfig> appConfigMap = BeanFactoryUtils.
                beansOfTypeIncludingAncestors(applicationContext, ApplicationConfig.class, false, false);
        Map<String, ServiceConfig> servicesConfigMap = BeanFactoryUtils.
                beansOfTypeIncludingAncestors(applicationContext, ServiceConfig.class, false, false);
        Map<String, ReferenceConfig> referenceConfigMap = BeanFactoryUtils.
                beansOfTypeIncludingAncestors(applicationContext, ReferenceConfig.class, false, false);
        //获取全局唯一的App配置
        String appName = "";
        if (appConfigMap != null && 1 == appConfigMap.size()) {
            for (ApplicationConfig config : appConfigMap.values()) {
                appName = config.getName();
            }
        } else {
            //尝试从环境变量中获取 appName
            if (null == (appName = ConfigUtils.getProperty("dubbo.application.name"))) {
                logger.warn("Duplicate or no application configs to join up the hydra system,will user default application config");
                appName = "";
            }
        }
        this.config.setApplicationName(appName);
        //获取所有Service配置
        List serviceNames = new ArrayList();
        if (servicesConfigMap != null && servicesConfigMap.size() > 0) {
            for (ServiceConfig config : servicesConfigMap.values()) {
                if (config.getInterface() != HydraService.class.getName()
                        || config.getInterface() != LeaderService.class.getName())
                    serviceNames.add(config.getInterface());
            }
        } else {
            logger.warn("No Service configs to join up the hydra system,will user default config");
        }
        //referenceConfig
        if (referenceConfigMap != null && referenceConfigMap.size() > 0) {
            for (ReferenceConfig config : referenceConfigMap.values()) {
                System.out.println(config.getInterface());
                System.out.println(HydraService.class.getName());
                if (!config.getInterface() .equals(HydraService.class.getName())
                        && !config.getInterface() .equals(LeaderService.class.getName()))
                    serviceNames.add(config.getInterface());
            }
        } else {
            logger.warn("No reference configs to join up the hydra system,will user default config");
        }
        this.config.setServices(serviceNames);
        Tracer.setConfiguration(config, leaderService, hydraService);
    }


    //getter and setter
    public void setConfig(Configuration config) {
        this.config = config;
    }

    public Configuration getConfig() {
        return config;
    }

    public HydraService getHydraService() {
        return hydraService;
    }

    public void setHydraService(HydraService hydraService) {
        this.hydraService = hydraService;
    }

    public LeaderService getLeaderService() {
        return leaderService;
    }

    public void setLeaderService(LeaderService leaderService) {
        this.leaderService = leaderService;
    }
}
