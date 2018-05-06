package com.kekeinfo.core.business.monitor.substrate.dao;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.monitor.substrate.model.SubStrate;

public interface SubStrateDao extends KekeinfoEntityDao<Long, SubStrate> {
	SubStrate withAttach(Long gid);
	
}
