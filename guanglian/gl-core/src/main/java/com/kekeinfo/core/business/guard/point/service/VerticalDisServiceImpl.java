package com.kekeinfo.core.business.guard.point.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.gbase.service.GbaseServiceImpl;
import com.kekeinfo.core.business.guard.point.dao.VerticalDisDao;
import com.kekeinfo.core.business.guard.point.model.VerticalDis;

@Service("verticaldisService")
public class VerticalDisServiceImpl extends GbaseServiceImpl<VerticalDis> implements VerticalDisService {
	@SuppressWarnings("unused")
	private VerticalDisDao verticaldisDao;

	@Autowired
	public VerticalDisServiceImpl(VerticalDisDao verticaldisDao) {
		super(verticaldisDao);
		this.verticaldisDao = verticaldisDao;
	}

}
