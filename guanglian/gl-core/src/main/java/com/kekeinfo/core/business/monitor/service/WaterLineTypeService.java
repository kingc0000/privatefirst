package com.kekeinfo.core.business.monitor.service;

import java.util.List;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.monitor.model.WaterLineType;

public interface WaterLineTypeService extends KekeinfoEntityService<Long, WaterLineType> {
	List<WaterLineType> getByMid(Long mid) throws ServiceException;
	List<WaterLineType> getByTid(Long tid) throws ServiceException;
}
