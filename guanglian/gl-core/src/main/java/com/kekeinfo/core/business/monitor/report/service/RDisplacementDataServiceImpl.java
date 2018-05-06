package com.kekeinfo.core.business.monitor.report.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.monitor.report.dao.RDisplacementDataDao;
import com.kekeinfo.core.business.mreport.model.RDisplacementData;

@Service("rdisplacementDataServic")
public class RDisplacementDataServiceImpl extends RMBaseDataServiceImpl<RDisplacementData>
		implements RDisplacementDataService {
	
	@SuppressWarnings("unused")
	private RDisplacementDataDao buildingdataDao;

	@Autowired
	public RDisplacementDataServiceImpl(RDisplacementDataDao buildingdataDao) {
		super(buildingdataDao);
		this.buildingdataDao = buildingdataDao;
	}
	
	

}
