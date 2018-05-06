package com.kekeinfo.core.business.guard.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.guard.model.Guard;
import com.kekeinfo.core.business.guard.model.QGuard;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("guardDao")
public class GuardDaoImpl extends KekeinfoEntityDaoImpl<Long, Guard> implements GuardDao {
	public GuardDaoImpl() {
		super();
	}

	@Override
	public Guard getByIdWithImg(long cid) throws ServiceException {
		// TODO Auto-generated method stub
		QGuard qContact = QGuard.guard;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact)
		.leftJoin(qContact.images).fetch()
		.where(qContact.id.eq(cid));
		List<Guard> cs =query.list(qContact);
		if(cs!=null && cs.size()>0){
			return cs.get(0);
		}
		return null;
	}

	@Override
	public Guard getByCid(long cid) throws ServiceException {
		// TODO Auto-generated method stub
		QGuard qContact = QGuard.guard;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact)
		.leftJoin(qContact.images).fetch()
		.leftJoin(qContact.project).fetch()
		.where(qContact.id.eq(cid));
		List<Guard> cs =query.list(qContact);
		if(cs!=null && cs.size()>0){
			return cs.get(0);
		}
		return null;
	}

	@Override
	public List<Guard> withProject() throws ServiceException {
		// TODO Auto-generated method stub
				QGuard qContact = QGuard.guard;
				JPQLQuery query = new JPAQuery (getEntityManager());
				query.from(qContact)
				.leftJoin(qContact.project).fetch();
				List<Guard> cs =query.list(qContact);
				return cs;
	}

	@Override
	public List<Guard> getByPid(long cid) throws ServiceException {
		QGuard qContact = QGuard.guard;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact).where(qContact.project.id.eq(cid));
		List<Guard> cs =query.list(qContact);
		return cs;
	}

}