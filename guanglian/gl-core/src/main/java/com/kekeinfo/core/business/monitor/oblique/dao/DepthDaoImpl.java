package com.kekeinfo.core.business.monitor.oblique.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.monitor.oblique.model.Depth;
import com.kekeinfo.core.business.monitor.oblique.model.QDepth;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("depthDao")
public class DepthDaoImpl extends KekeinfoEntityDaoImpl<Long, Depth> implements DepthDao {
	public DepthDaoImpl() {
		super();
	}

	@Override
	public Depth findByDeep(BigDecimal deep,Long oid) {
		QDepth qDepth = QDepth.depth;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qDepth)
		.where(qDepth.deep.eq(deep).and(qDepth.oblique.id.eq(oid)));
		List<Depth> cs = query.list(qDepth);
		if(cs!=null && cs.size()>0){
			return cs.get(0);
		}
		return null;
	}

	@Override
	public List<Depth> getByOid(Long oid) {
		QDepth qDepth = QDepth.depth;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qDepth)
		.where(qDepth.oblique.id.eq(oid));
		List<Depth> cs = query.list(qDepth);
		return cs;
	}
}