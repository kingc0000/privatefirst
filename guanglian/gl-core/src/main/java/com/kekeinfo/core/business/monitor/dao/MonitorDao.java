package com.kekeinfo.core.business.monitor.dao;

import java.util.List;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.monitor.model.Monitor;

public interface MonitorDao extends KekeinfoEntityDao<Long, Monitor> {
	public Monitor getByIdWithImg(long cid) throws ServiceException;
	
	public Monitor getByCid(long cid) throws ServiceException;
	
	public List<Monitor> withProject() throws ServiceException;
	
	public List<Monitor> getByPid(long cid) throws ServiceException;

}
