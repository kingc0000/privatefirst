package com.kekeinfo.core.business.monitor.data.dao;

import org.springframework.stereotype.Repository;
import com.kekeinfo.core.business.monitor.data.model.DisplacementData;

@Repository("displacementdataDao")
public class DisplacementDataDaoImpl extends MdbaseDaoImpl<DisplacementData> implements DisplacementDataDao {
	public DisplacementDataDaoImpl() {
		super();
	}
}