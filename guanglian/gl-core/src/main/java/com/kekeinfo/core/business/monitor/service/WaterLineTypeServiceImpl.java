package com.kekeinfo.core.business.monitor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.monitor.model.WaterLineType;
import com.kekeinfo.core.business.monitor.dao.WaterLineTypeDao;

@Service("waterlinetypeService")
public class WaterLineTypeServiceImpl extends KekeinfoEntityServiceImpl<Long, WaterLineType>
		implements WaterLineTypeService {
	private WaterLineTypeDao waterlinetypeDao;

	@Autowired
	public WaterLineTypeServiceImpl(WaterLineTypeDao waterlinetypeDao) {
		super(waterlinetypeDao);
		this.waterlinetypeDao = waterlinetypeDao;
	}

	@Override
	public List<WaterLineType> getByMid(Long mid) throws ServiceException {
		// TODO Auto-generated method stub
		return waterlinetypeDao.getByMid(mid);
	}

	@Override
	public List<WaterLineType> getByTid(Long tid) throws ServiceException {
		// TODO Auto-generated method stub
		return waterlinetypeDao.getByTid(tid);
	}
}
