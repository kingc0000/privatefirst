package com.kekeinfo.core.business.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.data.dao.OWellDataDao;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.monitordata.model.OwellData;
import com.kekeinfo.core.business.observewell.service.ObservewellService;

@Service("oWellDataService")
public class OWellDataServiceImpl extends KekeinfoEntityServiceImpl<Long, OwellData> implements OWellDataService {
	
	@Autowired ObservewellService observewellService;
	@SuppressWarnings("unused")
	private OWellDataDao owellDataDao;
	
	@Autowired HoWellDataService hoWellDataService;
	
	@Autowired
	public OWellDataServiceImpl(OWellDataDao owellDataDao) {
		super(owellDataDao);
		this.owellDataDao = owellDataDao;
	}
}

