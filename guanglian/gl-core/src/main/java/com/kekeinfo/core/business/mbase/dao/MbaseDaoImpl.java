package com.kekeinfo.core.business.mbase.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.monitor.model.MPointEnumType;
import com.kekeinfo.core.business.monitor.surface.model.Building;
import com.kekeinfo.core.business.monitor.surface.model.Displacement;
import com.kekeinfo.core.business.monitor.surface.model.HiddenLine;
import com.kekeinfo.core.business.monitor.surface.model.MbasePoint;
import com.kekeinfo.core.business.monitor.surface.model.RingBeam;
import com.kekeinfo.core.business.monitor.surface.model.SupAxial;
import com.kekeinfo.core.business.monitor.surface.model.Surface;
import com.kekeinfo.core.business.monitor.surface.model.UpRight;
import com.kekeinfo.core.business.monitor.surface.model.WaterLine;

public abstract class MbaseDaoImpl<E> extends KekeinfoEntityDaoImpl<Long, MbasePoint<E>> implements MbaseDao<E>{

	public MbaseDaoImpl() {
		super();
	}
	 @SuppressWarnings({ "rawtypes", "unchecked" })
	public MbasePoint<E> getById(long id,MPointEnumType type){
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
			return (MbasePoint<E>) q.getSingleResult();
	 }
	 
	 @SuppressWarnings({ "rawtypes", "unchecked" })
	public MbasePoint<E> getByNO(String name,MPointEnumType type,long id){
		 Class clazz = getDataClassType(type);
			CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
			CriteriaQuery c = cb.createQuery(clazz);
			Root root = c.from(clazz);
			c.select(root);
			c.distinct(true);
			List<Predicate> criteList = new ArrayList<Predicate>();
			criteList.add(cb.equal(root.get("monitor"),id));
			criteList.add(cb.equal(root.get("markNO"),name));
			c.where(cb.and(criteList.toArray(new Predicate[0])));  
			TypedQuery q = getEntityManager().createQuery(c);  
			return (MbasePoint<E>) q.getSingleResult();
	 }
	 
	 @SuppressWarnings({ "rawtypes", "unchecked" })
	public List<MbasePoint<E>> getByMid(long id,MPointEnumType type){
		 Class clazz = getDataClassType(type);
			CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
			CriteriaQuery c = cb.createQuery(clazz);
			Root root = c.from(clazz);
			c.select(root);
			c.distinct(true);
			List<Predicate> criteList = new ArrayList<Predicate>();
			criteList.add(cb.equal(root.get("monitor"),id));
			c.where(cb.and(criteList.toArray(new Predicate[0])));  
			TypedQuery q = getEntityManager().createQuery(c);  
			return   q.getResultList();
	 }
	 
	 @SuppressWarnings("rawtypes")
		private Class getDataClassType (MPointEnumType type) {
			Class clazz = null;
			switch (type) {
			case Surface:
				clazz = Surface.class;
				break;
			case Building:
				clazz = Building.class;
				break;
			case WaterLine:
				clazz = WaterLine.class;
				break;
			case RingBeam:
				clazz = RingBeam.class;
				break;
			case SupAxial:
				clazz = SupAxial.class;
				break;
			case UpRight:
				clazz = UpRight.class;
				break;
			case HiddenLine:
				clazz = HiddenLine.class;
				break;
			case Displacement:
				clazz = Displacement.class;
				break;
			default:
				break;
			}
			return clazz;
		}
}
