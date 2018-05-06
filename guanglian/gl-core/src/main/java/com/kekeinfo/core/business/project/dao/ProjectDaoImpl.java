package com.kekeinfo.core.business.project.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.project.model.Project;
import com.kekeinfo.core.business.project.model.QProject;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("projectDao")
public class ProjectDaoImpl extends KekeinfoEntityDaoImpl<Long, Project> implements ProjectDao {
	public ProjectDaoImpl() {
		super();
	}

	@Override
	public Project getByIdWithDepartment(long pid) throws ServiceException {
		// TODO Auto-generated method stub
				QProject qContact = QProject.project;
				JPQLQuery query = new JPAQuery (getEntityManager());
				query.from(qContact)
				.leftJoin(qContact.department).fetch()
				.leftJoin(qContact.zone).fetch()
				.where(qContact.id.eq(pid));
				List<Project> cs =query.list(qContact);
				if(cs!=null && cs.size()>0){
					return cs.get(0);
				}
				return null;
	}

	@Override
	public List<Project> withGroupUser() {
		QProject qContact = QProject.project;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact)
		.leftJoin(qContact.wUsers).fetch()
		.leftJoin(qContact.cUsers).fetch();
		List<Project> cs =query.list(qContact);
		return cs;
	}

	@Override
	public Project withUserGroup(long pid) throws ServiceException {
		QProject qContact = QProject.project;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact)
		.leftJoin(qContact.wUsers).fetch()
		.leftJoin(qContact.cUsers).fetch()
		.where(qContact.id.eq(pid));
		List<Project> cs =query.list(qContact);
		if(cs!=null && cs.size()>0){
			return cs.get(0);
		}
		return null;
	}
}