package com.kekeinfo.core.business.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.data.dao.PWellDataDao;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.monitordata.model.PwellData;
import com.kekeinfo.core.business.pumpwell.service.PumpwellService;

@Service("pWellDataService")
public class PWellDataServiceImpl extends KekeinfoEntityServiceImpl<Long, PwellData> implements PWellDataService {
	
	@Autowired PumpwellService pumpwellService;
	@SuppressWarnings("unused")
	private PWellDataDao pwellDataDao;
	@Autowired HpWellDataService hpWellDataService;
	
	@Autowired
	public PWellDataServiceImpl(PWellDataDao pwellDataDao) {
		super(pwellDataDao);
		this.pwellDataDao = pwellDataDao;
	}

}
