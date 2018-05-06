package com.kekeinfo.core.business.daily.dao;

import java.util.List;

import com.kekeinfo.core.business.daily.model.MonitorDailyImage;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;

public interface MonitorDailyImageDao extends KekeinfoEntityDao<Long, MonitorDailyImage> {
	List<MonitorDailyImage> getByMid(Long gid);
}
