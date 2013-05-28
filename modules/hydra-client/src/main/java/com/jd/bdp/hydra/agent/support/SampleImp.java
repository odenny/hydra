package com.jd.bdp.hydra.agent.support;

import com.jd.bdp.hydra.agent.Sampler;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Date: 13-3-28
 * Time: 上午11:33
 * 采样率实现
 * 每秒采集100为上线，过了100按百分之10%采集
  */
public class SampleImp implements Sampler{
    private AtomicLong count = new AtomicLong();
    private int baseNumber = 100;
    private Long lastTime = -1L;

    public SampleImp(){
        lastTime = System.currentTimeMillis();
    }

    public boolean isSample(){
       boolean isSample = true;
       long n = count.incrementAndGet();
       if(System.currentTimeMillis() - lastTime  < 1000){
           if(n > baseNumber){
               n = n%10;
               if(n != 0){
                   isSample = false;
               }
           }
       }else{
           count.getAndSet(0);
           lastTime = System.currentTimeMillis();//
       }
       return isSample;
    }
}
