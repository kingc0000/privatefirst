package com.kekeinfo.core.business.data.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.monitordata.model.HpwellData;

@Repository("hpWellDataDao")
public class HpWellDataDaoImpl extends KekeinfoEntityDaoImpl<Long, HpwellData> implements HpWellDataDao {

	public HpWellDataDaoImpl() {
		super();
	}
	
}
