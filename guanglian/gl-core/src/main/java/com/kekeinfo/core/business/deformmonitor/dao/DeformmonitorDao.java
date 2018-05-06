package com.kekeinfo.core.business.deformmonitor.dao;

import java.util.List;

import com.kekeinfo.core.business.deformmonitor.model.Deformmonitor;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.generic.exception.ServiceException;


public interface DeformmonitorDao extends KekeinfoEntityDao<Long, Deformmonitor> {
	Deformmonitor getByIdWithCSite(Long pid) throws ServiceException;
	Deformmonitor getByIdWithPointLink(Long pid) throws ServiceException;
	List<Deformmonitor> getWarningData(List<Long> ids) throws ServiceException;
	List<Deformmonitor> getBycid(Long cid) throws ServiceException;
	public List<Deformmonitor> getByIds(List<Long> ids) throws ServiceException ;
}
