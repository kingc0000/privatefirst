package com.kekeinfo.core.business.xreport.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.xreport.model.QROblique;
import com.kekeinfo.core.business.xreport.model.ROblique;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("robliqueDao")
public class RObliqueDaoImpl extends KekeinfoEntityDaoImpl<Long, ROblique> implements RObliqueDao {
	public RObliqueDaoImpl() {
		super();
	}

	@Override
	public List<ROblique> getByXid(Long xid) {
		QROblique qGroup = QROblique.rOblique;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qGroup)
			.where(qGroup.xmreport.id.eq(xid));
		
		return query.list(qGroup);
	}
}