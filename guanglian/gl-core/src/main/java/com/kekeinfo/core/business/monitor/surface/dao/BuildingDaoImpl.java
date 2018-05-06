package com.kekeinfo.core.business.monitor.surface.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.mbase.dao.MbaseDaoImpl;
import com.kekeinfo.core.business.monitor.surface.model.Building;

@Repository("buildingDao")
public class BuildingDaoImpl extends MbaseDaoImpl<Building> implements BuildingDao {
	public BuildingDaoImpl() {
		super();
	}
}