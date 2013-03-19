package com.jd.bdp.hydra.dubbomonitor.provider.iml;


import com.jd.bdp.hydra.Span;
import com.jd.bdp.hydra.dubbomonitor.HydraDubbeConfig;
import com.jd.bdp.hydra.dubbomonitor.HydraService;
import com.jd.bdp.hydra.dubbomonitor.LogEvent;
import com.jd.glowworm.PB;
import com.lmax.disruptor.*;
import com.taobao.metamorphosis.Message;
import com.taobao.metamorphosis.client.MessageSessionFactory;
import com.taobao.metamorphosis.client.MetaClientConfig;
import com.taobao.metamorphosis.client.MetaMessageSessionFactory;
import com.taobao.metamorphosis.client.producer.MessageProducer;
import com.taobao.metamorphosis.utils.ZkUtils;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HydraServiceImpl implements HydraService {
    private MessageProducer messageProducer = null;
    private String topic;
    private final int bufferSize = 1024;
    private Properties config = loadConfig();
    private RingBuffer<LogEvent> ringBuffer = new RingBuffer(LogEvent.EVENT_FACTORY, new SingleThreadedClaimStrategy(bufferSize), new YieldingWaitStrategy());
    private SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();
    private LogEventHandler logEventHandler = new LogEventHandler();
    private BatchEventProcessor batchEventProcessor = new BatchEventProcessor(ringBuffer, sequenceBarrier, logEventHandler);
    private ExecutorService executorService = Executors.newSingleThreadExecutor();


    private void createMessageProducer() throws Exception {
        final MetaClientConfig metaClientConfig = new MetaClientConfig();
        final ZkUtils.ZKConfig zkConfig = new ZkUtils.ZKConfig();
        //设置zookeeper地址
        zkConfig.zkConnect = config.getProperty("metaq.zk");
        zkConfig.zkRoot = config.getProperty("metaq.zk.root");
        metaClientConfig.setZkConfig(zkConfig);

        // New session factory,强烈建议使用单例
        MessageSessionFactory sessionFactory = new MetaMessageSessionFactory(metaClientConfig);
        // create producer,强烈建议使用单例
        messageProducer = sessionFactory.createProducer();
        this.topic = config.getProperty("metaq.topic");
        messageProducer.setDefaultTopic(topic);
        // publish topic
        messageProducer.publish(topic);
    }

    private Properties loadConfig() {
        return HydraDubbeConfig.loadConfig("metaq.prop");
    }

    public HydraServiceImpl() throws Exception {

        ringBuffer.setGatingSequences(batchEventProcessor.getSequence());
        executorService.submit(batchEventProcessor);
        createMessageProducer();
    }

    public String getSeed() {
        return null;
    }

    public boolean push(Span span) {
        System.out.println(span);
        long index = ringBuffer.next();
        LogEvent logEvent = ringBuffer.get(index);
        logEvent.setValue(span);
        ringBuffer.publish(index);
        return true;
    }

    public class LogEventHandler implements EventHandler<LogEvent> {
        @Override
        public void onEvent(LogEvent logEvent, long l, boolean b) throws Exception {
            System.out.println(logEvent.getValue());
            byte[] o = PB.toPBBytes(logEvent.getValue());
            messageProducer.sendMessage(new Message(topic, o));
        }
    }
}