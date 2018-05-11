package com.yunjia.util;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public abstract class ObjectUtil {
	private static char[] templateCharArr = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','0','1','2','3','4','5','6','7','8','9'};
	
	
	
	public static char getRandomChar() {
		return templateCharArr[(int)(Math.random()*36)];
	}
	
	private static String generateOrderNum(String orderPrefix) {
		return new StringBuilder(32)
				.append(orderPrefix)
				.append("D")
				.append(DateUtil.formatCondenseDate(new Date()))
				.append(ObjectUtil.getRandomChar())
				.append(ObjectUtil.getRandomChar())
				.append(ObjectUtil.getRandomChar())
				.append(ObjectUtil.getRandomChar())
				.append(ObjectUtil.getRandomChar())
				.append(ObjectUtil.getRandomChar())
				.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(generateOrderNum("KS"));
		System.out.println("len: "+templateCharArr.length);
	}
	
	
	public static long getLongValue(JSONObject obj, String key){
		Object value = obj.getOrDefault(key, 0);
		if (value instanceof Number) {
			return ((Number) value).longValue();
        }
		
		if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0 //
                || "null".equals(strVal) //
                || "NULL".equals(strVal)) {
            	return 0L;
            }
            
            if (strVal.indexOf(',') != 0) {
                strVal = strVal.replaceAll(",", "");
                return Long.parseLong(strVal);
            }
        }
		
		return 0L;
	}
	
	/**
	 * 这个判断功能仅仅是一个简陋的判断的
	 * @param str
	 * @return
	 */
	public static boolean isJson(String str){
		if(str==null || "".equals(str)) {
			return false;
		}
		
		if( (str.matches("\\{(.+[:].+)*\\}") || str.matches("\\[(.+[:].+)*|(.+[,].+)*\\]|\\[\\]"))){
			return true;
		}
		return false;
	}
	
	/**
	 * 这个判断功能仅仅是一个简陋的判断的
	 * @param str
	 * @return
	 */
	public static boolean isNotJson(String str){
		return !isJson(str);
	}
	

	public static boolean isEmpty(Object... objects){
		if(objects==null || objects.length==0) {
			return true;
		}
		
		for(Object obj : objects){
			if(obj==null) {
				return true;
			}
			
			if(obj instanceof String){
				String s = (String) obj;
				if("".equals(s) || "".equals(s.trim())){
					return true;
				}
			}
			
			if(obj instanceof Collection){
				Collection<?> c = (Collection<?>) obj;
				if(c.isEmpty()) {
					return true;
				}
			}
			
			if(obj instanceof Map){
				Map<?,?> m = (Map<?,?>) obj;
				if(m.isEmpty()) {
					return true;
				}
			}
			
		}
		
		return false;
	}
	
	
	public static boolean isNotEmpty(Object... objects){
		return !isEmpty(objects);
	}
	
}
