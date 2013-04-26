package com.jd.bdp.hydra.benchmark.support;

/**
 * nfs-rpc
 *   Apache License
 *
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */

import com.alibaba.dubbo.common.utils.ConfigUtils;
import com.jd.bdp.hydra.benchmark.trigger.Trigger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * Abstract benchmark client,test for difference scenes Usage: -Dwrite.statistics=false BenchmarkClient serverIP
 * serverPort concurrents timeout codectype requestSize runtime(seconds) clientNums
 *
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public abstract class AbstractBenchmarkClient {

    public AbstractBenchmarkClient() {
        try {
            InputStream in = ClassLoader.getSystemResourceAsStream("benchmark.properties");
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static long maxTPS = 0;

    private static long minTPS = 0;

    private static long allRequestSum;

    private static long allResponseTimeSum;

    private static long allErrorRequestSum;

    private static long allErrorResponseTimeSum;

    private static int runtime;
    // < 0
    private static long below0sum;
    // (0,1]
    private static long above0sum;
    private static long above1sum;
    private static long above5sum;
    private static long above10sum;
    private static long above50sum;
    private static long above100sum;
    // (500,1000]
    private static long above500sum;
    // > 1000
    private static long above1000sum;

    private static Properties properties = new Properties();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void run(String[] args) throws Exception {
        //----------------read messages from outer files-----------------------
        final String serverIP = properties.getProperty("serverip");
        final int serverPort = Integer.parseInt(properties.getProperty("serverport"));
        final int concurrents = Integer.parseInt(properties.getProperty("concurrents"));
        final int timeout = Integer.parseInt(properties.getProperty("timeout"));
        final int callCount = Integer.parseInt(properties.getProperty("callCount"));
        long waitTime = Long.parseLong(properties.getProperty("waitTime")) * 1000L;
        runtime = Integer.parseInt(properties.getProperty("runtime"));
        final long endtime = System.nanoTime() / 1000L + runtime * 1000 * 1000L;
        final int clientNums = Integer.parseInt(properties.getProperty("connectionnums"));
        // ------------------Print start info------------------------------
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.SECOND, (int) (runtime / 1000));
        StringBuilder startInfo = new StringBuilder(dateFormat.format(currentDate));
        startInfo.append(" ready to start client benchmark");
        startInfo.append("callCount is: ").append(callCount).append(" Ts ");
        startInfo.append(",waitTime is:").append(waitTime).append(" ms ");
        startInfo.append("the benchmark will end at:").append(dateFormat.format(calendar.getTime()));
        System.out.println(startInfo.toString());
        //----------------- construct the test context  ---------------------
        CyclicBarrier barrier = new CyclicBarrier(concurrents);
        CountDownLatch latch = new CountDownLatch(concurrents);
        List<ClientRunnable> runnables = new ArrayList<ClientRunnable>();
        // will warm up before runnable start to work
        long beginTime = System.nanoTime() / 1000L + 30 * 1000 * 1000L;
        for (int i = 0; i < concurrents; i++) {
            ClientRunnable runnable = getClientRunnable(serverIP, serverPort, clientNums, timeout, barrier, latch,
                    beginTime, endtime);
            runnables.add(runnable);
        }
        //------------------------------  start to work--------------------------------------------
        startRunnables(runnables);
        latch.await();//任务等待，阻塞至concurrents任务跑完
        // read results & add all
        // key: runtime second range value: Long[2] array Long[0]: execute count Long[1]: response time sum
        Map<String, Long[]> times = new HashMap<String, Long[]>();
        Map<String, Long[]> errorTimes = new HashMap<String, Long[]>();
        for (ClientRunnable runnable : runnables) {
            List<long[]> results = runnable.getResults();
            long[] responseSpreads = results.get(0);
            below0sum += responseSpreads[0];
            above0sum += responseSpreads[1];
            above1sum += responseSpreads[2];
            above5sum += responseSpreads[3];
            above10sum += responseSpreads[4];
            above50sum += responseSpreads[5];
            above100sum += responseSpreads[6];
            above500sum += responseSpreads[7];
            above1000sum += responseSpreads[8];
            long[] tps = results.get(1);
            long[] responseTimes = results.get(2);
            long[] errorTPS = results.get(3);
            long[] errorResponseTimes = results.get(4);
            //统计所有调用流数据
            for (int i = 0; i < tps.length; i++) {
                String key = String.valueOf(i);
                if (times.containsKey(key)) {
                    Long[] successInfos = times.get(key);
                    Long[] errorInfos = errorTimes.get(key);
                    successInfos[0] += tps[i];
                    successInfos[1] += responseTimes[i];
                    errorInfos[0] += errorTPS[i];
                    errorInfos[1] += errorResponseTimes[i];
                    times.put(key, successInfos);
                    errorTimes.put(key, errorInfos);
                } else {
                    Long[] successInfos = new Long[2];
                    successInfos[0] = tps[i];
                    successInfos[1] = responseTimes[i];
                    Long[] errorInfos = new Long[2];
                    errorInfos[0] = errorTPS[i];
                    errorInfos[1] = errorResponseTimes[i];
                    times.put(key, successInfos);
                    errorTimes.put(key, errorInfos);
                }
            }

        }
        //---------------修正tps计算-----------------------------
        long ignoreRequest = 0;
        long ignoreErrorRequest = 0;
        int maxTimeRange = runtime - 30;
        // ignore the last 10 second requests,so tps can count more accurate
        for (int i = 0; i < 10; i++) {
            Long[] values = times.remove(String.valueOf(maxTimeRange - i));
            if (values != null) {
                ignoreRequest += values[0];
            }
            Long[] errorValues = errorTimes.remove(String.valueOf(maxTimeRange - i));
            if (errorValues != null) {
                ignoreErrorRequest += errorValues[0];
            }
        }
        for (Map.Entry<String, Long[]> entry : times.entrySet()) {
            long successRequest = entry.getValue()[0];
            long errorRequest = 0;
            if (errorTimes.containsKey(entry.getKey())) {
                errorRequest = errorTimes.get(entry.getKey())[0];
            }
            allRequestSum += successRequest;
            allResponseTimeSum += entry.getValue()[1];
            allErrorRequestSum += errorRequest;
            if (errorTimes.containsKey(entry.getKey())) {
                allErrorResponseTimeSum += errorTimes.get(entry.getKey())[1];
            }
            long currentRequest = successRequest + errorRequest;
            if (currentRequest > maxTPS) {
                maxTPS = currentRequest;
            }
            if (minTPS == 0 || currentRequest < minTPS) {
                minTPS = currentRequest;
            }
        }
        //------------------------------- 输出结果----------------------------------------------
        boolean isWriteResult = Boolean.parseBoolean(System.getProperty("write.statistics", "false"));
        if (isWriteResult) {
            BufferedWriter writer = new BufferedWriter(new FileWriter("benchmark.all.results"));
            for (Map.Entry<String, Long[]> entry : times.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue()[0] + "," + entry.getValue()[1] + "\r\n");
            }
            writer.close();
        }
        //--------------------------------print benchmark messages-----------------------------
        System.out.println("----------Benchmark Statistics--------------");
        System.out.println(" Concurrents: " + concurrents);
        System.out.println(" Runtime: " + runtime + " seconds");
        System.out.println(" Benchmark Time: " + times.keySet().size());
        long benchmarkRequest = allRequestSum + allErrorRequestSum;
        long allRequest = benchmarkRequest + ignoreRequest + ignoreErrorRequest;
        System.out.println(" Requests: " + allRequest + " Success: " + (allRequestSum + ignoreRequest) * 100
                / allRequest + "% (" + (allRequestSum + ignoreRequest) + ") Error: "
                + (allErrorRequestSum + ignoreErrorRequest) * 100 / allRequest + "% ("
                + (allErrorRequestSum + ignoreErrorRequest) + ")");
        System.out.println(" Avg TPS: " + benchmarkRequest / times.keySet().size() + " Max TPS: " + maxTPS
                + " Min TPS: " + minTPS);
        System.out.println(" Avg RT: " + (allErrorResponseTimeSum + allResponseTimeSum) / benchmarkRequest / 1000f
                + "ms");
        System.out.println(" RT <= 0: " + (below0sum * 100 / allRequest) + "% " + below0sum + "/" + allRequest);
        System.out.println(" RT (0,1]: " + (above0sum * 100 / allRequest) + "% " + above0sum + "/" + allRequest);
        System.out.println(" RT (1,5]: " + (above1sum * 100 / allRequest) + "% " + above1sum + "/" + allRequest);
        System.out.println(" RT (5,10]: " + (above5sum * 100 / allRequest) + "% " + above5sum + "/" + allRequest);
        System.out.println(" RT (10,50]: " + (above10sum * 100 / allRequest) + "% " + above10sum + "/" + allRequest);
        System.out.println(" RT (50,100]: " + (above50sum * 100 / allRequest) + "% " + above50sum + "/" + allRequest);
        System.out.println(" RT (100,500]: " + (above100sum * 100 / allRequest) + "% " + above100sum + "/" + allRequest);
        System.out.println(" RT (500,1000]: " + (above500sum * 100 / allRequest) + "% " + above500sum + "/"
                + allRequest);
        System.out.println(" RT > 1000: " + (above1000sum * 100 / allRequest) + "% " + above1000sum + "/" + allRequest);
        System.exit(0);
        //----------------------------------------------------------------------------------------------------------------
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{
                "classpath*:trigger-context.xml",
        });
        context.start();
        Trigger trigger = context.getBean("trigger", Trigger.class);
        trigger.startWorkWithSleep(callCount, waitTime);
        Thread.sleep(999999999);
    }

    //template function
    public abstract ClientRunnable getClientRunnable(String targetIP, int targetPort, int clientNums, int rpcTimeout,
                                                     CyclicBarrier barrier, CountDownLatch latch, long startTime,
                                                     long endTime) throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException;

    protected void startRunnables(List<ClientRunnable> runnables) {
        for (int i = 0; i < runnables.size(); i++) {
            final ClientRunnable runnable = runnables.get(i);
            Thread thread = new Thread(runnable, "benchmarkClient-" + i);
            thread.start();
        }
    }

}
