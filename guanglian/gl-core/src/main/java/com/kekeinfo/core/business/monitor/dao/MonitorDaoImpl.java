package com.kekeinfo.core.business.monitor.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.monitor.model.Monitor;
import com.kekeinfo.core.business.monitor.model.QMonitor;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("monitorDao")
public class MonitorDaoImpl extends KekeinfoEntityDaoImpl<Long, Monitor> implements MonitorDao {
	public MonitorDaoImpl() {
		super();
	}

	@Override
	public Monitor getByIdWithImg(long cid) throws ServiceException {
		// TODO Auto-generated method stub
				QMonitor qContact = QMonitor.monitor;
				JPQLQuery query = new JPAQuery (getEntityManager());
				query.from(qContact)
				.leftJoin(qContact.images).fetch()
				.where(qContact.id.eq(cid));
				List<Monitor> cs =query.list(qContact);
				if(cs!=null && cs.size()>0){
					return cs.get(0);
				}
				return null;
	}

	@Override
	public Monitor getByCid(long cid) throws ServiceException {
		QMonitor qContact = QMonitor.monitor;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact)
		.leftJoin(qContact.images).fetch()
		.leftJoin(qContact.project).fetch()
		.where(qContact.id.eq(cid));
		List<Monitor> cs =query.list(qContact);
		if(cs!=null && cs.size()>0){
			return cs.get(0);
		}
		return null;
	}

	@Override
	public List<Monitor> withProject() throws ServiceException {
		QMonitor qContact = QMonitor.monitor;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact)
		.leftJoin(qContact.project).fetch();
		List<Monitor> cs =query.list(qContact);
		return cs;
	}

	@Override
	public List<Monitor> getByPid(long cid) throws ServiceException {
		QMonitor qContact = QMonitor.monitor;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact).where(qContact.project.id.eq(cid));
		List<Monitor> cs =query.list(qContact);
		return cs;
	}

}