package com.kekeinfo.core.business.monitor.data.service;


import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.monitor.data.model.UpRightData;


public interface UpRightDataService extends MdbaseService<UpRightData> {
	
	public void saveOrUpdate(UpRightData upRightData) throws ServiceException;
	
	
}

