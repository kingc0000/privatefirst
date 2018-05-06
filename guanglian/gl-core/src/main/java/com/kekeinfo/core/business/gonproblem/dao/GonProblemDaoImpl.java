package com.kekeinfo.core.business.gonproblem.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.gonproblem.model.GonProblem;
import com.kekeinfo.core.business.gonproblem.model.QGonProblem;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("gonproblemDao")
public class GonProblemDaoImpl extends KekeinfoEntityDaoImpl<Long, GonProblem> implements GonProblemDao {
	public GonProblemDaoImpl() {
		super();
	}

	@Override
	public GonProblem withAttach(Long gid) {
		QGonProblem qContact = QGonProblem.gonProblem;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact)
		.leftJoin(qContact.attach).fetch()
		.where(qContact.id.eq(gid));
		List<GonProblem> cs =query.list(qContact);
		if(cs!=null && cs.size()>0){
			return cs.get(0);
		}
		return null;
	}
}