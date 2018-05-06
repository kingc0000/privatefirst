package com.kekeinfo.core.business.gbase.service;

import java.util.List;

import com.kekeinfo.core.business.gbase.dao.GbaseDao;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.guard.model.GPointEnumType;
import com.kekeinfo.core.business.guard.point.model.GbasePoint;

public class GbaseServiceImpl<E> extends KekeinfoEntityServiceImpl<Long, GbasePoint<E>> implements GbaseService<E>{

	private GbaseDao<E> mbaseDao;
	public GbaseServiceImpl(KekeinfoEntityDao<Long, GbasePoint<E>> genericDao) {
		super(genericDao);
		mbaseDao=(GbaseDao<E>) genericDao;
	}
	@Override
	public GbasePoint<E> getById(long id, GPointEnumType type) throws ServiceException {
		return mbaseDao.getById(id, type);
	}
	@Override
	public List<GbasePoint<E>> getByMid(long id, GPointEnumType type) throws ServiceException {
		return mbaseDao.getByMid(id, type);
	}
	@Override
	public  GbasePoint<E> getByNO(String name,GPointEnumType type,long id)throws ServiceException {
		// TODO Auto-generated method stub
		return mbaseDao.getByNO(name, type, id);
	}

}
