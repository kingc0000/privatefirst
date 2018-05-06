package com.kekeinfo.core.business.monitor.report.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.monitor.report.dao.RWaterLineDataDao;
import com.kekeinfo.core.business.mreport.model.RWaterLineData;

@Service("rwaterlinedataService")
public class RWaterLineDataServiceImpl extends RMBaseDataServiceImpl<RWaterLineData>
		implements RWaterLineDataService {
	@SuppressWarnings("unused")
	private RWaterLineDataDao waterlinedataDao;
	@Autowired
	public RWaterLineDataServiceImpl(RWaterLineDataDao waterlinedataDao) {
		super(waterlinedataDao);
		this.waterlinedataDao = waterlinedataDao;
	}
	
	
}
