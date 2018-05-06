package com.kekeinfo.core.business.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.data.dao.DeWellDataDao;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.monitordata.model.DewellData;

@Service("deWellDataService")
public class DeWellDataServiceImpl extends KekeinfoEntityServiceImpl<Long, DewellData> implements DeWellDataService {
	
	@SuppressWarnings("unused")
	private DeWellDataDao dewellDataDao;
	
	@Autowired
	public DeWellDataServiceImpl(DeWellDataDao pwellDataDao) {
		super(pwellDataDao);
		this.dewellDataDao = pwellDataDao;
	}

}
