package com.kekeinfo.core.business.greport.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.greport.model.RVerticalDisData;

@Repository("rverticalDao")
public class RVerticalDaoImpl extends GMBaseDataDaoImpl<RVerticalDisData> implements RVerticalDao {
	public RVerticalDaoImpl() {
		super();
	}
}