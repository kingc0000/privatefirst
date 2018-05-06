package com.kekeinfo.core.business.monitor.surface.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.mbase.service.MbaseServiceImpl;
import com.kekeinfo.core.business.monitor.surface.dao.SurfaceDao;
import com.kekeinfo.core.business.monitor.surface.model.Surface;

@Service("surfaceService")
public class SurfaceServiceImpl extends MbaseServiceImpl<Surface> implements SurfaceService {
	

	@Autowired
	public SurfaceServiceImpl(SurfaceDao surfaceDao) {
		super(surfaceDao);
		this.surfaceDao=surfaceDao;
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unused")
	private SurfaceDao surfaceDao;

	
	
	
}
