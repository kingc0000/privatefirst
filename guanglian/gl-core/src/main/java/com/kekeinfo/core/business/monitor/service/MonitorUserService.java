package com.kekeinfo.core.business.monitor.service;

import java.util.List;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.monitor.model.MonitorUser;

public interface MonitorUserService extends KekeinfoEntityService<Long, MonitorUser> {
	List<Object[]> getPinYin() throws ServiceException; 
}
