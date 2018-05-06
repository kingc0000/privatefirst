package com.kekeinfo.core.business.gbase.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.guard.data.model.DiameterConvertData;
import com.kekeinfo.core.business.guard.data.model.GDbasePoint;
import com.kekeinfo.core.business.guard.data.model.HshiftData;
import com.kekeinfo.core.business.guard.data.model.VerticalDisData;
import com.kekeinfo.core.business.guard.model.GPointEnumType;

public abstract class GDbaseDaoImpl<E> extends KekeinfoEntityDaoImpl<Long, GDbasePoint<E>> implements GDbaseDao<E>{
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public GDbasePoint<E> getLast(Date date,GPointEnumType type,long sid){
		Class clazz = getDataClassType(type);
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery c = cb.createQuery(clazz);
		Root root = c.from(clazz);
		c.select(root);
		c.distinct(true);
		Path path =root.get("calibration");
		c.orderBy(cb.desc(path));
		List<Predicate> criteList = new ArrayList<Predicate>();
		criteList.add(cb.equal(root.get("spoint"), sid));
		criteList.add(cb.lessThan(root.get("calibration"), date));
		c.where(cb.and(criteList.toArray(new Predicate[0])));  
		TypedQuery q = getEntityManager().createQuery(c); 
		List<GDbasePoint<E>> cs = q.getResultList();
		if(cs!=null && cs.size()>0){
			return cs.get(0);
		}else {
			return null;
		}
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public GDbasePoint<E>  getMax(Date date,GPointEnumType type,String mids){
		Class clazz = getDataClassType(type);
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery c = cb.createQuery(clazz);
		Root root = c.from(clazz);
		c.select(root);
		c.distinct(true);
		List<Predicate> criteList = new ArrayList<Predicate>();
		Expression<String> exp = root.get("spoint");
		String[] ids =mids.split(",");
		List<String> strList=new ArrayList<>();	
		for(String s:ids){
			strList.add(s);
		}
		criteList.add(exp.in(strList));

		Expression<Number> subItem =cb.sum(root.get("curtHeight"),cb.neg(root.get("initHeight")));
		cb.max(subItem);
		c.where(cb.and(criteList.toArray(new Predicate[0])));  
		TypedQuery q = getEntityManager().createQuery(c); 
		try {
			Object cs= q.getSingleResult();
			if(cs!=null) {
				return (GDbasePoint<E>) q.getSingleResult();
			}
		}catch (Exception e) {
		}
		
		
		return null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public GDbasePoint<E> getByDate(Date date, long sid,GPointEnumType type) {
		try {
			Class clazz = getDataClassType(type);
			CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
			CriteriaQuery c = cb.createQuery(clazz);
			Root root = c.from(clazz);
			c.select(root);
			c.distinct(true);
			List<Predicate> criteList = new ArrayList<Predicate>();
			//criteList.add(cb.equal(root.get("calibration"), date));
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			cal.set(Calendar.MILLISECOND, 999);
			Date date1= cal.getTime();
			criteList.add(cb.between(root.<Date>get("calibration"),date, date1));
			criteList.add(cb.equal(root.get("spoint"), sid));
			c.where(cb.and(criteList.toArray(new Predicate[0])));
			c.orderBy(cb.desc(root.get("calibration")));
			
			TypedQuery q = getEntityManager().createQuery(c);
			q.setMaxResults(1);
			List<GDbasePoint<E>> cs = q.getResultList();
			if(cs!=null && cs.size()>0){
				return cs.get(0);
			}else {
				return null;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
			
		}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public GDbasePoint<E> getNext(Date date, GPointEnumType type,long sid) {
		Class clazz = getDataClassType(type);
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery c = cb.createQuery(clazz);
		Root root = c.from(clazz);
		c.select(root);
		c.distinct(true);
		Path path =root.get("calibration");
		c.orderBy(cb.asc(path));
		List<Predicate> criteList = new ArrayList<Predicate>();
		criteList.add(cb.equal(root.get("spoint"), sid));
		criteList.add(cb.greaterThan(root.get("calibration"), date));
		c.where(cb.and(criteList.toArray(new Predicate[0])));  
		TypedQuery q = getEntityManager().createQuery(c); 
		List<GDbasePoint<E>> cs = q.getResultList();
		if(cs!=null && cs.size()>0){
			return cs.get(0);
		}else {
			return null;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public GDbasePoint<E> getEqualsHeightData(BigDecimal initHeight,long sid,GPointEnumType type){
		Class clazz = getDataClassType(type);
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery c = cb.createQuery(clazz);
		Root root = c.from(clazz);
		c.select(root);
		c.distinct(true);
		Path path =root.get("calibration");
		c.orderBy(cb.asc(path));
		List<Predicate> criteList = new ArrayList<Predicate>();
		criteList.add(cb.equal(root.get("spoint"), sid));
		criteList.add(cb.equal(root.get("initHeight"), initHeight));
		c.where(cb.and(criteList.toArray(new Predicate[0])));  
		TypedQuery q = getEntityManager().createQuery(c); 
		List<GDbasePoint<E>> cs = q.getResultList();
		if(cs!=null && cs.size()>0){
			return cs.get(0);
		}else {
			return null;
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public GDbasePoint<E> getByIdWithPoint(long id,GPointEnumType type){
		 Class clazz = getDataClassType(type);
			CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
			CriteriaQuery c = cb.createQuery(clazz);
			Root root = c.from(clazz);
			c.select(root);
			c.distinct(true);
			root.fetch("spoint", JoinType.LEFT);
			List<Predicate> criteList = new ArrayList<Predicate>();
			criteList.add(cb.equal(root.get("id"),id));
			c.where(cb.and(criteList.toArray(new Predicate[0])));  
			TypedQuery q = getEntityManager().createQuery(c);  
			return (GDbasePoint<E>) q.getSingleResult();
	}
	
	@SuppressWarnings("rawtypes")
	private Class getDataClassType (GPointEnumType type) {
		Class clazz = null;
		switch (type) {
		case VerticalDis:
			clazz = VerticalDisData.class;
			break;
		case DiameterConvert:
			clazz = DiameterConvertData.class;
			break;
		case Hshift:
			clazz = HshiftData.class;
			break;		
		default:
			break;
		}
		return clazz;
	}

}
