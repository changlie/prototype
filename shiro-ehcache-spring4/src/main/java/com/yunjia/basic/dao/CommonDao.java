package com.yunjia.basic.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.yunjia.basic.bean.FieldInfo;
import com.yunjia.common.dao.DaoTemplate;
import com.yunjia.util.StrUtil;

@Repository
public class CommonDao extends DaoTemplate {
	
	public Integer getTableWidth(String type) {
		String sql = "select width from sys_tables where name=?";
		return queryForColumn(sql, Integer.class, type);
	}

	public List<Map<String, Object>> getQueryFields(String target) {
		String sql = "select f.fname, f.ftype from sys_queryfields f, sys_tables t where f.tableId = t.id and t.name = ? and queryField=1 ORDER BY weight";
		return queryForMapList(sql, target);
	}
	
	public List<FieldInfo> getExtraConditions(String target){
		String sql = "select ex.fname, ex.cname, ex.ename, ex.editor, ex.dataId " + 
					 " from sys_extraConditions ex, sys_tables t " + 
					 " where ex.tableId=t.id and t.name=? order by ex.weight";
		return queryForEntityList(sql, FieldInfo.class, target);
	}
	public List<Map<String, Object>> getConditionsByQueryFields(String target){
		String sql = "select f.fname, f.cname, f.ename, f.ftype  "
				+ " from sys_queryfields f, sys_tables t where f.tableId = t.id and t.name = ? and titleField=1 order by f.weight";
		return queryForMapList(sql, target);
	}
	
	public List<Map<String, Object>> getLabels(String target){
		String sql = "select f.id, f.tableId, f.fname, f.cname, f.ename, f.ftype, f.width, f.queryField, f.titleField, f.sortable, f.fixed, f.weight "
				+ " from sys_queryfields f, sys_tables t where f.tableId = t.id and t.name = ? and titleField=1 order by f.weight";
		return queryForMapList(sql, target);
	}

	public List<Map<String, Object>> getQueryResult(String fields, String restSql, int curpage, int pageSize, List<Object> args) {
		return buildQuery().setSql(fields, restSql).pageList(curpage, pageSize, args.toArray());
	}
	
	public Long getQueryResultCount(String restSql) {
		return queryForCount("select count(*) "+restSql);
	}
	
	
	public void add(String type, JSONObject jObj) throws Exception{
		save(jObj, type);
	}
	
	public int update(String type, JSONObject jObj, String primaryKey) throws Exception{
		return update(jObj, type, primaryKey);
	}

	public void delete(String tableName, String key, String StrIds) {
		List<Integer> ids = StrUtil.getIntegerList(StrIds);
		String placeholders = StrUtil.getPlaceholders(ids.size());
		String sql = "delete from "+tableName+" where "+key+" in ("+placeholders+")";
		executeUpdate(sql, ids.toArray());
	}

	public String getQueryTarget(String type) {
		String sql = "select target from sys_tables where name =?";
		return queryForColumn(sql, String.class, type);
	}
	
	public String getOperateObj(String type) {
		String sql = "select opObj from sys_tables where name =?";
		return queryForColumn(sql, String.class, type);
	}

	public List<FieldInfo> getFormFields(String type) {
		String sql = "SELECT af.fname, af.cname, af.ename, af.editor, af.dataId "
				+ "FROM  sys_formFields af, sys_tables t where af.tableId=t.id and t.name=? order by weight";
		return queryForEntityList(sql, FieldInfo.class, type);
	}

	
	public List<Map<String, Object>> getRules(String type) {
		String sql = "select fr.field, fr.name, fr.validType, fr.dataType from sys_formRules fr, sys_tables t where fr.tableId = t.id and t.name=?";
		return queryForMapList(sql, type);
	}
	
	public List<Map<String, Object>> getOptionsInfo(String type){
		String sql = "SELECT op.field, op.dataId FROM sys_options op, sys_tables t where op.tableId = t.id and t.name=?";
		return queryForMapList(sql, type);
	}

	public List<Map<String, Object>> getOptionsBySql(String optionsSql) {
		return queryForMapList(optionsSql);
	}

	/***** TODO 类型集 ******/
	public List<Map<String, Object>> getOptions(String type) {
		String sql = "select name as label, num as value from sys_options where type=? order by weight";
		return queryForMapList(sql, type);
	}
	
	/******* TODO sqlset *******/
	public boolean existSql(String num, String type) {
		String sql = "select count(*) from sys_sqlSet where num=? and type=?";
		return queryForCount(sql, num, type)>0;
	}
	public String getSql(String num, String type) {
		String sql = "select content from sys_sqlSet where num=? and type=?";
		return queryForColumn(sql, String.class, num, type);
	}

	
}
