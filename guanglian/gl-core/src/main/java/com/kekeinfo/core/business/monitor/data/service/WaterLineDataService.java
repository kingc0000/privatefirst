package com.kekeinfo.core.business.monitor.data.service;


import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.monitor.data.model.WaterLineData;


public interface WaterLineDataService extends MdbaseService<WaterLineData> {
	
	public void saveOrUpdate(WaterLineData waterLineData) throws ServiceException;

}

