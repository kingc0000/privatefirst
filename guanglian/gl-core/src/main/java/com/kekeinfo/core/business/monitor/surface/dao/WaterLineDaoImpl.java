package com.kekeinfo.core.business.monitor.surface.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.mbase.dao.MbaseDaoImpl;
import com.kekeinfo.core.business.monitor.surface.model.WaterLine;

@Repository("waterlineDao")
public class WaterLineDaoImpl extends MbaseDaoImpl< WaterLine> implements WaterLineDao {
	public WaterLineDaoImpl() {
		super();
	}
}