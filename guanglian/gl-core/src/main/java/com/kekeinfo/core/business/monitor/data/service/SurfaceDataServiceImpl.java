package com.kekeinfo.core.business.monitor.data.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.monitor.data.dao.SurfaceDataDao;
import com.kekeinfo.core.business.monitor.data.model.MDbasePoint;
import com.kekeinfo.core.business.monitor.data.model.SurfaceData;
import com.kekeinfo.core.business.monitor.model.MPointEnumType;
import com.kekeinfo.core.business.monitor.surface.model.MbasePoint;
import com.kekeinfo.core.business.monitor.surface.model.Surface;
import com.kekeinfo.core.business.monitor.surface.service.SurfaceService;

@Service("surfacedataService")
public class SurfaceDataServiceImpl extends MdbaseServiceImpl<SurfaceData> implements SurfaceDataService {
	private SurfaceDataDao surfacedataDao;
	@Autowired private SurfaceService surfaceService;
	
	@Autowired
	public SurfaceDataServiceImpl(SurfaceDataDao surfacedataDao) {
		super(surfacedataDao);
		this.surfacedataDao = surfacedataDao;
	}
	
	@Transactional
	public void saveOrUpdate(SurfaceData surfaceData) throws ServiceException{
		MbasePoint<Surface> surface = surfaceService.getById(surfaceData.getSpoint().getId());
		// 每个时间点只能有一条数据
		MDbasePoint<SurfaceData> dbdata = this.surfacedataDao.getByDate(surfaceData.getCalibration(),
				surface.getId(), MPointEnumType.Surface);
		if (dbdata != null) {
			SurfaceData dbbdata = (SurfaceData) dbdata;
			surfaceData.setId(dbbdata.getId());
		}
		if (surfaceData.getId() == null) {
			super.save(surfaceData);
		} else {
			super.update(surfaceData);
		}
		MDbasePoint<SurfaceData> next = super.getNext(surfaceData.getCalibration(), MPointEnumType.Surface,surfaceData.getSpoint().getId());
		if (next != null) {
			next.setInitHeight(surfaceData.getCurtHeight());
			super.update(next);
		} 
	}
	
	@Override
	@Transactional
	public void deleteAndUpdate(MDbasePoint<SurfaceData> mdbase) throws ServiceException{
		super.delete(mdbase);
		SurfaceData bdata = (SurfaceData)mdbase;
		MDbasePoint<SurfaceData> tSurfaceData = surfacedataDao.getNext(mdbase.getCalibration(), MPointEnumType.Surface,bdata.getSpoint().getId());
		MDbasePoint<SurfaceData> bSurfaceData = surfacedataDao.getLast(mdbase.getCalibration(), MPointEnumType.Surface,bdata.getSpoint().getId());
		//有上级数据
		if(tSurfaceData!=null) {
			//上下级数据同时存在
			if(bSurfaceData!=null) {
				tSurfaceData.setInitHeight(bSurfaceData.getCurtHeight());
				this.saveOrUpdate(tSurfaceData);
			//只有上级数据存在+
			}else {
				tSurfaceData.setInitHeight(bdata.getSpoint().getInitHeight());
				this.saveOrUpdate(tSurfaceData);
			}
		}
	}
		
}
