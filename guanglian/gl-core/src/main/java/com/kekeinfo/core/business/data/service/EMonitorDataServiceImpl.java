package com.kekeinfo.core.business.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.data.dao.EMonitorDataDao;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.monitordata.model.EmonitorData;

@Service("eMonitorDataService")
public class EMonitorDataServiceImpl extends KekeinfoEntityServiceImpl<Long, EmonitorData> implements EMonitorDataService {
	
	
	@SuppressWarnings("unused")
	private EMonitorDataDao eMonitorDataDao;
	
	@Autowired
	public EMonitorDataServiceImpl(EMonitorDataDao eMonitorDataDao) {
		super(eMonitorDataDao);
		this.eMonitorDataDao = eMonitorDataDao;
	}

}
