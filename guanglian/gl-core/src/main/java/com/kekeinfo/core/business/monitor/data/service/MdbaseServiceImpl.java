package com.kekeinfo.core.business.monitor.data.service;

import java.math.BigDecimal;
import java.util.Date;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.monitor.data.dao.MdbaseDao;
import com.kekeinfo.core.business.monitor.data.model.MDbasePoint;
import com.kekeinfo.core.business.monitor.model.MPointEnumType;

public abstract class MdbaseServiceImpl<E> extends KekeinfoEntityServiceImpl<Long, MDbasePoint<E>> implements MdbaseService<E> {
	private MdbaseDao<E> baseDao;
	public MdbaseServiceImpl(KekeinfoEntityDao<Long, MDbasePoint<E>> genericDao) {
		super(genericDao);
		// TODO Auto-generated constructor stub
		baseDao=(MdbaseDao<E>) genericDao;
	}
	
	public MDbasePoint<E> getLast(Date date,MPointEnumType type,long sid) throws ServiceException{
		return baseDao.getLast(date, type,sid);
	}
	
	public MDbasePoint<E> getNext(Date date,MPointEnumType type,long sid) throws ServiceException{
		return baseDao.getNext(date, type,sid);
	}
	
	@Override
	public void deleteAndUpdate(MDbasePoint<E> mdbase) throws ServiceException {
		
	}
	
	public MDbasePoint<E>  getMax(Date date,MPointEnumType type,String mids) throws ServiceException{
		return baseDao.getMax(date, type, mids);
	}
	
	public MDbasePoint<E> getByIdWithPoint(long id,MPointEnumType type) throws ServiceException{
		return baseDao.getByIdWithPoint(id, type);
	}
	
	public MDbasePoint<E> getEqualsHeightData(BigDecimal initHeight,long sid,MPointEnumType type) throws ServiceException{
		return baseDao.getEqualsHeightData(initHeight, sid, type);
	}
	@Override
	public MDbasePoint<E> getByDate(Date date,long sid,MPointEnumType type) throws ServiceException{
		return baseDao.getByDate(date, sid, type);
		
	}
}
