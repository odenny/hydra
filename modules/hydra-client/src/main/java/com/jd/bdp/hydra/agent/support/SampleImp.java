package com.jd.bdp.hydra.agent.support;

import com.jd.bdp.hydra.agent.Sampler;

import java.util.concurrent.atomic.AtomicLong;

/**
  * User: yfliuyu
 * Date: 13-3-28
 * Time: 上午11:33
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
           lastTime = System.currentTimeMillis();//fixme:safe thread?
       }
       return isSample;
    }
}
