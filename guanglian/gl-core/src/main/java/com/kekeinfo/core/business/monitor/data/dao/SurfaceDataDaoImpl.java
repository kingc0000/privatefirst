package com.kekeinfo.core.business.monitor.data.dao;


import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.monitor.data.model.SurfaceData;

@Repository("surfacedataDao")
public class SurfaceDataDaoImpl extends MdbaseDaoImpl<SurfaceData> implements SurfaceDataDao {
	public SurfaceDataDaoImpl() {
		super();
	}

}