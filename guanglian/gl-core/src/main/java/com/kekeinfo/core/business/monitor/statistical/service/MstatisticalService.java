package com.kekeinfo.core.business.monitor.statistical.service;

import java.util.Date;
import java.util.List;

import com.kekeinfo.core.business.common.model.BasedataType;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.monitor.statistical.model.Mstatistical;

public interface MstatisticalService extends KekeinfoEntityService<Long, Mstatistical> {
	
	public void saveUpdate(Mstatistical mstatistical,List<BasedataType> blist) throws ServiceException;
	
	public Mstatistical getByDate(Date date) throws ServiceException;
	
}
