package com.kekeinfo.core.business.data.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.monitordata.model.EmonitorData;

@Repository("eMonitorDataDao")
public class EMonitorDataDaoImpl extends KekeinfoEntityDaoImpl<Long, EmonitorData> implements EMonitorDataDao {

	public EMonitorDataDaoImpl() {
		super();
	}
	
}
