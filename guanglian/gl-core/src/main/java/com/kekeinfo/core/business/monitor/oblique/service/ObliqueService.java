package com.kekeinfo.core.business.monitor.oblique.service;

import java.util.List;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.monitor.oblique.model.Oblique;

public interface ObliqueService extends KekeinfoEntityService<Long, Oblique> {
	List<Oblique> getByMid(Long mid) throws ServiceException;
}
