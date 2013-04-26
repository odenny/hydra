package com.jd.bdp.hydra.benchmark;

import com.jd.bdp.hydra.benchmark.support.AbstractBenchmarkClient;
import com.jd.bdp.hydra.benchmark.support.ClientRunnable;
import com.jd.bdp.hydra.benchmark.trigger.Trigger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * User: xiangkui
 * Date: 13-4-19
 * Time: 下午5:24
 */
public class BenchmarkClient extends AbstractBenchmarkClient {
    public BenchmarkClient() {
        try {
            InputStream in = ClassLoader.getSystemResourceAsStream("benchmark.properties");
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Properties properties = new Properties();

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static long runTime;


    public void run(String[] args) throws Exception {

    }


    @Override
    protected void startRunnables(List<ClientRunnable> runnables) {
        super.startRunnables(runnables);    //To change body of overridden methods use File | Settings | File Templates.
    }


    @Override
    public ClientRunnable getClientRunnable(String targetIP, int targetPort, int clientNums, int rpcTimeout, CyclicBarrier barrier, CountDownLatch latch, long startTime, long endTime) throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public static void main(String[] args) throws Exception {
        new BenchmarkClient().run(args);

    }
}
