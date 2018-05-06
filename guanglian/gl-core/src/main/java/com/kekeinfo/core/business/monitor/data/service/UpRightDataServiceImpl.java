package com.kekeinfo.core.business.monitor.data.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.monitor.data.dao.UpRightDataDao;
import com.kekeinfo.core.business.monitor.data.model.MDbasePoint;
import com.kekeinfo.core.business.monitor.data.model.UpRightData;
import com.kekeinfo.core.business.monitor.model.MPointEnumType;
import com.kekeinfo.core.business.monitor.surface.model.MbasePoint;
import com.kekeinfo.core.business.monitor.surface.model.UpRight;
import com.kekeinfo.core.business.monitor.surface.service.UpRightService;

@Service("uprightdataService")
public class UpRightDataServiceImpl extends MdbaseServiceImpl<UpRightData> implements UpRightDataService {

	private UpRightDataDao uprightdataDao;
	@Autowired
	private UpRightService uprightService;

	@Autowired
	public UpRightDataServiceImpl(UpRightDataDao uprightdataDao) {
		super(uprightdataDao);
		this.uprightdataDao = uprightdataDao;
	}
	
	@Transactional
	public void saveOrUpdate(UpRightData upRightData) throws ServiceException{
		MbasePoint<UpRight> upright = uprightService.getById(upRightData.getSpoint().getId());
		// 每个时间点只能有一条数据
		MDbasePoint<UpRightData> dbdata = this.uprightdataDao.getByDate(upRightData.getCalibration(),
				upright.getId(), MPointEnumType.UpRight);
		if (dbdata != null) {
			UpRightData dbbdata = (UpRightData) dbdata;
			upRightData.setId(dbbdata.getId());
		}
		if (upRightData.getId() == null) {
			super.save(upRightData);
		} else {
			super.update(upRightData);
		}
		MDbasePoint<UpRightData> next = super.getNext(upRightData.getCalibration(), MPointEnumType.UpRight,upRightData.getSpoint().getId());
		if (next != null) {
			next.setInitHeight(upRightData.getCurtHeight());
			super.update(next);
		} 
	}
	
	@Override
	@Transactional
	public void deleteAndUpdate(MDbasePoint<UpRightData> mdbase) throws ServiceException{
		super.delete(mdbase);
		UpRightData bdata = (UpRightData)mdbase;
		MDbasePoint<UpRightData> tUpRightData = uprightdataDao.getNext(mdbase.getCalibration(), MPointEnumType.UpRight,bdata.getSpoint().getId());
		MDbasePoint<UpRightData> bUpRightData = uprightdataDao.getLast(mdbase.getCalibration(), MPointEnumType.UpRight,bdata.getSpoint().getId());
		//有上级数据
				if(tUpRightData!=null) {
					//上下级数据同时存在
					if(bUpRightData!=null) {
						tUpRightData.setInitHeight(bUpRightData.getCurtHeight());
						this.saveOrUpdate(tUpRightData);
					//只有上级数据存在+
					}else {
						tUpRightData.setInitHeight(bdata.getSpoint().getInitHeight());
						this.saveOrUpdate(tUpRightData);
					}
				}
	}


	
}
