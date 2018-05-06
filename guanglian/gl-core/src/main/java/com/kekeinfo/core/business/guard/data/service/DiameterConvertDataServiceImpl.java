package com.kekeinfo.core.business.guard.data.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.gbase.service.GDbaseServiceImpl;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.guard.data.dao.DiameterConvertDataDao;
import com.kekeinfo.core.business.guard.data.model.DiameterConvertData;
import com.kekeinfo.core.business.guard.data.model.GDbasePoint;
import com.kekeinfo.core.business.guard.model.GPointEnumType;
import com.kekeinfo.core.business.guard.point.model.DiameterConvert;
import com.kekeinfo.core.business.guard.point.model.GbasePoint;
import com.kekeinfo.core.business.guard.point.service.DiameterConvertService;

@Service("diameterconvertdataService")
public class DiameterConvertDataServiceImpl extends GDbaseServiceImpl<DiameterConvertData> implements DiameterConvertDataService {
	private DiameterConvertDataDao diameterconvertdataDao;
	
	@Autowired
	private DiameterConvertService diameterconvertService;

	@Autowired
	public DiameterConvertDataServiceImpl(DiameterConvertDataDao diameterconvertdataDao) {
		super(diameterconvertdataDao);
		this.diameterconvertdataDao = diameterconvertdataDao;
	}

	@Override
	public void deleteAndUpdate(GDbasePoint<DiameterConvertData> mdbase) throws ServiceException {
		super.delete(mdbase);
		DiameterConvertData bdata = (DiameterConvertData)mdbase;
		GDbasePoint<DiameterConvertData> tBuildingData = diameterconvertdataDao.getNext(mdbase.getCalibration(), GPointEnumType.DiameterConvert,bdata.getSpoint().getId());
		GDbasePoint<DiameterConvertData> bBuildingData = diameterconvertdataDao.getLast(mdbase.getCalibration(), GPointEnumType.DiameterConvert,bdata.getSpoint().getId());
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
	public void saveOrUpdate(DiameterConvertData diameterConvertData) throws ServiceException {
		GbasePoint<DiameterConvert> building = diameterconvertService.getById(diameterConvertData.getSpoint().getId());
		// 每个时间点只能有一条数据
		GDbasePoint<DiameterConvertData> dbdata = this.diameterconvertdataDao.getByDate(diameterConvertData.getCalibration(),
				building.getId(), GPointEnumType.DiameterConvert);
		if (dbdata != null) {
			DiameterConvertData dbbdata = (DiameterConvertData) dbdata;
			diameterConvertData.setId(dbbdata.getId());
		}
		if (diameterConvertData.getId() == null) {
			super.save(diameterConvertData);
		} else {
			super.update(diameterConvertData);
		}
		GDbasePoint<DiameterConvertData> next = super.getNext(diameterConvertData.getCalibration(), GPointEnumType.DiameterConvert,diameterConvertData.getSpoint().getId());
		if (next != null) {
			next.setInitHeight(diameterConvertData.getCurtHeight());
			super.update(next);
		}
	}

	
	
}
