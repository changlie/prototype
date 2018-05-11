package com.yunjia.common.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.yunjia.exception.NotFoundAnnotation;

@Component
public class ClassCache {

	@Cacheable(value="entityClassCache", key="#root.methodName+#root.args[0].getSimpleName()")
	public String getTableName(Class<?> clazz) {
		TableName tableNameAnno = clazz.getAnnotation(TableName.class);
		if (tableNameAnno == null) {
			// throw Exception
			throw new NotFoundAnnotation(clazz + " 找不到用来说明表名的  @TableName");
		}
		String tableName = tableNameAnno.value();
		if (tableName == null || "".equals(tableName)) {
			tableName = clazz.getSimpleName();
		}
		return tableName;
	}
	
	@Cacheable(value="entityClassCache", key="#root.methodName+#root.args[0].getSimpleName()")
	public String getPrimaryKey(Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		String primaryKey =null;
		for (Field f : fields) {

			PrimaryKey primaryKeyAnno = f.getAnnotation(PrimaryKey.class);
			if (primaryKeyAnno != null) {
				primaryKey = f.getName();
			}
		}
		if (primaryKey == null) {
			// throw Exception
			throw new NotFoundAnnotation(clazz+" 找不到用来标注主键的  @primaryKey");
		}
		return primaryKey;
	}
	
	@Cacheable(value="entityClassCache", key="#root.methodName+#root.args[0].getSimpleName()")
	public String getFieldNames(Class<?> clazz) {
		StringBuilder fsstring = new StringBuilder();
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields) {
			String name = f.getName();
			if ("serialVersionUID".equals(name)) {
				continue;
			}

			if (fsstring.length() == 0) {
				fsstring.append(name);
			} else {
				fsstring.append(",").append(name);
			}
		}
		return fsstring.toString();
	}
	
	@Cacheable(value="entityClassCache", key="#root.methodName+#root.args[0].getSimpleName()")
	public Field[] getFields(Class<?> clazz) {
		List<Field> fields = new ArrayList<>();
		Field[] fieldArr = clazz.getDeclaredFields();
		for (Field f : fieldArr) {
			String name = f.getName();
			if ("serialVersionUID".equals(name)) {
				continue;
			}

			fields.add(f);
		}
		return fields.toArray(new Field[fields.size()]);
	}
	
}
