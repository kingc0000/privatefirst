package com.kekeinfo.core.business.gbase.service;

import java.util.List;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.guard.model.GPointEnumType;
import com.kekeinfo.core.business.guard.point.model.GbasePoint;

public interface GbaseService<E> extends KekeinfoEntityService<Long, GbasePoint<E>>{
	
	 public GbasePoint<E> getById(long id,GPointEnumType type) throws ServiceException;
	
	 public List<GbasePoint<E>> getByMid(long id,GPointEnumType type) throws ServiceException;
	 
	 GbasePoint<E> getByNO(String name,GPointEnumType type,long id) throws ServiceException;

}
