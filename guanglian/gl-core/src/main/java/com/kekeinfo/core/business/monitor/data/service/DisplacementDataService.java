package com.kekeinfo.core.business.monitor.data.service;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.monitor.data.model.DisplacementData;

public interface DisplacementDataService extends MdbaseService<DisplacementData> {
	
	public void saveOrUpdate(DisplacementData displacementData) throws ServiceException;
}
