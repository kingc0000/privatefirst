package com.kekeinfo.core.business.monitor.report.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.monitor.report.dao.RSurfaceDataDao;
import com.kekeinfo.core.business.mreport.model.RSurfaceData;

@Service("rsurfacedataService")
public class RSurfaceDataServiceImpl extends RMBaseDataServiceImpl<RSurfaceData> implements RSurfaceDataService {
	@SuppressWarnings("unused")
	private RSurfaceDataDao surfacedataDao;
	
	
	@Autowired
	public RSurfaceDataServiceImpl(RSurfaceDataDao surfacedataDao) {
		super(surfacedataDao);
		this.surfacedataDao = surfacedataDao;
	}
	

	
}
