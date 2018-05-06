package com.kekeinfo.core.business.gbase.service;

import java.math.BigDecimal;
import java.util.Date;

import com.kekeinfo.core.business.gbase.dao.GDbaseDao;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.guard.data.model.GDbasePoint;
import com.kekeinfo.core.business.guard.model.GPointEnumType;
import com.kekeinfo.core.business.monitor.data.model.MDbasePoint;
	
public abstract class GDbaseServiceImpl<E> extends KekeinfoEntityServiceImpl<Long, GDbasePoint<E>> implements GDbaseService<E> {

	private GDbaseDao<E> baseDao;
	
	public GDbaseServiceImpl(KekeinfoEntityDao<Long, GDbasePoint<E>> genericDao) {
		super(genericDao);
		baseDao=(GDbaseDao<E>) genericDao;
	}
	
	public GDbasePoint<E> getLast(Date date,GPointEnumType type,long sid) throws ServiceException{
		return baseDao.getLast(date, type,sid);
	}
	
	public GDbasePoint<E> getNext(Date date,GPointEnumType type,long sid) throws ServiceException{
		return baseDao.getNext(date, type,sid);
	}
	
	public void deleteAndUpdate(MDbasePoint<E> mdbase) throws ServiceException {
		
	}
	
	public GDbasePoint<E>  getMax(Date date,GPointEnumType type,String mids) throws ServiceException{
		return baseDao.getMax(date, type, mids);
	}
	
	public GDbasePoint<E> getByIdWithPoint(long id,GPointEnumType type) throws ServiceException{
		return baseDao.getByIdWithPoint(id, type);
	}
	
	public GDbasePoint<E> getEqualsHeightData(BigDecimal initHeight,long sid,GPointEnumType type) throws ServiceException{
		return baseDao.getEqualsHeightData(initHeight, sid, type);
	}
	
	public GDbasePoint<E> getByDate(Date date,long sid,GPointEnumType type) throws ServiceException{
		return baseDao.getByDate(date, sid, type);
	}

}
