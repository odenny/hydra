package com.jd.bdp.hydra.agent;

import com.jd.bdp.hydra.agent.support.SampleImp;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicLong;

/**
 * User: yfliuyu
 * Date: 13-3-29
 * Time: 下午4:59
 */
public class TestSample extends TestCase {
    private int threads = 40;
    private int singleN = 1000;
    private int sampleTotal = threads * singleN;
    private CyclicBarrier cyclicbarrier = new CyclicBarrier(threads);
    private CountDownLatch countDownLatch = new CountDownLatch(threads);
    private Sampler sampler = new SampleImp();

    private AtomicLong trueCount = new AtomicLong(0);
    private AtomicLong falseCount = new AtomicLong(0);

    class TestTask extends Thread {
        public void run() {
            try {
                cyclicbarrier.await();
                for (int i = 0; i < singleN; i++) {
                    if (sampler.isSample()) {
                        trueCount.incrementAndGet();
                    } else {
                        falseCount.incrementAndGet();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
            }
        }
    }

    public void testHashSampler(){
        long start = System.currentTimeMillis();
        long tc = 0L;
        long fc = 0L;
        for(int i = 0 ; i < singleN*10; i++){
            if(sampler.isSample()){
                tc++;
            }else{
                fc++;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("----------singlethreading-------------");
        System.out.println("total time : " + (System.currentTimeMillis() - start));
        System.out.println("case total :" + singleN*10);
        System.out.println("sampler count :" + tc);
        System.out.println("discard count :" + fc);
    }


    public void testConcurrentHashSampler() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < threads; i++) {
            new TestTask().start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("----------multithreading-------------");
        System.out.println("total time : " + (System.currentTimeMillis() - start));
        System.out.println("case total :" + sampleTotal);
        System.out.println("sampler count :" + trueCount.longValue());
        System.out.println("discard count :" + falseCount.longValue());
    }


    public void testSampleMod(){
        long n = 178;
        //while(true){
            n = n % 10;
            System.out.println(n);
        //}
    }
}
