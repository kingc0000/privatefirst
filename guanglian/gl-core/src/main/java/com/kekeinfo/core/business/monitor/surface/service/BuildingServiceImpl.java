package com.kekeinfo.core.business.monitor.surface.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.mbase.service.MbaseServiceImpl;
import com.kekeinfo.core.business.monitor.surface.model.Building;
import com.kekeinfo.core.business.monitor.surface.dao.BuildingDao;

@Service("buildingService")
public class BuildingServiceImpl extends MbaseServiceImpl<Building> implements BuildingService {
	@SuppressWarnings("unused")
	private BuildingDao buildingDao;

	@Autowired
	public BuildingServiceImpl(BuildingDao buildingDao) {
		super(buildingDao);
		this.buildingDao = buildingDao;
	}
}
