package com.kekeinfo.core.business.zone.service;


import java.util.List;
import java.util.Map;

import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.zone.model.Zone;
import com.kekeinfo.core.business.generic.exception.ServiceException;


public interface ZoneService extends KekeinfoEntityService<Long, Zone> {
	
	Zone getByCode(String code);

	List<Zone> getAllZone()throws ServiceException;
	
	Map<String, Zone> getZones() throws ServiceException;
	
	List<Object[]> getPinYin() throws ServiceException;


}
