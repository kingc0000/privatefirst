package com.kekeinfo.core.business.monitor.report.dao;


import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.mreport.model.RSurfaceData;

@Repository("rsurfacedataDao")
public class RSurfaceDataDaoImpl extends RMBaseDataDaoImpl<RSurfaceData> implements RSurfaceDataDao {
	public RSurfaceDataDaoImpl() {
		super();
	}

	
}