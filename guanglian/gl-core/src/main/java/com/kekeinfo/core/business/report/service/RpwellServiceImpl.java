package com.kekeinfo.core.business.report.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.report.dao.RpwellDao;
import com.kekeinfo.core.business.report.model.Rpwell;

@Service("rpwellService")
public class RpwellServiceImpl extends KekeinfoEntityServiceImpl<Long, Rpwell> implements RpwellService {
	
	@SuppressWarnings("unused")
	private RpwellDao dailyDao;
	
	
	@Autowired
	public RpwellServiceImpl(RpwellDao dailyDao) {
		super(dailyDao);
		this.dailyDao = dailyDao;
	}
	
	
}
