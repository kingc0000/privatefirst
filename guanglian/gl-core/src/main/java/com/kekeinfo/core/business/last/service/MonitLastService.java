package com.kekeinfo.core.business.last.service;

import java.util.List;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.last.model.MonitLast;

public interface MonitLastService extends KekeinfoEntityService<Long, MonitLast> {
	List<MonitLast> getByUserID(long userid) throws ServiceException;
	
	void saveNew(MonitLast clast) throws ServiceException;
}
