package com.jd.bdp.hydra.dubbomonitor.provider.impl;

import com.jd.bdp.hydra.Span;
import com.jd.bdp.hydra.dubbomonitor.HydraService;
import com.jd.bdp.hydra.mysql.service.InsertService;

import java.io.IOException;
import java.util.List;

public class HydraMysqlServiceImpl implements HydraService {
    private InsertService insertService;

    public InsertService getInsertService() {
        return insertService;
    }

    public void setInsertService(InsertService insertService) {
        this.insertService = insertService;
    }

    @Override
    public boolean push(List<Span> span) throws IOException {
        boolean rs = false;
        if(span != null){
            for(Span s : span){
                insertService.addSpan(s);
                insertService.addTrace(s);
                insertService.addAnnotation(s);
            }
        }
        return rs;
    }
}