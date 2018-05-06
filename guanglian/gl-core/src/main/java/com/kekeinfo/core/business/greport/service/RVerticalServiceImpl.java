package com.kekeinfo.core.business.greport.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.greport.dao.RVerticalDao;
import com.kekeinfo.core.business.greport.model.RVerticalDisData;

@Service("rverticalService")
public class RVerticalServiceImpl extends GMBaseDataServiceImpl<RVerticalDisData> implements RVerticalService {
	@SuppressWarnings("unused")
	private RVerticalDao rverticalDao;

	@Autowired
	public RVerticalServiceImpl(RVerticalDao rverticalDao) {
		super(rverticalDao);
		this.rverticalDao = rverticalDao;
	}
}
