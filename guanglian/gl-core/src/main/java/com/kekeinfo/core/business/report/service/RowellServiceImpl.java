package com.kekeinfo.core.business.report.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.report.dao.RowellDao;
import com.kekeinfo.core.business.report.model.Rowell;

@Service("rowellService")
public class RowellServiceImpl extends KekeinfoEntityServiceImpl<Long, Rowell> implements RowellService {
	
	@SuppressWarnings("unused")
	private RowellDao dailyDao;
	
	
	@Autowired
	public RowellServiceImpl(RowellDao dailyDao) {
		super(dailyDao);
		this.dailyDao = dailyDao;
	}
	
	
}
