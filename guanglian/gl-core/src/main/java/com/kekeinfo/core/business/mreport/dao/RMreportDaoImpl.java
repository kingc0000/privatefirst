package com.kekeinfo.core.business.mreport.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.mreport.model.QRMreport;
import com.kekeinfo.core.business.mreport.model.RMreport;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("rmreportDao")
public class RMreportDaoImpl extends KekeinfoEntityDaoImpl<Long, RMreport> implements RMreportDao {
	public RMreportDaoImpl() {
		super();
	}

	@Override
	public RMreport getByRid(Long rid) {
		QRMreport qoObservewell = QRMreport.rMreport;
		//QMonitor qmonitor = QMonitor.monitor;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qoObservewell)
		//.leftJoin(qoObservewell.monitor, qmonitor).fetch()
		//.leftJoin(qmonitor.project).fetch()
		//.leftJoin(qoObservewell.monitor.project).fetch()
		.where(qoObservewell.id.eq(rid));
		
		List<RMreport> obs=query.list(qoObservewell);
		if(obs!=null && obs.size()>0){
			return obs.get(0);
		}
		return null;
	}
}