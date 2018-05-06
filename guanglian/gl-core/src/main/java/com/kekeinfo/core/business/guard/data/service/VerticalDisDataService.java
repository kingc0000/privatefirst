package com.kekeinfo.core.business.guard.data.service;



import com.kekeinfo.core.business.gbase.service.GDbaseService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.guard.data.model.VerticalDisData;


public interface VerticalDisDataService extends GDbaseService<VerticalDisData> {
	
	public void saveOrUpdate(VerticalDisData verticalDisData) throws ServiceException;
	
}

