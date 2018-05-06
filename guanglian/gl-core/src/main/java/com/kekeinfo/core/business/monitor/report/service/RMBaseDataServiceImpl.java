package com.kekeinfo.core.business.monitor.report.service;

import java.util.List;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.monitor.model.MPointEnumType;
import com.kekeinfo.core.business.monitor.report.dao.RMBaseDataDao;
import com.kekeinfo.core.business.mreport.model.RMBaseData;

public abstract class RMBaseDataServiceImpl<E> extends KekeinfoEntityServiceImpl<Long, RMBaseData<E>> implements RMBaseDataService<E> {
	private RMBaseDataDao<E> baseDao;
	public RMBaseDataServiceImpl(KekeinfoEntityDao<Long, RMBaseData<E>> genericDao) {
		super(genericDao);
		// TODO Auto-generated constructor stub
		baseDao=(RMBaseDataDao<E>) genericDao;
	}
	
	
	@Override
	public  List<RMBaseData<E>> getByRid(long rid,MPointEnumType type) throws ServiceException {
		return baseDao.getByRid(rid, type);
	}
	
	
}
