package com.kekeinfo.core.business.monitor.report.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.mreport.model.RHiddenLineData;

@Repository("rhiddenlinedataDao")
public class RHiddenLineDataDaoImpl extends RMBaseDataDaoImpl<RHiddenLineData> implements RHiddenLineDataDao {
	public RHiddenLineDataDaoImpl() {
		super();
	}

}