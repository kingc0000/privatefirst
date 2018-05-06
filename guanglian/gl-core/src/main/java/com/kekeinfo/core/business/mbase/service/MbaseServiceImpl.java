package com.kekeinfo.core.business.mbase.service;

import java.util.List;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.mbase.dao.MbaseDao;
import com.kekeinfo.core.business.monitor.model.MPointEnumType;
import com.kekeinfo.core.business.monitor.surface.model.MbasePoint;

public abstract class MbaseServiceImpl<E> extends KekeinfoEntityServiceImpl<Long, MbasePoint<E>> implements MbaseService<E> {

	private MbaseDao<E> mbaseDao;
	public MbaseServiceImpl(KekeinfoEntityDao<Long, MbasePoint<E>> genericDao) {
		super(genericDao);
		mbaseDao=(MbaseDao<E>) genericDao;
	}
	public MbasePoint<E> getById(long id,MPointEnumType type) throws ServiceException{
		MbasePoint<E> mbase = this.mbaseDao.getById(id, type);
		return mbase;
	}
	
	public MbasePoint<E> getByNO(String name,MPointEnumType type,long id)throws ServiceException{
		MbasePoint<E> mbase = this.mbaseDao.getByNO(name, type,id);
		return mbase;
	}
	
	public List<MbasePoint<E>> getByMid(long id,MPointEnumType type) throws ServiceException{
		return this.mbaseDao.getByMid(id, type);
	}
}
