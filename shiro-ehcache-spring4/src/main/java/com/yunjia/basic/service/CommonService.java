package com.yunjia.basic.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunjia.basic.bean.Condition;
import com.yunjia.basic.bean.FieldInfo;
import com.yunjia.basic.bean.FormField;
import com.yunjia.basic.bean.PageBean;
import com.yunjia.util.ObjectUtil;
import com.yunjia.util.StrUtil;

@Service
public class CommonService extends CommonExtensionService{
	public static final int chinese = 0;
	public static final int english = 1;
	
	
//	@Autowired
//	private HttpSession session;
	@Autowired
	private OptionsService optionsService;
	
	/**
	 * 获取表格   标题
	 * @param type
	 * @return
	 */
	public List<Map<String, Object>> getLabels(String type) {
		return commonDao.getLabels(type);
	}
	
	/**
	 * 获取 
	 * <br> 表格宽度
	 * <br>表单字段，
	 * <br>表单验证规则，
	 * <br>表单选项关键字列表，
	 * <br>查询时的可用条件...
	 * @param type
	 * @return 
	 */
	public JSONObject getMetaInfo(String type) {
		Integer tableWidth = commonDao.getTableWidth(type);
		//表单验证规则
		List<Map<String, Object>> formRules = commonDao.getRules(type);
		
		//表单验证字段
		List<FieldInfo> rawFormFields = commonDao.getFormFields(type);
		List<FormField> formFields = formatFormFields(rawFormFields);
		
		List<Map<String,Object>> conditionsFromQueryFields = commonDao.getConditionsByQueryFields(type);
		List<FieldInfo> extraConditions = commonDao.getExtraConditions(type);
		
		/** 下拉数据集的key */
		Set<String> optionDataIds = getOptionDataIds(rawFormFields, extraConditions);
		
		/** todo 获取查询可用的条件 */
		Map<String, FieldInfo> formFieldsMap = getFormFieldsMap(rawFormFields);
		List<Condition> conditions = getConditions(conditionsFromQueryFields, extraConditions, formFieldsMap);
		
		
		JSONObject jobj = new JSONObject();
		jobj.fluentPut("tableWidth", tableWidth)
			.fluentPut("formRules", formRules)
			.fluentPut("formFields", formFields)
			.fluentPut("dataIds", optionDataIds)
			.fluentPut("conditions", conditions);
		return jobj;
	}



	private List<FormField> formatFormFields(List<FieldInfo> rawFormFields) {
		List<FormField> formFields = new ArrayList<>();
		for (FieldInfo fieldInfo : rawFormFields) {
			String label = fieldInfo.getCname();
			String fname = fieldInfo.getFname();
			String editor = fieldInfo.getEditor();
			String dataId = fieldInfo.getDataId();
			FormField f = new FormField(fname, label, editor, dataId);
			formFields.add(f);
		}
		return formFields;
	}

	private Map<String, FieldInfo> getFormFieldsMap(List<FieldInfo> formFields) {
		Map<String, FieldInfo> formFieldsMap = new HashMap<>();
		for (FieldInfo f : formFields) {
			String fieldName = f.getFname();
			
			formFieldsMap.put(fieldName, f);
		}
		return formFieldsMap;
	}

	private Set<String> getOptionDataIds(List<FieldInfo> formFields, List<FieldInfo> extraConditions) {
		Set<String> optionDataIds = new HashSet<>();
		for (FieldInfo f : formFields) {
			getDataId(optionDataIds, f);
		}
		for (FieldInfo f : extraConditions) {
			getDataId(optionDataIds, f);
		}
		return optionDataIds;
	}

	private void getDataId(Set<String> optionDataIds, FieldInfo f) {
		String dataId = f.getDataId();
		if(ObjectUtil.isEmpty(dataId)) {
			return;
		}
		optionDataIds.add(dataId);
	}

	private List<Condition> getConditions(List<Map<String, Object>> conditionsFromQueryFields,
			List<FieldInfo> extraConditions, Map<String, FieldInfo> formFieldsMap) {
//		Integer  language = (Integer) session.getAttribute("language");
		List<Condition> conditions = new ArrayList<>();
		
		
		for (FieldInfo c1 : extraConditions) {
			String lebal = c1.getCname();
			
			Condition c = new Condition(c1.getFname(), lebal, c1.getEditor(), c1.getDataId());
			
			conditions.add(c);
		}
		
		for(Map<String, Object> c2: conditionsFromQueryFields) {
			String lebal = (String) c2.get("cname");
			String fieldName = (String) c2.get("fname");
			String dataId = "";
			String editor = "text";
			
			FieldInfo fieldInfo = formFieldsMap.get(fieldName);
			if(fieldInfo != null) {
				dataId = fieldInfo.getDataId();
				editor = fieldInfo.getEditor();
			}
			
			Condition c = new Condition(fieldName, lebal, editor, dataId);
			conditions.add(c);
		}
		return conditions;
	}


	/**
	 * 获取查询时下拉框的选项
	 * @param type
	 * @return
	 */
	public Map<String, Object> getQueryOptions(String type) {
		Map<String, Object> ret = new HashMap<>();
		List<Map<String, Object>> optionsInfo = commonDao.getOptionsInfo(type);
		optionsInfo.forEach(op -> {
			String field = (String) op.get("field");
			String dataId = (String) op.get("dataId");
			List<Map<String, Object>> options = optionsService.getOptions(dataId);
			ret.put(field, options);
		});
		return ret;
	}

	

	/**
	 * TODO 公共的新增方法
	 */
	@Override
	public void add(String type, String obj) throws Exception {
		if(handleSpecialSave(type, obj)) {
			return ;
		}
		
		String tableName = getUpdateTableName(type);
		JSONObject jObj = JSON.parseObject(obj);
		
		beforeAdd(type, jObj);
		
		commonDao.add(tableName, jObj);
	}
	
	/**
	 * TODO 公共的修改方法
	 */
	@Override
	public void update(String type, String obj, String key) throws Exception {
		if(handleSpecialSave(type, obj)) {
			return ;
		}
		
		String tableName = getUpdateTableName(type);
		
		JSONObject jObj = JSON.parseObject(obj);
		commonDao.update(tableName, jObj, key);
	}

	/**
	 * TODO 公共的删除方法
	 */
	@Override
	public void delete(String type, String key, String ids) throws Exception {
		if(handleSpecialDelete(type, ids)) {
			return;
		}
		
		String tableName = getUpdateTableName(type);
		
		commonDao.delete(tableName, key, ids);
	}
	
	
	/**
	 * TODO 公共的查询方法
	 */
	public PageBean<Map<String, Object>> query(String type, String conditonsJson, String orderJson, int curPage,
			int pageSize) throws Exception {
		/** 要获取的  字段 列表  */
		String fieldsStr = getQueryFields(type);
		/** 获取 表名或  视图名   */
		String queryTarget = getQueryTableName(type);
		
		List<Object> args = new ArrayList<Object>();
		StringBuilder restSql = jointConditions(queryTarget, args, conditonsJson);
		
		addDefaultConditions(type, restSql, args);
		/*** 获取总记录数   */
		Long count = commonDao.getQueryResultCount(restSql.toString());
		
		if(ObjectUtil.isEmpty(orderJson)) {
			jointDefaultOrder(type, restSql);
		}else {
			jointOrder(restSql, orderJson);
		}
		
		List<Map<String, Object>> list = commonDao.getQueryResult(fieldsStr, restSql.toString(), curPage, pageSize, args);
		
		return new PageBean<>(list, count);
	}
	
	
	/**
	 * 拼接sql的查询条件
	 * @param queryTarget
	 * @param args 
	 * @param conditions
	 * @return
	 */
	protected StringBuilder jointConditions(String queryTarget, List<Object> args, String conditions) {
		StringBuilder sql = new StringBuilder();
		sql.append(" from ").append(queryTarget).append(" where 1=1 ");
		
		JSONArray jArr = JSON.parseArray(conditions);
		int size = jArr.size();
		for (int i = 0; i < size; i++) {
			JSONObject jObj = jArr.getJSONObject(i);
			Object value = jObj.get("value");
			if(ObjectUtil.isEmpty(value)) {
				continue;
			}
			
			Object editor = jObj.getOrDefault("editor", "text");
			Object link = jObj.getOrDefault("link", "=");
			String fieldName = jObj.getString("field");
			/** TODO 处理特殊的查询条件   **/
			if(handleSpecialCondition(sql, args, jObj, fieldName, value)) {
				continue;
			}
			
			sql.append(" and ");
			//key ...
			jointConditionKey(sql, editor, fieldName);
			
			// link ... value ...
			jointConditionValue(sql, args, editor, link, value);
		}
		return sql;
	}

	/** 拼接条件的 字段名  **/
	private void jointConditionKey(StringBuilder sql, Object editor, String field) {
		
		if("date".equals(editor)){
			sql.append("convert(char(10), ").append(field).append(", 23)");
		}else{
			sql.append(field);
		}
	}
	/** 拼接条件的连接符及值  **/
	private void jointConditionValue(StringBuilder sql, List<Object> args, Object editor, Object link, Object value) {
		if("select".equals(editor)){
			sql.append(" in (").append(StrUtil.convertSqlIds((String) value)).append(")");
			return ;
		}
		if("like".equals(link)){
			sql.append(" like '"+StrUtil.clearBlankSymbol(value.toString())+"%' ");
			return ;
		}
		
		sql.append(link).append("?");
		args.add(value);
	}
	

	/**
	 * 拼接   sql的  排序 语句
	 * @param restSql
	 * @param orderJson
	 */
	protected void jointOrder(StringBuilder restSql, String orderJson) {
		
	}

	

}
