package com.yunjia.common.dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.SqlProvider;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public abstract class DaoTemplate {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	protected ClassCache classCache;
	
	/*** TODO 2. 新增   (update by object) 
	 * @throws Exception ***/
	protected final void saveEntity(Object obj, Class<?> clazz, String tableName) throws Exception{
		Map<String,Object> map = convertEntityToMap(obj, clazz);
		save(tableName, map);
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected final void save(Object obj, String tableName) throws Exception {
		if(obj instanceof Map) {
			save(tableName, (Map)obj);
		}else {
			Map<String,Object> map = convertEntityToMap(obj);
			save(tableName, map);
		}
	}
	private void save(String tableName, Map<String,Object> map) throws Exception {
		StringBuilder sql = new StringBuilder();
		StringBuilder sql1 = new StringBuilder();
		List<Object> args = new ArrayList<>(); 
		
		sql.append("insert into ").append(tableName).append("(");
		sql1.append(" values(");
		Set<String> keys = map.keySet();
		int signal = 0;//用于处理分隔符','
		for(String key: keys){
			Object value = map.get(key);
			
			if(value==null || "".equals(value) || "serialVersionUID".equals(key)) {
				continue;
			}
			
			//头处理
			if(signal!=0){
				sql.append(",");
				sql1.append(",");
			}else{
				signal=1;
			}
			//key-value处理
			sql.append(key);
			sql1.append("?");
			args.add(value);
		}
		sql.append(") ");
		sql1.append(")");
		sql.append(sql1);
		
		jdbcTemplate.update(sql.toString(), new ArgumentPreparedStatementSetter(args.toArray()));
	}
	
	protected final long saveEntityForId(Object obj, Class<?> clazz, String tableName) throws Exception{
		Map<String,Object> map = convertEntityToMap(obj, clazz);
		return saveForId(tableName, map);
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected final long saveForId(Object obj, String tableName) throws Exception {
		if(obj instanceof Map) {
			return saveForId(tableName, (Map)obj);
		}else {
			Map<String,Object> map = convertEntityToMap(obj);
			return saveForId(tableName, map);
		}
	}
	private long saveForId(String tableName, Map<String,Object> map) throws Exception {
		StringBuilder sql = new StringBuilder();
		StringBuilder sql1 = new StringBuilder();
		List<Object> args = new ArrayList<>(); 
		
		sql.append("insert into ").append(tableName).append("(");
		sql1.append(" values(");
		Set<String> keys = map.keySet();
		int signal = 0;//用于处理分隔符','
		for(String key: keys){
			Object value = map.get(key);
			
			if(value==null || "".equals(value) || "serialVersionUID".equals(key)) {
				continue;
			}
			
			//头处理
			if(signal!=0){
				sql.append(",");
				sql1.append(",");
			}else{
				signal=1;
			}
			//key-value处理
			sql.append(key);
			sql1.append("?");
			args.add(value);
		}
		sql.append(") ");
		sql1.append(")");
		sql.append(sql1);
		
		String finalsql = sql.toString();
		Object[] final_args = args.toArray();
		System.out.println("finalsql: "+finalsql);
		System.out.println("final_args: "+Arrays.toString(final_args));
		return saveForIdBySql(finalsql, final_args);
	}
	protected final long saveForIdBySql(String finalsql, Object... final_args) {
		KeyHolder keyHolder = new GeneratedKeyHolder();  
		jdbcTemplate.update(new PreparedStatementCreator() {  
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {  
				PreparedStatement ps = (PreparedStatement) connection.prepareStatement(finalsql, Statement.RETURN_GENERATED_KEYS);  
				int len = final_args.length;
				for (int i = 0; i < len ; i++) {
					Object value = final_args[i];
					if(value instanceof java.util.Date){
						ps.setTimestamp(i+1, new Timestamp(((java.util.Date)value).getTime()));
					}else if(value instanceof java.sql.Date){
						ps.setDate(i+1, (java.sql.Date)value);
					}else{
						ps.setObject(i+1, value);
					}
				}
				return ps;  
			}
		}, keyHolder);
		return keyHolder.getKey().longValue();
	}
	
	protected final <T> void batchSave(List<T> objList, String tableName) throws Exception {
		for (T obj : objList) {
			save(obj, tableName);
		}
	}
	
	
	/*** TODO 3. 更新操作   (update by object)    ***/
	protected final int updateEntity(Object obj, Class<?> clazz, String tableName, String primaryKey) throws Exception{
		Map<String,Object> map = convertEntityToMap(obj, clazz);
		return update(tableName, primaryKey, map);
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected final int update(Object obj, String tableName, String primaryKey) throws Exception{
		
		if(obj instanceof Map) {
			return update(tableName, primaryKey, (Map)obj);
		}else {
			Map<String,Object> map = convertEntityToMap(obj);
			return update(tableName, primaryKey, map);
		}
	}
	private int update(String tableName, String primaryKey, Map<String, Object> map) throws Exception{
		StringBuilder sql = new StringBuilder();
		
		List<Object> args = new ArrayList<>();
		
		sql.append("update ").append(tableName).append(" set ");
		Set<String> keys = map.keySet();
		int signal = 0;//用于处理分隔符','
		for(String key: keys){
			Object value = map.get(key);
			//值为空的，不拼入sql语句
			if(value==null || "".equals(value) || "serialVersionUID".equals(key)) {
				continue;
			}
			//key为主键跳过
			if(key.equals(primaryKey)) {
				continue;
			}
			//头处理
			if(signal!=0){
				sql.append(",");
			}else{
				signal=1;
			}
			//key-value处理
			sql.append(key).append("=?");
			args.add(value);
		}
		sql.append(" where ").append(primaryKey).append("=?");
		args.add(map.get(primaryKey));
		int raw = jdbcTemplate.update(sql.toString(), new ArgumentPreparedStatementSetter(args.toArray()));
		return raw;
	}
	

	/** TODO 4. update by sql **/
	protected final int executeUpdate(final String sql) throws DataAccessException {
		class UpdateStatementCallback implements StatementCallback<Integer>, SqlProvider {
			@Override
			public Integer doInStatement(Statement stmt) throws SQLException {
				int rows = stmt.executeUpdate(sql);
				return rows;
			}
			@Override
			public String getSql() {
				return sql;
			}
		}
		return jdbcTemplate.execute(new UpdateStatementCallback());
	}
	protected final int executeUpdate(String sql, Object... args) {
		return jdbcTemplate.update(sql, new ArgumentPreparedStatementSetter(args));
	}

	
	/*** TODO 5. 公共分页查询方法  ***/
	protected final PageQuery buildQuery(){
		return new PageQuery(jdbcTemplate);
	}
	protected <V> QueryPlus<V> buildQuery(Class<V> clazz){
		return new QueryPlus<>(clazz, jdbcTemplate, classCache);
	}


	protected final <T> T queryForColumn(String tableName, String fieldName, String KeyName, Object KeyValue,
			Class<T> clazz) {
		StringBuilder sql = new StringBuilder("select ");
		sql.append(fieldName).append(" from ").append(tableName);
		sql.append(" where ").append(KeyName).append("=?");
		List<T> list = jdbcTemplate.query(sql.toString(), new SingleColumnRowMapper<T>(clazz), KeyValue);
		if (list!=null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	protected final <T> T queryForColumn(String sql, Class<T> clazz, Object... args) {
		List<T> list = jdbcTemplate.query(sql, new SingleColumnRowMapper<T>(clazz), args);
		if (list!=null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	protected final <T> List<T> queryForColumnList(String sql, Class<T> clazz, Object... args) {
		return jdbcTemplate.query(sql, new SingleColumnRowMapper<T>(clazz), args);
	}



	protected final Long queryForCount(String sql, Object... args) {
		List<Long> list = jdbcTemplate.query(sql, new SingleColumnRowMapper<Long>(Long.class), args);
		if (list!=null && list.size() > 0) {
			return list.get(0);
		}
		return 0L;
	}
	protected final Long queryForCountByTableName(String tableName) {
		String sql = "select count(*) from " + tableName;
		List<Long> list = jdbcTemplate.query(sql, new SingleColumnRowMapper<Long>(Long.class));
		if (list!=null && list.size() > 0) {
			return list.get(0);
		}
		return 0L;
	}

	protected final <T> T queryForEntity(String sql, Class<T> clazz, Object... args) {
		List<T> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<T>(clazz), args);
		if (list!=null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	protected final <T> List<T> queryForEntityList(String sql, Class<T> clazz, Object... args) {
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<T>(clazz), args);
	}

	/** 供PageQuery, QueryPlus  调用的static方法  **/
	static final <T> T queryForEntity(JdbcTemplate jdbcTempl, String sql, Class<T> clazz, Object... args) {
		List<T> list = jdbcTempl.query(sql, new BeanPropertyRowMapper<T>(clazz), args);
		if (list!=null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	static final <T> List<T> queryForEntityList(JdbcTemplate jdbcTempl, String sql, Class<T> clazz, Object... args) {
		return jdbcTempl.query(sql, new BeanPropertyRowMapper<T>(clazz), args);
	}


	protected final Map<String, Object> queryForMap(String sql, Object... args) {
		List<Map<String, Object>> list = jdbcTemplate.query(sql, new ColumnMapRowMapper(), args);
		if (list!=null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	protected final List<Map<String, Object>> queryForMapList(String sql, Object... args) {
		return jdbcTemplate.query(sql, new ColumnMapRowMapper(), args);
	}
	
	/** 供PageQuery, QueryPlus  调用的static方法  **/
	static final Map<String, Object> queryForMap(JdbcTemplate jdbcTempl, String sql, Object... args) {
		List<Map<String, Object>> list = jdbcTempl.query(sql, new ColumnMapRowMapper(), args);
		if (list!=null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	static final List<Map<String, Object>> queryForMapList(JdbcTemplate jdbcTempl, String sql, Object... args) {
		return jdbcTempl.query(sql, new ColumnMapRowMapper(), args);
	}

	
	/** TODO 1. common method  **/
	/**
	 * 把对象转成map, 值为空的成员变量忽略。
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> convertEntityToMap(Object obj) throws Exception{
		return convertEntityToMap(obj, obj.getClass());
	}
	
	private Map<String, Object> convertEntityToMap(Object obj, Class<?> clazz) throws Exception{
		Map<String, Object> map = new HashMap<>();
		Field[] fields = classCache.getFields(clazz);
		for(Field f : fields){
			f.setAccessible(true);
			
			String name = f.getName();
			if("serialVersionUID".equals(name)) {
				continue;
			}
			
			Object value = f.get(obj);
			if(value==null) {
				continue;
			}
			
			map.put(name, value);
		}
		return map;
	}
	
	/***
	 * "a, name as storeName, num ssnum" ==> "a, storeName, ssnum"
	 * @param origin "a, name as storeName, num ssnum"
	 * @return "a, storeName, ssnum"
	 */
	static String filterFields(String origin) {
		String[] split = origin.split(",");
		StringBuilder ret = new StringBuilder();
		for (String s : split) {
			s = s.trim();
			String[] pArr = s.split("\\s+as\\s+");
			if(pArr.length>1) {
				ret.append(",").append(pArr[1]);
				continue;
			}
			pArr = s.split("\\s+");
			if(pArr.length>1) {
				ret.append(",").append(pArr[1]);
				continue;
			}
			ret.append(",").append(pArr[0]);
		}
		return ret.substring(1);
	}
	
	
}
