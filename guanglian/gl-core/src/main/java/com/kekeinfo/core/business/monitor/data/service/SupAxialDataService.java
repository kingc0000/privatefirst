package com.kekeinfo.core.business.monitor.data.service;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.monitor.data.model.SupAxialData;

public interface SupAxialDataService extends MdbaseService<SupAxialData> {
	
	public void saveOrUpdate(SupAxialData supAxialData) throws ServiceException;
	
}
