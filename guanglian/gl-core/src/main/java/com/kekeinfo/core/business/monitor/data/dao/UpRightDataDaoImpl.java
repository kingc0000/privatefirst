package com.kekeinfo.core.business.monitor.data.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.monitor.data.model.UpRightData;

@Repository("uprightdataDao")
public class UpRightDataDaoImpl extends MdbaseDaoImpl<UpRightData> implements UpRightDataDao {
	public UpRightDataDaoImpl() {
		super();
	}

}