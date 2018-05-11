package com.yunjia.system.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.yunjia.common.dao.DaoTemplate;
import com.yunjia.system.entity.Resource;

/**
 * <p>Date: 14-1-28
 * <p>Version: 1.0
 */
@Repository
public class ResourceDao extends DaoTemplate{

    public void createResource(final Resource resource) throws Exception {
    	save(resource, "sys_resource");
    }
    
    
    public List<Map<String, Object>> getMenuList(String username, long parentId) {
    	String sql = "select res.id, res.name as title, res.num as [index], url as route  from sys_resource res, sys_role_resource rr, sys_user u, sys_role r, sys_auth auth "
    			+ " where res.id=rr.resourceId and rr.roleId=r.id and r.id=auth.roleId and auth.userId=u.id "
    			+ " and res.available=1 and r.available=1 and res.type=1 and res.parentId=? and u.username=? "
    			+ " order by res.weight ";
    	return queryForMapList(sql, parentId, username);
	}
    
    public List<Map<String, Object>> getResourceList(String type, long parentId) {
    	String sql = "select res.id, res.name as title  from sys_resource res where  res.type=? and res.parentId=? order by res.weight ";
    	return queryForMapList(sql, type, parentId);
    }
    public List<Map<String, Object>> getResourceList(String type, long parentId, String available) {
    	String sql = "select res.id, res.name as title  from sys_resource res where  res.type=? and res.parentId=? and available=?  order by res.weight";
    	return queryForMapList(sql, type, parentId, available);
    }
    
    

    public void deleteResource(Long resourceId) {
        //首先把与Resource关联的相关表的数据删掉
        String sql = "delete from sys_role_resource where resourceId=?";
        executeUpdate(sql, resourceId);

        sql = "delete from sys_resource where id=?";
        executeUpdate(sql, resourceId);
    }
    
    public void tempDelete(Long resourceId) {
    	String sql = "update sys_resource set available=0 where id=?";
    	executeUpdate(sql, resourceId);
    }

	public long getParentId(long id) {
		String sql = "select parentId from sys_resource where id=?";
		return queryForColumn(sql, Long.class, id);
	}

	public List<Long> getResourceIds(String roleId, String type, Long parentId) {
		String sql = "select res.id  from sys_resource res, sys_role_resource rr where res.id=rr.resourceId and res.available=1 and res.parentId=? and res.type=? and rr.roleId=? ";
		return queryForColumnList(sql, Long.class, parentId, type, roleId);
	}
	
	public Long getResourceIdsCount(String type, Long parentId) {
		String sql = "select count(*)  from sys_resource res where res.available=1 and parentId=? and res.type=? ";
		return queryForCount(sql, parentId, type);
	}
	

	public List<Long> getParentIds(String resourceIds) {
		String sql = "select distinct parentId from sys_resource where id in ("+resourceIds+")";
		return queryForColumnList(sql, Long.class);
	}

}
