package com.zxq.iov.cloud.trace;

import java.util.ArrayList;
import java.util.List;

import com.saicmotor.telematics.framework.core.common.SpringContext;

public class Span {
	
	public Span() {
	}
	
	public Span(String traceId, String spanId) {
		this.traceId = traceId;
		this.spanId = spanId;
		appId = SpringContext.getInstance().getProperty("WEB_ENV_NAME");
	}
	
	private String traceId;
	
	private String spanId;
	
	private String signature;
	
	private String appId;
	
	private List<Annotation> annotations = new ArrayList<Annotation>();
	
	private List<BinaryAnnotation> binaryAnnotations = new ArrayList<BinaryAnnotation>();
	
	public void addAnnotation(Annotation annotation) {
		annotations.add(annotation);
	}
	
	public void addBinaryAnnotation(BinaryAnnotation binaryAnnotation) {
		binaryAnnotations.add(binaryAnnotation);
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public String getSpanId() {
		return spanId;
	}

	public void setSpanId(String spanId) {
		this.spanId = spanId;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public List<Annotation> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(List<Annotation> annotations) {
		this.annotations = annotations;
	}

	public List<BinaryAnnotation> getBinaryAnnotations() {
		return binaryAnnotations;
	}

	public void setBinaryAnnotations(List<BinaryAnnotation> binaryAnnotations) {
		this.binaryAnnotations = binaryAnnotations;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

}
