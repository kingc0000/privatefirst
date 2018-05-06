package com.kekeinfo.core.business.monitor.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.monitor.data.dao.SupAxialDataDao;
import com.kekeinfo.core.business.monitor.data.model.MDbasePoint;
import com.kekeinfo.core.business.monitor.data.model.SupAxialData;
import com.kekeinfo.core.business.monitor.model.MPointEnumType;
import com.kekeinfo.core.business.monitor.surface.model.MbasePoint;
import com.kekeinfo.core.business.monitor.surface.model.SupAxial;
import com.kekeinfo.core.business.monitor.surface.service.SupAxialService;

@Service("supaxialdataService")
public class SupAxialDataServiceImpl extends MdbaseServiceImpl<SupAxialData>
		implements SupAxialDataService {
	private SupAxialDataDao supaxialdataDao;
	@Autowired
	private SupAxialService supaxialService;

	@Autowired
	public SupAxialDataServiceImpl(SupAxialDataDao supaxialdataDao) {
		super(supaxialdataDao);
		this.supaxialdataDao = supaxialdataDao;
	}
	
	@Transactional
	public void saveOrUpdate(SupAxialData supAxialData) throws ServiceException{
		MbasePoint<SupAxial> supAxial = supaxialService.getById(supAxialData.getSpoint().getId());
		// 每个时间点只能有一条数据
		MDbasePoint<SupAxialData> dbdata = this.supaxialdataDao.getByDate(supAxialData.getCalibration(),
				supAxial.getId(), MPointEnumType.SupAxial);
		if (dbdata != null) {
			SupAxialData dbbdata = (SupAxialData) dbdata;
			supAxialData.setId(dbbdata.getId());
		}
		if (supAxialData.getId() == null) {
			super.save(supAxialData);
		} else {
			super.update(supAxialData);
		}
		MDbasePoint<SupAxialData> next = super.getNext(supAxialData.getCalibration(), MPointEnumType.SupAxial,supAxialData.getId());
		if (next != null) {
			next.setInitHeight(supAxialData.getCurtHeight());
			super.update(next);
		} 
	}
	
	@Override
	@Transactional
	public void deleteAndUpdate(MDbasePoint<SupAxialData> mdbase) throws ServiceException{
		super.delete(mdbase);
		SupAxialData bdata = (SupAxialData)mdbase;
		MDbasePoint<SupAxialData> tSupAxialData = supaxialdataDao.getNext(mdbase.getCalibration(), MPointEnumType.SupAxial,bdata.getSpoint().getId());
		MDbasePoint<SupAxialData> bSupAxialData = supaxialdataDao.getLast(mdbase.getCalibration(), MPointEnumType.SupAxial,bdata.getSpoint().getId());
		//有上级数据
				if(tSupAxialData!=null) {
					//上下级数据同时存在
					if(bSupAxialData!=null) {
						tSupAxialData.setInitHeight(bSupAxialData.getCurtHeight());
						this.saveOrUpdate(tSupAxialData);
					//只有上级数据存在+
					}else {
						tSupAxialData.setInitHeight(bdata.getSpoint().getInitHeight());
						this.saveOrUpdate(tSupAxialData);
					}
				}
	}
	
}
