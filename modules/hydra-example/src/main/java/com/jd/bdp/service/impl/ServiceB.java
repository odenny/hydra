package com.jd.bdp.service.impl;


import com.jd.bdp.service.impl.support.AbstractService;
import com.jd.bdp.service.inter.InterfaceB;
import com.jd.bdp.service.inter.support.Service;

/**
 * User: xiangkui
 * Date: 13-4-8
 * Time: 下午2:34
 */
public class ServiceB extends AbstractService implements InterfaceB {
    private Service downService;//下游服务

    @Override
    public void startUp(Object... args) {
        //自身服务暴露
//        DemoService mockDemoService = new MockDemoService();
//        URL serverUrl=URL.valueOf("dubbo://127.0.0.1:17979/" + DemoService.class.getName()+"?filter=hydra");
//        Invoker<DemoService> sercerInvoker=proxyFactory.getInvoker(mockDemoService, DemoService.class,serverUrl);
//        Exporter<DemoService> exporter = protocol.export(sercerInvoker);
//        //服务引用
//        Invoker<DemoService> clientInvoker=protocol.refer(DemoService.class,serverUrl);
//        DemoService service = proxyFactory.getProxy(clientInvoker);
//        String result=service.sayHello("xiangkui");
//        System.out.println(result);
    }


    @Override
    public void shutDown() {
    }

    static int mySwitch = 1;
    /*服务调用*/
    @Override
    public Object function(Object... objects) {
        Object result = mySwitch > 0 ? downService.function(objects) : new String("haven't to call other services ,return directely");
        mySwitch = -mySwitch;
        return result;
    }

    //getter and setter
    public Service getDownService() {
        return downService;
    }

    public void setDownService(Service downService) {
        this.downService = downService;
    }
}
