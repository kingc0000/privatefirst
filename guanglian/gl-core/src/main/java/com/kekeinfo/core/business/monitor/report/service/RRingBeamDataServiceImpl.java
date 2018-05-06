package com.kekeinfo.core.business.monitor.report.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.monitor.report.dao.RRingBeamDataDao;
import com.kekeinfo.core.business.mreport.model.RRingBeamData;

@Service("rringbeamdataService")
public class RRingBeamDataServiceImpl extends RMBaseDataServiceImpl<RRingBeamData>
		implements RRingBeamDataService {
	@SuppressWarnings("unused")
	private RRingBeamDataDao ringbeamdataDao;
	

	@Autowired
	public RRingBeamDataServiceImpl(RRingBeamDataDao ringbeamdataDao) {
		super(ringbeamdataDao);
		this.ringbeamdataDao = ringbeamdataDao;
	}
	
	
	

}
