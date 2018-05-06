package com.kekeinfo.core.business.greport.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.greport.model.RDiameterConvertData;

@Repository("rdiameterDao")
public class RDiameterDaoImpl extends GMBaseDataDaoImpl<RDiameterConvertData> implements RDiameterDao {
	public RDiameterDaoImpl() {
		super();
	}	
}