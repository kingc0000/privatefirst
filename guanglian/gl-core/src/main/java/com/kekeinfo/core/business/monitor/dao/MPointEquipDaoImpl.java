package com.kekeinfo.core.business.monitor.dao;

import org.springframework.stereotype.Repository;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.monitor.model.MPoint;

@Repository("mpointDao")
public class MPointEquipDaoImpl extends KekeinfoEntityDaoImpl<Long, MPoint> implements MPointEquipDao {
	public MPointEquipDaoImpl() {
		super();
	}
}