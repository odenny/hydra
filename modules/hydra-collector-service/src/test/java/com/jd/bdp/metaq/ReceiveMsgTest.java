package com.jd.bdp.metaq;


import com.taobao.metamorphosis.Message;
import com.taobao.metamorphosis.client.consumer.MessageListener;
import org.junit.Test;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * User: xiangkui
 * Date: 13-4-27
 * Time: 上午10:43
 */
public class ReceiveMsgTest {
    private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    class MyConsumer implements MessageListener {
        @Override
        public void recieveMessages(Message message) {
        }

        @Override
        public Executor getExecutor() {
            return executorService;
        }
    }
    @Test
    public void testReceiveMsg() throws Exception {
    }
}
