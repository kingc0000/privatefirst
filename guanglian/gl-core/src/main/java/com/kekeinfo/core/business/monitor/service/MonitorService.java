package com.kekeinfo.core.business.monitor.service;

import java.util.List;

import com.kekeinfo.core.business.department.model.DepartmentNode;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.monitor.model.Monitor;
import com.kekeinfo.core.business.user.model.User;

public interface MonitorService extends KekeinfoEntityService<Long, Monitor> {
	void  saveOrUpdate(Monitor monitor,User user,String delids,List<DepartmentNode> pNodes) throws ServiceException;
	void deleteByCid(Long cid) throws ServiceException;
	
	void deleteWithPermission(Long cid) throws ServiceException;
	public Monitor getByIdWithImg(long cid) throws ServiceException;
	
	public Monitor getByCid(long cid) throws ServiceException;
	
	public List<Monitor> withProject() throws ServiceException;
	
	public List<Monitor> getByPid(long cid) throws ServiceException;

}
