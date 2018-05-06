package com.kekeinfo.core.business.daily.dao;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;

import java.util.Date;

import com.kekeinfo.core.business.daily.model.MonitorDaily;

public interface MonitorDailyDao extends KekeinfoEntityDao<Long, MonitorDaily> {
	MonitorDaily getBydate(Date date,long mid);
	
	MonitorDaily withImg(Long mdid);
}

