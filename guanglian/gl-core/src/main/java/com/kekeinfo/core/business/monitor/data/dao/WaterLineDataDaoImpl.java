package com.kekeinfo.core.business.monitor.data.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.monitor.data.model.WaterLineData;

@Repository("waterlinedataDao")
public class WaterLineDataDaoImpl extends MdbaseDaoImpl<WaterLineData> implements WaterLineDataDao {
	public WaterLineDataDaoImpl() {
		super();
	}

}