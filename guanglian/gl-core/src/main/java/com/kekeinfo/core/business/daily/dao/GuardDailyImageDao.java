package com.kekeinfo.core.business.daily.dao;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;

import java.util.List;

import com.kekeinfo.core.business.daily.model.GuardDailyImage;

public interface GuardDailyImageDao extends KekeinfoEntityDao<Long, GuardDailyImage> {
	List<GuardDailyImage> getByGid(Long gid);
}
