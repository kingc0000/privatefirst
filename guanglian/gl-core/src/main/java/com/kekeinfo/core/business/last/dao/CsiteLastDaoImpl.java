package com.kekeinfo.core.business.last.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.last.model.CsiteLast;
import com.kekeinfo.core.business.last.model.QCsiteLast;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("csitelastDao")
public class CsiteLastDaoImpl extends KekeinfoEntityDaoImpl<Long, CsiteLast> implements CsiteLastDao {
	public CsiteLastDaoImpl() {
		super();
	}

	@Override
	public List<CsiteLast> getByUserID(long userid) {
		// TODO Auto-generated method stub
		JPQLQuery query = new JPAQuery (getEntityManager());
		QCsiteLast csiteLast = QCsiteLast.csiteLast;
		query.from(csiteLast)
		.where(csiteLast.uid.eq(userid))
		.orderBy(csiteLast.dateModified.desc());
		query.limit(5);
		List<CsiteLast> cs =query.list(csiteLast);
		/**
		if(cs.size()>5){
			for(int i=5;i<cs.size();i++){
				this.delete(cs.get(i));
			}
		}*/
		return cs;
	}
}