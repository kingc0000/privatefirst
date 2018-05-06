package com.kekeinfo.core.business.report.service;


import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.report.model.Report;


public interface ReportService extends KekeinfoEntityService<Long, Report> {

	public void saveOrUpdate(Report report) throws ServiceException; 
	
	public Report getIdWithWell(Long id) throws ServiceException; 
}
