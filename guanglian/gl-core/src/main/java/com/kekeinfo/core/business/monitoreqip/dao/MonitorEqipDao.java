package com.kekeinfo.core.business.monitoreqip.dao;

import java.util.List;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.monitoreqip.model.MonitorEqip;

public interface MonitorEqipDao extends KekeinfoEntityDao<Long, MonitorEqip> {
	List<MonitorEqip> getByMid(long mid);
}
