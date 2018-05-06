package com.kekeinfo.core.business.xreport.dao;

import java.util.List;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.xreport.model.ROblique;

public interface RObliqueDao extends KekeinfoEntityDao<Long, ROblique> {
	List<ROblique> getByXid(Long xid);
}
