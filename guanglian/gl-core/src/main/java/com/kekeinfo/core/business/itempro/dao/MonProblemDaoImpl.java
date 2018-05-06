package com.kekeinfo.core.business.itempro.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.monproblem.model.MonProblem;
import com.kekeinfo.core.business.monproblem.model.QMonProblem;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("monProblemDao")
public class MonProblemDaoImpl extends KekeinfoEntityDaoImpl<Long, MonProblem> implements MonProblemDao{

	@Override
	public MonProblem withAttach(Long gid) {
		QMonProblem qContact = QMonProblem.monProblem;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact)
		.leftJoin(qContact.attach).fetch()
		.where(qContact.id.eq(gid));
		List<MonProblem> cs =query.list(qContact);
		if(cs!=null && cs.size()>0){
			return cs.get(0);
		}
		return null;
	}

	
}
