package com.yunjia.system.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunjia.system.dao.RoleDao;
import com.yunjia.system.dao.UserDao;
import com.yunjia.system.entity.Employee;
import com.yunjia.system.entity.Role;
import com.yunjia.system.entity.User;
import com.yunjia.system.entity.UserType;
import com.yunjia.system.shiro.ext.PasswordHelper;
import com.yunjia.util.ObjectUtil;
import com.yunjia.util.ReflectionUtil;
import com.yunjia.util.StrUtil;

/**
 * 涉及用户的所有操作
 * @author changlie
 *
 */
@Service
public class UserService {

	@Autowired
	private HttpSession session;
	@Autowired
	protected UserDao userDao;
	@Autowired
	private RoleDao roleDao;
    @Autowired
    protected PasswordHelper passwordHelper;
    /**
     * 从session中获取当前用户的信息
     * @return
     */
    public User getUserFromSession() {
    	return (User) session.getAttribute("user");
    }
    /**
     * 保存当前用户信息至session中
     * @param user
     */
    public void saveUserToSession(User user) {
    	session.setAttribute("user", user);
    }
    
    /**
     * 保存用户（同时保存用户，角色的关联）
     * @param jsonObj
     * @return
     * @throws Exception
     */
    @Transactional
    public long saveUser(String jsonObj) throws Exception {
    	User user = JSON.parseObject(jsonObj, User.class);
    	
    	JSONObject jObj = JSON.parseObject(jsonObj);
    	String roleIds = jObj.getString("roleIds");
    	
    	long userId = saveUser(user);
    	
    	saveUserRoles(userId, roleIds);
    	
    	return userId;
    }
    
	
	/**
	 * 保存用户信息
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public long saveUser(User user) throws Exception {
		
    	if(ObjectUtil.isEmpty(user.getId())) {
    		//新增
    		Integer maxUserId = userDao.getMaxUserId();
    		int newUserId = maxUserId ==null ? 0 : maxUserId.intValue()+1;
    		user.setId(newUserId);
			user.setCreatetime(new Date());
			createUser(user);
			return newUserId;
		}
    	
		//更新
		updateUser(user);
    	return user.getId();
	}
	/**
	 * 保存用户所拥有的角色。
	 * @param userId
	 * @param roleIds
	 */
	private void saveUserRoles(long userId, String roleIds) {
		System.out.println("clearUserRoles: "+userId);
		userDao.clearUserRoles(userId);
		List<Integer> ids = StrUtil.getIntegerList(roleIds);
		for (Integer id : ids) {
			userDao.addUserRoles(userId, id);
		}
	}
	
    /**
     * 保存员工信息。
     * @param jsonObj
     * @throws Exception
     */
    @Transactional
    public void saveEmployee(String jsonObj) throws Exception {
    	JSONObject jsonObject = JSON.parseObject(jsonObj);
    	
    	Map<String, Object> user = ReflectionUtil.filterMapByEntity(jsonObject, User.class);
    	Map<String, Object> emp = ReflectionUtil.filterMapByEntity(jsonObject, Employee.class);
    	completeDepartmentInfo(emp);
    	
    	long id = ObjectUtil.getLongValue(jsonObject, "id");
    	if(id == 0) {
    		user.put("createDate", new Date());
    		user.put("type", UserType.employee);
    		long empId = userDao.addEmployee(emp);
    		
    		user.put("id", empId);
    		userDao.addUser(user);
    	}else {
    		userDao.updateUser(user);
    		emp.put("uid", id);
    		userDao.updateEmployee(emp);
    	}
    }
    
    /**
     * 展开员工所属部门。 
     * @param emp
     */
    private void completeDepartmentInfo(Map<String, Object> emp) {
    	String depIds = (String) emp.getOrDefault("depIds", "");
    	if("".equals(depIds) || !depIds.contains(",")) {
			return;
		}
    	
    	String[] split = depIds.split(",");
    	for (int i = 0; i < split.length; i++) {
			emp.put("depId"+(i+1), split[i]);
		}
	}
    
    /**
     * 删除  员工记录
     * @param ids
     */
    public void deleteEmployees(String ids) {
		List<Long> idList = StrUtil.getLongList(ids);
		for (Long id : idList) {
			userDao.deleteEmployee(id);
			userDao.deleteUser(id);
		}
	}

    /**
     * 创建用户
     * @param user
     * @return 
     * @throws Exception if error occur
     */
    public void createUser(User user) throws Exception {
        //加密密码
        passwordHelper.encryptPassword(user);
        userDao.createUser(user);
    }
    
    /**
     * 修改用户信息
     * @param user
     * @throws Exception
     */
    public void updateUser(User user) throws Exception {
    	String oldPwd = userDao.findOne(user.getId()).getPassword();
    	if(ObjectUtil.isEmpty(user.getPassword()) 
    		|| oldPwd.equals(user.getPassword())) {
    		userDao.updateUser(user);
    	}else {
    		//加密密码
    		passwordHelper.encryptPassword(user);
    		userDao.updateUser(user);
    	}
    }
    
    /**
     * 修改密码
     * @param userId 用户id
     * @param newPassword 新密码
     * @throws Exception 
     */
    public void changePassword(Integer userId, String newPassword) throws Exception {
        User user =userDao.findOne(userId);
        user.setPassword(newPassword);
        passwordHelper.encryptPassword(user);
        userDao.updateUser(user);
    }
    
    /**
     * 修改当前用户的密码
     * @param oldPwd
     * @param newPwd
     * @return
     * @throws Exception
     */
    public boolean updateCurrentUserPassword(String oldPwd, String newPwd) throws Exception {
    	User user = getUserFromSession();
    	if(!passwordHelper.match(user, oldPwd)) {
    		return false;
    	}
    	user.setPassword(newPwd);
    	passwordHelper.encryptPassword(user);
    	return userDao.updateUser(user)>0;
    }

    
    /**
     * 根据用户名查找用户
     * @param username
     * @return
     */
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    /**
     * 根据用户名查找其角色
     * @param username
     * @return
     */
    public Set<String> findRoles(String username) {
        return userDao.findRoles(username);
    }

    /**
     * 根据用户名查找其权限
     * @param username
     * @return
     */
    public Set<String> findPermissions(String username) {
        return userDao.findPermissions(username);
    }
    
    /**
     * 添加角色
     * @param role
     * @return
     * @throws Exception 
     */
    public void createRole(Role role) throws Exception {
    	roleDao.createRole(role);
    }
    
    
    /**
     * 判断是否已登录
     * @return
     */
	public boolean isLogin() {
		User user = getUserFromSession();
		if(user!=null) {
			return true;
		}
		return false;
	}


}
