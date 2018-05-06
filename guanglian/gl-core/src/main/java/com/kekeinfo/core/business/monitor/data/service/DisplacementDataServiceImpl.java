package com.kekeinfo.core.business.monitor.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.monitor.data.model.DisplacementData;
import com.kekeinfo.core.business.monitor.data.model.MDbasePoint;
import com.kekeinfo.core.business.monitor.model.MPointEnumType;
import com.kekeinfo.core.business.monitor.surface.model.Displacement;
import com.kekeinfo.core.business.monitor.surface.model.MbasePoint;
import com.kekeinfo.core.business.monitor.surface.service.DisplacementService;
import com.kekeinfo.core.business.monitor.data.dao.DisplacementDataDao;

@Service("displacementdataService")
public class DisplacementDataServiceImpl extends MdbaseServiceImpl<DisplacementData>
		implements DisplacementDataService {
	@Autowired
	private DisplacementService displacementService;
	private DisplacementDataDao displacementdataDao;

	@Autowired
	public DisplacementDataServiceImpl(DisplacementDataDao displacementdataDao) {
		super(displacementdataDao);
		this.displacementdataDao = displacementdataDao;
	}
	
	@Override
	@Transactional
	public void saveOrUpdate(DisplacementData displacementData) throws ServiceException {
		MbasePoint<Displacement> building = displacementService.getById(displacementData.getSpoint().getId());
		// 每个时间点只能有一条数据
		MDbasePoint<DisplacementData> dbdata = this.displacementdataDao.getByDate(displacementData.getCalibration(),
				building.getId(), MPointEnumType.Displacement);
		if (dbdata != null) {
			DisplacementData dbbdata = (DisplacementData) dbdata;
			displacementData.setId(dbbdata.getId());
		}
		if (displacementData.getId() == null) {
			super.save(displacementData);
		} else {
			super.update(displacementData);
		}
		MDbasePoint<DisplacementData> next = super.getNext(displacementData.getCalibration(),
				MPointEnumType.Displacement, displacementData.getSpoint().getId());
		if (next != null) {
			next.setInitHeight(displacementData.getCurtHeight());
			super.update(next);
		}
	}

	

	@Override
	@Transactional
	public void deleteAndUpdate(MDbasePoint<DisplacementData> mdbase) throws ServiceException {
		super.delete(mdbase);
		DisplacementData bdata = (DisplacementData) mdbase;
		MDbasePoint<DisplacementData> tDisplacementData = displacementdataDao.getNext(mdbase.getCalibration(),
				MPointEnumType.Displacement, bdata.getSpoint().getId());
		MDbasePoint<DisplacementData> bDisplacementData = displacementdataDao.getLast(mdbase.getCalibration(),
				MPointEnumType.Displacement, bdata.getSpoint().getId());
		// 有上级数据
		if (tDisplacementData != null) {
			// 上下级数据同时存在
			if (bDisplacementData != null) {
				tDisplacementData.setInitHeight(bDisplacementData.getCurtHeight());
				this.saveOrUpdate(tDisplacementData);
				// 只有上级数据存在+
			} else {
				tDisplacementData.setInitHeight(bdata.getSpoint().getInitHeight());
				this.saveOrUpdate(tDisplacementData);
			}
		}
	}

}
