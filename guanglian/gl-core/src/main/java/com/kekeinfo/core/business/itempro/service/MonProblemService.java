package com.kekeinfo.core.business.itempro.service;


import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.monproblem.model.MonProblem;

public interface MonProblemService extends KekeinfoEntityService<Long, MonProblem>{

	public void saveUpdate(MonProblem gp,String dels) throws ServiceException;
	public void deletewithattach(Long gid) throws ServiceException;
	public MonProblem withAttach(Long gid) throws ServiceException;

    
		
}
