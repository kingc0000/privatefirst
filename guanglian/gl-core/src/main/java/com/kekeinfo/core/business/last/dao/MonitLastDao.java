package com.kekeinfo.core.business.last.dao;

import java.util.List;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.last.model.MonitLast;

public interface MonitLastDao extends KekeinfoEntityDao<Long, MonitLast> {
	List<MonitLast> getByUserID(long userid);
}
