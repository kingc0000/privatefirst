package com.kekeinfo.core.business.deformmonitor.service;

import java.util.List;

import com.kekeinfo.core.business.deformmonitor.model.Deformmonitor;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;

public interface DeformmonitorService extends KekeinfoEntityService<Long, Deformmonitor> {
	Deformmonitor getByIdWithCSite(Long pid) throws ServiceException;
	Deformmonitor getByIdWithPointLink(Long pid) throws ServiceException;
	public void saveStatus(Deformmonitor dData) throws ServiceException;
	public List<Deformmonitor> getWarningData(List<Long> ids) throws ServiceException;
	List<Deformmonitor> getBycid(Long cid) throws ServiceException;
	public void saveQcode(Deformmonitor iwell) throws ServiceException;
	public List<Deformmonitor> getByIds(List<Long> ids) throws ServiceException ;
}
	
