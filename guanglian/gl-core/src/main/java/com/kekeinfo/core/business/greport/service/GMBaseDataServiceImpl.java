package com.kekeinfo.core.business.greport.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.greport.model.GMBaseData;
import com.kekeinfo.core.business.guard.model.GPointEnumType;
import com.kekeinfo.core.business.greport.dao.GMBaseDataDao;

@Service("gmbasedataService")
public abstract class GMBaseDataServiceImpl<E> extends KekeinfoEntityServiceImpl<Long, GMBaseData<E>> implements GMBaseDataService<E> {
	@SuppressWarnings({ "rawtypes" })
	private GMBaseDataDao gmbasedataDao;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Autowired
	public GMBaseDataServiceImpl(GMBaseDataDao gmbasedataDao) {
		super(gmbasedataDao);
		this.gmbasedataDao = gmbasedataDao;
	}
	
	@SuppressWarnings("unchecked")
	public List<GMBaseData<E>> getByGid(long gid,GPointEnumType type) throws ServiceException{
		return this.gmbasedataDao.getByGid(gid, type);
	}
}
