package com.kekeinfo.core.business.data.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.monitordata.model.HemonitorData;

@Repository("heMonitorDataDao")
public class HeMonitorDataDaoImpl extends KekeinfoEntityDaoImpl<Long, HemonitorData> implements HeMonitorDataDao {

	public HeMonitorDataDaoImpl() {
		super();
	}
	
}
