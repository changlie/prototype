package com.yunjia.basic.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.yunjia.basic.bean.PageBean;
import com.yunjia.basic.dao.CommonDao;
import com.yunjia.system.service.SystemService;
import com.yunjia.system.service.UserService;

/**
 * 出于优化的考虑，把频繁调用的方法集中在此类，方便使用缓存策略
 * @author changlie
 *
 */
public abstract class AbstractCommonService {
	
	@Autowired
	protected CommonDao commonDao;
	@Autowired
	protected UserService userService;
	@Autowired
	protected SystemService systemService;
	
	
	/***
	 * 用于增，删，改的表名
	 * @param type
	 * @return
	 */
	public String getUpdateTableName(String type) {
		return commonDao.getOperateObj(type);
	}
	
	/***
	 * 用于查询的视图或者表名
	 * @param type
	 * @return
	 */
	protected String getQueryTableName(String type) {
		return commonDao.getQueryTarget(type);
	}
	
	/**
	 * 拼接查询字段
	 * @param type
	 * @return
	 */
	protected String getQueryFields(String type) {
		List<Map<String, Object>> fields = commonDao.getQueryFields(type);
		StringBuilder ret = new StringBuilder();
		fields.forEach(m -> {
			if (ret.length() == 0) {
				ret.append(m.get("fname"));
			} else {
				ret.append(",").append(m.get("fname"));
			}
		});
		return ret.toString();
	}
	
	public abstract PageBean<Map<String, Object>> query(String type, String conditonsJson, String orderJson, int curPage, int pageSize) throws Exception;
	
	public abstract void add(String type, String obj) throws Exception;
	
	public abstract void update(String type, String obj, String key) throws Exception; 
	
	public abstract void delete(String type, String key, String ids) throws Exception; 
}
