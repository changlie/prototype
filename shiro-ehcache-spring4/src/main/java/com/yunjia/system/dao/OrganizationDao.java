package com.yunjia.system.dao;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunjia.common.dao.DaoModel;
import com.yunjia.system.entity.Organization;
import com.yunjia.system.entity.TreeNode;

@Repository
public class OrganizationDao extends DaoModel<Organization, Integer>{

	@Override
	protected Class<Organization> doGetClass() {
		return Organization.class;
	}
	
	public List<TreeNode> getSubDepartments(long id) {
		String sql = "select id, name as title from sys_organization where parentId=?";
		return queryForEntityList(sql, TreeNode.class, id);
	}
	
}
