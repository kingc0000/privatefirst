package com.kekeinfo.core.business.guard.data.dao;


import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.gbase.dao.GDbaseDaoImpl;
import com.kekeinfo.core.business.guard.data.model.DiameterConvertData;

@Repository("diameterconvertdataDao")
public class DiameterConvertDataDaoImpl extends GDbaseDaoImpl<DiameterConvertData> implements DiameterConvertDataDao {
	public DiameterConvertDataDaoImpl() {
		super();
	}

	
}