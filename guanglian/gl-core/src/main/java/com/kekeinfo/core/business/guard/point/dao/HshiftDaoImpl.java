package com.kekeinfo.core.business.guard.point.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.gbase.dao.GbaseDaoImpl;
import com.kekeinfo.core.business.guard.point.model.Hshift;

@Repository("hshiftDao")
public class HshiftDaoImpl extends GbaseDaoImpl<Hshift> implements HshiftDao {
	public HshiftDaoImpl() {
		super();
	}
}