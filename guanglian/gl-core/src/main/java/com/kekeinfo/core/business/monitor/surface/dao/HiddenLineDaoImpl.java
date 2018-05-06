package com.kekeinfo.core.business.monitor.surface.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.mbase.dao.MbaseDaoImpl;
import com.kekeinfo.core.business.monitor.surface.model.HiddenLine;

@Repository("hiddenlineDao")
public class HiddenLineDaoImpl extends MbaseDaoImpl<HiddenLine> implements HiddenLineDao {
	public HiddenLineDaoImpl() {
		super();
	}
}