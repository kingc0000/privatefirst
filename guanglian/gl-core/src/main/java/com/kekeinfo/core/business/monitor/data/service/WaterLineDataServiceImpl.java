package com.kekeinfo.core.business.monitor.data.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.monitor.data.dao.WaterLineDataDao;
import com.kekeinfo.core.business.monitor.data.model.MDbasePoint;
import com.kekeinfo.core.business.monitor.data.model.WaterLineData;
import com.kekeinfo.core.business.monitor.model.MPointEnumType;
import com.kekeinfo.core.business.monitor.surface.model.MbasePoint;
import com.kekeinfo.core.business.monitor.surface.model.WaterLine;
import com.kekeinfo.core.business.monitor.surface.service.WaterLineService;

@Service("waterlinedataService")
public class WaterLineDataServiceImpl extends MdbaseServiceImpl<WaterLineData>
		implements WaterLineDataService {
	private WaterLineDataDao waterlinedataDao;
	@Autowired
	private WaterLineService waterlineService;

	@Autowired
	public WaterLineDataServiceImpl(WaterLineDataDao waterlinedataDao) {
		super(waterlinedataDao);
		this.waterlinedataDao = waterlinedataDao;
	}
	
	@Transactional
	public void saveOrUpdate(WaterLineData waterLineData) throws ServiceException{
		MbasePoint<WaterLine> waterLine = waterlineService.getById(waterLineData.getSpoint().getId());
		// 每个时间点只能有一条数据
		MDbasePoint<WaterLineData> dbdata = this.waterlinedataDao.getByDate(waterLineData.getCalibration(),
				waterLine.getId(), MPointEnumType.WaterLine);
		if (dbdata != null) {
			WaterLineData dbbdata = (WaterLineData) dbdata;
			waterLineData.setId(dbbdata.getId());
		}
		if (waterLineData.getId() == null) {
			super.save(waterLineData);
		} else {
			super.update(waterLineData);
		}
		MDbasePoint<WaterLineData> next = super.getNext(waterLineData.getCalibration(), MPointEnumType.WaterLine,waterLineData.getSpoint().getId());
		if (next != null) {
			next.setInitHeight(waterLineData.getCurtHeight());
			super.update(next);
		} 
	}

	@Override
	@Transactional
	public void deleteAndUpdate(MDbasePoint<WaterLineData> mdbase) throws ServiceException{
		super.delete(mdbase);
		WaterLineData bdata = (WaterLineData)mdbase;
		MDbasePoint<WaterLineData> tWaterLineData = waterlinedataDao.getNext(mdbase.getCalibration(), MPointEnumType.WaterLine,bdata.getSpoint().getId());
		MDbasePoint<WaterLineData> bWaterLineData = waterlinedataDao.getLast(mdbase.getCalibration(), MPointEnumType.WaterLine,bdata.getSpoint().getId());
		//有上级数据
		if(tWaterLineData!=null) {
			//上下级数据同时存在
			if(bWaterLineData!=null) {
				tWaterLineData.setInitHeight(bWaterLineData.getCurtHeight());
				this.saveOrUpdate(tWaterLineData);
			//只有上级数据存在+
			}else {
				tWaterLineData.setInitHeight(bdata.getSpoint().getInitHeight());
				this.saveOrUpdate(tWaterLineData);
			}
		}
	}


	
}
