package com.kekeinfo.core.business.last.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.last.model.GuardLast;
import com.kekeinfo.core.business.last.model.QGuardLast;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("guardlastDao")
public class GuardLastDaoImpl extends KekeinfoEntityDaoImpl<Long, GuardLast> implements GuardLastDao {
	public GuardLastDaoImpl() {
		super();
	}

	@Override
	public List<GuardLast> getByUserID(long userid) {
		JPQLQuery query = new JPAQuery (getEntityManager());
		QGuardLast csiteLast = QGuardLast.guardLast;
		query.from(csiteLast)
		.where(csiteLast.uid.eq(userid))
		.orderBy(csiteLast.dateModified.desc());
		query.limit(5);
		List<GuardLast> cs =query.list(csiteLast);
		
		return cs;
	}
}