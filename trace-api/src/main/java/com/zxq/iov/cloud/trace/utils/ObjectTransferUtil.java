package com.zxq.iov.cloud.trace.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectTransferUtil {

	public static <T> T getObjFromJson(String jsonContent, String nodeName, Class<T> classT) {
		JsonNode node = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			node = mapper.readTree(jsonContent).findValue(nodeName);
			if (node == null) {
				return null;
			} else {
				return mapper.readValue(node.toString(), classT);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T getObjFromJson(String jsonContent, Class<T> classT) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(jsonContent, classT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
