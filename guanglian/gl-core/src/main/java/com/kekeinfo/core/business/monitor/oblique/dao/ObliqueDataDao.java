package com.kekeinfo.core.business.monitor.oblique.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.monitor.oblique.model.ObliqueData;

public interface ObliqueDataDao extends KekeinfoEntityDao<Long, ObliqueData> {
	
	public ObliqueData getByDate(Date date,long sid);
	public ObliqueData getLast(Date date,long sid);
	public ObliqueData getNext(Date date,long sid);
	public ObliqueData  getMax(Date date,String mids);
	public ObliqueData getByIdWithPoint(long id);
	public ObliqueData getEqualsHeightData(BigDecimal initHeight,long sid);
	
	List<ObliqueData> getByDid(List<Long> dids,Date date);
	
}
