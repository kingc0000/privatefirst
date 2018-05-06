package com.kekeinfo.core.business.user.service;

import java.util.List;

import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.model.Permission;
import com.kekeinfo.core.business.user.model.PermissionCriteria;
import com.kekeinfo.core.business.user.model.PermissionList;

public interface PermissionService extends KekeinfoEntityService<Integer, Permission> {

	public Permission getByName(String name) throws ServiceException;

	List<Permission> listPermission()  throws ServiceException;
	List<Permission> listAdminPermission() throws ServiceException;

	Permission getById(Integer permissionId);

	void saveOrUpdate(Permission permission);

//	void deletePermission(Permission permission) throws ServiceException;

	List<Permission> getPermissions(List<Integer> groupIds) throws ServiceException;

	void deletePermission(Permission permission) throws ServiceException;

	PermissionList listByCriteria(PermissionCriteria criteria) throws ServiceException ;

	void removePermission(Permission permission, Group group) throws ServiceException;

}
