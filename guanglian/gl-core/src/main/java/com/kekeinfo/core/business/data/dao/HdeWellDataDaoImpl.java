package com.kekeinfo.core.business.data.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.monitordata.model.HdewellData;

@Repository("hdeWellDataDao")
public class HdeWellDataDaoImpl extends KekeinfoEntityDaoImpl<Long, HdewellData> implements HdeWellDataDao {

	public HdeWellDataDaoImpl() {
		super();
	}
	
}
