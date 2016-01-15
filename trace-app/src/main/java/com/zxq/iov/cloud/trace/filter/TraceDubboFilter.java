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
package com.zxq.iov.cloud.trace.filter;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcInvocation;
import com.zxq.iov.cloud.trace.Annotation;
import com.zxq.iov.cloud.trace.AnnotationType;
import com.zxq.iov.cloud.trace.BinaryAnnotation;
import com.zxq.iov.cloud.trace.Span;
import com.zxq.iov.cloud.trace.TraceContext;
import com.zxq.iov.cloud.trace.Tracer;

@Activate(group = { Constants.PROVIDER, Constants.CONSUMER })
public class TraceDubboFilter implements Filter {
	
	public TraceDubboFilter() {
		System.out.println("-------------------------------------------------\n\n\n\n\n\n\n\n\n\n");
		System.out.println("-------TraceDubboFilter------------");
		System.out.println("-------------------------------------------------\n\n\n\n\n\n\n\n\n\n");
	}

	private Tracer tracer = Tracer.getTracer();

	// 调用过程拦截
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

		System.out.println("-------------------dubbo filter----------------------------------");
		RpcContext rc = RpcContext.getContext();

		boolean isSample = Boolean.getBoolean(rc.getAttachment("isSample"));
		String traceId = rc.getAttachment("traceId");
		String parentSpanId = rc.getAttachment("parentSpanId");

		TraceContext tc = tracer.getTraceContext();
		if (tc == null) {
			tc = new TraceContext();
			tc.setTraceId(traceId);
			tc.setIsSample(isSample);
			tc.setParentSpanId(parentSpanId);
			tc.setCurrentSpanId(parentSpanId);
			tracer.setTraceContext(tc);
		}

		Span span = new Span(traceId, parentSpanId);
		span.addAnnotation(new Annotation(AnnotationType.SR.name(), System.currentTimeMillis(), getLocalIp(), null));

		try {
			RpcInvocation invocation1 = (RpcInvocation) invocation;
			invocation1.setAttachment("traceId", traceId);
			invocation1.setAttachment("isSample", String.valueOf(isSample));
			invocation1.setAttachment("parentSpanId", tc.getCurrentSpanId());
			Result result = invoker.invoke(invocation);
			if (result.getException() != null) {
				span.addBinaryAnnotation(
						new BinaryAnnotation("exception", System.currentTimeMillis(), getLocalIp(), null));
			}
			return result;
		} finally {
			if (span != null) {
				span.addAnnotation(
						new Annotation(AnnotationType.SS.name(), System.currentTimeMillis(), getLocalIp(), null));
			}
		}
	}

	private String getLocalIp() {

		InetAddress ip = null;
		String ipStr = null;
		boolean b = false;
		try {
			Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = allNetInterfaces.nextElement();
				Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					ip = addresses.nextElement();
					if (ip != null && ip instanceof Inet4Address && !ip.isLoopbackAddress() && !ip.isMulticastAddress()
							&& !ip.isSiteLocalAddress()) {
						ipStr = ip.getHostAddress();
						b = true;
						break;
					}
				}
				if (b) {
					break;
				}
			}
		} catch (Exception e) {
			ipStr = "";
		}
		return ipStr;
	}

}