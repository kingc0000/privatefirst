package com.kekeinfo.core.business.report.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.report.dao.RiwellDao;
import com.kekeinfo.core.business.report.model.Riwell;

@Service("riwellService")
public class RiwellServiceImpl extends KekeinfoEntityServiceImpl<Long, Riwell> implements RiwellService {
	
	@SuppressWarnings("unused")
	private RiwellDao dailyDao;
	
	
	@Autowired
	public RiwellServiceImpl(RiwellDao dailyDao) {
		super(dailyDao);
		this.dailyDao = dailyDao;
	}
	
	
}
