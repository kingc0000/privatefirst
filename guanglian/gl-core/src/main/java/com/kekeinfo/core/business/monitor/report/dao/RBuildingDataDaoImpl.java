package com.kekeinfo.core.business.monitor.report.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.mreport.model.RBuildingData;

@Repository("rbuildingdataDao")
public class RBuildingDataDaoImpl extends RMBaseDataDaoImpl<RBuildingData> implements RBuildingDataDao {
	public RBuildingDataDaoImpl() {
		super();
	}
	
}