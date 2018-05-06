package com.kekeinfo.core.business.monitor.oblique.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.monitor.oblique.model.Oblique;
import com.kekeinfo.core.business.monitor.oblique.model.QOblique;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("obliqueDao")
public class ObliqueDaoImpl extends KekeinfoEntityDaoImpl<Long, Oblique> implements ObliqueDao {
	public ObliqueDaoImpl() {
		super();
	}

	@Override
	public List<Oblique> getByMid(Long mid) {
		QOblique qContact = QOblique.oblique;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact)
		.where(qContact.monitor.id.eq(mid));
		List<Oblique> cs =query.list(qContact);
		
		return cs;
	}
}