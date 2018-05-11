package com.yunjia.basic.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

/**
 * 对公共的增删改查，实现扩展
 * 
 * @author changlie
 */
public abstract class CommonExtensionService extends AbstractCommonService {

	/**
	 * 执行新增操作前
	 * @param type
	 * @param obj
	 */
	protected void beforeAdd(String type, JSONObject obj) {
		switch (type) {

		}
	}

	/**
	 * 处理特殊的保存操作（新增，修改）
	 * 
	 * @param type
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	protected boolean handleSpecialSave(String type, String obj) throws Exception {
		switch (type) {
		case "user":
			userService.saveUser(obj);
			return true;
		case "employee":
			userService.saveEmployee(obj);
			return true;
		}
		return false;
	}

	/**
	 * 处理特殊删除操作，通常是级联删除
	 * 
	 * @param type
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	protected boolean handleSpecialDelete(String type, String ids) throws Exception {
		switch (type) {
		case "employee":
			userService.deleteEmployees(ids);
			return true;

		}
		return false;
	}
	
	
	/**
	 * 拼接   sql 默认的  排序 语句
	 * @param type
	 * @param ret
	 */
	protected void jointDefaultOrder(String type, StringBuilder ret) {
		switch (type) {
		case "orderPrefix":
			ret.append(" order by storeNum, num ");
			return;

		}
	}
	
	/**
	 * 添加 默认的  隐藏的  查询条件
	 * @param type
	 * @param restSql
	 * @param args 
	 */
	protected void addDefaultConditions(String type, StringBuilder restSql, List<Object> args) {
		switch (type) {
		case "user":
			restSql.append(" and id>0 ");
			return;

		}
	}
	
	/**
	 * 处理特殊查询条件
	 * 
	 */
	protected boolean handleSpecialCondition(StringBuilder sql, List<Object> args, JSONObject jObj, String fieldName,
			Object value) {
		switch (fieldName) {
		// e.g.
		// case "f":
		// return true;

		}
		return false;
	}

}
