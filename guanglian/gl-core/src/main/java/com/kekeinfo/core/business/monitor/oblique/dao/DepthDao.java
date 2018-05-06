package com.kekeinfo.core.business.monitor.oblique.dao;

import java.math.BigDecimal;
import java.util.List;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.monitor.oblique.model.Depth;

public interface DepthDao extends KekeinfoEntityDao<Long, Depth> {
	
	public Depth findByDeep(BigDecimal deep,Long oid);
	List<Depth> getByOid(Long oid);
	
}
