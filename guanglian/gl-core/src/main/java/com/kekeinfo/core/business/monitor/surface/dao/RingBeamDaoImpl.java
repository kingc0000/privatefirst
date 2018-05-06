package com.kekeinfo.core.business.monitor.surface.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.mbase.dao.MbaseDaoImpl;
import com.kekeinfo.core.business.monitor.surface.model.RingBeam;

@Repository("ringbeamDao")
public class RingBeamDaoImpl extends MbaseDaoImpl<RingBeam> implements RingBeamDao {
	public RingBeamDaoImpl() {
		super();
	}

}