package com.kekeinfo.core.business.mbase.dao;
import java.util.List;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.monitor.model.MPointEnumType;
import com.kekeinfo.core.business.monitor.surface.model.MbasePoint;


public interface MbaseDao<E> extends KekeinfoEntityDao<Long, MbasePoint<E>> {
	 MbasePoint<E> getById(long id,MPointEnumType type);
	 MbasePoint<E> getByNO(String name,MPointEnumType type,long id);
	 public List<MbasePoint<E>> getByMid(long id,MPointEnumType type);
}
