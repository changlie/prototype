package com.yunjia.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ReflectionUtil {

	/**
	 * 利用指定的实体类成员变量，过滤出指定的键值对。
	 * <p>origin:{id:23,name:"tom", age:98, pids:"2,9,10", remark:"nothing"}</p>
	 * <pre>指定的class:
	 * class T {
	 * 	private int id;
	 * 	private String name;
	 * 	...
	 * }
	 * </pre>
	 * <p>result: {id:23, name:"tom"}</p>
	 * @param origin
	 * @param clazz
	 * @return
	 */
	public static Map<String, Object> filterMapByEntity(Map<String, Object> origin, Class<?> clazz) {
		Map<String, Object> ret = new HashMap<String, Object>();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			String fname = field.getName();
			Object value = origin.get(fname);
			
			if(ObjectUtil.isEmpty(value)) {
				continue;
			}
			
			ret.put(fname, value);
		}
		return ret;
	}
	
}
