package com.kekeinfo.core.business.daily.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.daily.model.GuardDaily;
import com.kekeinfo.core.business.daily.model.QGuardDaily;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("guarddailyDao")
public class GuardDailyDaoImpl extends KekeinfoEntityDaoImpl<Long, GuardDaily> implements GuardDailyDao {
	public GuardDailyDaoImpl() {
		super();
	}

	@Override
	public GuardDaily wihtimg(Long gid) {
		QGuardDaily qDaily = QGuardDaily.guardDaily;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qDaily)
		.leftJoin(qDaily.guardDailyImages).fetch()
		.where(qDaily.id.eq(gid));
		
		List<GuardDaily> ds = query.list(qDaily);
		if(ds!=null && ds.size()>0){
			return ds.get(0);
		}
		return null;
	}

	@Override
	public GuardDaily byDate(Date date,Long gid) {
		QGuardDaily qDaily = QGuardDaily.guardDaily;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qDaily)
		.leftJoin(qDaily.guardDailyImages).fetch()
		.where(qDaily.datec.eq(date).and(qDaily.guard.id.eq(gid)));
		
		List<GuardDaily> ds = query.list(qDaily);
		if(ds!=null && ds.size()>0){
			return ds.get(0);
		}
		return null;
	}
}