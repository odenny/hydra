package com.jd.bdp.hydra.benchmark;

import com.jd.bdp.hydra.benchmark.support.AbstractBenchmarkClient;
import com.jd.bdp.hydra.benchmark.support.ClientRunnable;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * User: xiangkui
 * Date: 13-4-19
 * Time: 下午5:24
 */
public class BenchmarkClient extends AbstractBenchmarkClient {
    @SuppressWarnings("rawtypes")
    @Override
    public ClientRunnable getClientRunnable(String targetIP, int targetPort, int clientNums, int rpcTimeout,
                                            CyclicBarrier barrier, CountDownLatch latch, long startTime, long endTime) throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
        String runnable = properties.getProperty("classname");
        Class[] parameterTypes = new Class[] { String.class, int.class, int.class, int.class, CyclicBarrier.class,
                CountDownLatch.class, long.class, long.class };
        Object[] parameters = new Object[] { targetIP, targetPort, clientNums, rpcTimeout, barrier, latch, startTime,
                endTime };
        return (ClientRunnable) Class.forName(runnable).getConstructor(parameterTypes).newInstance(parameters);
    }

    public static void main(String[] args) throws Exception {
        new BenchmarkClient().run(args);
    }
}
