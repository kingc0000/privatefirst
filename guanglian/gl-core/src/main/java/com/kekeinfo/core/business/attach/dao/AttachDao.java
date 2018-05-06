package com.kekeinfo.core.business.attach.dao;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.attach.model.Attach;

public interface AttachDao extends KekeinfoEntityDao<Long, Attach> {
	Attach getByName(String name);
}
