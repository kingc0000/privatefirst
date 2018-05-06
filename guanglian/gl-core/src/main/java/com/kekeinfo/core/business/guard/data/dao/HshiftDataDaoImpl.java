package com.kekeinfo.core.business.guard.data.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.gbase.dao.GDbaseDaoImpl;
import com.kekeinfo.core.business.guard.data.model.HshiftData;

@Repository("hshiftdataDao")
public class HshiftDataDaoImpl extends GDbaseDaoImpl<HshiftData> implements HshiftDataDao {
	public HshiftDataDaoImpl() {
		super();
	}
}