package com.jd.bdp.hydra.dubbo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.jd.bdp.hydra.agent.support.Configuration;
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
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, ApplicationConfig> appConfigMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, ApplicationConfig.class, false, false);
        Map<String, ServiceConfig> servicesConfigMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, ServiceConfig.class, false, false);
        //获取全局唯一的App配置
        String appName="defaultApp";
        if (appConfigMap != null && 1==appConfigMap.size()) {
            for (ApplicationConfig config : appConfigMap.values()) {
                appName=config.getName();
            }
        }else {
            logger.error("Duplicate or no application configs to join up the hydra system,will user default application config");
        }
        this.config.setApplicationName(appName);
        //获取所有Service配置
        List serviceNames=new ArrayList();
        if (servicesConfigMap != null && servicesConfigMap.size()>0) {
            for (ServiceConfig config : servicesConfigMap.values()) {
                serviceNames.add(config.getInterface());
            }
        }else {
            logger.error("No Service configs to join up the hydra system,will user default config");
        }
        this.config.setServices(serviceNames);
    }
    //getter and setter
    public void setConfig(Configuration config) {
        this.config = config;
    }

    public Configuration getConfig() {
        return config;
    }
}
