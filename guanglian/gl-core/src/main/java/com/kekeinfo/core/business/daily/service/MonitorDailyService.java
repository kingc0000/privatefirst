package com.kekeinfo.core.business.daily.service;

import java.util.Date;

import com.kekeinfo.core.business.daily.model.MonitorDaily;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;

public interface MonitorDailyService extends KekeinfoEntityService<Long, MonitorDaily> {
	
	/**
	 * 保存日志信息，同时根据前端传过来的日志图片id删除日志图片
	 * @param daily
	 * @param dels
	 * @throws ServiceException
	 */
	void save(MonitorDaily monitorDaily, String dels) throws ServiceException;
	
	MonitorDaily getBydate(Date date,long mid) throws ServiceException;
	MonitorDaily withImg(Long mdid) throws ServiceException;
}
