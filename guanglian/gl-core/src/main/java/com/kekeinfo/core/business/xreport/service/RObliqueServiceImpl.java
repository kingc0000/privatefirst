package com.kekeinfo.core.business.xreport.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.xreport.model.ROblique;
import com.kekeinfo.core.business.xreport.dao.RObliqueDao;

@Service("robliqueService")
public class RObliqueServiceImpl extends KekeinfoEntityServiceImpl<Long, ROblique> implements RObliqueService {
	private RObliqueDao robliqueDao;

	@Autowired
	public RObliqueServiceImpl(RObliqueDao robliqueDao) {
		super(robliqueDao);
		this.robliqueDao = robliqueDao;
	}

	@Override
	public List<ROblique> getByXid(Long xid) throws ServiceException {
		// TODO Auto-generated method stub
		return robliqueDao.getByXid(xid);
	}
}
