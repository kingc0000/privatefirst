package com.kekeinfo.core.business.guard.service;

import java.util.List;

import com.kekeinfo.core.business.department.model.DepartmentNode;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.guard.model.Guard;
import com.kekeinfo.core.business.user.model.User;

public interface GuardService extends KekeinfoEntityService<Long, Guard> {
	void  saveOrUpdate(Guard monitor,User user,String delids,List<DepartmentNode> pNodes) throws ServiceException;
	void deleteByCid(Long cid) throws ServiceException;
	
	void deleteWithPermission(Long cid) throws ServiceException;
	public Guard getByIdWithImg(long cid) throws ServiceException;
	public Guard getByCid(long cid) throws ServiceException;
	
	List<Guard> withProject() throws ServiceException;
	
	List<Guard> getByPid(long cid) throws ServiceException;
	
}
