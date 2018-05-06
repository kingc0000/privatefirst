package com.kekeinfo.core.business.guardeqip.service;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.guardeqip.model.GuardEqip;

public interface GuardEqipService extends KekeinfoEntityService<Long, GuardEqip> {
	public void saveUpdate(GuardEqip ge) throws ServiceException;
	
	public void removeGuard(GuardEqip ge) throws ServiceException;
}
