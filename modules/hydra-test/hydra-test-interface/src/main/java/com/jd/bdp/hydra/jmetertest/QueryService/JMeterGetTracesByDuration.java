package com.jd.bdp.hydra.jmetertest.QueryService;

import com.alibaba.fastjson.JSONArray;
import com.jd.bdp.hydra.jmetertest.support.AbstractJmeterDBContext;
import com.jd.bdp.hydra.jmetertest.support.HbaseDBContext;
import com.jd.bdp.hydra.store.inter.QueryService;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

/**
 * User: xiangkui
 * Date: 13-5-14
 * Time: 下午6:43
 */
public class JMeterGetTracesByDuration extends AbstractJavaSamplerClient {
    AbstractJmeterDBContext dbContext;
    private static Logger logger = LoggerFactory.getLogger(JMeterGetTracesByDuration.class);
    private QueryService queryService;
    //arguments
    private String serviceId;
    private long startTime;
    private int sum;
    private int durationMin;
    private int durationMax;

    public void setupTest(JavaSamplerContext arg) {
        //服务初始化 完成queryService的注入
        dbContext=new HbaseDBContext();
        String[] configs = dbContext.getConfigLocations();
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(configs);
        context.start();
        queryService = context.getBean("queryService", QueryService.class);
        if (null == queryService) {
            logger.error("Didn't get the service [ " + queryService.getClass().getSimpleName() + " ]");
        }
        serviceId = arg.getParameter("serviceId");
        startTime = arg.getLongParameter("startTime");
        sum = arg.getIntParameter("sum");
        durationMin = arg.getIntParameter("durationMin");
        durationMax = arg.getIntParameter("durationMax");
    }

    // 设置传入的参数，可以设置多个，已设置的参数会显示到Jmeter的参数列表中
    public Arguments getDefaultParameters() {
        Date currentDate = new Date();
        Arguments arguments = new Arguments();
        arguments.addArgument("serviceId", "0");
        arguments.addArgument("startTime", String.valueOf(currentDate.getTime()));
        arguments.addArgument("sum", "100");
        arguments.addArgument("durationMin", "0");
        arguments.addArgument("durationMax", String.valueOf(Integer.MAX_VALUE));
        return arguments;
    }

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult sr = new SampleResult();
        sr.setSampleLabel("JmeterGetTracesByDuration"+serviceId);
        sr.setDataEncoding("UTF-8");
        sr.sampleStart();
        try {
            JSONArray jsonArray = queryService.getTracesByDuration(serviceId, startTime, sum, durationMin, durationMax);
            sr.setResponseData(jsonArray.toJSONString());
            sr.setResponseMessage(jsonArray.toJSONString());
            logger.info(jsonArray.toJSONString());
            sr.setSuccessful(true);
        } catch (Exception e) {
            sr.setSuccessful(false);
            sr.setResponseMessage(e.getMessage());
        } finally {
            sr.sampleEnd();
            sr.setDataEncoding("UTF-8");
            return sr;
        }

    }

    // 结束方法，实际运行时每个线程仅执行一次，在测试方法运行结束后执行，类似于LoadRunner中的end方法
    public void teardownTest(JavaSamplerContext arg0) {
    }


}
