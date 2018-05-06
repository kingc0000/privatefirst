package com.kekeinfo.core.business.attach.service;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.attach.model.Attach;

public interface AttachService extends KekeinfoEntityService<Long, Attach> {
	Attach getByName(String name) throws ServiceException;
}
