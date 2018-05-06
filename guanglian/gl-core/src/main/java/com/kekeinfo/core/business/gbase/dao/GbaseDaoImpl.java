package com.kekeinfo.core.business.gbase.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.guard.model.GPointEnumType;
import com.kekeinfo.core.business.guard.point.model.DiameterConvert;
import com.kekeinfo.core.business.guard.point.model.GbasePoint;
import com.kekeinfo.core.business.guard.point.model.Hshift;
import com.kekeinfo.core.business.guard.point.model.VerticalDis;

public abstract class GbaseDaoImpl<E> extends KekeinfoEntityDaoImpl<Long, GbasePoint<E>> implements GbaseDao<E> {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public GbasePoint<E> getById(long id,GPointEnumType type){
		Class clazz = getDataClassType(type);
			CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
			CriteriaQuery c = cb.createQuery(clazz);
			Root root = c.from(clazz);
			c.select(root);
			c.distinct(true);
			List<Predicate> criteList = new ArrayList<Predicate>();
			criteList.add(cb.equal(root.get("id"),id));
			c.where(cb.and(criteList.toArray(new Predicate[0])));  
			TypedQuery q = getEntityManager().createQuery(c);  
			return (GbasePoint<E>) q.getSingleResult();
	 }
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<GbasePoint<E>> getByMid(long id,GPointEnumType type){
		 Class clazz = getDataClassType(type);
			CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
			CriteriaQuery c = cb.createQuery(clazz);
			Root root = c.from(clazz);
			c.select(root);
			c.distinct(true);
			List<Predicate> criteList = new ArrayList<Predicate>();
			criteList.add(cb.equal(root.get("guard"),id));
			c.where(cb.and(criteList.toArray(new Predicate[0])));  
			TypedQuery q = getEntityManager().createQuery(c);  
			return   q.getResultList();
	 }
	 @SuppressWarnings({ "rawtypes", "unchecked" })
		public GbasePoint<E> getByNO(String name,GPointEnumType type,long id){
			 Class clazz = getDataClassType(type);
				CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
				CriteriaQuery c = cb.createQuery(clazz);
				Root root = c.from(clazz);
				c.select(root);
				c.distinct(true);
				List<Predicate> criteList = new ArrayList<Predicate>();
				criteList.add(cb.equal(root.get("guard"),id));
				criteList.add(cb.equal(root.get("markNO"),name));
				c.where(cb.and(criteList.toArray(new Predicate[0])));  
				TypedQuery q = getEntityManager().createQuery(c);  
				return (GbasePoint<E>) q.getSingleResult();
		 }
	 
	 @SuppressWarnings("rawtypes")
		private Class getDataClassType (GPointEnumType type) {
			Class clazz = null;
			switch (type) {
			case VerticalDis:
				clazz = VerticalDis.class;
				break;
			case DiameterConvert:
				clazz = DiameterConvert.class;
				break;
			case Hshift:
				clazz = Hshift.class;
				break;	
			default:
				break;
			}
			return clazz;
		}
	 
}
