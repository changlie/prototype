package com.yunjia.common.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.util.Assert;

public class Conditions {
	private StringBuilder conditionClause;
	private List<Object> args;
	
	public void setConditions(String conditionClause, Object... args) {
		if(this.conditionClause==null) {
			this.conditionClause = new StringBuilder(conditionClause);
			this.args = Arrays.asList(args);
		}else {
			this.conditionClause.append(" and ").append(conditionClause);
			this.args.addAll(Arrays.asList(args));
		}
	}
	
	public String getConditionClause() {
		return conditionClause==null ? null : conditionClause.toString();
	}
	public Object[] getArgs() {
		return args==null ? null : args.toArray();
	}
	
	public boolean isEmpty() {
		return conditionClause == null ;
	}
	
	/** 包含 **/
	public void likeCondition(String key, Object value) {
		addCondition(key, value, "like");
	}
	/** 等于 **/
	public void eqCondition(String key, Object value) {
		addCondition(key, value, "=");
	}
	/** 不等于 **/
	public void neCondition(String key, Object value) {
		addCondition(key, value, "!=");
	}
	/** 大于 **/
	public void gtCondition(String key, Object value) {
		addCondition(key, value, ">");
	}
	/** 小于 **/
	public void ltCondition(String key, Object value) {
		addCondition(key, value, "<");
	}
	/** 大于等于 **/
	public void geCondition(String key, Object value) {
		addCondition(key, value, ">=");
	}
	/** 小于等于 **/
	public void leCondition(String key, Object value) {
		addCondition(key, value, "<=");
	}
	
	private void addCondition(String key, Object value, String operationType) {
		Assert.hasLength(key, "条件key不能为空！");
		Assert.notNull(value, "条件值不能为空！");
		
		if(value instanceof String) {
			// 去掉空白符。
			value = value.toString().replaceAll("\\s", "");
			// value为空串则不加入查询。
			if("".equals(value)) {
				return;
			}
		}
		
		if(conditionClause==null) {
			conditionClause = new StringBuilder(30);
			args = new ArrayList<Object>();
		}else {
			conditionClause.append(" and ");
		}
		
		conditionClause.append(key);
		if("like".equals(operationType)) {
			conditionClause.append(" like '%").append(value).append("%' ");
		}else {
			conditionClause.append(operationType).append("? ");
			args.add(value);
		}
	}
	
}
