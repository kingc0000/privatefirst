package com.kekeinfo.core.business.guard.data.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.gbase.service.GDbaseServiceImpl;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.guard.data.dao.VerticalDisDataDao;
import com.kekeinfo.core.business.guard.data.model.GDbasePoint;
import com.kekeinfo.core.business.guard.data.model.VerticalDisData;
import com.kekeinfo.core.business.guard.model.GPointEnumType;
import com.kekeinfo.core.business.guard.point.model.GbasePoint;
import com.kekeinfo.core.business.guard.point.model.VerticalDis;
import com.kekeinfo.core.business.guard.point.service.VerticalDisService;

@Service("verticaldisdataService")
public class VerticalDisDataServiceImpl extends GDbaseServiceImpl<VerticalDisData> implements VerticalDisDataService {
	
	private VerticalDisDataDao verticaldisdataDao;
	
	@Autowired
	private VerticalDisService verticaldisService;
	
	@Autowired
	public VerticalDisDataServiceImpl(VerticalDisDataDao verticaldisdataDao) {
		super(verticaldisdataDao);
		this.verticaldisdataDao = verticaldisdataDao;
	}

	@Override
	public void deleteAndUpdate(GDbasePoint<VerticalDisData> mdbase) throws ServiceException {
		super.delete(mdbase);
		VerticalDisData bdata = (VerticalDisData)mdbase;
		GDbasePoint<VerticalDisData> tBuildingData = verticaldisdataDao.getNext(mdbase.getCalibration(), GPointEnumType.VerticalDis,bdata.getSpoint().getId());
		GDbasePoint<VerticalDisData> bBuildingData = verticaldisdataDao.getLast(mdbase.getCalibration(), GPointEnumType.VerticalDis,bdata.getSpoint().getId());
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

	@Override
	public void saveOrUpdate(VerticalDisData verticalDisData) throws ServiceException {
		GbasePoint<VerticalDis> building = verticaldisService.getById(verticalDisData.getSpoint().getId());
		// 每个时间点只能有一条数据
		GDbasePoint<VerticalDisData> dbdata = this.verticaldisdataDao.getByDate(verticalDisData.getCalibration(),
				building.getId(), GPointEnumType.VerticalDis);
		if (dbdata != null) {
			VerticalDisData dbbdata = (VerticalDisData) dbdata;
			verticalDisData.setId(dbbdata.getId());
		}
		if (verticalDisData.getId() == null) {
			super.save(verticalDisData);
		} else {
			super.update(verticalDisData);
		}
		GDbasePoint<VerticalDisData> next = super.getNext(verticalDisData.getCalibration(), GPointEnumType.VerticalDis,verticalDisData.getSpoint().getId());
		if (next != null) {
			next.setInitHeight(verticalDisData.getCurtHeight());
			super.update(next);
		}
	}

	
	
	
	
	
	
}
