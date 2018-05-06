package com.kekeinfo.core.business.monitor.report.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.monitor.report.dao.RHiddenLineDataDao;
import com.kekeinfo.core.business.mreport.model.RHiddenLineData;

@Service("rhiddenlinedataService")
public class RHiddenLineDataServiceImpl extends RMBaseDataServiceImpl<RHiddenLineData>
		implements RHiddenLineDataService {
	@SuppressWarnings("unused")
	private RHiddenLineDataDao hiddenlinedataDao;
	
	@Autowired
	public RHiddenLineDataServiceImpl(RHiddenLineDataDao hiddenlinedataDao) {
		super(hiddenlinedataDao);
		this.hiddenlinedataDao = hiddenlinedataDao;
	}
	
	
	
	
}
