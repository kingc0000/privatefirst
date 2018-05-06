package com.kekeinfo.core.business.deformmonitor.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.constructionsite.model.QConstructionSite;
import com.kekeinfo.core.business.deformmonitor.model.Deformmonitor;
import com.kekeinfo.core.business.deformmonitor.model.QDeformmonitor;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.pointlink.model.QDeformLink;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("deformmonitorDao")
public class DeformmonitorDaoImpl extends KekeinfoEntityDaoImpl<Long, Deformmonitor> implements DeformmonitorDao {

	public DeformmonitorDaoImpl() {
		super();
	}

	@Override
	public Deformmonitor getByIdWithCSite(Long pid) throws ServiceException {
		// TODO Auto-generated method stub
		QDeformmonitor qDeformmonitor = QDeformmonitor.deformmonitor;
		QConstructionSite qContact = QConstructionSite.constructionSite;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qDeformmonitor)
		.leftJoin(qDeformmonitor.cSite,qContact).fetch()
		.where(qDeformmonitor.id.eq(pid));
		
		return query.uniqueResult(qDeformmonitor);
	}
	
	public Deformmonitor getByIdWithPointLink(Long pid) throws ServiceException {
		QDeformmonitor qDeformmonitor = QDeformmonitor.deformmonitor;
		QDeformLink qLink = QDeformLink.deformLink;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qDeformmonitor)
		.leftJoin(qDeformmonitor.pointLink, qLink).fetch()
		.leftJoin(qLink.gateway).fetch()
		.where(qDeformmonitor.id.eq(pid));
		return query.uniqueResult(qDeformmonitor);
	}

	@Override
	public List<Deformmonitor> getWarningData(List<Long> ids) throws ServiceException {
		// TODO Auto-generated method stub
		QDeformmonitor qDeformmonitor = QDeformmonitor.deformmonitor;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qDeformmonitor)
		.leftJoin(qDeformmonitor.cSite).fetch();
		
		BooleanBuilder pBuilder = new BooleanBuilder();
		//数据告警或断电
		pBuilder.and(qDeformmonitor.dataStatus.gt(0).or(qDeformmonitor.powerStatus.gt(1)));
		if(ids!=null){
			pBuilder.and(qDeformmonitor.cSite.id.in(ids));
		}
		query.where(pBuilder);
		return query.list(qDeformmonitor);
	}

	@Override
	public List<Deformmonitor> getBycid(Long cid) throws ServiceException {
		// TODO Auto-generated method stub
				QDeformmonitor qDeformmonitor = QDeformmonitor.deformmonitor;
				JPQLQuery query = new JPAQuery (getEntityManager());
				query.from(qDeformmonitor)
				.leftJoin(qDeformmonitor.pointInfo).fetch()
				.where(qDeformmonitor.cSite.id.eq(cid));
				
				
				return query.list(qDeformmonitor);
	}

	@Override
	public List<Deformmonitor> getByIds(List<Long> ids) throws ServiceException {
		QDeformmonitor qDeformmonitor = QDeformmonitor.deformmonitor;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qDeformmonitor)
		.where(qDeformmonitor.id.in(ids));
		return query.list(qDeformmonitor);
	}
	
}
