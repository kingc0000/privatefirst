package com.kekeinfo.core.business.job.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.job.model.GJob;

public interface GJobService extends KekeinfoEntityService<Long, GJob> {
	List<GJob> getByGidsAndDate(List<Long> ids,Date date,Date edate) throws ServiceException;
	String saveUpdate(GJob gjob) throws ServiceException, ParseException;
	List<GJob> getToday(Date today,Long uid) throws ServiceException; 
	List<GJob> getEndDate(Date end) throws ServiceException; ;
	List<GJob> getNoAvaliable(Date end) throws ServiceException; 
	List<GJob> getEndDate(Date end,Long uid) throws ServiceException; 
	public void changestatus(GJob gjob) throws ServiceException, ParseException;
}
