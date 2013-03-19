import com.jd.bdp.hydra.Span;
import com.jd.glowworm.PB;
import com.taobao.metamorphosis.Message;
import com.taobao.metamorphosis.client.MessageSessionFactory;
import com.taobao.metamorphosis.client.MetaClientConfig;
import com.taobao.metamorphosis.client.MetaMessageSessionFactory;
import com.taobao.metamorphosis.client.consumer.ConsumerConfig;
import com.taobao.metamorphosis.client.consumer.MessageListener;
import com.taobao.metamorphosis.exception.MetaClientException;
import com.taobao.metamorphosis.utils.ZkUtils;
import org.apache.log4j.Logger;

import java.util.concurrent.Executor;

/**
 * User: yfliuyu
 * Date: 13-3-19
 * Time: 上午11:33
 */
public class MessageConsumer {
    private static Logger logger =
            Logger.getLogger(MessageConsumer.class.getName());
    private com.taobao.metamorphosis.client.consumer.MessageConsumer consumer = null;
    private String topic = "hydra_test";


    public MessageConsumer() throws MetaClientException {
        final MetaClientConfig metaClientConfig = new MetaClientConfig();
        final String group = "meta-example";
        ConsumerConfig consumerConfig = new ConsumerConfig(group);
        final ZkUtils.ZKConfig zkConfig = new ZkUtils.ZKConfig();
        //设置zookeeper地址
        zkConfig.zkConnect = "192.168.227.83:2181,192.168.227.86:2181,192.168.228.85:2181";
        zkConfig.zkRoot = "/meta";
        metaClientConfig.setZkConfig(zkConfig);
        // New session factory,强烈建议使用单例
        MessageSessionFactory sessionFactory = new MetaMessageSessionFactory(metaClientConfig);
        // create producer,强烈建议使用单例
        consumerConfig.setConsumeFromMaxOffset();
        consumer = sessionFactory.createConsumer(consumerConfig);

        consumer.subscribe(topic, 1024 * 1024, new MessageListener() {

            public void recieveMessages(Message message) {
                logger.info("Receive message " + PB.parsePBBytes(message.getData(), Span.class));
            }

            public Executor getExecutor() {
                return null;
            }
        });
        consumer.completeSubscribe();
    }

    public static void main(String[] strings) throws Exception {
        MessageConsumer metaQC = new MessageConsumer();
    }
}
