package com.kekeinfo.core.business.monitor.surface.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.mbase.service.MbaseServiceImpl;
import com.kekeinfo.core.business.monitor.surface.dao.DisplacementDao;
import com.kekeinfo.core.business.monitor.surface.model.Displacement;

@Service("displacementService")
public class DisplacementServiceImpl extends MbaseServiceImpl<Displacement> implements DisplacementService {
	@SuppressWarnings("unused")
	private DisplacementDao displacementDao;

	@Autowired
	public DisplacementServiceImpl(DisplacementDao displacementDao) {
		super(displacementDao);
		this.displacementDao = displacementDao;
	}
}
