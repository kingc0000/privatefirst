package com.kekeinfo.core.business.xreport.dao;

import java.util.List;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.xreport.model.RObliqueData;

public interface RObliqueDataDao extends KekeinfoEntityDao<Long, RObliqueData> {
	List<RObliqueData> getByRid(Long rid);
}
