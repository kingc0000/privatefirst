package com.kekeinfo.core.business.monitor.substrate.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.monitor.substrate.model.QSubStrate;
import com.kekeinfo.core.business.monitor.substrate.model.SubStrate;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("substrateDao")
public class SubStrateDaoImpl extends KekeinfoEntityDaoImpl<Long, SubStrate> implements SubStrateDao {
	public SubStrateDaoImpl() {
		super();
	}

	@Override
	public SubStrate withAttach(Long gid) {
		QSubStrate qContact = QSubStrate.subStrate;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact)
		.leftJoin(qContact.attach).fetch()
		.where(qContact.id.eq(gid));
		List<SubStrate> cs =query.list(qContact);
		if(cs!=null && cs.size()>0){
			return cs.get(0);
		}
		return null;
	}

	
}