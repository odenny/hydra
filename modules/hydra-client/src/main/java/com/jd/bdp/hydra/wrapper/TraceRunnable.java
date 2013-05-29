package com.jd.bdp.hydra.wrapper;

import com.jd.bdp.hydra.Span;
import com.jd.bdp.hydra.agent.Tracer;

/**
 * Date: 13-5-29
 * Time: 上午9:50
 */
public class TraceRunnable implements Runnable{
    private final Span parent;
    private final Runnable runnable;
    private Tracer tracer = Tracer.getTracer();


    public TraceRunnable(Runnable r){
        this.parent = tracer.getParentSpan();
        this.runnable = r;
    }

    public TraceRunnable(Runnable r, Span p){
        this.runnable = r;
        this.parent = p;
    }
    @Override
    public void run() {
        if(parent != null){
            tracer.setParentSpan(parent);
        }
        runnable.run();
    }
}
