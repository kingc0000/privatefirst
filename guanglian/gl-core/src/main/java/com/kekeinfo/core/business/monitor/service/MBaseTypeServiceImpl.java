package com.kekeinfo.core.business.monitor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.monitor.dao.MBaseTypeDao;
import com.kekeinfo.core.business.monitor.model.MBaseType;

@Service("mbasetypeService")
public class MBaseTypeServiceImpl extends KekeinfoEntityServiceImpl<Long, MBaseType> implements MBaseTypeService {
	private MBaseTypeDao mbasetypeDao;

	@Autowired
	public MBaseTypeServiceImpl(MBaseTypeDao mbasetypeDao) {
		super(mbasetypeDao);
		this.mbasetypeDao = mbasetypeDao;
	}

	@Override
	public List<MBaseType> getByMid(Long mid) throws ServiceException {
		// TODO Auto-generated method stub
		return mbasetypeDao.getByMid(mid);
	}
}
