package com.kekeinfo.core.business.monitor.report.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.mreport.model.RWaterLineData;

@Repository("rwaterlinedataDao")
public class RWaterLineDataDaoImpl extends RMBaseDataDaoImpl<RWaterLineData> implements RWaterLineDataDao {
	public RWaterLineDataDaoImpl() {
		super();
	}

	
}