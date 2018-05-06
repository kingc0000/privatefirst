package com.kekeinfo.core.business.daily.service;


import java.util.Date;

import com.kekeinfo.core.business.daily.model.Daily;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;


public interface DailyService extends KekeinfoEntityService<Long, Daily> {

	/**
	 * 保存日志信息，同时根据前端传过来的日志图片id删除日志图片
	 * @param daily
	 * @param dels
	 * @throws ServiceException
	 */
	void save(Daily daily, String dels) throws ServiceException;
	Daily getByDate(Long pid, Date date1) throws ServiceException;

}
