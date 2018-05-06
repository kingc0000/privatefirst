package com.kekeinfo.core.business.monitor.oblique.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.monitor.oblique.dao.DepthDao;
import com.kekeinfo.core.business.monitor.oblique.model.Depth;

@Service("depthService")
public class DepthServiceImpl extends KekeinfoEntityServiceImpl<Long, Depth> implements DepthService {
	private DepthDao depthDao;

	@Autowired
	public DepthServiceImpl(DepthDao depthDao) {
		super(depthDao);
		this.depthDao = depthDao;
	}

	@Override
	public Depth findByDeep(BigDecimal deep,Long oid) throws ServiceException {
		return depthDao.findByDeep(deep,oid);
	}

	@Override
	public List<Depth> getByOid(Long oid) throws ServiceException {
		// TODO Auto-generated method stub
		return depthDao.getByOid(oid);
	}
	
	
}
