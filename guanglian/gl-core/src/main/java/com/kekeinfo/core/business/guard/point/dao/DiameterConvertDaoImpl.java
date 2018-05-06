package com.kekeinfo.core.business.guard.point.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.gbase.dao.GbaseDaoImpl;
import com.kekeinfo.core.business.guard.point.model.DiameterConvert;

@Repository("diameterconvertDao")
public class DiameterConvertDaoImpl extends GbaseDaoImpl<DiameterConvert> implements DiameterConvertDao {
	public DiameterConvertDaoImpl() {
		super();
	}
}