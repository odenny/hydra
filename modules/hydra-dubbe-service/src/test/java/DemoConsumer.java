import com.jd.bdp.hydra.dubbeMonitor.HydraService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Random;

public class DemoConsumer {

    public static void main(String[] args) throws Exception {

        try {

            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"dubbo-hydra-consumer.xml"});
            context.start();
            HydraService service = (HydraService) context.getBean("hydraService"); // 获取远程服务代理

            int i = 1000000;
            while (i-- > 0) {
                String str = service.getSeed();
                System.out.println("message:" + str);
                Random random = new Random();
                long time = 0 + random.nextInt(2000);
                Thread.sleep(time);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}