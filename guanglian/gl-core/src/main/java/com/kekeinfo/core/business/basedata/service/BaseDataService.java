package com.kekeinfo.core.business.basedata.service;


import java.util.List;

import com.kekeinfo.core.business.common.model.BasedataType;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;


public interface BaseDataService extends KekeinfoEntityService<Long, BasedataType> {
	List<BasedataType> listByType(String storeType);
	void saveOrUpdate(BasedataType bd) throws ServiceException;
	
	void remove(Long bd) throws ServiceException;
}
