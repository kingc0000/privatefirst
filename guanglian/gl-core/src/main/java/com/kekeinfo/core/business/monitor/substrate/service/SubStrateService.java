package com.kekeinfo.core.business.monitor.substrate.service;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.monitor.substrate.model.SubStrate;

public interface SubStrateService extends KekeinfoEntityService<Long, SubStrate> {
	
	public void saveUpdate(SubStrate gp,String dels) throws ServiceException;
	public void deletewithattach(Long gid) throws ServiceException;
	public SubStrate withAttach(Long gid) throws ServiceException;
   
	
}
