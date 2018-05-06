package com.kekeinfo.core.business.report.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.report.dao.RewellDao;
import com.kekeinfo.core.business.report.model.Rewell;

@Service("rewellService")
public class RewellServiceImpl extends KekeinfoEntityServiceImpl<Long, Rewell> implements RewellService {
	
	@SuppressWarnings("unused")
	private RewellDao dailyDao;
	
	
	@Autowired
	public RewellServiceImpl(RewellDao dailyDao) {
		super(dailyDao);
		this.dailyDao = dailyDao;
	}
	
	
}
