package com.kekeinfo.core.business.monitor.surface.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.mbase.service.MbaseServiceImpl;
import com.kekeinfo.core.business.monitor.surface.dao.SupAxialDao;
import com.kekeinfo.core.business.monitor.surface.model.SupAxial;

@Service("supaxialService")
public class SupAxialServiceImpl extends MbaseServiceImpl<SupAxial> implements SupAxialService {
	@SuppressWarnings("unused")
	private SupAxialDao supaxialDao;

	@Autowired
	public SupAxialServiceImpl(SupAxialDao supaxialDao) {
		super(supaxialDao);
		this.supaxialDao = supaxialDao;
	}
}
