package com.kekeinfo.core.business.monitor.surface.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kekeinfo.core.business.mbase.service.MbaseServiceImpl;
import com.kekeinfo.core.business.monitor.surface.model.UpRight;
import com.kekeinfo.core.business.monitor.surface.dao.UpRightDao;

@Service("uprightService")
public class UpRightServiceImpl extends MbaseServiceImpl<UpRight> implements UpRightService {
	@SuppressWarnings("unused")
	private UpRightDao uprightDao;

	@Autowired
	public UpRightServiceImpl(UpRightDao uprightDao) {
		super(uprightDao);
		this.uprightDao = uprightDao;
	}
}