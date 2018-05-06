package com.kekeinfo.core.business.monitor.data.service;

import java.math.BigDecimal;
import java.util.Date;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.monitor.data.model.MDbasePoint;
import com.kekeinfo.core.business.monitor.model.MPointEnumType;

public interface MdbaseService<E> extends KekeinfoEntityService<Long, MDbasePoint<E>> {
	public void saveOrUpdate(MDbasePoint<E> mdbase) throws ServiceException;
	
	public void deleteAndUpdate(MDbasePoint<E> mdbase) throws ServiceException;
	
	public MDbasePoint<E> getByDate(Date date,long sid,MPointEnumType type) throws ServiceException;
	
	public MDbasePoint<E> getLast(Date date,MPointEnumType type,long sid) throws ServiceException;
	
	public MDbasePoint<E> getNext(Date date,MPointEnumType type,long sid) throws ServiceException;
	
	public MDbasePoint<E>  getMax(Date date,MPointEnumType type,String mids) throws ServiceException;
	
	public MDbasePoint<E> getByIdWithPoint(long id,MPointEnumType type) throws ServiceException;
	
	public MDbasePoint<E> getEqualsHeightData(BigDecimal initHeight,long sid,MPointEnumType type) throws ServiceException;
}
