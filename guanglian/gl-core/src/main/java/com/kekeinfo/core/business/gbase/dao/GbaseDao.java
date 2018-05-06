package com.kekeinfo.core.business.gbase.dao;

import java.util.List;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.guard.model.GPointEnumType;
import com.kekeinfo.core.business.guard.point.model.GbasePoint;

public interface GbaseDao<E> extends KekeinfoEntityDao<Long, GbasePoint<E>> {
	
	 GbasePoint<E> getById(long id,GPointEnumType type);
	 public List<GbasePoint<E>> getByMid(long id,GPointEnumType type);
	 GbasePoint<E> getByNO(String name,GPointEnumType type,long id);

}
