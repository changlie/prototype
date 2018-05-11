package com.yunjia.system.service;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunjia.common.controller.base.ResponseBean;
import com.yunjia.system.dao.ResourceDao;
import com.yunjia.system.dao.RoleDao;
import com.yunjia.system.entity.User;
import com.yunjia.util.ObjectUtil;
import com.yunjia.util.StrUtil;

/**
 * 资源，菜单，权限的管理
 * @author changlie
 */
@Service
public class SystemService {
	
	@Autowired
	private ResourceDao resourceDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private UserService userService;
	
	/**
	 * 获取用户信息及其菜单列表
	 * @return
	 */
	public ResponseBean getMenuUserInfo() {
		User user = userService.getUserFromSession();
		Map<String, Object> menuInfo = getMenuInfo(user.getUsername());
		return new ResponseBean().success(menuInfo)
								.put("username", user.getUsername())
								.put("nickname", user.getNickname())
								.put("uid", user.getId()+"")
								.put("admin", isAdminForInt());
	}
	
	/**
	 * 例子：
	 * roles=admin,root
	 * <p>当前用户角色为： admin; 返回 true
	 * <p>当前用户角色为： admin,root; 返回 true
	 * <p>当前用户角色为： emp; 返回 false
	 * @param roles
	 * @return
	 */
	public boolean hasRoles(String...roles) {
		if(roles.length<1) {
			return false;
		}
		boolean[] hasRoles = SecurityUtils.getSubject().hasRoles(Arrays.asList(roles));
		boolean flag = false;
		for (boolean b : hasRoles) {
			flag = (flag || b);
		}
		return flag;
	}
	
	public int isAdminForInt() {
		return hasRoles("admin", "root") ? 1 : 0;
	}
	
	public boolean isAdmin() {
		return hasRoles("admin", "root");
	}
	
	

	public Map<String, Object> getMenuInfo(String username) {
		
		Map<String, Object> menuInfo = new HashMap<String, Object>();
		Map<String, Object> menuMap = new HashMap<String, Object>();
		
		//获取一级菜单
		List<Map<String, Object>> Leval1Menus = resourceDao.getMenuList(username, 0);
		for(Map<String, Object> item : Leval1Menus) {
			int id = (int) item.getOrDefault("id", -1);
			
			//获取二级菜单
			List<Map<String, Object>> Leval2Menus = resourceDao.getMenuList(username, id);
			if(ObjectUtil.isNotEmpty(Leval2Menus)) {
				item.put("subs", Leval2Menus);
				Leval2Menus.forEach( obj ->{
					menuMap.put((String) obj.get("route"), obj);
				});
			}else {
				menuMap.put((String) item.get("route"), item);
			}
		}
		
		menuInfo.put("menuTree", Leval1Menus);
		menuInfo.put("menuMap", menuMap);
		return menuInfo;
	}
	
	/**
	 * 把浏览器传过来的类型转成数据库中对应的类型
	 * @param type
	 * @return
	 */
	private String convertTypeForResource(String type) {
		if("menu".equals(type)) {
			type = "1";
		}else if("perm".equals(type)) {
			type = "2";
		}
		return type;
	}
	
	
	public List<Map<String, Object>> getMenuTree(String type){
		type = convertTypeForResource(type);
		//获取一级菜单
		List<Map<String, Object>> Leval1Menus = resourceDao.getResourceList(type, 0);
		getMenuTree(type, Leval1Menus);
		return Leval1Menus;
	}
	/**
	 * 用递归抓取tree状的资源列表
	 * @param type
	 * @param menuTree
	 */
	public void getMenuTree(String type, List<Map<String, Object>> menuTree){
		if(ObjectUtil.isEmpty(menuTree)) {
			return;
		}
		for(Map<String, Object> item : menuTree) {
			long id = (long) item.getOrDefault("id", -1);
			
			//获取下一级菜单
			List<Map<String, Object>> subMenus = resourceDao.getResourceList(type, id);
			if(ObjectUtil.isNotEmpty(subMenus)) {
				item.put("subs", subMenus);
				getMenuTree(type, subMenus);
			}
		}
		
	}
	
	
	
	
	public List<Map<String, Object>> getMenuTree(String type, String available){
		type = convertTypeForResource(type);
		//获取一级菜单
		List<Map<String, Object>> Leval1Menus = resourceDao.getResourceList(type, 0, available);
		getMenuTree(type, Leval1Menus);
		return Leval1Menus;
	}
	/**
	 * 用递归抓取tree状的资源列表
	 * @param type
	 * @param menuTree
	 * @param available
	 */
	public void getMenuTree(String type, List<Map<String, Object>> menuTree, String available){
		if(ObjectUtil.isEmpty(menuTree)) {
			return;
		}
		for(Map<String, Object> item : menuTree) {
			long id = (long) item.getOrDefault("id", -1);
			
			//获取下一级菜单
			List<Map<String, Object>> subMenus = resourceDao.getResourceList(type, id, available);
			if(ObjectUtil.isNotEmpty(subMenus)) {
				item.put("subs", subMenus);
				getMenuTree(type, subMenus);
			}
		}
		
	}


	public String getParentIds(long parentId) {
		StringBuilder ids = new StringBuilder();
		getParentIds(parentId, ids);
		ids.append(parentId).append("/");
		return ids.toString();
	}
	
	/**
	 * 用递归把父级id弄成继承状的字符串
	 */
	public void getParentIds(long id, StringBuilder ids) {
		long supParentId = resourceDao.getParentId(id);
		if(supParentId==0) {
			return;
		}
		getParentIds(supParentId, ids);
		ids.append(supParentId).append("/");
	}


	/**
	 * 获取角色所拥有的资源id列表
	 * @param roleId
	 * @param type
	 * @return
	 */
	public List<Long> getResourceIds(String roleId, String type) {
		type = convertTypeForResource(type);
		List<Long> ret = new ArrayList<Long>();
		List<Long> resourceIds1 = resourceDao.getResourceIds(roleId, type, 0L);
		for(Long id : resourceIds1) {
			Long subCount = resourceDao.getResourceIdsCount(type, id);
			List<Long> resourceIds2 = resourceDao.getResourceIds(roleId, type, id);
			if(subCount.intValue() == resourceIds2.size()) {
				ret.add(id);
			}else {
				ret.addAll(resourceIds2);
			}
		}
		return ret;
	}
	
	
	
	
	public void associateResource(Long roleId, Long... resourceIds) {
		roleDao.correlationPermissions(roleId, resourceIds);
	}
	public void removeAssociateResource(Long roleId, Long... resourceIds) {
		roleDao.uncorrelationPermissions(roleId, resourceIds);
	}


	public void resourceAssociateOnly(Long roleId, String resourceIds, String type) {
		type = convertTypeForResource(type);
		if(ObjectUtil.isEmpty(roleId)) {
			return;
		}
		roleDao.clearCorrelationPermissions(roleId, type);//把之前的资源关联清除
		
		if(ObjectUtil.isEmpty(resourceIds)) {
			return;
		}
		//由于进行资源关联时， 不一定会传父级id, 所以进行父级资源关联
		List<Long> parentIds = resourceDao.getParentIds(resourceIds);
		parentIds.forEach(resourceId -> {
			if(!roleDao.exists(roleId, resourceId)) {
				roleDao.correlationPermissions(roleId, resourceId);
			}
		});
		
		List<Long> longList = StrUtil.getLongList(resourceIds);
		longList.forEach(resourceId -> {
			if(!roleDao.exists(roleId, resourceId)) {
				roleDao.correlationPermissions(roleId, resourceId);
			}
		});
	}

	
	
}
