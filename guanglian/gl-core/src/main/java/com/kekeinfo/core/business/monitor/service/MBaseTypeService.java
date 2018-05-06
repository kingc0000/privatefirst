package com.kekeinfo.core.business.monitor.service;

import java.util.List;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.monitor.model.MBaseType;

public interface MBaseTypeService extends KekeinfoEntityService<Long, MBaseType> {
	
	List<MBaseType> getByMid(Long mid) throws ServiceException;
}
