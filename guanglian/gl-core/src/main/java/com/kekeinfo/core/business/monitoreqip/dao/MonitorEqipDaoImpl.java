package com.kekeinfo.core.business.monitoreqip.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.monitoreqip.model.MonitorEqip;
import com.kekeinfo.core.business.monitoreqip.model.QMonitorEqip;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("monitoreqipDao")
public class MonitorEqipDaoImpl extends KekeinfoEntityDaoImpl<Long, MonitorEqip> implements MonitorEqipDao {
	public MonitorEqipDaoImpl() {
		super();
	}

	@Override
	public List<MonitorEqip> getByMid(long mid) {
		QMonitorEqip qContact = QMonitorEqip.monitorEqip;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact)
		.leftJoin(qContact.equip).fetch()
		.where(qContact.monitor.id.eq(mid));
		List<MonitorEqip> cs =query.list(qContact);
		
		return cs;
	}
}