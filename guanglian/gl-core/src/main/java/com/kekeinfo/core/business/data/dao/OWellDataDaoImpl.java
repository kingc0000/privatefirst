package com.kekeinfo.core.business.data.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.monitordata.model.OwellData;

@Repository("oWellDataDao")
public class OWellDataDaoImpl extends KekeinfoEntityDaoImpl<Long, OwellData> implements OWellDataDao {

	public OWellDataDaoImpl() {
		super();
	}
	
}
