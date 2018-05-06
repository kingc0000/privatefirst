package com.kekeinfo.core.business.greport.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.greport.model.GMBaseData;
import com.kekeinfo.core.business.greport.model.RDiameterConvertData;
import com.kekeinfo.core.business.greport.model.RHshiftData;
import com.kekeinfo.core.business.greport.model.RVerticalDisData;
import com.kekeinfo.core.business.guard.model.GPointEnumType;

@Repository("gmbasedataDao")
public abstract class GMBaseDataDaoImpl<E> extends KekeinfoEntityDaoImpl<Long, GMBaseData<E>> implements GMBaseDataDao<E> {
	public GMBaseDataDaoImpl() {
		super();
	}
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<GMBaseData<E>> getByGid(long rid,GPointEnumType type){
		Class clazz = getDataClassType(type);
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery c = cb.createQuery(clazz);
		Root root = c.from(clazz);
		c.select(root);
		c.distinct(true);
		Path path =root.get("markNO");
		c.orderBy(cb.asc(path));
		List<Predicate> criteList = new ArrayList<Predicate>();
		criteList.add(cb.equal(root.get("greport"), rid));
		c.where(cb.and(criteList.toArray(new Predicate[0])));  
		TypedQuery q = getEntityManager().createQuery(c); 
		List<GMBaseData<E>> cs = q.getResultList();
		return cs;
	}
	
	@SuppressWarnings({ "rawtypes" })
	private Class getDataClassType (GPointEnumType type) {
		Class clazz = null;
		switch (type) {
		case VerticalDis:
			clazz = RVerticalDisData.class;
			break;
		case DiameterConvert:
			clazz = RDiameterConvertData.class;
			break;
		case Hshift:
			clazz=RHshiftData.class;
			break;
		}
		return clazz;
	}
}