package com.kekeinfo.core.business.zone.dao;

import java.util.List;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.zone.model.Zone;


public interface ZoneDao extends KekeinfoEntityDao<Long,Zone> {	
	
	List<Object[]> getPinYin() throws ServiceException;
	
	List<Zone> list();
	
}
