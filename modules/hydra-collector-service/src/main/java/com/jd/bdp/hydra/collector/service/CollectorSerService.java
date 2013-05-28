package com.jd.bdp.hydra.collector.service;

import com.jd.bdp.hydra.Span;
import com.jd.bdp.hydra.store.inter.InsertService;
import com.jd.dd.glowworm.PB;
import com.taobao.metamorphosis.Message;
import com.taobao.metamorphosis.client.consumer.MessageConsumer;
import com.taobao.metamorphosis.client.consumer.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Date: 13-4-17
 * Time: 下午5:03
 */
public class CollectorSerService {
    private static final Logger log = LoggerFactory.getLogger(CollectorSerService.class);
    private String topic;
    private MessageConsumer consumer;
    private InsertService insertService;
    private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public void setConsumer(MessageConsumer consumer) {
        this.consumer = consumer;
    }

    public void setInsertService(InsertService insertService) {
        this.insertService = insertService;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    class HbaseConsumer implements MessageListener {
        @Override
        public void recieveMessages(Message message) {
            persistent(message);
        }

        @Override
        public Executor getExecutor() {
            return executorService;
        }
    }


    public void subscribe() throws Exception {
        consumer.subscribe(topic, 1024 * 1024, new HbaseConsumer()).completeSubscribe();
    }

    public void persistent(Message message) {
        List<Span> spanList;
        try {
            spanList = (List) PB.parsePBBytes(message.getData());
        } catch (Exception e) {
            log.error(e.getMessage());
            return;
        }
        try {
            if (spanList != null) {
                for (Span s : spanList) {
                    insertService.addSpan(s);
                    insertService.addAnnotation(s);
                    insertService.addTrace(s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }
}
