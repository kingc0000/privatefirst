package com.kekeinfo.core.business.monitor.surface.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.mbase.dao.MbaseDaoImpl;
import com.kekeinfo.core.business.monitor.surface.model.UpRight;

@Repository("uprightDao")
public class UpRightDaoImpl extends MbaseDaoImpl<UpRight> implements UpRightDao {
	public UpRightDaoImpl() {
		super();
	}
}