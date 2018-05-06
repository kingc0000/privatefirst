package com.kekeinfo.core.business.monitor.dao;

import java.util.List;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.monitor.model.MBaseType;

public interface MBaseTypeDao extends KekeinfoEntityDao<Long, MBaseType> {
	List<MBaseType> getByMid(Long mid);
}
