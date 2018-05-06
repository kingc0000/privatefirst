package com.kekeinfo.core.business.greport.dao;

import java.util.List;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.greport.model.GMBaseData;
import com.kekeinfo.core.business.guard.model.GPointEnumType;

public interface GMBaseDataDao<E> extends KekeinfoEntityDao<Long, GMBaseData<E>> {
	public List<GMBaseData<E>> getByGid(long rid,GPointEnumType type);
}
