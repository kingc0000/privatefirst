package com.kekeinfo.core.business.monitor.data.dao;


import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.monitor.data.model.BuildingData;

@Repository("buildingdataDao")
public class BuildingDataDaoImpl extends MdbaseDaoImpl<BuildingData> implements BuildingDataDao {
	public BuildingDataDaoImpl() {
		super();
	}

}