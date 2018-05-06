package com.kekeinfo.core.business.user.dao;

import java.util.List;
import java.util.Set;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.user.model.Group;

public interface GroupDao extends KekeinfoEntityDao<Integer, Group> {

	List<Group> getGroupsListBypermissions(Set<Integer> ids);

	
	List<Group> listGroupByIds(Set<Integer> ids);
	
	Group getByName(String name);
	
	List<Group> listGroupWithPermission();
	
	List<Group> listGroupByPermissionName(String name,int type);
}
