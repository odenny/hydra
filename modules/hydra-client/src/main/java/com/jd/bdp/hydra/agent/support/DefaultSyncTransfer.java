package com.jd.bdp.hydra.agent.support;

import com.jd.bdp.hydra.Span;
import com.jd.bdp.hydra.agent.SyncTransfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Date: 13-3-19
 * Time: 下午6:26
 * 异步发送实现类
 */
public class DefaultSyncTransfer implements SyncTransfer {

    private static Logger logger = LoggerFactory.getLogger(DefaultSyncTransfer.class);

    private ArrayBlockingQueue<Span> queue;

    private ScheduledExecutorService executors = null;
    private List<Span> spansCache;


    //serviceName isReady
    private volatile boolean isReady = false; //是否获得种子等全局注册信息

    private ConcurrentHashMap<String, Boolean> isServiceReady = new ConcurrentHashMap<String, Boolean>();

    private GenerateTraceId generateTraceId = new GenerateTraceId(0L);

    private TraceService traceService;

    private Long flushSize;

    private Long waitTime;

    private TransferTask task;


    @Override
    public void setTraceService(TraceService traceService) {
        this.traceService = traceService;
    }

    public DefaultSyncTransfer(Configuration c) {
        this.flushSize = c.getFlushSize() == null ? 1024L : c.getFlushSize();
        this.waitTime = c.getDelayTime() == null ? 60000L : c.getDelayTime();
        this.queue = new ArrayBlockingQueue<Span>(c.getQueueSize());
        this.spansCache = new ArrayList<Span>();
        this.executors = Executors.newSingleThreadScheduledExecutor();
        this.task = new TransferTask();
    }

    @Override
    public String appName() {
        //fixme
        return "test";
    }

    private class TransferTask extends Thread {
        TransferTask() {
            this.setName("TransferTask-Thread");
        }

        @Override
        public void run() {
            for (; ; ) {
                try {
                    if (!isReady()) {//重试直到注册成功
                        //全局信息网络注册，输入流：应用名 @ 输出流：包含种子的Map对象
                        boolean r = traceService.registerService(appName(), new ArrayList<String>());
                        if (r) {
                            generateTraceId = new GenerateTraceId(traceService.getSeed());
                            isReady = true;
                        } else {
                            synchronized (this) {
                                this.wait(waitTime);
                            }
                        }
                    } else {
                        while (!task.isInterrupted()) {
                            //检查是否有未注册服务，先注册
                            for (Map.Entry<String, Boolean> entry : isServiceReady.entrySet()) {
                                if (false == entry.getValue()) {//没有注册，先注册
                                    boolean r = traceService.registerService(appName(), entry.getKey());
                                    if (r) {
                                        entry.setValue(true);
                                    }
                                }
                            }
                            //-----------------------------
                            Span first = queue.take();
                            spansCache.add(first);
                            queue.drainTo(spansCache);
                            traceService.sendSpan(spansCache);
                            spansCache.clear();
                        }
                    }

                } catch (Throwable e) {
                    e.printStackTrace();
                    logger.info(e.getMessage());
                }
            }
        }

    }

    @Override
    public boolean isReady() {
        return isReady;
    }

    @Override
    public boolean isServiceReady(String serviceName) {
        if (serviceName != null && isServiceReady.containsKey(serviceName))
            return isServiceReady.get(serviceName);
        else
            return false;
    }

    @Override
    public void syncSend(Span span) {
        try {
            queue.add(span);
        } catch (Exception e) {
            logger.info(" span : ignore ..");
        }
    }

    @Override
    public void start() throws Exception {
        if (traceService != null && !task.isAlive()) {
            task.start();
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    cancel();
                }
            });
        } else if (traceService == null) {
            throw new Exception("TraceServie is null.can't starting SyncTransfer");
        }
    }

    public void cancel() {
        task.interrupt();
    }

    @Override
    public String getServiceId(String name) {
        String serviceId = null;
        serviceId = traceService.getServiceId(name);
        //可能是未注册的服务
        if (null == serviceId) {
            isServiceReady.putIfAbsent(name, false);//设置未注册标志，交给task去注册
        }
        return serviceId;
    }

    @Override
    public Long getTraceId() {
        return generateTraceId.getTraceId();
    }
    @Override
    public Long getSpanId() {
        return generateTraceId.getTraceId();
    }
}
