package com.kekeinfo.core.business.user.service;

import java.util.List;
import java.util.Set;

import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.user.model.Group;

public interface GroupService extends KekeinfoEntityService<Integer, Group> {


	List<Group> listGroupByIds(Set<Integer> ids) throws ServiceException;
	
	Group getByName(String name) throws ServiceException;
	
	void saveOrUpdate(Group group) throws ServiceException;
	
	void deleteById(Integer id) throws ServiceException;
	
	List<Group> listGroupWithPermission() throws ServiceException;
	
	List<Group> listGroupByPermissionName(String name,int type) throws ServiceException;

}
