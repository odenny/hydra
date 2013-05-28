package com.jd.bdp.hydra.dubbomonitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

/**
 * Date: 13-3-19
 * Time: 上午9:43
 */
public class HydraDubbeConfig {
    private static final Logger log = LoggerFactory.getLogger(HydraDubbeConfig.class);

    public static Properties loadConfig(String conf) {
        try {
            InputStream in = HydraDubbeConfig.class.getClassLoader().getResourceAsStream(conf);
            Properties p = new Properties();
            p.load(in);
            return p;
        } catch (Exception e) {
            log.error("Can't load config file  fail : \n" + e.getStackTrace());
        }
        return null;
    }
}
