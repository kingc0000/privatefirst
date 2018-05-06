package com.kekeinfo.core.business.monitor.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.monitor.data.dao.RingBeamDataDao;
import com.kekeinfo.core.business.monitor.data.model.MDbasePoint;
import com.kekeinfo.core.business.monitor.data.model.RingBeamData;
import com.kekeinfo.core.business.monitor.model.MPointEnumType;
import com.kekeinfo.core.business.monitor.surface.model.MbasePoint;
import com.kekeinfo.core.business.monitor.surface.model.RingBeam;
import com.kekeinfo.core.business.monitor.surface.service.RingBeamService;

@Service("ringbeamdataService")
public class RingBeamDataServiceImpl extends MdbaseServiceImpl<RingBeamData>
		implements RingBeamDataService {
	private RingBeamDataDao ringbeamdataDao;
	
	@Autowired
	private RingBeamService ringbeamService;

	@Autowired
	public RingBeamDataServiceImpl(RingBeamDataDao ringbeamdataDao) {
		super(ringbeamdataDao);
		this.ringbeamdataDao = ringbeamdataDao;
	}
	
	@Transactional
	public void saveOrUpdate(RingBeamData ringBeamData) throws ServiceException{
		MbasePoint<RingBeam> ringBeam = ringbeamService.getById(ringBeamData.getSpoint().getId());
		// 每个时间点只能有一条数据
		MDbasePoint<RingBeamData> dbdata = this.ringbeamdataDao.getByDate(ringBeamData.getCalibration(),
				ringBeam.getId(), MPointEnumType.RingBeam);
		if (dbdata != null) {
			RingBeamData dbbdata = (RingBeamData) dbdata;
			ringBeamData.setId(dbbdata.getId());
		}
		if (ringBeamData.getId() == null) {
			super.save(ringBeamData);
		} else {
			super.update(ringBeamData);
		}
		MDbasePoint<RingBeamData> next = super.getNext(ringBeamData.getCalibration(), MPointEnumType.RingBeam,ringBeamData.getSpoint().getId());
		if (next != null) {
			next.setInitHeight(ringBeamData.getCurtHeight());
			super.update(next);
		} 
	}
	
	@Override
	@Transactional
	public void deleteAndUpdate(MDbasePoint<RingBeamData> mdbase) throws ServiceException{
		super.delete(mdbase);
		RingBeamData bdata = (RingBeamData)mdbase;
		MDbasePoint<RingBeamData> tRingBeamData = ringbeamdataDao.getNext(mdbase.getCalibration(), MPointEnumType.RingBeam,bdata.getSpoint().getId());
		MDbasePoint<RingBeamData> bRingBeamData = ringbeamdataDao.getLast(mdbase.getCalibration(), MPointEnumType.RingBeam,bdata.getSpoint().getId());
		//有上级数据
		if(tRingBeamData!=null) {
			//上下级数据同时存在
			if(bRingBeamData!=null) {
				tRingBeamData.setInitHeight(bRingBeamData.getCurtHeight());
				this.saveOrUpdate(tRingBeamData);
			//只有上级数据存在+
			}else {
				tRingBeamData.setInitHeight(bdata.getSpoint().getInitHeight());
				this.saveOrUpdate(tRingBeamData);
			}
		}
	}
	
}
