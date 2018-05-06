package com.kekeinfo.core.business.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.data.dao.IWellDataDao;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.invertedwell.service.InvertedwellService;
import com.kekeinfo.core.business.monitordata.model.IwellData;

@Service("iWellDataService")
public class IWellDataServiceImpl extends KekeinfoEntityServiceImpl<Long, IwellData> implements IWellDataService {
	
	@Autowired InvertedwellService invertedwellService;
	@SuppressWarnings("unused")
	private IWellDataDao iwellDataDao;
	
	@Autowired HiWellDataService hiWellDataService;
	
	@Autowired
	public IWellDataServiceImpl(IWellDataDao iwellDataDao) {
		super(iwellDataDao);
		this.iwellDataDao = iwellDataDao;
	}

}
