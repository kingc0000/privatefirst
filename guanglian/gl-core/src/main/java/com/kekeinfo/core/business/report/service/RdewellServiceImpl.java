package com.kekeinfo.core.business.report.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.report.dao.RdewellDao;
import com.kekeinfo.core.business.report.model.Rdewell;

@Service("rdewellService")
public class RdewellServiceImpl extends KekeinfoEntityServiceImpl<Long, Rdewell> implements RdewellService {
	
	@SuppressWarnings("unused")
	private RdewellDao dailyDao;
	
	
	@Autowired
	public RdewellServiceImpl(RdewellDao dailyDao) {
		super(dailyDao);
		this.dailyDao = dailyDao;
	}
	
	
}
