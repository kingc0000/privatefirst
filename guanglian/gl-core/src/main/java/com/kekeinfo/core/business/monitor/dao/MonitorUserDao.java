package com.kekeinfo.core.business.monitor.dao;

import java.util.List;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.monitor.model.MonitorUser;

public interface MonitorUserDao extends KekeinfoEntityDao<Long, MonitorUser> {
	List<Object[]> getPinYin();
}
