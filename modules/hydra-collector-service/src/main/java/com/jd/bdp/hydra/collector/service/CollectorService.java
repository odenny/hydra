package com.jd.bdp.hydra.collector.service;

import com.jd.bdp.hydra.Span;
import com.jd.bdp.hydra.hbase.service.HbaseService;
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
import java.util.concurrent.atomic.AtomicLong;

/**
 * User: yfliuyu
 * Date: 13-4-17
 * Time: 下午5:03
 */
public class CollectorService {
    private static final Logger log = LoggerFactory.getLogger(CollectorService.class);
    private MessageConsumer consumer;
    private HbaseService hbaseService;
    private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private AtomicLong c = new AtomicLong(0L);

    public void setConsumer(MessageConsumer consumer) {
        this.consumer = consumer;
    }

    public void setHbaseService(HbaseService hbaseService) {
        this.hbaseService = hbaseService;
    }

    class HbaseConsumer implements MessageListener{
        @Override
        public void recieveMessages(Message message) {
            persistent(message);
        }

        @Override
        public Executor getExecutor() {
            return executorService;
        }
    }


    public void subscribe(String topic)throws Exception{
        consumer.subscribe(topic,1024*1024,new HbaseConsumer()).completeSubscribe();
    }

    public void persistent(Message message){
        List<Span> spanList = PB.parsePBBytes(message.getData(),ArrayList.class);
        try{
          for(Span s : spanList){
              hbaseService.addSpan(s);
              hbaseService.annotationIndex(s);
              hbaseService.durationIndex(s);
          }
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
