package com.kekeinfo.core.business.daily.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.daily.model.MonitorDaily;
import com.kekeinfo.core.business.daily.model.QMonitorDaily;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("monitordailyDao")
public class MonitorDailyDaoImpl extends KekeinfoEntityDaoImpl<Long, MonitorDaily> implements MonitorDailyDao {
	public MonitorDailyDaoImpl() {
		super();
	}

	@Override
	public MonitorDaily getBydate(Date date,long mid) {
		QMonitorDaily qDaily = QMonitorDaily.monitorDaily;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qDaily)
		.where(qDaily.datec.eq(date).and(qDaily.monitor.id.eq(mid)));
		
		List<MonitorDaily> ds = query.list(qDaily);
		if(ds!=null && ds.size()>0){
			return ds.get(0);
		}
		return null;
	}

	@Override
	public MonitorDaily withImg(Long mdid) {
		QMonitorDaily qDaily = QMonitorDaily.monitorDaily;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qDaily)
		.leftJoin(qDaily.monitorDailyImages).fetch()
		.where(qDaily.id.eq(mdid));
		
		List<MonitorDaily> ds = query.list(qDaily);
		if(ds!=null && ds.size()>0){
			return ds.get(0);
		}
		return null;
	}
}