package com.kekeinfo.core.business.guard.point.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.gbase.dao.GbaseDaoImpl;
import com.kekeinfo.core.business.guard.point.model.VerticalDis;

@Repository("verticaldisDao")
public class VerticalDisDaoImpl extends GbaseDaoImpl<VerticalDis> implements VerticalDisDao {
	public VerticalDisDaoImpl() {
		super();
	}

	
}