package com.kekeinfo.core.business.gbase.dao;

import java.math.BigDecimal;
import java.util.Date;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.guard.data.model.GDbasePoint;
import com.kekeinfo.core.business.guard.model.GPointEnumType;

public interface GDbaseDao<E> extends KekeinfoEntityDao<Long, GDbasePoint<E>>{

	public GDbasePoint<E> getByDate(Date date,long sid,GPointEnumType type);
	public GDbasePoint<E> getLast(Date date,GPointEnumType type,long sid);
	public GDbasePoint<E> getNext(Date date,GPointEnumType type,long sid);
	public GDbasePoint<E>  getMax(Date date,GPointEnumType type,String mids);
	public GDbasePoint<E> getByIdWithPoint(long id,GPointEnumType type);
	public GDbasePoint<E> getEqualsHeightData(BigDecimal initHeight,long sid,GPointEnumType type);
	
}
