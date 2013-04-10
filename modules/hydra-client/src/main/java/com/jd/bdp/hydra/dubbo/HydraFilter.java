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
import com.alibaba.dubbo.rpc.*;
import com.jd.bdp.hydra.BinaryAnnotation;
import com.jd.bdp.hydra.Endpoint;
import com.jd.bdp.hydra.Span;
import com.jd.bdp.hydra.agent.Tracer;
import com.jd.bdp.hydra.agent.support.TracerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
@Activate(group = {Constants.PROVIDER, Constants.CONSUMER})
public class HydraFilter implements Filter {

    private static Logger logger= LoggerFactory.getLogger(HydraFilter.class);

    private Tracer tracer = Tracer.getTracer();

    // 调用过程拦截
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        long start = System.currentTimeMillis();
        RpcContext context = RpcContext.getContext();
        Span span = null;
        Endpoint endpoint = null;
        try {

            if (context.isConsumerSide()) {
                span = tracer.getParentSpan();
                if (span == null) {
                    span = tracer.newSpan(context.getMethodName());
                }
            } else if (context.isProviderSide()) {
                Long traceId, parentId, spanId;
                traceId = TracerUtils.getAttachmentLong(invocation.getAttachment(TracerUtils.TID));
                parentId = TracerUtils.getAttachmentLong(invocation.getAttachment(TracerUtils.PID));
                spanId = TracerUtils.getAttachmentLong(invocation.getAttachment(TracerUtils.SID));
                boolean isSample = TracerUtils.getAttachmentBoolean(invocation.getAttachment(TracerUtils.SAMPLE));
                span = tracer.genSpan(traceId, parentId, spanId, context.getMethodName(), isSample);
            }
            endpoint = tracer.newEndPoint();
            endpoint.setServiceName(tracer.getServiceId(RpcContext.getContext().getUrl().getServiceInterface()));
            endpoint.setIp(context.getLocalAddressString());
            endpoint.setPort(context.getLocalPort());
            invokerBefore(invocation, span, endpoint, start);
            RpcInvocation invocation1 = (RpcInvocation)invocation;
            setAttachment(span,invocation1);
            Result result = invoker.invoke(invocation);
            return result;
        } catch (RpcException e) {
            BinaryAnnotation exAnnotation = new BinaryAnnotation();
            exAnnotation.setKey(TracerUtils.EXCEPTION);
            exAnnotation.setValue(e.getMessage().getBytes());
            exAnnotation.setType("string");
            tracer.addBinaryAnntation(exAnnotation);
            throw e;
        } finally {
            if (span != null) {
                long end = System.currentTimeMillis();
                invokerAfter(invocation, endpoint, span, end);
            }
        }
    }


    private void setAttachment(Span span,RpcInvocation invocation){
        if(span.isSample()){
            invocation.setAttachment(TracerUtils.PID,span.getParentId() != null ? String.valueOf(span.getParentId()) : null);
            invocation.setAttachment(TracerUtils.SID,span.getId() != null ? String.valueOf(span.getId()) : null);
            invocation.setAttachment(TracerUtils.TID,span.getTraceId() != null ? String.valueOf(span.getTraceId()) : null);
        }
    }

    private void invokerAfter(Invocation invocation, Endpoint endpoint, Span span, long end) {
        RpcContext context = RpcContext.getContext();
        if (context.isConsumerSide() && span.isSample()) {
            tracer.clientReceiveRecord(span, endpoint, end);
        } else if (context.isProviderSide()) {
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
}