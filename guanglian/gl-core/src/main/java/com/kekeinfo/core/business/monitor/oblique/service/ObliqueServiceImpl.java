package com.kekeinfo.core.business.monitor.oblique.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.monitor.oblique.model.Oblique;
import com.kekeinfo.core.business.monitor.oblique.dao.ObliqueDao;

@Service("obliqueService")
public class ObliqueServiceImpl extends KekeinfoEntityServiceImpl<Long, Oblique> implements ObliqueService {
	private ObliqueDao obliqueDao;

	@Autowired
	public ObliqueServiceImpl(ObliqueDao obliqueDao) {
		super(obliqueDao);
		this.obliqueDao = obliqueDao;
	}

	@Override
	public List<Oblique> getByMid(Long mid) throws ServiceException {
		// TODO Auto-generated method stub
		return obliqueDao.getByMid(mid);
	}
}
