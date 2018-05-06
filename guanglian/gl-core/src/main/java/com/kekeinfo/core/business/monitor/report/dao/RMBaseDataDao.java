package com.kekeinfo.core.business.monitor.report.dao;
import java.util.List;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.monitor.model.MPointEnumType;
import com.kekeinfo.core.business.mreport.model.RMBaseData;


public interface RMBaseDataDao<E> extends KekeinfoEntityDao<Long, RMBaseData<E>> {
	public List<RMBaseData<E>> getByRid(long rid,MPointEnumType type);
}
