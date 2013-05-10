package com.jd.bdp.hydra.dubbomonitor;


import com.jd.bdp.hydra.Span;

import java.io.IOException;
import java.util.List;

public interface HydraService {
    boolean push(List<Span> span) throws IOException;
}