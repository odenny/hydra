package com.jd.bdp.hydra.agent.support;

import com.jd.bdp.hydra.Span;
import com.jd.bdp.hydra.agent.SyncTransfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * User: yfliuyu
 * Date: 13-3-19
 * Time: 下午6:26
 */
public class DefaultSyncTransfer implements SyncTransfer {

    private static Logger logger = LoggerFactory.getLogger(DefaultSyncTransfer.class);

    private ArrayBlockingQueue<Span> queue;

    private ScheduledExecutorService executors = null;
    private List<Span> spansCache;

    private volatile boolean isReady = false;

    private GenerateTraceId generateTraceId;

    private TraceService traceService;

    private Long flushSize;

    private Long waitTime;

    private TransferTask task;

    private Configuration configuration;

    public void setTraceService(TraceService traceService) {
        this.traceService = traceService;
    }

    public DefaultSyncTransfer(Configuration c) {
        this.configuration = c;
        this.flushSize = c.getFlushSize() == null ? 1024L : c.getFlushSize();
        this.waitTime = c.getDelayTime() == null ? 60000L : c.getDelayTime();
        this.queue = new ArrayBlockingQueue<Span>(c.getQueueSize());
        this.spansCache = new ArrayList<Span>();
        this.executors = Executors.newSingleThreadScheduledExecutor();
        this.task = new TransferTask();
    }

    @Override
    public String appName() {
        return configuration.getApplicationName();
    }

    private class TransferTask extends Thread {

        TransferTask() {
            this.setName("TransferTask-Thread");
        }

        @Override
        public void run(){
            for (; ; ) {
                try {
                    if (!isReady()) {
                        boolean r = traceService.registerService(configuration.getApplicationName(), configuration.getServices());
                        if (r) {
                            generateTraceId = new GenerateTraceId(traceService.getSeed());
                            isReady = true;
                        }else{
                            synchronized (this) {
                                this.wait(waitTime);
                            }
                        }
                    } else {
                        while (!task.isInterrupted()) {
                            Span first = queue.take();
                            spansCache.add(first);
                            queue.drainTo(spansCache);
                            traceService.sendSpan(spansCache);
                            spansCache.clear();
                        }
                    }
                }catch (Throwable e){
                    logger.info(e.getMessage());
                    //ig
                }
            }
        }
    }

    @Override
    public boolean isReady() {
        return isReady;
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
        if (traceService != null) {
            logger.info("traceService " + isReady);
            task.start();
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    cancel();
                }
            });
        } else {
            throw new Exception("TraceServie is null.can't starting SyncTransfer");
        }
    }

    public void cancel() {
        task.interrupt();
    }

    @Override
    public String getServiceId(String name) {
        if (isReady) {
            return traceService.getServiceId(name);
        }
        return null;
    }

    @Override
    public Long getTraceId() {
        if (isReady) {
            return generateTraceId.getTraceId();
        }
        return null;
    }

    @Override
    public Long getSpanId() {
        if (isReady) {
            return generateTraceId.getTraceId();//fixme:getSpanId
        }
        return null;
    }
}
