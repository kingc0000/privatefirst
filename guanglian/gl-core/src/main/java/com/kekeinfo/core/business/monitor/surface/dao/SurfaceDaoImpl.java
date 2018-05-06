package com.kekeinfo.core.business.monitor.surface.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.mbase.dao.MbaseDaoImpl;
import com.kekeinfo.core.business.monitor.surface.model.Surface;

@Repository("surfaceDao")
public class SurfaceDaoImpl extends MbaseDaoImpl<Surface> implements SurfaceDao {
	public SurfaceDaoImpl() {
		super();
	}

	
}