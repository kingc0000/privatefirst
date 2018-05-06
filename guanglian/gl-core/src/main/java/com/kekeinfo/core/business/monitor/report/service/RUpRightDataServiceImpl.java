package com.kekeinfo.core.business.monitor.report.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.monitor.report.dao.RUpRightDataDao;
import com.kekeinfo.core.business.mreport.model.RUpRightData;

@Service("ruprightdataService")
public class RUpRightDataServiceImpl extends RMBaseDataServiceImpl<RUpRightData> implements RUpRightDataService {

	@SuppressWarnings("unused")
	private RUpRightDataDao uprightdataDao;
	

	@Autowired
	public RUpRightDataServiceImpl(RUpRightDataDao uprightdataDao) {
		super(uprightdataDao);
		this.uprightdataDao = uprightdataDao;
	}
	

}
