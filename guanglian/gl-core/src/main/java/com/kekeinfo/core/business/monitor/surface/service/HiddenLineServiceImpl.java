package com.kekeinfo.core.business.monitor.surface.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kekeinfo.core.business.mbase.service.MbaseServiceImpl;
import com.kekeinfo.core.business.monitor.surface.model.HiddenLine;
import com.kekeinfo.core.business.monitor.surface.dao.HiddenLineDao;

@Service("hiddenlineService")
public class HiddenLineServiceImpl extends MbaseServiceImpl<HiddenLine> implements HiddenLineService {
	@SuppressWarnings("unused")
	private HiddenLineDao hiddenlineDao;

	@Autowired
	public HiddenLineServiceImpl(HiddenLineDao hiddenlineDao) {
		super(hiddenlineDao);
		this.hiddenlineDao = hiddenlineDao;
	}
}
