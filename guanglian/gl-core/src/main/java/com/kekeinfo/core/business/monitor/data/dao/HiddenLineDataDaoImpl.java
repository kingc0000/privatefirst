package com.kekeinfo.core.business.monitor.data.dao;


import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.monitor.data.model.HiddenLineData;

@Repository("hiddenlinedataDao")
public class HiddenLineDataDaoImpl extends MdbaseDaoImpl<HiddenLineData> implements HiddenLineDataDao {
	public HiddenLineDataDaoImpl() {
		super();
	}

}