package com.kekeinfo.core.business.monitor.surface.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.mbase.dao.MbaseDaoImpl;
import com.kekeinfo.core.business.monitor.surface.model.SupAxial;

@Repository("supaxialDao")
public class SupAxialDaoImpl extends MbaseDaoImpl<SupAxial> implements SupAxialDao {
	public SupAxialDaoImpl() {
		super();
	}
}