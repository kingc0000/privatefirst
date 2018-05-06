package com.kekeinfo.core.business.guard.data.service;


import com.kekeinfo.core.business.gbase.service.GDbaseService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.guard.data.model.DiameterConvertData;

public interface DiameterConvertDataService extends GDbaseService<DiameterConvertData> {
	
	public void saveOrUpdate(DiameterConvertData diameterConvertData) throws ServiceException;
}
