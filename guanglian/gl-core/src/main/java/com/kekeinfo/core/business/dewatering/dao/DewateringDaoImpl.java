package com.kekeinfo.core.business.dewatering.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.constructionsite.model.QConstructionSite;
import com.kekeinfo.core.business.dewatering.model.Dewatering;
import com.kekeinfo.core.business.dewatering.model.QDewatering;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.pointlink.model.QDewateringLink;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("dewateringDao")
public class DewateringDaoImpl extends KekeinfoEntityDaoImpl<Long, Dewatering> implements DewateringDao {

	public DewateringDaoImpl() {
		super();
	}

	@Override
	public Dewatering getByIdWithCSite(Long pid) throws ServiceException {
		QDewatering qPumpwell = QDewatering.dewatering;
		QConstructionSite qContact = QConstructionSite.constructionSite;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qPumpwell)
		.leftJoin(qPumpwell.cSite,qContact).fetch()
		.leftJoin(qPumpwell.pointInfo).fetch()
		.where(qPumpwell.id.eq(pid));
		
		return query.uniqueResult(qPumpwell);
	}
	
	public Dewatering getByIdWithPointLink(Long pid) throws ServiceException {
		QDewatering qPumpwell = QDewatering.dewatering;
		QDewateringLink qLink = QDewateringLink.dewateringLink;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qPumpwell)
		.leftJoin(qPumpwell.pointLink, qLink).fetch()
		.leftJoin(qLink.gateway).fetch()
		.where(qPumpwell.id.eq(pid));
		
		return query.uniqueResult(qPumpwell);
	}

	@Override
	public List<Dewatering> getWarningData(List<Long> ids) throws ServiceException {
		// TODO Auto-generated method stub
		QDewatering qPumpwell = QDewatering.dewatering;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qPumpwell)
		.leftJoin(qPumpwell.cSite).fetch();
		
		BooleanBuilder pBuilder = new BooleanBuilder();
		pBuilder.and(qPumpwell.dataStatus.gt(0).or(qPumpwell.powerStatus.gt(1)));
		if(ids!=null){
			pBuilder.and(qPumpwell.cSite.id.in(ids));
		}
		query.where(pBuilder);
		return query.list(qPumpwell);
	}

	/**
	 * 根据项目id和抽水井状态，获取抽水井集合
	 * @param pid
	 * @param status
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<Dewatering> getListByPidAndPowerStatus(Long pid, Integer[] status) throws ServiceException {
		QDewatering qPumpwell = QDewatering.dewatering;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qPumpwell);
		
		BooleanBuilder pBuilder = new BooleanBuilder();
		if(pid!=null){
			pBuilder.and(qPumpwell.cSite.id.eq(pid));
		}
		if (status!=null) {
			pBuilder.and(qPumpwell.powerStatus.in(status));
		}
		query.where(pBuilder).orderBy(qPumpwell.name.asc());
		return query.list(qPumpwell);

	}

	@Override
	public List<Dewatering> getByCid(Long cid) throws ServiceException {
		QDewatering qPumpwell = QDewatering.dewatering;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qPumpwell)
		.leftJoin(qPumpwell.pointInfo).fetch()
		.where(qPumpwell.cSite.id.eq(cid));
		
		
		return query.list(qPumpwell);
	}

	@Override
	public List<Dewatering> getByIds(List<Long> ids) throws ServiceException {
		// TODO Auto-generated method stub
				QDewatering qPumpwell = QDewatering.dewatering;
				JPQLQuery query = new JPAQuery (getEntityManager());
				query.from(qPumpwell)
				.where(qPumpwell.id.in(ids));
				return query.list(qPumpwell);
	}
	
}
