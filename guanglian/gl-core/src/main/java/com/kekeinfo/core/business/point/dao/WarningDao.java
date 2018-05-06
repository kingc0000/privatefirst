package com.kekeinfo.core.business.point.dao;

import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.generic.model.PointEnumType;
import com.kekeinfo.core.business.monitordata.model.WarningData;
import com.kekeinfo.core.business.point.model.Basepoint;
import com.kekeinfo.core.business.point.model.BasepointInfo;
import com.kekeinfo.core.business.point.model.BasepointLink;


public interface WarningDao extends KekeinfoEntityDao<Long, WarningData<Basepoint<BasepointLink<?>, BasepointInfo<?>>>> {

	@SuppressWarnings("rawtypes")
	Entitites<WarningData> getListByCid(Long cid, PointEnumType type, Integer warningType, String search, Integer limit, Integer offset);
	
	@SuppressWarnings("rawtypes")
	WarningData findLastWarning(Long pid, PointEnumType type, Integer warningType);

	void deleteById(Long id, PointEnumType pointType);
}
