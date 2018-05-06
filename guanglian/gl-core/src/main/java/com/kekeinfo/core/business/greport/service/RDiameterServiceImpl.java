package com.kekeinfo.core.business.greport.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.greport.dao.RDiameterDao;
import com.kekeinfo.core.business.greport.model.RDiameterConvertData;

@Service("rdiameterService")
public class RDiameterServiceImpl extends GMBaseDataServiceImpl<RDiameterConvertData> implements RDiameterService {
	@SuppressWarnings("unused")
	private RDiameterDao rdiameterDao;

	@Autowired
	public RDiameterServiceImpl(RDiameterDao rdiameterDao) {
		super(rdiameterDao);
		this.rdiameterDao = rdiameterDao;
	}
}
