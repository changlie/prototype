package com.yunjia.common.dao;

import java.util.Map;

public abstract class DaoModel<T, K> extends DaoTemplate{
	private Class<T> entityClass;

	public DaoModel() {
		entityClass = doGetClass();
	}

	protected abstract Class<T> doGetClass();

	protected String getTableName() {
		return classCache.getTableName(entityClass);
	}
	protected String getPrimaryKey() {
		return classCache.getPrimaryKey(entityClass);
	}
	protected String getFieldNames() {
		return classCache.getFieldNames(entityClass);
	}

	public void save(T obj) throws Exception {
		saveEntity(obj, entityClass, getTableName());
	}
	/**
	 * @param map 是T类型， 实体转成的map集合
	 * @return
	 * @throws Exception
	 */
	public void save(Map<String, Object> map) throws Exception {
		save(map, getTableName());
	}

	public long saveForId(T obj) throws Exception {
		return saveEntityForId(obj, entityClass, getTableName());
	}
	/**
	 * @param map 是T类型， 实体转成的map集合
	 * @return
	 * @throws Exception
	 */
	public long saveForId(Map<String, Object> map) throws Exception {
		return saveForId(map, getTableName());
	}
	
	public int update(T obj) throws Exception {
		return updateEntity(obj, entityClass, getTableName(), getPrimaryKey());
	}
	/**
	 * @param map 是T类型， 实体转成的map集合
	 * @return
	 * @throws Exception
	 */
	public int update(Map<String, Object> map) throws Exception {
		return update(map, getTableName(), getPrimaryKey());
	}
	
	public T get(K id) {
		StringBuilder sql = new StringBuilder(30);
		sql.append("select ").append(getFieldNames()).append(" from ")
		.append(getTableName()).append(" where ").append(getPrimaryKey()).append("=?");
		return queryForEntity(sql.toString(), entityClass, id);
	}
	
	protected T get(String key, Object value) {
		StringBuilder sql = new StringBuilder(30);
		sql.append("select ").append(getFieldNames()).append(" from ")
		.append(getTableName()).append(" where ").append(key).append("=?");
		return queryForEntity(sql.toString(), entityClass, value);
	}
	
	protected QueryPlus<T> defaultQuery() {
		return buildQuery(entityClass);
	}
	
	public int delete(K id){
		StringBuilder sql = new StringBuilder(20);
		sql.append("delete from ").append(getTableName()).append(" where ").append(getPrimaryKey()).append("=?");
		return executeUpdate(sql.toString(), id);
	}
	
	protected int delete(String key, Object value) {
		StringBuilder sql = new StringBuilder(20);
		sql.append("delete from ").append(getTableName()).append(" where ").append(key).append("=?");
		return executeUpdate(sql.toString(), value);
	}
}
