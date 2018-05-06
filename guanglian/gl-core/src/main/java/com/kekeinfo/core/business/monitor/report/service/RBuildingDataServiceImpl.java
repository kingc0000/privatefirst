package com.kekeinfo.core.business.monitor.report.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.monitor.report.dao.RBuildingDataDao;
import com.kekeinfo.core.business.mreport.model.RBuildingData;

@Service("rbuildingdataService")
public class RBuildingDataServiceImpl extends RMBaseDataServiceImpl<RBuildingData>
		implements RBuildingDataService {
	
	@SuppressWarnings("unused")
	private RBuildingDataDao buildingdataDao;

	@Autowired
	public RBuildingDataServiceImpl(RBuildingDataDao buildingdataDao) {
		super(buildingdataDao);
		this.buildingdataDao = buildingdataDao;
	}
	
	

}
