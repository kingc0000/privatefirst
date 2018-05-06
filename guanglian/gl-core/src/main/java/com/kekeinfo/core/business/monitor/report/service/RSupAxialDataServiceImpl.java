package com.kekeinfo.core.business.monitor.report.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.monitor.report.dao.RSupAxialDataDao;
import com.kekeinfo.core.business.mreport.model.RSupAxialData;

@Service("rsupaxialdataService")
public class RSupAxialDataServiceImpl extends RMBaseDataServiceImpl<RSupAxialData>
		implements RSupAxialDataService {
	@SuppressWarnings("unused")
	private RSupAxialDataDao supaxialdataDao;

	@Autowired
	public RSupAxialDataServiceImpl(RSupAxialDataDao supaxialdataDao) {
		super(supaxialdataDao);
		this.supaxialdataDao = supaxialdataDao;
	}
	

}
