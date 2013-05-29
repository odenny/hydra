package com.jd.bdp.hydra.jmetertest.support;

/**
 * User: xiangkui
 * Date: 13-5-28
 * Time: 下午5:01
 */
public class MysqlDBContext extends AbstractJmeterDBContext{
    @Override
    public String[] getConfigLocations() {
        String[] location = {
                "classpath*:hydra-mysql-interface-test.xml"
        };
        return location;
    }
}
