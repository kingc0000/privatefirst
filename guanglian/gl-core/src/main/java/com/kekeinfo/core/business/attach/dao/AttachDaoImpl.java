package com.kekeinfo.core.business.attach.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.kekeinfo.core.business.attach.model.Attach;
import com.kekeinfo.core.business.attach.model.QAttach;

@Repository("attachDao")
public class AttachDaoImpl extends KekeinfoEntityDaoImpl<Long, Attach> implements AttachDao {
	public AttachDaoImpl() {
		super();
	}

	@Override
	public Attach getByName(String name) {
		QAttach qBaseData = QAttach.attach;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qBaseData)	
			.where(qBaseData.fileName.endsWithIgnoreCase(name));
		List<Attach> as = query.list(qBaseData);
		if(as!=null && as.size()>0) return as.get(0);
		return null;
	}
}