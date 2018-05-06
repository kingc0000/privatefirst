package com.kekeinfo.core.business.monitor.oblique.dao;

import java.util.List;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.monitor.oblique.model.Oblique;

public interface ObliqueDao extends KekeinfoEntityDao<Long, Oblique> {
	List<Oblique> getByMid(Long mid);
}
