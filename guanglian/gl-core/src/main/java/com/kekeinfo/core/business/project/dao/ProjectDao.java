package com.kekeinfo.core.business.project.dao;


import java.util.List;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.project.model.Project;


public interface ProjectDao extends KekeinfoEntityDao<Long, Project> {
	Project getByIdWithDepartment(long pid) throws ServiceException;
	Project withUserGroup(long pid) throws ServiceException;
	List<Project> withGroupUser();
}

