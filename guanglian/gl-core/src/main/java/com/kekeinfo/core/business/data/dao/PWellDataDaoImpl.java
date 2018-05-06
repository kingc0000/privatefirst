package com.kekeinfo.core.business.data.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.monitordata.model.PwellData;

@Repository("pWellDataDao")
public class PWellDataDaoImpl extends KekeinfoEntityDaoImpl<Long, PwellData> implements PWellDataDao {

	public PWellDataDaoImpl() {
		super();
	}
	
}
