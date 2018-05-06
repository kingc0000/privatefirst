package com.kekeinfo.web.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.utils.CacheUtils;

@Component
public class WebApplicationCacheUtils {
	
	@Autowired
	private CacheUtils cache;
	
	public Object getFromCache(String key) throws ServiceException {
		return cache.getFromCache(key);
	}
	
	/**
	 * Constants.DEPARTMENTS 所有部门（包括项目project+摄像头） List<Department>
	 * Constants.WATER_CSITE 所有地下水项目 List<UnderWater>
	 * Constants.PROEJCT_MONITOR 所有监测项目 List<MonitorEntity>
	 * Constants.PROEJCT_GUARD 所有监护项目 List<GuardEntity>
	 * Constants.DEPARTMENT_WATER 地下水部门
	 * @param key
	 * @param object
	 * @throws ServiceException
	 */
	public void putInCache(String key, Object object) throws ServiceException {
		cache.putInCache(object, key);
	}

}
