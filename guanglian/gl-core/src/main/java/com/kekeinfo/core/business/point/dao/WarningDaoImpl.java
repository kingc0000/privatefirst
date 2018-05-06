package com.kekeinfo.core.business.point.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.generic.model.PointEnumType;
import com.kekeinfo.core.business.monitordata.model.WarningData;
import com.kekeinfo.core.business.monitordata.model.WdewellData;
import com.kekeinfo.core.business.monitordata.model.WemonitorData;
import com.kekeinfo.core.business.monitordata.model.WiwellData;
import com.kekeinfo.core.business.monitordata.model.WowellData;
import com.kekeinfo.core.business.monitordata.model.WpwellData;
import com.kekeinfo.core.business.point.model.Basepoint;
import com.kekeinfo.core.business.point.model.BasepointInfo;
import com.kekeinfo.core.business.point.model.BasepointLink;

@Repository("warningDao")
public class WarningDaoImpl extends KekeinfoEntityDaoImpl<Long, WarningData<Basepoint<BasepointLink<?>, BasepointInfo<?>>>> implements WarningDao {

	public WarningDaoImpl() {
		super();
	}

	@SuppressWarnings("rawtypes")
	private Class getClassType(PointEnumType type) {
		Class clazz = null;
		switch (type) {
		case PUMP:
			clazz = WpwellData.class;
			break;
		case DEWATERING:
			clazz = WdewellData.class;
			break;
		case INVERTED:
			clazz = WiwellData.class;
			break;
		case OBSERVE:
			clazz = WowellData.class;
			break;
		case DEFORM:
			clazz = WemonitorData.class;
			break;
		default:
			break;
		}
		return clazz;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Entitites<WarningData> getListByCid(Long cid, PointEnumType type, Integer warningType, String search, Integer limit, Integer offset) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		Class clazz = getClassType(type);
		CriteriaQuery c = cb.createQuery(clazz);
		Root r = c.from(clazz);
		
		c.select(r);
		c.distinct(true);
		r.fetch("point", JoinType.LEFT);
		//r.fetch("pointInfo", JoinType.LEFT);

		List<Predicate> criteList = new ArrayList<Predicate>();
		criteList.add(cb.equal(r.get("cSite").get("id"), cid));  
		if (warningType!=null) {
			criteList.add(cb.equal(r.get("warningType"), warningType));
		}
		if (StringUtils.isNotBlank(search)) {
			criteList.add(cb.like(r.get("point").get("name"), "%"+search+"%"));
		}
		if (criteList.size() == 1) {  
			c.where(criteList.get(0));  
		} else {  
			c.where(cb.and(criteList.toArray(new Predicate[0])));  
		}
		c.orderBy(cb.desc(r.get("auditSection").get("dateCreated")));
		Entitites<WarningData> result = new Entitites<WarningData>();
		result.setEntites(buildTypedQuery(c, limit, offset).getResultList());
		//如果limit为空，则不需要获取结果总数
		if (limit==null) {
			result.setTotalCount(result.getEntites().size()); 	
		} else {
			result.setTotalCount(countEntityByCriteria(c, clazz).intValue()); ////获取结果总数
		}
	    return result;
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public WarningData findLastWarning(Long pid, PointEnumType type, Integer warningType) {
		Class clazz = getClassType(type);

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery c = cb.createQuery(clazz);
		Root r = c.from(clazz);
		
		List<Predicate> criteList = new ArrayList<Predicate>();
		criteList.add(cb.equal(r.get("point").get("id"), pid));
		
		if (warningType!=null) {
			criteList.add(cb.equal(r.get("warningType"), warningType));
		}
		if (criteList.size() == 1) {  
			c.where(criteList.get(0));  
		} else {  
			c.where(cb.and(criteList.toArray(new Predicate[0])));  
		}
		c.orderBy(cb.desc(r.get("auditSection").get("dateCreated")));
		
		List<WarningData> result = buildTypedQuery(c, 1, 0).getResultList();
		if (result.isEmpty()) {
			return null;
		} else return result.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteById(Long id, PointEnumType pointType) {
		EntityManager manager = super.getEntityManager();
		manager.remove(manager.getReference(getClassType(pointType), id));
	}
	
}
