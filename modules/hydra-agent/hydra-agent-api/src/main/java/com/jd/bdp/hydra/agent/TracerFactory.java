package com.jd.bdp.hydra.agent;


public interface TracerFactory {
    /**
     *
     * @param args  外部传入的args参数，TracerFactory用来决定生成策略
     * @return 一处跟踪的切片点，为被通知对象。
     */
    Tracer getTracer(Object ... args);

}