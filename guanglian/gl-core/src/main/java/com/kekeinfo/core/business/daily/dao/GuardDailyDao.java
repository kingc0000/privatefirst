package com.kekeinfo.core.business.daily.dao;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;

import java.util.Date;

import com.kekeinfo.core.business.daily.model.GuardDaily;

public interface GuardDailyDao extends KekeinfoEntityDao<Long, GuardDaily> {
	GuardDaily wihtimg(Long gid);
	
	GuardDaily byDate(Date date,Long gid);
}
