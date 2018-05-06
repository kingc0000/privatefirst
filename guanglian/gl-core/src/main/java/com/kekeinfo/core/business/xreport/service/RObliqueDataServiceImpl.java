package com.kekeinfo.core.business.xreport.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.xreport.model.RObliqueData;
import com.kekeinfo.core.business.xreport.dao.RObliqueDataDao;

@Service("robliquedataService")
public class RObliqueDataServiceImpl extends KekeinfoEntityServiceImpl<Long, RObliqueData>
		implements RObliqueDataService {
	private RObliqueDataDao robliquedataDao;

	@Autowired
	public RObliqueDataServiceImpl(RObliqueDataDao robliquedataDao) {
		super(robliquedataDao);
		this.robliquedataDao = robliquedataDao;
	}

	@Override
	public List<RObliqueData> getByRid(Long rid) throws ServiceException {
		// TODO Auto-generated method stub
		return robliquedataDao.getByRid(rid);
	}
}
