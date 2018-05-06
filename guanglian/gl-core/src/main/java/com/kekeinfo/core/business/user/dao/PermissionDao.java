package com.kekeinfo.core.business.user.dao;

import java.util.List;
import java.util.Set;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.user.model.Permission;
import com.kekeinfo.core.business.user.model.PermissionCriteria;
import com.kekeinfo.core.business.user.model.PermissionList;

public interface PermissionDao extends KekeinfoEntityDao<Integer, Permission> {

	List<Permission> listPermission();
	
	List<Permission> listAdminPermission();

	Permission getById(Integer permissionId);

	@SuppressWarnings("rawtypes")
	List<Permission> getPermissionsListByGroups(Set permissionIds);

	PermissionList listByCriteria(PermissionCriteria criteria);
	
	Permission getByName(String name) ;



}
