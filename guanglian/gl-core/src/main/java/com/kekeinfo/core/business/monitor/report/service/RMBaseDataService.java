package com.kekeinfo.core.business.monitor.report.service;

import java.util.List;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.monitor.model.MPointEnumType;
import com.kekeinfo.core.business.mreport.model.RMBaseData;

public interface RMBaseDataService<E> extends KekeinfoEntityService<Long, RMBaseData<E>> {
	public List<RMBaseData<E>> getByRid(long rid,MPointEnumType type) throws ServiceException;
}
