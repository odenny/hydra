package com.jd.bdp.hydra.agent.dubbo.tracker;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.monitor.MonitorService;
import com.alibaba.dubbo.monitor.dubbo.DubboMonitor;
import com.alibaba.dubbo.rpc.*;
import com.jd.bdp.hydra.agent.dubbo.DubboTracer;
import com.jd.bdp.hydra.agent.dubbo.DubboTracerFactory;
import com.jd.bdp.hydra.agent.dubbo.DubboTracerService;
import junit.framework.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: xiangkui
 * Date: 13-3-19
 * Time: 上午10:12
 * To change this template use File | Settings | File Templates.
 */
public class DubboTracerImlTest {
    private volatile URL lastStatistics;

    private final Invoker<DubboTracerService> tracerInvoker = new Invoker<DubboTracerService>() {
        public Class<DubboTracerService> getInterface() {
            return DubboTracerService.class;
        }
        public URL getUrl() {
            return URL.valueOf("hydra://127.0.0.1:7070?interval=20");
        }
        public boolean isAvailable() {
            return false;
        }
        public Result invoke(Invocation invocation) throws RpcException {
            return null;
        }
        public void destroy() {
        }
    };

    private final DubboTracerService dubboTracerService = new DubboTracerService() {

        public void collect(URL statistics) {
            DubboTracerImlTest.this.lastStatistics = statistics;
        }

        public List<URL> lookup(URL query) {
            return Arrays.asList(DubboTracerImlTest.this.lastStatistics);
        }

    };

    @Test
    public void testCount() throws Exception {
        DubboTracer tracer = new DubboTracerIml(tracerInvoker, dubboTracerService);
        URL statistics = new URL("dubbo", "10.20.153.10", 0)
                .addParameter(DubboTracerService.APPLICATION, "morgan")
                .addParameter(DubboTracerService.INTERFACE, "MemberService")
                .addParameter(DubboTracerService.METHOD, "findPerson")
                .addParameter(DubboTracerService.CONSUMER, "10.20.153.11")
                .addParameter(DubboTracerService.SUCCESS, 1)
                .addParameter(DubboTracerService.FAILURE, 0)
                .addParameter(DubboTracerService.ELAPSED, 3)
                .addParameter(DubboTracerService.MAX_ELAPSED, 3)
                .addParameter(DubboTracerService.CONCURRENT, 1)
                .addParameter(DubboTracerService.MAX_CONCURRENT, 1);
        tracer.collect(statistics);
        while (lastStatistics == null) {
            Thread.sleep(10);
        }
        Assert.assertEquals(lastStatistics.getParameter(DubboTracerService.APPLICATION), "morgan");
        Assert.assertEquals(lastStatistics.getProtocol(), "dubbo");
        Assert.assertEquals(lastStatistics.getHost(), "10.20.153.10");
        Assert.assertEquals(lastStatistics.getParameter(DubboTracerService.APPLICATION), "morgan");
        Assert.assertEquals(lastStatistics.getParameter(DubboTracerService.INTERFACE), "MemberService");
        Assert.assertEquals(lastStatistics.getParameter(DubboTracerService.METHOD), "findPerson");
        Assert.assertEquals(lastStatistics.getParameter(DubboTracerService.CONSUMER), "10.20.153.11");
        Assert.assertEquals(lastStatistics.getParameter(DubboTracerService.SUCCESS), "1");
        Assert.assertEquals(lastStatistics.getParameter(DubboTracerService.FAILURE), "0");
        Assert.assertEquals(lastStatistics.getParameter(DubboTracerService.ELAPSED), "3");
        Assert.assertEquals(lastStatistics.getParameter(DubboTracerService.MAX_ELAPSED), "3");
        Assert.assertEquals(lastStatistics.getParameter(DubboTracerService.CONCURRENT), "1");
        Assert.assertEquals(lastStatistics.getParameter(DubboTracerService.MAX_CONCURRENT), "1");
        tracer.destroy();
    }

    @Test
    public void testMonitorFactory() throws Exception {
        MockDubboTracerService tracerService = new MockDubboTracerService();
        URL statistics = new URL("dubbo", "10.20.153.10", 0)
                .addParameter(DubboTracerService.APPLICATION, "morgan")
                .addParameter(DubboTracerService.INTERFACE, "MemberService")
                .addParameter(DubboTracerService.METHOD, "findPerson")
                .addParameter(DubboTracerService.CONSUMER, "10.20.153.11")
                .addParameter(DubboTracerService.SUCCESS, 1)
                .addParameter(DubboTracerService.FAILURE, 0)
                .addParameter(DubboTracerService.ELAPSED, 3)
                .addParameter(DubboTracerService.MAX_ELAPSED, 3)
                .addParameter(DubboTracerService.CONCURRENT, 1)
                .addParameter(DubboTracerService.MAX_CONCURRENT, 1);

        Protocol protocol = ExtensionLoader.getExtensionLoader(Protocol.class).getAdaptiveExtension();
        ProxyFactory proxyFactory = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getAdaptiveExtension();
        DubboTracerFactory tracerFactory = ExtensionLoader.getExtensionLoader(DubboTracerFactory.class).
                getAdaptiveExtension();

        Exporter<DubboTracerService> exporter = protocol.export(proxyFactory.getInvoker(
                tracerService, DubboTracerService.class,
                URL.valueOf("dubbo://127.0.0.1:17979/" + DubboTracerService.class.getName())));

        try {
            DubboTracer tracer = tracerFactory.getTracer(URL.valueOf("hydra://127.0.0.1:17979?interval=10"));
            try {
                tracer.collect(statistics);
                int i = 0;
                while(tracerService.getStatistics() == null && i < 200) {
                    i ++;
                    Thread.sleep(10);
                }
                System.out.println("----:"+tracerService);
                URL result = tracerService.getStatistics();
                Assert.assertEquals(1, result.getParameter(DubboTracerService.SUCCESS, 0));
                Assert.assertEquals(3, result.getParameter(DubboTracerService.ELAPSED, 0));
            } finally {
                tracer.destroy();
            }
        } finally {
            exporter.unexport();
        }
    }
}
