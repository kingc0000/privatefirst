package com.kekeinfo.core.business.monitor.statistical.dao;


import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.monitor.statistical.model.Mstatistical;
import com.kekeinfo.core.business.monitor.statistical.model.QMstatistical;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("mstatisticalDao")
public class MstatisticalDaoImpl extends KekeinfoEntityDaoImpl<Long, Mstatistical> implements MstatisticalDao {
	
	public MstatisticalDaoImpl() {
		super();
	}

	@Override
	public Mstatistical getByDate(Date date) {
		QMstatistical qContact = QMstatistical.mstatistical;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact)
		.where(qContact.mDaily.datec.eq(date));
		List<Mstatistical> cs =query.list(qContact);
		if(cs!=null && cs.size()>0){
			return cs.get(0);
		}
		return null;
	}

}