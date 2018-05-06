package com.kekeinfo.core.business.monitoreqip.service;

import java.util.List;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.monitoreqip.model.MonitorEqip;

public interface MonitorEqipService extends KekeinfoEntityService<Long, MonitorEqip> {
	List<MonitorEqip> getByMid(long mid) throws ServiceException;
	
	public void saveOrUpdate(MonitorEqip monitorEqip, String mpids[]) throws ServiceException;
	
	public void remove(MonitorEqip monitorEqip) throws ServiceException;
}
