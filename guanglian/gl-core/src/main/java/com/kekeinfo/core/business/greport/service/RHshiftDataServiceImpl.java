package com.kekeinfo.core.business.greport.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.greport.dao.RHshiftDataDao;
import com.kekeinfo.core.business.greport.model.RHshiftData;

@Service("rhshiftdataService")
public class RHshiftDataServiceImpl extends GMBaseDataServiceImpl<RHshiftData> implements RHshiftDataService {
	@SuppressWarnings("unused")
	private RHshiftDataDao rhshiftdataDao;

	@Autowired
	public RHshiftDataServiceImpl(RHshiftDataDao rhshiftdataDao) {
		super(rhshiftdataDao);
		this.rhshiftdataDao = rhshiftdataDao;
	}
}
