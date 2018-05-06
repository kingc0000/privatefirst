package com.kekeinfo.core.business.monitor.data.service;


import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.monitor.data.model.SurfaceData;

public interface SurfaceDataService extends MdbaseService<SurfaceData> {
	public void saveOrUpdate(SurfaceData surfaceData) throws ServiceException;
	
	
}
