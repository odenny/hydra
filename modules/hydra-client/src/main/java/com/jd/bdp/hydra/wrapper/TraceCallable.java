package com.jd.bdp.hydra.wrapper;

import com.jd.bdp.hydra.Span;
import com.jd.bdp.hydra.agent.Tracer;

import java.util.concurrent.Callable;

/**
 * Date: 13-5-29
 * Time: 上午9:44
 */
public class TraceCallable<V> implements Callable<V> {

    private final Callable<V> impl;
    private final Span parent;
    private final Tracer tracer = Tracer.getTracer();

    public TraceCallable(Callable<V> impl) {
        this.parent = tracer.getParentSpan();
        this.impl = impl;
    }

    public TraceCallable(Span parent, Callable<V> impl) {
        this.impl = impl;
        this.parent = parent;
    }

    @Override
    public V call() throws Exception {
        if (parent != null) {
            tracer.setParentSpan(parent);
        }
        return impl.call();
    }

    public Callable<V> getImpl() {
        return impl;
    }
}
