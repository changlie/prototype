package com.yunjia.common.dao;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.yunjia.util.LogUtil;
import com.yunjia.util.ObjectUtil;

/**
 * 为sql server 分页定制（版本支持，2008以上）
 * 
 * @author changlie
 *
 */
public class PageQuery  {
	private JdbcTemplate jdbcTemplate;
	private String select;
	private String restClause;

	public PageQuery(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public PageQuery setSql(String select, String restClause) {
		this.select = select;
		this.restClause = restClause;
		return this;
	}
	public PageQuery setSql(String restClause) {
		this.restClause = restClause;
		return this;
	}
	
	private final List<Map<String, Object>> queryForMapList(String sql, Object... args) {
		return DaoTemplate.queryForMapList(jdbcTemplate, sql, args);
	}
	private final <T> List<T> queryForEntityList(String sql, Class<T> clazz, Object... args) {
		return DaoTemplate.queryForEntityList(jdbcTemplate, sql, clazz, args);
	}
	

	public List<Map<String, Object>> pageList(int curpage, int pageSize, Object... args) {
		if (ObjectUtil.isNotEmpty(restClause)) {
			StringBuilder sql = getPageSql(curpage, pageSize);
			LogUtil.i("args:", Arrays.toString(args));
			return queryForMapList(sql.toString(), args);
		}
		return null;
	}
	public <T> List<T> pageList(Class<T> clazz, int curpage, int pageSize, Object... args) {
		if (ObjectUtil.isNotEmpty(restClause)) {
			StringBuilder sql = getPageSql(curpage, pageSize);
			return queryForEntityList(sql.toString(), clazz, args);
		}
		return null;
	}

	private StringBuilder getPageSql(int curpage, int pageSize) {
		StringBuilder sql = new StringBuilder(50);
		if (curpage == 1) {// 当查询第一页时
			if (ObjectUtil.isEmpty(select)) {
				sql.append("select top ").append(pageSize).append(" * ");
			} else {
				sql.append("select top ").append(pageSize).append(" ").append(select).append(" ");
			}
			sql.append(restClause);
		} else {
			if (ObjectUtil.isEmpty(select)) {
				sql.append("select * from ( select top ").append(pageSize * curpage)
						.append(" *,ROW_NUMBER() OVER (ORDER BY CURRENT_TIMESTAMP) as _ROW_INDEX ");
			} else {
				String filterFields = DaoTemplate.filterFields(select);
				sql.append("select ").append(filterFields).append(" from ( select top ").append(pageSize * curpage)
						.append(" ").append(select)
						.append(",ROW_NUMBER() OVER (ORDER BY CURRENT_TIMESTAMP) as _ROW_INDEX ");
			}
			sql.append(restClause);
			sql.append(") virtab where _ROW_INDEX>").append((curpage - 1) * pageSize);
		}
		LogUtil.i(getClass() + " ==>sql: " + sql);
		return sql;
	}
	
	

}
