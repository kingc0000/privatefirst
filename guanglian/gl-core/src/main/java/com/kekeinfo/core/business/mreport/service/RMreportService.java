package com.kekeinfo.core.business.mreport.service;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.mreport.model.RMreport;

public interface RMreportService extends KekeinfoEntityService<Long, RMreport> {
	public	int createReport(RMreport rMreport) throws ServiceException;
	public RMreport getByRid(Long rid) throws ServiceException;
}
