package com.kekeinfo.core.business.data.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.monitordata.model.HiwellData;

@Repository("hiWellDataDao")
public class HiWellDataDaoImpl extends KekeinfoEntityDaoImpl<Long, HiwellData> implements HiWellDataDao {

	public HiWellDataDaoImpl() {
		super();
	}
	
}
