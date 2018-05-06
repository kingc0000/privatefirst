package com.kekeinfo.core.business.mbase.service;

import java.util.List;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.monitor.model.MPointEnumType;
import com.kekeinfo.core.business.monitor.surface.model.MbasePoint;

public interface MbaseService<E> extends KekeinfoEntityService<Long, MbasePoint<E>> {
	MbasePoint<E> getById(long id,MPointEnumType type) throws ServiceException;
	 MbasePoint<E> getByNO(String name,MPointEnumType type,long id) throws ServiceException;
	 public List<MbasePoint<E>> getByMid(long id,MPointEnumType type) throws ServiceException;
	
}
