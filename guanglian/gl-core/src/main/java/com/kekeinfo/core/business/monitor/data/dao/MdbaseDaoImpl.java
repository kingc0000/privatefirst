package com.kekeinfo.core.business.monitor.data.dao;

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
import com.kekeinfo.core.business.monitor.data.model.BuildingData;
import com.kekeinfo.core.business.monitor.data.model.DisplacementData;
import com.kekeinfo.core.business.monitor.data.model.HiddenLineData;
import com.kekeinfo.core.business.monitor.data.model.MDbasePoint;
import com.kekeinfo.core.business.monitor.data.model.RingBeamData;
import com.kekeinfo.core.business.monitor.data.model.SupAxialData;
import com.kekeinfo.core.business.monitor.data.model.SurfaceData;
import com.kekeinfo.core.business.monitor.data.model.UpRightData;
import com.kekeinfo.core.business.monitor.data.model.WaterLineData;
import com.kekeinfo.core.business.monitor.model.MPointEnumType;

public abstract class MdbaseDaoImpl<E> extends KekeinfoEntityDaoImpl<Long, MDbasePoint<E>> implements MdbaseDao<E>{

	public MdbaseDaoImpl() {
		super();
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public MDbasePoint<E> getLast(Date date,MPointEnumType type,long sid){
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
		List<MDbasePoint<E>> cs = q.getResultList();
		if(cs!=null && cs.size()>0){
			return cs.get(0);
		}else {
			return null;
		}
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public MDbasePoint<E>  getMax(Date date,MPointEnumType type,String mids){
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
				return (MDbasePoint<E>) q.getSingleResult();
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		
		return null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public MDbasePoint<E> getByDate(Date date, long sid,MPointEnumType type) {
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
			List<MDbasePoint<E>> cs = q.getResultList();
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
	public MDbasePoint<E> getNext(Date date, MPointEnumType type,long sid) {
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
		List<MDbasePoint<E>> cs = q.getResultList();
		if(cs!=null && cs.size()>0){
			return cs.get(0);
		}else {
			return null;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public MDbasePoint<E> getEqualsHeightData(BigDecimal initHeight,long sid,MPointEnumType type){
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
		List<MDbasePoint<E>> cs = q.getResultList();
		if(cs!=null && cs.size()>0){
			return cs.get(0);
		}else {
			return null;
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public MDbasePoint<E> getByIdWithPoint(long id,MPointEnumType type){
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
			return (MDbasePoint<E>) q.getSingleResult();
	}
	
	@SuppressWarnings("rawtypes")
	private Class getDataClassType (MPointEnumType type) {
		Class clazz = null;
		switch (type) {
		case Surface:
			clazz = SurfaceData.class;
			break;
		case Building:
			clazz = BuildingData.class;
			break;
		case WaterLine:
			clazz = WaterLineData.class;
			break;
		case RingBeam:
			clazz = RingBeamData.class;
			break;
		case SupAxial:
			clazz = SupAxialData.class;
			break;
		case UpRight:
			clazz = UpRightData.class;
			break;
		case HiddenLine:
			clazz = HiddenLineData.class;
			break;
		case Displacement:
			clazz = DisplacementData.class;
			break;
		default:
			break;
		}
		return clazz;
	}
}
