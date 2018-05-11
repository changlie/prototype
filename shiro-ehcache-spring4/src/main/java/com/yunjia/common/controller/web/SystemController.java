package com.yunjia.common.controller.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yunjia.common.controller.base.ControllerSupport;
import com.yunjia.common.controller.base.ResponseBean;
import com.yunjia.system.entity.TreeNode;
import com.yunjia.system.service.OrganizationService;
import com.yunjia.system.service.SystemService;
import com.yunjia.system.service.UserService;
import com.yunjia.util.ObjectUtil;
/**
 * 主要负责角色权限的管理,用户管理
 * @author li
 *
 */
@RestController
@RequestMapping("/system")
public class SystemController extends ControllerSupport{
	
	@Autowired
	private SystemService systemService;
	@Autowired
	private UserService userService;
	@Autowired
	private OrganizationService organizationService;

	@RequestMapping("/updateUser")
	public ResponseBean updateUser(String oldPwd, String newPwd) throws Exception {
		if(!userService.updateCurrentUserPassword(oldPwd, newPwd)) {
			return failure("原密码不匹配！");
		}
		return success();
	}
	
	@RequestMapping("/treeData")
	public ResponseBean treeData(String type, String available) throws Exception {
		if(ObjectUtil.isNotEmpty(available)) {
			return success(systemService.getMenuTree(type, available));
		}
		
		return success(systemService.getMenuTree(type));
	}
	
	@RequestMapping("/resourceIds")
	public ResponseBean resourceIds(String roleId, String type) throws Exception {
		List<Long> resourceIds = systemService.getResourceIds(roleId, type);
		return success(resourceIds);
	}
	
	@RequestMapping("/resource/{operate}")
	public ResponseBean resource(Long roleId, Long resourceId, @PathVariable String operate) throws Exception {
		if("associate".equals(operate)) {
			systemService.associateResource(roleId, resourceId);
		}else if("removeAssociate".equals(operate)) {
			systemService.removeAssociateResource(roleId, resourceId);
		}
			
		return success();
	}
	
	/**
	 * 这个接口会进行资源排他关联，即进行资源关联，先将当前角色之前的资源关联清空。
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/resource/associate/only")
	public ResponseBean resourceAssociateOnly(Long roleId, String resourceIds, String type) throws Exception {
		systemService.resourceAssociateOnly(roleId, resourceIds, type);
		return success();
	}
	
	/**
	 * 组织树
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/organizationTree")
	public ResponseBean organizationTree() throws Exception {
		List<TreeNode> organicationTree = organizationService.getOrganicationTree();
		return success(organicationTree);
	}
	
}
