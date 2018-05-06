package com.kekeinfo.core.business.monitor.report.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.monitor.model.MPointEnumType;
import com.kekeinfo.core.business.mreport.model.RBuildingData;
import com.kekeinfo.core.business.mreport.model.RDisplacementData;
import com.kekeinfo.core.business.mreport.model.RHiddenLineData;
import com.kekeinfo.core.business.mreport.model.RMBaseData;
import com.kekeinfo.core.business.mreport.model.RRingBeamData;
import com.kekeinfo.core.business.mreport.model.RSupAxialData;
import com.kekeinfo.core.business.mreport.model.RSurfaceData;
import com.kekeinfo.core.business.mreport.model.RUpRightData;
import com.kekeinfo.core.business.mreport.model.RWaterLineData;

public abstract class RMBaseDataDaoImpl<E> extends KekeinfoEntityDaoImpl<Long, RMBaseData<E>> implements RMBaseDataDao<E>{

	public RMBaseDataDaoImpl() {
		super();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<RMBaseData<E>> getByRid(long rid,MPointEnumType type){
		Class clazz = getDataClassType(type);
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery c = cb.createQuery(clazz);
		Root root = c.from(clazz);
		c.select(root);
		c.distinct(true);
		List<Predicate> criteList = new ArrayList<Predicate>();
		criteList.add(cb.equal(root.get("report"), rid));
		c.where(cb.and(criteList.toArray(new Predicate[0])));  
		TypedQuery q = getEntityManager().createQuery(c); 
		List<RMBaseData<E>> cs = q.getResultList();
		return cs;
	}
	
	@SuppressWarnings("rawtypes")
	private Class getDataClassType (MPointEnumType type) {
		Class clazz = null;
		switch (type) {
		case Surface:
			clazz = RSurfaceData.class;
			break;
		case Building:
			clazz = RBuildingData.class;
			break;
		case WaterLine:
			clazz = RWaterLineData.class;
			break;
		case RingBeam:
			clazz = RRingBeamData.class;
			break;
		case SupAxial:
			clazz = RSupAxialData.class;
			break;
		case UpRight:
			clazz = RUpRightData.class;
			break;
		case HiddenLine:
			clazz = RHiddenLineData.class;
			break;
		case Displacement:
			clazz = RDisplacementData.class;
			break;
		default:
			break;
		}
		return clazz;
	}
}
