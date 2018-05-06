package com.kekeinfo.core.business.guard.data.service;

import com.kekeinfo.core.business.gbase.service.GDbaseService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.guard.data.model.HshiftData;

public interface HshiftDataService extends GDbaseService<HshiftData> {
	public void saveOrUpdate(HshiftData hshiftData) throws ServiceException;
}
