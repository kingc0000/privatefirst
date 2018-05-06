package com.kekeinfo.core.business.monitor.data.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.monitor.data.model.SupAxialData;

@Repository("supaxialdataDao")
public class SupAxialDataDaoImpl extends MdbaseDaoImpl<SupAxialData> implements SupAxialDataDao {
	public SupAxialDataDaoImpl() {
		super();
	}

	
}