package com.yunjia.system.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.yunjia.common.dao.DaoTemplate;
import com.yunjia.system.entity.Employee;
import com.yunjia.system.entity.ResourceType;
import com.yunjia.system.entity.User;

@Repository
public class UserDao extends DaoTemplate{
	
	public void createUser(User user) throws Exception {
        save(user, "sys_user");
    }

    public int updateUser(User user) throws Exception {
    	return update(user, "sys_user", "id");
    }

    public void deleteUser(Long userId) {
        String sql = "delete from sys_user where id=?";
        executeUpdate(sql, userId);
    }
    
    public void tempDelete(Long userId) {
    	String sql = "update sys_user set deleted=1 where id=?";
        executeUpdate(sql, userId);
    }
    
    public void tempDelete(String username) {
    	String sql = "update sys_user set deleted=1 where username=?";
    	executeUpdate(sql, username);
    }

    

    public void clearUserRoles(long userId) {
    	String sql = "delete from sys_auth where userId=?";
    	executeUpdate(sql, userId);
	}
    public void addUserRoles(long userId, Integer id) {
    	String sql = "insert into sys_auth(userId, roleId) values(?,?)";
    	executeUpdate(sql, userId, id);
	}
    
   
    
    public User findOne(Integer userId) {
        String sql = "select id, username, nickname, email, tel, password, salt, createtime, type, available "
        		+ "from sys_user where id=?";
        return queryForEntity(sql, User.class, userId);
    }

    
    public User findByUsername(String username) {
    	String sql = "select id, username, nickname, email, tel, password, salt, createtime, type, available from sys_user where username=? and available=1";
    	 return queryForEntity(sql, User.class, username);
    }

    public Set<String> findRoles(String username) {
        String sql = "select num from sys_role r, sys_user u, sys_auth au where au.roleId = r.id and au.userId=u.id and r.available=1 and u.username=?";
        List<String> roles = queryForColumnList(sql, String.class, username);
        return new HashSet<String>(roles);
    }

    
    public Set<String> findPermissions(String username) {
        String sql = "select permission "
        		+ " from sys_role r, sys_user u, sys_auth au, sys_resource res, sys_role_resource rr "
        		+ " where r.available=1 and au.roleId = r.id and au.userId=u.id "
        		+ " and res.available=1 and r.id=rr.roleId and res.id=rr.resourceId "
        		+ "	and res.type=? and u.username=?";
        List<String> perms = queryForColumnList(sql, String.class, ResourceType.Button, username);
        return new HashSet<String>(perms);
    }
    
    public Integer getMaxUserId() {
		String sql = "select top 1 id from sys_user where id<10000 order by id desc";
		return queryForColumn(sql, Integer.class);
	}

    public void addUser(Map<String, Object> user) throws Exception {
    	save(user, "sys_user");
    }
    public long updateUser(Map<String, Object> user) throws Exception {
    	return update(user, "sys_user", "id");
    }
    
    
	public long addEmployee(Map<String, Object> emp) throws Exception {
		return saveForId(emp, "sys_employee");
	}
	public void updateEmployee(Map<String, Object> emp) throws Exception {
		update(emp, "sys_employee", "uid");
	}
	
	public void addEmployee(Employee emp) throws Exception {
		save(emp, "sys_employee");
	}
	public void updateEmployee(Employee emp) throws Exception {
		update(emp, "sys_employee", "uid");
	}
	
	public boolean existEmployee(long userId) {
		String sql = "select count(*) from sys_employee where uid=?";
		return queryForCount(sql, userId)>0;
	}

	public int deleteEmployee(Long id) {
		String sql = "delete from sys_employee where uid=?";
		return executeUpdate(sql, id);
	}

	

	


}
