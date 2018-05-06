package com.kekeinfo.core.business.daily.service;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;

import java.util.Date;

import com.kekeinfo.core.business.daily.model.GuardDaily;

public interface GuardDailyService extends KekeinfoEntityService<Long, GuardDaily> {
	public void saveUpdate(GuardDaily gd,String dels) throws ServiceException;
	GuardDaily wihtimg(Long gid) throws ServiceException;
	
	public void deletewithimg(Long gid) throws ServiceException;
	
	GuardDaily byDate(Date date,Long gid) throws ServiceException;
}
