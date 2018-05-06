package com.kekeinfo.core.business.daily.service;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;

import java.util.List;

import com.kekeinfo.core.business.daily.model.GuardDailyImage;

public interface GuardDailyImageService extends KekeinfoEntityService<Long, GuardDailyImage> {
	List<GuardDailyImage> getByGid(Long gid) throws ServiceException;
}
