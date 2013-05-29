package com.jd.bdp.hydra.jmetertest.support;

/**
 * User: xiangkui
 * Date: 13-5-28
 * Time: 下午5:01
 */
public class HbaseDBContext extends AbstractJmeterDBContext {
    @Override
    public  String[] getConfigLocations() {
        String[] location = {
                "classpath*:hydra-hbase-interface-test.xml"
        };
        return location;
    }
}
