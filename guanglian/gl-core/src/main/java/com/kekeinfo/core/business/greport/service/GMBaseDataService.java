package com.kekeinfo.core.business.greport.service;

import java.util.List;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.greport.model.GMBaseData;
import com.kekeinfo.core.business.guard.model.GPointEnumType;

public interface GMBaseDataService<E> extends KekeinfoEntityService<Long, GMBaseData<E>> {
	public List<GMBaseData<E>> getByGid(long gid,GPointEnumType type) throws ServiceException;
}
