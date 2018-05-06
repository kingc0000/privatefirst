package com.kekeinfo.core.business.mreport.dao;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.mreport.model.RMreport;

public interface RMreportDao extends KekeinfoEntityDao<Long, RMreport> {
	RMreport getByRid(Long rid);
}
