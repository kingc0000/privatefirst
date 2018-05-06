package com.kekeinfo.core.business.monitor.data.service;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.monitor.data.model.HiddenLineData;

public interface HiddenLineDataService extends MdbaseService<HiddenLineData> {
	
	public void saveOrUpdate(HiddenLineData hiddenLineData) throws ServiceException;
}
