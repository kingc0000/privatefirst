package com.kekeinfo.core.business.gonproblem.service;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.gonproblem.model.GonProblem;

public interface GonProblemService extends KekeinfoEntityService<Long, GonProblem> {
	public void saveUpdate(GonProblem gp,String dels) throws ServiceException;
	public void deletewithattach(Long gid) throws ServiceException;
	public GonProblem withAttach(Long gid) throws ServiceException;
}
