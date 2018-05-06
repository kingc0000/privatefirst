package com.kekeinfo.core.business.data.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.monitordata.model.IwellData;

@Repository("iWellDataDao")
public class IWellDataDaoImpl extends KekeinfoEntityDaoImpl<Long, IwellData> implements IWellDataDao {

	public IWellDataDaoImpl() {
		super();
	}
	
}
