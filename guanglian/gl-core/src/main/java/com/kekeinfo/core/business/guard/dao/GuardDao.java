package com.kekeinfo.core.business.guard.dao;

import java.util.List;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.guard.model.Guard;

public interface GuardDao extends KekeinfoEntityDao<Long, Guard> {
	public Guard getByIdWithImg(long cid) throws ServiceException;
	public Guard getByCid(long cid) throws ServiceException;
	
	List<Guard> withProject() throws ServiceException;
	
	List<Guard> getByPid(long cid) throws ServiceException;
	
}
