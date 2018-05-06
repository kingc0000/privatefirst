package com.kekeinfo.core.business.guard.data.dao;


import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.gbase.dao.GDbaseDaoImpl;
import com.kekeinfo.core.business.guard.data.model.VerticalDisData;

@Repository("verticaldisdataDao")
public class VerticalDisDataDaoImpl extends GDbaseDaoImpl<VerticalDisData> implements VerticalDisDataDao {
	public VerticalDisDataDaoImpl() {
		super();
	}

	
}