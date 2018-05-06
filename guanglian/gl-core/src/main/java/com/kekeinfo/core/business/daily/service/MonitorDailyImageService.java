package com.kekeinfo.core.business.daily.service;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;

import java.util.List;

import com.kekeinfo.core.business.daily.model.MonitorDailyImage;

public interface MonitorDailyImageService extends KekeinfoEntityService<Long, MonitorDailyImage> {
	List<MonitorDailyImage> getByMid(Long gid) throws ServiceException;
}
