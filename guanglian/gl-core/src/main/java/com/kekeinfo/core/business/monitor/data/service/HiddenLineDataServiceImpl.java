package com.kekeinfo.core.business.monitor.data.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.monitor.data.dao.HiddenLineDataDao;
import com.kekeinfo.core.business.monitor.data.model.HiddenLineData;
import com.kekeinfo.core.business.monitor.data.model.MDbasePoint;
import com.kekeinfo.core.business.monitor.model.MPointEnumType;
import com.kekeinfo.core.business.monitor.surface.model.HiddenLine;
import com.kekeinfo.core.business.monitor.surface.model.MbasePoint;
import com.kekeinfo.core.business.monitor.surface.service.HiddenLineService;

@Service("hiddenlinedataService")
public class HiddenLineDataServiceImpl extends MdbaseServiceImpl<HiddenLineData>
		implements HiddenLineDataService {
	
	private HiddenLineDataDao hiddenlinedataDao;
	@Autowired
	private HiddenLineService hiddenlineService;

	@Autowired
	public HiddenLineDataServiceImpl(HiddenLineDataDao hiddenlinedataDao) {
		super(hiddenlinedataDao);
		this.hiddenlinedataDao = hiddenlinedataDao;
	}
	
	@Transactional
	public void saveOrUpdate(HiddenLineData hiddenLineData) throws ServiceException{
		MbasePoint<HiddenLine> hiddenLine = hiddenlineService.getById(hiddenLineData.getSpoint().getId());
		// 每个时间点只能有一条数据
		MDbasePoint<HiddenLineData> dbdata = this.hiddenlinedataDao.getByDate(hiddenLineData.getCalibration(),
				hiddenLine.getId(), MPointEnumType.HiddenLine);
		if (dbdata != null) {
			HiddenLineData dbbdata = (HiddenLineData) dbdata;
			hiddenLineData.setId(dbbdata.getId());
		}
		if (hiddenLineData.getId() == null) {
			super.save(hiddenLineData);
		} else {
			super.update(hiddenLineData);
		}
		MDbasePoint<HiddenLineData> next = super.getNext(hiddenLineData.getCalibration(), MPointEnumType.HiddenLine,hiddenLineData.getSpoint().getId());
		if (next != null) {
			next.setInitHeight(hiddenLineData.getCurtHeight());
			super.update(next);
		} 
	}
	
	@Override
	@Transactional
	public void deleteAndUpdate(MDbasePoint<HiddenLineData> mdbase) throws ServiceException{
		super.delete(mdbase);
		HiddenLineData bdata = (HiddenLineData)mdbase;
		MDbasePoint<HiddenLineData> tHiddenLineData = hiddenlinedataDao.getNext(mdbase.getCalibration(), MPointEnumType.HiddenLine,bdata.getSpoint().getId());
		MDbasePoint<HiddenLineData> bHiddenLineData = hiddenlinedataDao.getLast(mdbase.getCalibration(), MPointEnumType.HiddenLine,bdata.getSpoint().getId());
		//有上级数据
				if(tHiddenLineData!=null) {
					//上下级数据同时存在
					if(bHiddenLineData!=null) {
						tHiddenLineData.setInitHeight(bHiddenLineData.getCurtHeight());
						this.saveOrUpdate(tHiddenLineData);
					//只有上级数据存在+
					}else {
						tHiddenLineData.setInitHeight(bdata.getSpoint().getInitHeight());
						this.saveOrUpdate(tHiddenLineData);
					}
				}
	}
	
}
