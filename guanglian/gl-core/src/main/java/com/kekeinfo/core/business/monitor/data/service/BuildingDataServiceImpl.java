package com.kekeinfo.core.business.monitor.data.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.monitor.data.dao.BuildingDataDao;
import com.kekeinfo.core.business.monitor.data.model.BuildingData;
import com.kekeinfo.core.business.monitor.data.model.MDbasePoint;
import com.kekeinfo.core.business.monitor.model.MPointEnumType;
import com.kekeinfo.core.business.monitor.surface.model.Building;
import com.kekeinfo.core.business.monitor.surface.model.MbasePoint;
import com.kekeinfo.core.business.monitor.surface.service.BuildingService;

@Service("buildingdataService")
public class BuildingDataServiceImpl extends MdbaseServiceImpl<BuildingData> implements BuildingDataService {
	@Autowired
	private BuildingService buildingService;
	private BuildingDataDao buildingdataDao;

	@Autowired
	public BuildingDataServiceImpl(BuildingDataDao buildingdataDao) {
		super(buildingdataDao);
		this.buildingdataDao = buildingdataDao;
	}

	@Transactional
	public void saveOrUpdate(BuildingData buildingData) throws ServiceException {
		MbasePoint<Building> building = buildingService.getById(buildingData.getSpoint().getId());
		// 每个时间点只能有一条数据
		MDbasePoint<BuildingData> dbdata = this.buildingdataDao.getByDate(buildingData.getCalibration(),
				building.getId(), MPointEnumType.Building);
		if (dbdata != null) {
			BuildingData dbbdata = (BuildingData) dbdata;
			buildingData.setId(dbbdata.getId());
		}
		if (buildingData.getId() == null) {
			super.save(buildingData);
		} else {
			super.update(buildingData);
		}
		MDbasePoint<BuildingData> next = super.getNext(buildingData.getCalibration(), MPointEnumType.Building,buildingData.getSpoint().getId());
		if (next != null) {
			next.setInitHeight(buildingData.getCurtHeight());
			super.update(next);
		} 
	
	}

	@Override
	@Transactional
	public void deleteAndUpdate(MDbasePoint<BuildingData> mdbase) throws ServiceException{
		super.delete(mdbase);
		BuildingData bdata = (BuildingData)mdbase;
		MDbasePoint<BuildingData> tBuildingData = buildingdataDao.getNext(mdbase.getCalibration(), MPointEnumType.Building,bdata.getSpoint().getId());
		MDbasePoint<BuildingData> bBuildingData = buildingdataDao.getLast(mdbase.getCalibration(), MPointEnumType.Building,bdata.getSpoint().getId());
		//有上级数据
		if(tBuildingData!=null) {
			//上下级数据同时存在
			if(bBuildingData!=null) {
				tBuildingData.setInitHeight(bBuildingData.getCurtHeight());
				this.saveOrUpdate(tBuildingData);
			//只有上级数据存在+
			}else {
				tBuildingData.setInitHeight(bdata.getSpoint().getInitHeight());
				this.saveOrUpdate(tBuildingData);
			}
		}
	}


}
