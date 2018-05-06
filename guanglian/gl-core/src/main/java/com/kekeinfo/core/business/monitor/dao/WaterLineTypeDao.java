package com.kekeinfo.core.business.monitor.dao;

import java.util.List;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.monitor.model.WaterLineType;

public interface WaterLineTypeDao extends KekeinfoEntityDao<Long, WaterLineType> {
	List<WaterLineType> getByMid(Long mid);
	List<WaterLineType> getByTid(Long tid);
}
