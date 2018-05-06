package com.kekeinfo.core.business.monitor.oblique.service;

import java.math.BigDecimal;
import java.util.List;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.monitor.oblique.model.Depth;

public interface DepthService extends KekeinfoEntityService<Long, Depth> {

	public Depth findByDeep(BigDecimal deep,Long oid) throws ServiceException;
	List<Depth> getByOid(Long oid) throws ServiceException;
	
}
