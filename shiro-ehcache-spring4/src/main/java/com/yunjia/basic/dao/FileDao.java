package com.yunjia.basic.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunjia.basic.bean.FileInfo;
import com.yunjia.common.dao.DaoTemplate;
import com.yunjia.util.StrUtil;

@Repository
public class FileDao extends DaoTemplate{

	public long saveFileInfo(String name, String relativePath) {
		String sql = "insert into sys_file(name, path) values(?,?)";
		return saveForIdBySql(sql, name, relativePath);
	}
	
	
	public void deleteFileInfo(long id) {
		String sql = "delete from sys_file where id=?";
		executeUpdate(sql, id);
	}

	public FileInfo getFileInfo(long fileId) {
		String sql = "select id, name, path from sys_file where id=?";
		return queryForEntity(sql, FileInfo.class, fileId);
	}


	public List<FileInfo> getFileInfos(String fileIds) {
		List<Integer> ids = StrUtil.getIntegerList(fileIds);
		String placeholders = StrUtil.getPlaceholders(ids.size());
		String sql = "select id, name, path from sys_file where id in ("+placeholders+")";
		return queryForEntityList(sql, FileInfo.class, ids.toArray());
	}
	
}
