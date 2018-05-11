package com.yunjia.util;

import java.math.BigDecimal;

public abstract class Calculator {
	
	public static double add(double num1, double num2){
		BigDecimal bigDecimal1 = new BigDecimal(num1);
		BigDecimal bigDecimal2 = new BigDecimal(num2);
		return bigDecimal1.add(bigDecimal2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	public static double subtract(double num1, double num2){
		BigDecimal bigDecimal1 = new BigDecimal(num1);
		BigDecimal bigDecimal2 = new BigDecimal(num2);
		return bigDecimal1.subtract(bigDecimal2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	public static double multiply(double num1, double num2){
		BigDecimal bigDecimal1 = new BigDecimal(num1);
		BigDecimal bigDecimal2 = new BigDecimal(num2);
		return bigDecimal1.multiply(bigDecimal2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	public static double divide(double num1, double num2){
		BigDecimal bigDecimal1 = new BigDecimal(num1);
		BigDecimal bigDecimal2 = new BigDecimal(num2);
		return bigDecimal1.divide(bigDecimal2, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
}
