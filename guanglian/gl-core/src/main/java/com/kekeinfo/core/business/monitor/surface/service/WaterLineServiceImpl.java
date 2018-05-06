package com.kekeinfo.core.business.monitor.surface.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kekeinfo.core.business.mbase.service.MbaseServiceImpl;
import com.kekeinfo.core.business.monitor.surface.model.WaterLine;
import com.kekeinfo.core.business.monitor.surface.dao.WaterLineDao;

@Service("waterlineService")
public class WaterLineServiceImpl extends MbaseServiceImpl<WaterLine> implements WaterLineService {
	@SuppressWarnings("unused")
	private WaterLineDao waterlineDao;

	@Autowired
	public WaterLineServiceImpl(WaterLineDao waterlineDao) {
		super(waterlineDao);
		this.waterlineDao = waterlineDao;
	}
}
