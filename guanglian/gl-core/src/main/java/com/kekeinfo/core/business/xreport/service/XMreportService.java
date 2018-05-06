package com.kekeinfo.core.business.xreport.service;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.xreport.model.XMreport;

public interface XMreportService extends KekeinfoEntityService<Long, XMreport> {
	public void createXreport(XMreport xr) throws ServiceException;
	public void deleteXReport(XMreport xr) throws ServiceException;
}
