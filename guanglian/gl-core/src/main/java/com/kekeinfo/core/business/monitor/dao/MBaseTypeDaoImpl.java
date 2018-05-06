package com.kekeinfo.core.business.monitor.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.monitor.model.MBaseType;
import com.kekeinfo.core.business.monitor.model.QMBaseType;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("mbasetypeDao")
public class MBaseTypeDaoImpl extends KekeinfoEntityDaoImpl<Long, MBaseType> implements MBaseTypeDao {
	public MBaseTypeDaoImpl() {
		super();
	}

	@Override
	public List<MBaseType> getByMid(Long mid) {
		QMBaseType qContact = QMBaseType.mBaseType;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact)
		.where(qContact.monitor.id.eq(mid));
		List<MBaseType> cs =query.list(qContact);
		return cs;
	}
}