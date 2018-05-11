package com.yunjia.system.dao;

import org.springframework.stereotype.Repository;

import com.yunjia.common.dao.DaoTemplate;
import com.yunjia.system.entity.Role;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-1-28
 * <p>Version: 1.0
 */
@Repository
public class RoleDao extends DaoTemplate{

    public void createRole(Role role) throws Exception {
    	save(role, "sys_role");
    }
    

    public void deleteRole(Long roleId) {
        //首先把和role关联的相关表数据删掉
        String sql = "delete from sys_role_resource where roleId=?";
        executeUpdate(sql, roleId);
        sql = "delete from sys_auth where roleId=?";
        executeUpdate(sql, roleId);
        
        sql = "delete from sys_role where id=?";
        executeUpdate(sql, roleId);
    }

    
    public void correlationPermissions(Long roleId, Long... resourceIds) {
        if(resourceIds == null || resourceIds.length == 0) {
            return;
        }
        String sql = "insert into sys_role_resource(roleId, resourceId) values(?,?)";
        for(Long resourceId : resourceIds) {
            if(!exists(roleId, resourceId)) {
                executeUpdate(sql, roleId, resourceId);
            }
        }
    }

    public void clearCorrelationPermissions(Long roleId, String type) {
    	String sql = "delete from sys_role_resource where resourceId in (select id from sys_resource where type=?) and roleId=?";
    	executeUpdate(sql, type, roleId);
    }
    
    public void uncorrelationPermissions(Long roleId, Long... resourceIds) {
        if(resourceIds == null || resourceIds.length == 0) {
            return;
        }
        String sql = "delete from sys_role_resource where roleId=? and resourceId=?";
        for(Long resourceId : resourceIds) {
            if(exists(roleId, resourceId)) {
                executeUpdate(sql, roleId, resourceId);
            }
        }
    }

    public boolean exists(Long roleId, Long resourceId) {
        String sql = "select count(*) from sys_role_resource where roleId=? and resourceId=?";
        return queryForColumn(sql, Integer.class, roleId, resourceId) != 0;
    }

}
