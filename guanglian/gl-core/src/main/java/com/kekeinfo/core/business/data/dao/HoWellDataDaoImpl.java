package com.kekeinfo.core.business.data.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.monitordata.model.HowellData;

@Repository("hoWellDataDao")
public class HoWellDataDaoImpl extends KekeinfoEntityDaoImpl<Long, HowellData> implements HoWellDataDao {

	public HoWellDataDaoImpl() {
		super();
	}
	
}
