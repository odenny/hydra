package com.jd.bdp.service.impl.support;

import com.jd.bdp.service.inter.support.Service;

/**
 * User: xiangkui
 * Date: 13-4-8
 * Time: 下午2:36
 */
public abstract class AbstractService implements Service {
    private String name;
    private String appName;
    private Service downService;//下游服务


    /**
     * 开启服务
     */
    public void startUp(Object ... args){

    }

    /**
     * 关闭服务
     */
    public void shutDown(){

    }

    /**
     * 方法调用
     * @param objects
     * @return
     */
    @Override
    public Object function(Object... objects) {
        return null;
    }

    //getter and setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
    public Service getDownService() {
        return downService;
    }

    public void setDownService(Service downService) {
        this.downService = downService;
    }
}
