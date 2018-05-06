package com.kekeinfo.core.business.monitor.data.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.monitor.data.model.RingBeamData;

@Repository("ringbeamdataDao")
public class RingBeamDataDaoImpl extends MdbaseDaoImpl<RingBeamData> implements RingBeamDataDao {
	public RingBeamDataDaoImpl() {
		super();
	}

}