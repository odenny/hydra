package com.jd.bdp.hydra.agent.support;

import com.jd.bdp.hydra.agent.support.util.BinaryReadWrite;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * User: xiangkui
 * Date: 13-3-29
 * Time: 下午6:20
 */
public class GenerateTraceIdTest {
    GenerateTraceId generater=new GenerateTraceId(0x400000L);
    /**
     * 功能性测试
     */
    @Test
    public void testGetTraceId_BasicFunction() throws Exception {
        System.out.println("基本功能测试");
        for(int i=0;i<3;i++){
            System.out.println(generater.getTraceId());
        }
        System.out.println("------------------------");
        Thread.sleep(10);
    }
    /**
     * 数据重复性测试
     */
    @Test
    public void testGetTraceId_CompleteFunction() throws Exception {
        System.out.println("数据重复性测试");
        long counts=100000; //100w次跟踪
        BinaryReadWrite readWriter=new BinaryReadWrite();
        Long old=0L;
        for(int i=0;i<counts;i++){
            Long buildData=generater.getTraceId();
            Assert.assertTrue(old < buildData);
            readWriter.writeLong(buildData);
            old=buildData;
        }
        readWriter.flushWrite();
        System.out.println("-->没有重复数据");
        System.out.println("ID存储到"+readWriter.getS_FilePath()+"下");
        System.out.println("------------------------");
        Thread.sleep(10);
    }
    /**
     * 单线程压力测试
     */
    @Test
    public void testGetTraceId_SingleTreand() throws Exception {
        long counts=100000;//每轮方法调用次数
        int  rounds=3;//跑n轮，求平均值
        long[] results=new long[rounds];
        for(int i=0;i<rounds;i++){
            long startTime=System.currentTimeMillis();
            for(long j=0;j<counts;j++){
                generater.getTraceId();
            }
            long endTime=System.currentTimeMillis();
            results[i]=(endTime-startTime);
        }
        long sum=0;
        for(int i=0;i<rounds;i++){
            sum+=results[i];
        }
        System.out.println("单线程测试");
        System.out.println("轮数="+rounds);
        System.out.println("每轮提交量="+counts);
        System.out.println("平均耗时="+sum/rounds+" ms");
        System.out.printf("Tps=%.1f 次/ms\n", 1.0 * counts / (sum / rounds));
        System.out.println("------------------------");
        Thread.sleep(10);
    }
    /*
    并发压力测试
    * */
    @Test
    public void testGetTraceId_MutilTreand() throws Exception {
        ThreadPoolExecutor threadPool;
        //并发数
        int concurrent = 10;
        //每并发请求数
        long commint=100*1000;
        int rounds=3;//测试轮数
        threadPool = new ThreadPoolExecutor(concurrent, concurrent,1, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(30),
                new ThreadPoolExecutor.DiscardOldestPolicy());
        long sumResult=0;
        int  completeTask=0;
        for(int k:new int[rounds]){
            long startTime=System.currentTimeMillis();
            //创建traceId申请请求队列
            for (int i = 0; i < concurrent; i++) {
                try {
                    RequestTasker task = new RequestTasker(i,commint);
                    threadPool.execute(task);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            while(threadPool.getCompletedTaskCount()-completeTask<concurrent){
                Thread.sleep(5);
            }
            long endTime=System.currentTimeMillis();
            sumResult+=(endTime-startTime);
            completeTask+=concurrent;
        }

        System.out.println("多线程并发测试");
        System.out.println("轮数="+rounds);
        System.out.println("每轮提交量="+concurrent*commint);
        System.out.println("平均耗时="+sumResult/rounds+" ms");
        System.out.printf("Tps=%.1f 次/ms\n",1.0*concurrent*commint/(sumResult/rounds));
        threadPool.shutdown();
        System.out.println("------------------------");
        Thread.sleep(10);

    }

    class RequestTasker implements Runnable {
        int index;
        long commitCounts;
        public RequestTasker(int index,long commitCounts) {
            this.index=index;
            this.commitCounts=commitCounts;
        }
        public void run() {
            for (int i = 0; i < commitCounts; i++) {
                generater.getTraceId();
            }
        }
    }
}
