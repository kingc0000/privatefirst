package com.kekeinfo.core.business.project.service;


import java.util.List;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.project.model.Project;
import com.kekeinfo.core.business.user.model.User;


public interface ProjectService extends KekeinfoEntityService<Long, Project> {
	Project getByIdWithDepartment(long pid) throws ServiceException;
	Project withUserGroup(long pid) throws ServiceException;
	int  saveOrUpdate(Project project,User user) throws ServiceException;
	List<Project> withGroupUser() throws ServiceException;
	
	void delete(Project project)throws ServiceException;
}

