package com.jd.bdp.hydra.agent;


import com.jd.bdp.hydra.Endpoint;
import com.jd.bdp.hydra.Span;
import com.jd.bdp.hydra.agent.support.Configuration;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;


public class TracerTest extends TestCase {

    private int threads = 20;
    private Tracer tracer = null;
    private CyclicBarrier cyclicbarrier = new CyclicBarrier(threads);
    private CountDownLatch countDownLatch = new CountDownLatch(threads);

    class TestTask extends Thread{
        public void run(){
            try {
                cyclicbarrier.await();
                generate(10000);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
            }

        }
    }

    @Test
    public void testTracer() throws Exception {
        tracer = Tracer.getTracer();
        Configuration configuration = new Configuration();
        configuration.setApplicationName("test1");
        configuration.setQueueSize(1024);
        tracer.setConfig(configuration,new TestLeaderService(),new TestHydraService());
        for(int k = 0 ; k < threads ; k++){
            Thread t = new TestTask();
            t.start();
        }
        countDownLatch.await();
    }

    private void generate(int length) {
        long s = System.currentTimeMillis();
        Endpoint clientEndPoint = new Endpoint();
        clientEndPoint.setIp("127.0.1.1");
        clientEndPoint.setPort(1234);
        clientEndPoint.setServiceName("app1");
        Endpoint serverEndPoint = new Endpoint();
        serverEndPoint.setIp("127.0.0.1");
        serverEndPoint.setPort(1235);
        serverEndPoint.setServiceName("app1");
        Long id = null;

        for (int i = 0; i < length; i++) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            String method = "method_" + i;
            Span span = null;
            span = tracer.newSpan(method);
            if (span.isSample()) {
                long start = System.currentTimeMillis();
                tracer.clientSendRecord(span, clientEndPoint, start);
                tracer.serverReceiveRecord(span, serverEndPoint, start + 5000);
                tracer.serverSendRecord(tracer.getParentSpan(), serverEndPoint, start + 100000);
                tracer.removeParentSpan();
                tracer.clientReceiveRecord(span, clientEndPoint, start + 15000);
            }
        }
    }
}
