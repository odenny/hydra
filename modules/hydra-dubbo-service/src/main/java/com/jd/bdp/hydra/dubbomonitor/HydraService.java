package com.jd.bdp.hydra.dubbomonitor;


import com.jd.bdp.hydra.Span;

public interface HydraService {
    String getSeed();

    boolean push(Span span);
}