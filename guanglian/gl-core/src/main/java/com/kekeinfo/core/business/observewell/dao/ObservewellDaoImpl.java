package com.kekeinfo.core.business.observewell.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.constructionsite.model.QConstructionSite;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.monitordata.model.HpwellData;
import com.kekeinfo.core.business.monitordata.model.PwellData;
import com.kekeinfo.core.business.monitordata.model.WpwellData;
import com.kekeinfo.core.business.observewell.model.Observewell;
import com.kekeinfo.core.business.observewell.model.QObservewell;
import com.kekeinfo.core.business.pointlink.model.QObserveLink;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("observewellDao")
public class ObservewellDaoImpl extends KekeinfoEntityDaoImpl<Long, Observewell> implements ObservewellDao {

	public ObservewellDaoImpl() {
		super();
	}

	@Override
	public Observewell getByIdWithCSite(Long pid) throws ServiceException {
		// TODO Auto-generated method stub
		QObservewell qoObservewell = QObservewell.observewell;
		QConstructionSite qContact = QConstructionSite.constructionSite;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qoObservewell)
		.leftJoin(qoObservewell.cSite,qContact).fetch()
		.leftJoin(qoObservewell.pointInfo).fetch()
		.where(qoObservewell.id.eq(pid));
		
		List<Observewell> obs=query.list(qoObservewell);
		if(obs!=null && obs.size()>0){
			return obs.get(0);
		}
		return null;
	}
	public Observewell getByIdWithPointLink(Long pid) throws ServiceException {
		QObservewell qoObservewell = QObservewell.observewell;
		QObserveLink qLink = QObserveLink.observeLink;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qoObservewell)
		.leftJoin(qoObservewell.pointLink,qLink).fetch()
		.leftJoin(qLink.gateway).fetch()
		.where(qoObservewell.id.eq(pid));
		
		List<Observewell> obs=query.list(qoObservewell);
		if(obs!=null && obs.size()>0){
			return obs.get(0);
		}
		return null;
	}
	

	@Override
	public List<Observewell> getWarningData(List<Long> ids) throws ServiceException {
		// TODO Auto-generated method stub
		QObservewell qoObservewell = QObservewell.observewell;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qoObservewell)
		.leftJoin(qoObservewell.cSite).fetch();
		
		BooleanBuilder pBuilder = new BooleanBuilder();
		pBuilder.and(qoObservewell.dataStatus.gt(0).or(qoObservewell.powerStatus.gt(1)));
		if(ids!=null){
			pBuilder.and(qoObservewell.cSite.id.in(ids));
		}
		query.where(pBuilder);
		return query.list(qoObservewell);
	}
	
	/**
	 * 根据项目id和观测井状态，获取观测井集合
	 * @param pid
	 * @param status
	 * @return
	 * @throws ServiceException
	 */
	public List<Observewell> getListByPidAndPowerStatus(Long pid, Integer[] status) throws ServiceException{
		QObservewell qObservewell = QObservewell.observewell;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qObservewell);
		
		BooleanBuilder pBuilder = new BooleanBuilder();
		if(pid!=null){
			pBuilder.and(qObservewell.cSite.id.eq(pid));
		}
		if (status!=null) {
			pBuilder.and(qObservewell.powerStatus.in(status));
		}
		query.where(pBuilder).orderBy(qObservewell.name.asc());
		return query.list(qObservewell);
	}

	@Override
	public List<Observewell> getBycid(Long cid) throws ServiceException {
		// TODO Auto-generated method stub
		QObservewell qoObservewell = QObservewell.observewell;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qoObservewell)
		.leftJoin(qoObservewell.pointInfo).fetch()
		.where(qoObservewell.cSite.id.eq(cid));
		
		return query.list(qoObservewell);
	}

	@Override
	public void batchSavePwellData(List<HpwellData> hpwellList) throws ServiceException {
		EntityManager entityManager = getEntityManager();
		for (HpwellData hpwellData : hpwellList) {
			entityManager.persist(hpwellData);
			PwellData pwellData = new PwellData();
			try {
				PropertyUtils.copyProperties(pwellData, hpwellData);
				pwellData.setId(null);
				pwellData.setHid(hpwellData.getId());
				entityManager.persist(pwellData);
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				e.printStackTrace();
				throw new ServiceException(e);
			}
		}
		entityManager.flush();
        entityManager.clear();
		
	}

	@Override
	public List<Observewell> getByIds(List<Long> ids) throws ServiceException {
		QObservewell qoObservewell = QObservewell.observewell;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qoObservewell)
		.where(qoObservewell.id.in(ids));
		return query.list(qoObservewell);
	}

	@Override
	public void batchSaveWpwellData(List<WpwellData> wpwellDataList) {
		EntityManager entityManager = getEntityManager();
		for (WpwellData wpwellData : wpwellDataList) {
			entityManager.persist(wpwellData);
		}
		entityManager.flush();
        entityManager.clear();
	}
	
}
