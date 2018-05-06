package com.kekeinfo.core.business.monitor.data.service;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.monitor.data.model.BuildingData;

public interface BuildingDataService extends MdbaseService<BuildingData> {
	public void saveOrUpdate(BuildingData buildingData) throws ServiceException;
}
