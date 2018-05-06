package com.kekeinfo.core.business.monitor.statistical.dao;

import java.util.Date;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.monitor.statistical.model.Mstatistical;

public interface MstatisticalDao extends KekeinfoEntityDao<Long, Mstatistical> {
	
	Mstatistical getByDate(Date date);
}
