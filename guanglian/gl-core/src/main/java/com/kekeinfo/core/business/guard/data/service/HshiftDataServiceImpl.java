package com.kekeinfo.core.business.guard.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.gbase.service.GDbaseServiceImpl;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.guard.data.dao.HshiftDataDao;
import com.kekeinfo.core.business.guard.data.model.GDbasePoint;
import com.kekeinfo.core.business.guard.data.model.HshiftData;
import com.kekeinfo.core.business.guard.model.GPointEnumType;
import com.kekeinfo.core.business.guard.point.model.GbasePoint;
import com.kekeinfo.core.business.guard.point.model.Hshift;
import com.kekeinfo.core.business.guard.point.service.HshiftService;

@Service("hshiftdataService")
public class HshiftDataServiceImpl extends GDbaseServiceImpl<HshiftData> implements HshiftDataService {
	private HshiftDataDao hshiftdataDao;

	@Autowired HshiftService hshiftService;
	
	@Autowired
	public HshiftDataServiceImpl(HshiftDataDao hshiftdataDao) {
		super(hshiftdataDao);
		this.hshiftdataDao = hshiftdataDao;
	}

	@Override
	public void deleteAndUpdate(GDbasePoint<HshiftData> mdbase) throws ServiceException {
		super.delete(mdbase);
		HshiftData bdata = (HshiftData)mdbase;
		GDbasePoint<HshiftData> tBuildingData = hshiftdataDao.getNext(mdbase.getCalibration(), GPointEnumType.Hshift,bdata.getSpoint().getId());
		GDbasePoint<HshiftData> bBuildingData = hshiftdataDao.getLast(mdbase.getCalibration(), GPointEnumType.Hshift,bdata.getSpoint().getId());
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
	public void saveOrUpdate(HshiftData hshiftData) throws ServiceException {
		GbasePoint<Hshift> building = hshiftService.getById(hshiftData.getSpoint().getId());
		// 每个时间点只能有一条数据
		GDbasePoint<HshiftData> dbdata = this.hshiftdataDao.getByDate(hshiftData.getCalibration(),
				building.getId(), GPointEnumType.Hshift);
		if (dbdata != null) {
			HshiftData dbbdata = (HshiftData) dbdata;
			hshiftData.setId(dbbdata.getId());
		}
		if (hshiftData.getId() == null) {
			super.save(hshiftData);
		} else {
			super.update(hshiftData);
		}
		GDbasePoint<HshiftData> next = super.getNext(hshiftData.getCalibration(), GPointEnumType.Hshift,hshiftData.getSpoint().getId());
		if (next != null) {
			next.setInitHeight(hshiftData.getCurtHeight());
			super.update(next);
		}
	}
}
