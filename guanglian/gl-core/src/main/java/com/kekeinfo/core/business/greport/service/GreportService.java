package com.kekeinfo.core.business.greport.service;


import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.greport.model.Greport;


public interface GreportService extends KekeinfoEntityService<Long, Greport> {
	public void createReport(Greport report) throws ServiceException;
}

