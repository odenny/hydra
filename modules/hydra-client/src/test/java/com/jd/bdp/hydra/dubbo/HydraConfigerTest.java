package com.jd.bdp.hydra.dubbo;

import junit.framework.Assert;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * User: xiangkui
 * Date: 13-4-7
 * Time: 下午6:26
 */
public class HydraConfigerTest extends AbstractDependencyInjectionSpringContextTests {

    private HydraConfiger hydraConfiger;

    public void setHydraConfiger(HydraConfiger hydraConfiger) {
        this.hydraConfiger = hydraConfiger;
    }

    @Override
    protected String[] getConfigLocations() {
        String[] location = {
                "classpath:/hydra-config.xml",
                "classpath:/dubbo-demo-App.xml"
        };
        return location;
    }

    public void testSetConfig() throws Exception {
        Assert.assertEquals("application",hydraConfiger.getConfig().getApplicationName());
        for(String name:hydraConfiger.getConfig().getServices()){
            Assert.assertEquals("demo.DemoService",name);
        }
    }
}
