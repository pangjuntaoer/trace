package com.zxq.iov.cloud.trace;

import java.util.concurrent.ArrayBlockingQueue;

public class Tracer {

	private ThreadLocal<TraceContext> traceContextThreadLocal = new ThreadLocal<TraceContext>();

	private static Tracer tracer = new Tracer();

	public static final String DEFAULT_PARENT_SPAN_ID = "0";

	private ArrayBlockingQueue<Span> queue = new ArrayBlockingQueue<Span>(10000);

	private Long requestCounts = 0l;

	private long pre;

	private Tracer() {
		pre = System.currentTimeMillis();
		new TransferTask().start();
	}

	public void sendSpan(Span span) {
		try {
			queue.add(span);
		} catch (Exception e) {
			// ignore
		}
	}

	public synchronized String genCurrentSpanId(String parentSpanId) {
		String currentSpanId = getTraceContext().getCurrentSpanId();
		if (currentSpanId == null) {
			currentSpanId = parentSpanId;
		} else if (currentSpanId == parentSpanId) {
			currentSpanId = parentSpanId + ".1";
		} else {
			int len = currentSpanId.lastIndexOf(".");
			int id = Integer.parseInt(currentSpanId.substring(len + 1));
			currentSpanId = currentSpanId.substring(0, len + 1) + (id + 1);
		}
		getTraceContext().setCurrentSpanId(currentSpanId);
		return currentSpanId;
	}

	public synchronized boolean isSample() {
		 long current = System.currentTimeMillis();
		 long period = current - pre;
		 pre = current;
		 requestCounts++;
		 if ((period >= 1000) || (period < 100 && requestCounts % 20 == 0)
		 || (period >= 100 && period < 1000 && requestCounts % 10 == 0)) {
		 return true;
		 }
		 return false;
	}

	private class TransferTask extends Thread {
		@Override
		public void run() {
			for (;;) {
				try {
					Span first = queue.take();
					KafkaProducer.getInstance().send(first);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static Tracer getTracer() {
		return tracer;
	}

	public TraceContext getTraceContext() {
		return traceContextThreadLocal.get();
	}

	public void setTraceContext(TraceContext traceContext) {
		traceContextThreadLocal.set(traceContext);
	}

	public void removeTraceContext() {
		traceContextThreadLocal.remove();
	}

}
