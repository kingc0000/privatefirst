package com.kekeinfo.core.business.monitor.surface.dao;

import org.springframework.stereotype.Repository;
import com.kekeinfo.core.business.mbase.dao.MbaseDaoImpl;
import com.kekeinfo.core.business.monitor.surface.model.Displacement;

@Repository("displacementDao")
public class DisplacementDaoImpl extends MbaseDaoImpl<Displacement> implements DisplacementDao {
	public DisplacementDaoImpl() {
		super();
	}
}