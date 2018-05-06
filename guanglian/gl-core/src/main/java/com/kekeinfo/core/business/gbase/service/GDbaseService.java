package com.kekeinfo.core.business.gbase.service;

import java.math.BigDecimal;
import java.util.Date;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.guard.data.model.GDbasePoint;
import com.kekeinfo.core.business.guard.model.GPointEnumType;

public interface GDbaseService<E> extends KekeinfoEntityService<Long, GDbasePoint<E>>{
	
    public void saveOrUpdate(GDbasePoint<E> mdbase) throws ServiceException;
	
	public void deleteAndUpdate(GDbasePoint<E> mdbase) throws ServiceException;
	
	public GDbasePoint<E> getLast(Date date,GPointEnumType type,long sid) throws ServiceException;
	
	public GDbasePoint<E> getNext(Date date,GPointEnumType type,long sid) throws ServiceException;
	
	public GDbasePoint<E>  getMax(Date date,GPointEnumType type,String mids) throws ServiceException;
	
	public GDbasePoint<E> getByIdWithPoint(long id,GPointEnumType type) throws ServiceException;
	
	public GDbasePoint<E> getEqualsHeightData(BigDecimal initHeight,long sid,GPointEnumType type) throws ServiceException;
	
	public GDbasePoint<E> getByDate(Date date,long sid,GPointEnumType type) throws ServiceException;

}
