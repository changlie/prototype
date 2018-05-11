package com.yunjia.system.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunjia.system.dao.OrganizationDao;
import com.yunjia.system.entity.TreeNode;
import com.yunjia.util.ObjectUtil;

@Service
public class OrganizationService {
	@Autowired
	private OrganizationDao organizationDao;
	

	public List<TreeNode> getOrganicationTree() {
		List<TreeNode> nodes = organizationDao.getSubDepartments(0);
		getOrganicationTree(nodes);
		return nodes;
	}
	
	private void getOrganicationTree(List<TreeNode> nodes) {
		if(ObjectUtil.isEmpty(nodes)) {
			return;
		}
		
		for (TreeNode node : nodes) {
			long id = node.getId();
			List<TreeNode> subNodes = organizationDao.getSubDepartments(id);
			if(ObjectUtil.isNotEmpty(subNodes)) {
				node.setSubs(subNodes);
				getOrganicationTree(subNodes);
			}
		}
	}

	public List<Map<String, Object>> getCascadelist() {
		List<TreeNode> nodes = organizationDao.getSubDepartments(0);
		List<Map<String, Object>> ret = getSubList(nodes);
		return ret;
	}

	private List<Map<String, Object>> getSubList(List<TreeNode> nodes) {
		List<Map<String, Object>> ret = new ArrayList<Map<String,Object>>();
		
		for (TreeNode node : nodes) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("value", node.getId()+"");
			map.put("label", node.getTitle());
			List<TreeNode> subNodes = organizationDao.getSubDepartments(node.getId());
			if(ObjectUtil.isNotEmpty(subNodes)) {
				List<Map<String, Object>> subList = getSubList(subNodes);
				map.put("children", subList);
			}
			
			ret.add(map);
		}
		
		return ret;
	}


}
