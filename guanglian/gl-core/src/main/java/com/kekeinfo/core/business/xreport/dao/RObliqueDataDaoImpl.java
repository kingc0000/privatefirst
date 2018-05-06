package com.kekeinfo.core.business.xreport.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.xreport.model.QRObliqueData;
import com.kekeinfo.core.business.xreport.model.RObliqueData;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("robliquedataDao")
public class RObliqueDataDaoImpl extends KekeinfoEntityDaoImpl<Long, RObliqueData> implements RObliqueDataDao {
	public RObliqueDataDaoImpl() {
		super();
	}

	@Override
	public List<RObliqueData> getByRid(Long rid) {
		QRObliqueData qContact = QRObliqueData.rObliqueData;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact)
		.where(qContact.rObliqu.id.eq(rid))
		.orderBy(qContact.depth.asc());
		List<RObliqueData> cs =query.list(qContact);
		
		return cs;
	}
}