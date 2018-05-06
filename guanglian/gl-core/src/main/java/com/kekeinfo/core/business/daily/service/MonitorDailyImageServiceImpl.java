package com.kekeinfo.core.business.daily.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.daily.model.MonitorDailyImage;
import com.kekeinfo.core.business.daily.dao.MonitorDailyImageDao;

@Service("monitordailyimageService")
public class MonitorDailyImageServiceImpl extends KekeinfoEntityServiceImpl<Long, MonitorDailyImage>
		implements MonitorDailyImageService {
	private MonitorDailyImageDao monitordailyimageDao;

	@Autowired
	public MonitorDailyImageServiceImpl(MonitorDailyImageDao monitordailyimageDao) {
		super(monitordailyimageDao);
		this.monitordailyimageDao = monitordailyimageDao;
	}

	@Override
	public List<MonitorDailyImage> getByMid(Long gid) throws ServiceException {
		// TODO Auto-generated method stub
		return monitordailyimageDao.getByMid(gid);
	}
}
