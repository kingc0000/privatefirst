package com.kekeinfo.core.business.data.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.monitordata.model.DewellData;

@Repository("deWellDataDao")
public class DeWellDataDaoImpl extends KekeinfoEntityDaoImpl<Long, DewellData> implements DeWellDataDao {

	public DeWellDataDaoImpl() {
		super();
	}
	
}
