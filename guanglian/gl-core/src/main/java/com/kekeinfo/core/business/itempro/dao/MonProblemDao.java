package com.kekeinfo.core.business.itempro.dao;


import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.monproblem.model.MonProblem;

public interface MonProblemDao extends KekeinfoEntityDao<Long, MonProblem>{

	MonProblem withAttach(Long gid);
	
}
