package com.kekeinfo.core.business.last.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.last.model.MonitLast;
import com.kekeinfo.core.business.last.model.QMonitLast;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("monitlastDao")
public class MonitLastDaoImpl extends KekeinfoEntityDaoImpl<Long, MonitLast> implements MonitLastDao {
	public MonitLastDaoImpl() {
		super();
	}

	@Override
	public List<MonitLast> getByUserID(long userid) {
		JPQLQuery query = new JPAQuery (getEntityManager());
		QMonitLast csiteLast = QMonitLast.monitLast;
		query.from(csiteLast)
		.where(csiteLast.uid.eq(userid))
		.orderBy(csiteLast.dateModified.desc());
		query.limit(5);
		List<MonitLast> cs =query.list(csiteLast);
		
		return cs;
	}
}