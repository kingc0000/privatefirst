package com.kekeinfo.core.business.gonproblem.dao;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.gonproblem.model.GonProblem;

public interface GonProblemDao extends KekeinfoEntityDao<Long, GonProblem> {
	GonProblem withAttach(Long gid);
}
