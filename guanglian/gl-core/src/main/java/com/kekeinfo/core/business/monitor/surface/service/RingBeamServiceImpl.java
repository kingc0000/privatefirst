package com.kekeinfo.core.business.monitor.surface.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kekeinfo.core.business.mbase.service.MbaseServiceImpl;
import com.kekeinfo.core.business.monitor.surface.model.RingBeam;
import com.kekeinfo.core.business.monitor.surface.dao.RingBeamDao;

@Service("ringbeamService")
public class RingBeamServiceImpl extends MbaseServiceImpl<RingBeam> implements RingBeamService {
	@SuppressWarnings("unused")
	private RingBeamDao ringbeamDao;

	@Autowired
	public RingBeamServiceImpl(RingBeamDao ringbeamDao) {
		super(ringbeamDao);
		this.ringbeamDao = ringbeamDao;
	}
}
