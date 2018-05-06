package com.kekeinfo.core.business.last.service;


import java.util.List;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.last.model.GuardLast;


public interface GuardLastService extends KekeinfoEntityService<Long, GuardLast> {
	List<GuardLast> getByUserID(long userid) throws ServiceException;
	
	void saveNew(GuardLast clast) throws ServiceException;
}

