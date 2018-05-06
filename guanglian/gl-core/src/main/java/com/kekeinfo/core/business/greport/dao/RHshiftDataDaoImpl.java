package com.kekeinfo.core.business.greport.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.greport.model.RHshiftData;

@Repository("rhshiftdataDao")
public class RHshiftDataDaoImpl extends GMBaseDataDaoImpl<RHshiftData> implements RHshiftDataDao {
	public RHshiftDataDaoImpl() {
		super();
	}
}