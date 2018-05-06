package com.kekeinfo.core.business.guard.point.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.gbase.service.GbaseServiceImpl;
import com.kekeinfo.core.business.guard.point.dao.HshiftDao;
import com.kekeinfo.core.business.guard.point.model.Hshift;

@Service("hshiftService")
public class HshiftServiceImpl extends GbaseServiceImpl<Hshift> implements HshiftService {
	@SuppressWarnings("unused")
	private HshiftDao hshiftDao;

	@Autowired
	public HshiftServiceImpl(HshiftDao hshiftDao) {
		super(hshiftDao);
		this.hshiftDao = hshiftDao;
	}
}
