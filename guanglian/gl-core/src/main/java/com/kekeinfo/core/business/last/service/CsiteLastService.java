package com.kekeinfo.core.business.last.service;

import java.util.List;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.last.model.CsiteLast;

public interface CsiteLastService extends KekeinfoEntityService<Long, CsiteLast> {
	List<CsiteLast> getByUserID(long userid) throws ServiceException;
	
	void saveNew(CsiteLast clast) throws ServiceException;
}
