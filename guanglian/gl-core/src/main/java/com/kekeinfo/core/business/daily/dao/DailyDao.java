package com.kekeinfo.core.business.daily.dao;

import java.util.Date;

import com.kekeinfo.core.business.daily.model.Daily;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.generic.exception.ServiceException;


public interface DailyDao extends KekeinfoEntityDao<Long,Daily> {	
	Daily getByDate(Long pid, Date date1) throws ServiceException;

}
