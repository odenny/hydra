package com.jd.bdp.hydra.dubbomonitor.persistent.service.impl;

import com.jd.bdp.hydra.dubbomonitor.persistent.dao.AppMapper;
import com.jd.bdp.hydra.dubbomonitor.persistent.entity.AppPara;
import com.jd.bdp.hydra.dubbomonitor.persistent.service.AppService;

/**
 * User: biandi
 * Date: 13-4-3
 * Time: 下午1:16
 */
public class AppServiceImpl implements AppService {

    @Override
    public synchronized Integer getAppId(String appName) {
        AppPara app = appMapper.getApp(appName);
        if (app == null){
            app = new AppPara();
            app.setName(appName);
            appMapper.addApp(app);
            return app.getId();
        }else {
            return app.getId();
        }
    }

    private AppMapper appMapper;

    public void setAppMapper(AppMapper appMapper) {
        this.appMapper = appMapper;
    }
}
