import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DemoProvider {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"dubbo-hydra-provider.xml"});
        context.start();
        System.in.read(); // 按任意键退出
    }
}