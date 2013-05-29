package com.jd.bdp.hydra.jmetertest.support;

/**
 * User: xiangkui
 * Date: 13-5-28
 * Time: 下午5:01
 */
public abstract class AbstractJmeterDBContext {
    /**
     * 获取spring上下文，集中处理
     *
     * @return 返回要加载的spring上下文文件位置
     */
    public abstract String[] getConfigLocations();
}
