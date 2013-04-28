package com.jd.bdp.hydra.metaq;

import com.jd.bdp.hydra.dubbomonitor.HydraDubbeConfig;
import com.taobao.metamorphosis.Message;
import com.taobao.metamorphosis.client.MessageSessionFactory;
import com.taobao.metamorphosis.client.MetaClientConfig;
import com.taobao.metamorphosis.client.MetaMessageSessionFactory;
import com.taobao.metamorphosis.client.producer.MessageProducer;
import com.taobao.metamorphosis.utils.ZkUtils;
import org.junit.Test;

import java.util.Properties;

/**
 * User: xiangkui
 * Date: 13-4-27
 * Time: 上午10:43
 */
public class SendMsgTest {
    private MessageProducer messageProducer = null;
    private String topic;

    private Properties config = loadConfig();

    private Properties loadConfig() {
        return HydraDubbeConfig.loadConfig("metaq.prop");
    }

    public MessageProducer getMessageProducer() {
        return messageProducer;
    }

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

    @Test
    public void testSendMsg() throws Exception {
        SendMsgTest sender = new SendMsgTest();
        sender.createMessageProducer();
        MessageProducer myproducer = sender.getMessageProducer();

        for (int i = 0; i < 100; i++)
            try {
                myproducer.sendMessage(new Message("hydra_test", new String("test").getBytes()));
                System.out.println("发送成功！");
            } catch (Exception e) {
                e.printStackTrace();
            }

    }
}
