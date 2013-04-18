package com.jd.bdp.hydra.dubbomonitor.provider.impl;

import com.jd.bdp.hydra.Span;
import com.jd.bdp.hydra.dubbomonitor.HydraDubbeConfig;
import com.jd.bdp.hydra.dubbomonitor.HydraService;
import com.jd.dd.glowworm.PB;
import com.taobao.metamorphosis.Message;
import com.taobao.metamorphosis.client.MessageSessionFactory;
import com.taobao.metamorphosis.client.MetaClientConfig;
import com.taobao.metamorphosis.client.MetaMessageSessionFactory;
import com.taobao.metamorphosis.client.producer.MessageProducer;
import com.taobao.metamorphosis.exception.MetaClientException;
import com.taobao.metamorphosis.utils.ZkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;

public class HydraServiceImpl implements HydraService {
    private static final Logger log = LoggerFactory.getLogger(HydraServiceImpl.class);
    private MessageProducer messageProducer = null;
    private String topic;
    private final int bufferSize = 1024;
    private Properties config = loadConfig();

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
        createMessageProducer();
    }

    @Override
    public boolean push(List<Span> span) {
        boolean rs = false;
        if(span != null){
            byte[] b = PB.toPBBytes(span);
            try {
                messageProducer.sendMessage(new Message(topic,b));
                rs = true;
            } catch (MetaClientException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } finally {

            }
        }
        return rs;
    }
}