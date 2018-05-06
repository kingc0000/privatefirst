package com.kekeinfo.core.business.monitor.oblique.service;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.monitor.oblique.model.ObliqueData;


public interface ObliqueDataService extends KekeinfoEntityService<Long, ObliqueData> {
	
    public void saveOrUpdate(ObliqueData obliqueData) throws ServiceException;
	
	public void deleteAndUpdate(ObliqueData obliqueData) throws ServiceException;
	
	public ObliqueData getByDate(Date date,long sid) throws ServiceException;
	
	public ObliqueData getLast(Date date,long sid) throws ServiceException;
	
	public ObliqueData getNext(Date date,long sid) throws ServiceException;
	
	public ObliqueData  getMax(Date date,String mids) throws ServiceException;
	
	public ObliqueData getByIdWithPoint(long id) throws ServiceException;
	
	public ObliqueData getEqualsHeightData(BigDecimal initHeight,long sid) throws ServiceException;
	
	List<ObliqueData> getByDid(List<Long> dids,Date date) throws ServiceException;
	
}

