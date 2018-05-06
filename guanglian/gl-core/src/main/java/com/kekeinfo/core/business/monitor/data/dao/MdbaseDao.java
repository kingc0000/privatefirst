package com.kekeinfo.core.business.monitor.data.dao;
import java.math.BigDecimal;
import java.util.Date;


import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.monitor.data.model.MDbasePoint;
import com.kekeinfo.core.business.monitor.model.MPointEnumType;


public interface MdbaseDao<E> extends KekeinfoEntityDao<Long, MDbasePoint<E>> {
	public MDbasePoint<E> getByDate(Date date,long sid,MPointEnumType type);
	public MDbasePoint<E> getLast(Date date,MPointEnumType type,long sid);
	public MDbasePoint<E> getNext(Date date,MPointEnumType type,long sid);
	public MDbasePoint<E>  getMax(Date date,MPointEnumType type,String mids);
	public MDbasePoint<E> getByIdWithPoint(long id,MPointEnumType type);
	public MDbasePoint<E> getEqualsHeightData(BigDecimal initHeight,long sid,MPointEnumType type);
}
