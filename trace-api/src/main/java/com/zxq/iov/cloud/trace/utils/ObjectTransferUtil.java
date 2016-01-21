package com.zxq.iov.cloud.trace.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class ObjectTransferUtil {

	public static <T> T convertMap(Class<T> type, Map<String, Object> map) throws Exception {
		BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
		T obj = type.newInstance(); // 创建 JavaBean 对象
		// 给 JavaBean 对象的属性赋值
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();
			if (map.containsKey(propertyName)) {
				// 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
				Object value = map.get(propertyName);
				Object[] args = new Object[1];
				args[0] = value;
				String classType = descriptor.getPropertyType().toString();
				System.out.println("propertyName: " + propertyName + ", type: " + descriptor.getPropertyType());

				if ("class [B".equals(classType)) {
					String aa = (String) value;
					args[0] = aa == null ? null : aa.getBytes();
				} else if ("class java.lang.Long".equals(classType)) {
					Long v = null;
					if (value != null) {
						v = Long.valueOf(value.toString());
					}
					args[0] = v;
				}
				descriptor.getWriteMethod().invoke(obj, args);
			}
		}
		return obj;
	}

}
