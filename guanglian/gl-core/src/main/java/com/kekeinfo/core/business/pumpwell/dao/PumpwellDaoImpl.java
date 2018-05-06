package com.kekeinfo.core.business.pumpwell.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.constructionsite.model.QConstructionSite;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.monitordata.model.HowellData;
import com.kekeinfo.core.business.monitordata.model.OwellData;
import com.kekeinfo.core.business.monitordata.model.WowellData;
import com.kekeinfo.core.business.pointlink.model.QPumpLink;
import com.kekeinfo.core.business.pumpwell.model.Pumpwell;
import com.kekeinfo.core.business.pumpwell.model.QPumpwell;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("pumpwellDao")
public class PumpwellDaoImpl extends KekeinfoEntityDaoImpl<Long, Pumpwell> implements PumpwellDao {

	public PumpwellDaoImpl() {
		super();
	}

	@Override
	public Pumpwell getByIdWithCSite(Long pid) throws ServiceException {
		QPumpwell qPumpwell = QPumpwell.pumpwell;
		QConstructionSite qContact = QConstructionSite.constructionSite;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qPumpwell)
		.leftJoin(qPumpwell.cSite,qContact).fetch()
		.leftJoin(qPumpwell.pointInfo).fetch()
		.where(qPumpwell.id.eq(pid));
		
		return query.uniqueResult(qPumpwell);
	}
	@Override
	public Pumpwell getByIdWithPointLink(Long pid) throws ServiceException {
		QPumpwell qPumpwell = QPumpwell.pumpwell;
		QPumpLink qLink = QPumpLink.pumpLink;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qPumpwell)
		.leftJoin(qPumpwell.pointLink, qLink).fetch()
		.leftJoin(qLink.gateway).fetch()
		.where(qPumpwell.id.eq(pid));
		return query.uniqueResult(qPumpwell);
	}

	@Override
	public List<Pumpwell> getWarningData(List<Long> ids) throws ServiceException {
		// TODO Auto-generated method stub
		QPumpwell qPumpwell = QPumpwell.pumpwell;
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
	public List<Pumpwell> getListByPidAndPowerStatus(Long pid, Integer[] status) throws ServiceException {
		QPumpwell qPumpwell = QPumpwell.pumpwell;
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
	
	/**
	 * 获取指定网关的所有抽水井测点，如果网关id为null，则获取所有网关不为空的抽水井测点
	 * @param gateway
	 * @return
	 * @throws ServiceException
	 */
	public List<Pumpwell> getListByGateway(Long gateway) throws ServiceException {
//		QPumpwell qPumpwell = QPumpwell.pumpwell;
//		JPQLQuery query = new JPAQuery (getEntityManager());
//		query.from(qPumpwell).leftJoin(qPumpwell.gateway).fetch().leftJoin(qPumpwell.cSite);
//		
//		BooleanBuilder pBuilder = new BooleanBuilder();
//		if(gateway!=null){
//			pBuilder.and(qPumpwell.basepointLink.gateway.id.eq(gateway));
//		} else {
//			pBuilder.and(qPumpwell.basepointLink.gateway.id.isNotNull());
//		}
//		query.where(pBuilder).orderBy(qPumpwell.cSite.id.asc());
//		return query.list(qPumpwell);
//		
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

		CriteriaQuery<Pumpwell> c = cb.createQuery(Pumpwell.class);
		Root<Pumpwell> pumpwell = c.from(Pumpwell.class);
//		Join<Pumpwell, PumpLink> = pumpwell.join("pointLink");
		c.select(pumpwell);
		c.distinct(true);
		pumpwell.fetch("pointLink").fetch("gateway", JoinType.LEFT);
		pumpwell.fetch("cSite", JoinType.LEFT);

		List<Predicate> criteList = new ArrayList<Predicate>();
		if (gateway != null) {
			criteList.add(cb.equal(pumpwell.get("pointLink").get("gateway").get("id"), gateway));  
		} else {
			criteList.add(cb.gt(pumpwell.get("pointLink").get("gateway").get("id"), 0));
		}
		if (criteList.size() == 1) {  
			c.where(criteList.get(0));  
		} else {  
			c.where(cb.and(criteList.toArray(new Predicate[0])));  
		}
		
		TypedQuery<Pumpwell> q = getEntityManager().createQuery(c);  
	    return q.getResultList();  
	}

	@Override
	public List<Pumpwell> getByCid(Long cid) throws ServiceException {
		// TODO Auto-generated method stub
		QPumpwell qPumpwell = QPumpwell.pumpwell;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qPumpwell)
		.leftJoin(qPumpwell.pointInfo).fetch()
		.where(qPumpwell.cSite.id.eq(cid));
		return query.list(qPumpwell);
	}

	@Override
	public void batchSaveOwellData(List<HowellData> howellList) throws ServiceException {
		EntityManager entityManager = getEntityManager();
		for (HowellData howellData : howellList) {
			entityManager.persist(howellData);
			OwellData owellData = new OwellData();
			try {
				PropertyUtils.copyProperties(owellData, howellData);
				owellData.setId(null);
				owellData.setHid(howellData.getId());
				entityManager.persist(owellData);
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				e.printStackTrace();
				throw new ServiceException(e);
			}
		}
		entityManager.flush();
        entityManager.clear();
	}
	
	/**
	 * 根据项目id和抽水井状态，获取抽水井集合
	 * @param pid
	 * @param status
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<Pumpwell> getByIds(List<Long> ids) throws ServiceException {
		QPumpwell qPumpwell = QPumpwell.pumpwell;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qPumpwell)
		.where(qPumpwell.id.in(ids));
		
		return query.list(qPumpwell);

	}
	
	@Override
	public void batchSaveWowellData(List<WowellData> wowellList) throws ServiceException {
		EntityManager entityManager = getEntityManager();
		for (WowellData wowellData : wowellList) {
			entityManager.persist(wowellData);
		}
		entityManager.flush();
        entityManager.clear();
	}
}
