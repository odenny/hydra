/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jd.bdp.hydra.dubbo;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.container.Container;
import com.alibaba.dubbo.container.spring.SpringContainer;
import com.alibaba.dubbo.rpc.*;
import com.jd.bdp.hydra.BinaryAnnotation;
import com.jd.bdp.hydra.Endpoint;
import com.jd.bdp.hydra.Span;
import com.jd.bdp.hydra.agent.Tracer;
import com.jd.bdp.hydra.agent.support.TracerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;

/**
 *
 */
@Activate(group = {Constants.PROVIDER, Constants.CONSUMER})
public class HydraFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(HydraFilter.class);

    private String serviceId = null;

    private Tracer tracer = null;

    // 调用过程拦截
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        if (serviceId == null) {
            Tracer.startTraceWork();
            this.serviceId = tracer.getServiceId(RpcContext.getContext().getUrl().getServiceInterface());
            //有可能为null
            if (serviceId == null) {
                // logger.debug("serviceId is null,will not trace until service registy successfully");
                return invoker.invoke(invocation);
            }
        }
        long start = System.currentTimeMillis();
        RpcContext context = RpcContext.getContext();
        boolean isConsumerSide = context.isConsumerSide();
        Span span = null;
        Endpoint endpoint = null;
        try {
            endpoint = tracer.newEndPoint();
//            endpoint.setServiceName(serviceId);
            endpoint.setIp(context.getLocalAddressString());
            endpoint.setPort(context.getLocalPort());
            if (context.isConsumerSide()) {
                Span span1 = tracer.getParentSpan();
                if (span1 == null) { //为rootSpan
                    span = tracer.newSpan(context.getMethodName(), endpoint, this.serviceId);
                } else {
                    span = tracer.genSpan(span1.getTraceId(), span1.getId(), tracer.genSpanId(), context.getMethodName(), span1.isSample(), this.serviceId);
                }
            } else if (context.isProviderSide()) {
                Long traceId, parentId, spanId;
                traceId = TracerUtils.getAttachmentLong(invocation.getAttachment(TracerUtils.TID));
                parentId = TracerUtils.getAttachmentLong(invocation.getAttachment(TracerUtils.PID));
                spanId = TracerUtils.getAttachmentLong(invocation.getAttachment(TracerUtils.SID));
                boolean isSample = (traceId != null);
                span = tracer.genSpan(traceId, parentId, spanId, context.getMethodName(), isSample, this.serviceId);
            }
            invokerBefore(invocation, span, endpoint, start);
            RpcInvocation invocation1 = (RpcInvocation) invocation;
            setAttachment(span, invocation1);
            Result result = invoker.invoke(invocation);
            if (result.getException() != null){
                catchException(result.getException(), endpoint);
            }
            return result;
        }catch (RpcException e) {
            catchException(e, endpoint);
            throw e;
        }finally {
            if (span != null) {
                long end = System.currentTimeMillis();
                invokerAfter(invocation, endpoint, span, end, isConsumerSide);
            }
        }
    }

    private void catchException(Throwable e, Endpoint endpoint) {
        BinaryAnnotation exAnnotation = new BinaryAnnotation();
        exAnnotation.setKey(TracerUtils.EXCEPTION);
        exAnnotation.setValue(e.getMessage());
        exAnnotation.setType("string");
        exAnnotation.setHost(endpoint);
        tracer.addBinaryAnntation(exAnnotation);
    }

    private void setAttachment(Span span, RpcInvocation invocation) {
        if (span.isSample()) {
            invocation.setAttachment(TracerUtils.PID, span.getParentId() != null ? String.valueOf(span.getParentId()) : null);
            invocation.setAttachment(TracerUtils.SID, span.getId() != null ? String.valueOf(span.getId()) : null);
            invocation.setAttachment(TracerUtils.TID, span.getTraceId() != null ? String.valueOf(span.getTraceId()) : null);
        }
    }

    private void invokerAfter(Invocation invocation, Endpoint endpoint, Span span, long end, boolean isConsumerSide) {
        if (isConsumerSide && span.isSample()) {
            tracer.clientReceiveRecord(span, endpoint, end);
        } else {
            if (span.isSample()) {
                tracer.serverSendRecord(span, endpoint, end);
            }
            tracer.removeParentSpan();
        }
    }

    private void invokerBefore(Invocation invocation, Span span, Endpoint endpoint, long start) {
        RpcContext context = RpcContext.getContext();
        if (context.isConsumerSide() && span.isSample()) {
            tracer.clientSendRecord(span, endpoint, start);
        } else if (context.isProviderSide()) {
            if (span.isSample()) {
                tracer.serverReceiveRecord(span, endpoint, start);
            }
            tracer.setParentSpan(span);
        }
    }

    //setter
    public void setTracer(Tracer tracer) {
        this.tracer = tracer;
    }

    /*加载Filter的时候加载hydra配置上下文*/
    static {
        logger.info("Hydra filter is loading hydra-config file...");
        String resourceName = "classpath*:hydra-config.xml";
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{
                resourceName
        });
        logger.info("Hydra config context is starting,config file path is:" + resourceName);
        context.start();
    }
}