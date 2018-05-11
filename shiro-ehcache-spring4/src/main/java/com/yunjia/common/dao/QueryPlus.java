package com.yunjia.common.dao;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.yunjia.util.LogUtil;

public class QueryPlus<V> {
	private JdbcTemplate jdbcTemplate;
	private String fieldNames;
	private Conditions conditions = new Conditions();;
	private Class<V> clazz;
	private String tableName;

	public QueryPlus(Class<V> clazz, JdbcTemplate jdbcTemplate, ClassCache classCache) {
		this.clazz = clazz;
		this.jdbcTemplate = jdbcTemplate;

		fieldNames = classCache.getFieldNames(clazz);
		tableName = classCache.getTableName(clazz);
	}
	
	public QueryPlus<V> setFieldNames(String fieldNames) {
		this.fieldNames = fieldNames;
		return this;
	}

	/** 等于 **/
	public QueryPlus<V> addCondition(String key, Object value) {
		conditions.eqCondition(key, value);
		return this;
	}

	/** 包含 **/
	public QueryPlus<V> likeCondition(String key, Object value) {
		conditions.likeCondition(key, value);
		return this;
	}

	/** 等于 **/
	public QueryPlus<V> eqCondition(String key, Object value) {
		conditions.eqCondition(key, value);
		return this;
	}

	/** 不等于 **/
	public QueryPlus<V> neCondition(String key, Object value) {
		conditions.neCondition(key, value);
		return this;
	}

	/** 大于 **/
	public QueryPlus<V> gtCondition(String key, Object value) {
		conditions.gtCondition(key, value);
		return this;
	}

	/** 小于 **/
	public QueryPlus<V> ltCondition(String key, Object value) {
		conditions.ltCondition(key, value);
		return this;
	}

	/** 大于等于 **/
	public QueryPlus<V> geCondition(String key, Object value) {
		conditions.geCondition(key, value);
		return this;
	}

	/** 小于等于 **/
	public QueryPlus<V> leCondition(String key, Object value) {
		conditions.leCondition(key, value);
		return this;
	}

	public QueryPlus<V> setConditions(String conditionClause, Object... args) {
		this.conditions.setConditions(conditionClause, args);
		return this;
	}
	public QueryPlus<V> setConditions(String conditionClause) {
		this.conditions.setConditions(conditionClause);
		return this;
	}

	private final <T> T queryForEntity(String sql, Class<T> clazz, Object... args) {
		return DaoTemplate.queryForEntity(jdbcTemplate, sql, clazz, args);
	}
	private final <T> List<T> queryForEntityList(String sql, Class<T> clazz, Object... args) {
		return DaoTemplate.queryForEntityList(jdbcTemplate, sql, clazz, args);
	}
	private final Map<String, Object> queryForMap(String sql, Object... args) {
		return DaoTemplate.queryForMap(jdbcTemplate, sql, args);
	}
	private final List<Map<String, Object>> queryForMapList(String sql, Object... args) {
		return DaoTemplate.queryForMapList(jdbcTemplate, sql, args);
	}

	public V singleResult() {
		StringBuilder sql = new StringBuilder(30);
		sql.append("select top 1 ").append(fieldNames).append(" from ").append(tableName);
		if (conditions == null || conditions.isEmpty()) {
			LogUtil.i("singleResult==>sql: ", sql);
			return queryForEntity(sql.toString(), clazz);
		}

		sql.append(" where ").append(conditions.getConditionClause());
		LogUtil.i("singleResult==>sql: ", sql);
		LogUtil.i("singleResult==>args: ", Arrays.toString(conditions.getArgs()));
		return queryForEntity(sql.toString(), clazz, conditions.getArgs());
	}
	public Map<String, Object> singleMapResult() {
		StringBuilder sql = new StringBuilder(30);
		sql.append("select top 1 ").append(fieldNames).append(" from ").append(tableName);
		if (conditions == null || conditions.isEmpty()) {
			LogUtil.i("singleMapResult==>sql: ", sql);
			return queryForMap(sql.toString());
		}

		sql.append(" where ").append(conditions.getConditionClause());
		LogUtil.i("singleMapResult==>sql: ", sql);
		LogUtil.i("singleMapResult==>args: ", Arrays.toString(conditions.getArgs()));
		return queryForMap(sql.toString(), conditions.getArgs());
	}

	public List<V> list() {
		StringBuilder sql = new StringBuilder(30);
		sql.append("select ").append(fieldNames).append(" from ").append(tableName);
		if (conditions == null || conditions.isEmpty()) {
			LogUtil.i("list==>sql: " + sql);
			return queryForEntityList(sql.toString(), clazz);
		}

		sql.append(" where ").append(conditions.getConditionClause());
		LogUtil.i("list==>sql: " + sql);
		LogUtil.i("list==>args: " + Arrays.toString(conditions.getArgs()));
		return queryForEntityList(sql.toString(), clazz, conditions.getArgs());
	}
	public List<Map<String, Object>> mapList() {
		StringBuilder sql = new StringBuilder(30);
		sql.append("select ").append(fieldNames).append(" from ").append(tableName);
		if (conditions == null || conditions.isEmpty()) {
			LogUtil.i("list==>sql: " + sql);
			return queryForMapList(sql.toString());
		}

		sql.append(" where ").append(conditions.getConditionClause());
		LogUtil.i("list==>sql: " + sql);
		LogUtil.i("list==>args: " + Arrays.toString(conditions.getArgs()));
		return queryForMapList(sql.toString(), conditions.getArgs());
	}

	public List<V> page(int curPage, int pageSize) {
		StringBuilder sql;
		if (conditions == null || conditions.isEmpty()) {
			sql = getPageSql(fieldNames, curPage, pageSize, false, null);
			return queryForEntityList(sql.toString(), clazz);
		}
		sql = getPageSql(fieldNames, curPage, pageSize, true, conditions.getConditionClause());
		return queryForEntityList(sql.toString(), clazz, conditions.getArgs());
	}
	public List<Map<String, Object>> pageMapList(int curPage, int pageSize) {
		StringBuilder sql;
		if (conditions == null || conditions.isEmpty()) {
			sql = getPageSql(fieldNames, curPage, pageSize, false, null);
			return queryForMapList(sql.toString());
		}
		sql = getPageSql(fieldNames, curPage, pageSize, true, conditions.getConditionClause());
		return queryForMapList(sql.toString(), conditions.getArgs());
	}

	
	private StringBuilder getPageSql(String fields, int curpage, int pageSize,
			boolean hasConditions, String conditionClause) {
		StringBuilder sql = new StringBuilder(50);
		if (curpage == 1) {// 当查询第一页时
			sql.append("select top ").append(pageSize).append(" ").append(fields).append(" from ").append(tableName);
			if(hasConditions) {
				sql.append(" where ").append(conditionClause);
			}
		} else {
			String filterFields = DaoTemplate.filterFields(fields);
			sql.append("select ").append(filterFields).append(" from (");
			sql.append("select top ").append(pageSize * curpage).append(" ").append(fields)
			.append(",ROW_NUMBER() OVER (ORDER BY CURRENT_TIMESTAMP) as _ROW_INDEX ").append(" from ")
			.append(tableName);
			if(hasConditions) {
				sql.append(" where ").append(conditionClause);
			}
			sql.append(") virtab where _ROW_INDEX>").append((curpage - 1) * pageSize);
		}
		LogUtil.i(clazz.getSimpleName() + " ==>page sql: " + sql);
		return sql;
	}
	
}
