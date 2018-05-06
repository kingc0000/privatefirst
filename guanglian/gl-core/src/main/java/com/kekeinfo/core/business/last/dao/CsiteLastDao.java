package com.kekeinfo.core.business.last.dao;

import java.util.List;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.last.model.CsiteLast;

public interface CsiteLastDao extends KekeinfoEntityDao<Long, CsiteLast> {
	List<CsiteLast> getByUserID(long userid);
}
