import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: xiangkui
 * Date: 13-3-19
 * Time: 下午6:44
 * To change this template use File | Settings | File Templates.
 */
public class StartHydra {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"dubbo-hydra-provider.xml"});
        context.start();
        System.in.read(); // 按任意键退出
    }

}
