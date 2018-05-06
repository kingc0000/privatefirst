package com.kekeinfo.core.business.monitor.data.service;


import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.monitor.data.model.RingBeamData;


public interface RingBeamDataService extends MdbaseService< RingBeamData> {
	
	public void saveOrUpdate(RingBeamData ringBeamData) throws ServiceException;
	
}

