package com.kekeinfo.core.business.point.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.model.PointEnumType;
import com.kekeinfo.core.business.point.model.BasepointLink;
import com.kekeinfo.core.business.pointlink.model.DeformLink;
import com.kekeinfo.core.business.pointlink.model.DewateringLink;
import com.kekeinfo.core.business.pointlink.model.InvertedLink;
import com.kekeinfo.core.business.pointlink.model.ObserveLink;
import com.kekeinfo.core.business.pointlink.model.PumpLink;

@Repository("pointLinkDao")
public class PointLinkDaoImpl extends KekeinfoEntityDaoImpl<Long, BasepointLink<?>> implements PointLinkDao {

	public PointLinkDaoImpl() {
		super();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<BasepointLink> getListByCid(Long cid, PointEnumType type) throws ServiceException {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

		CriteriaQuery c = null;
		Root r = null;
		
		switch (type) {
		case PUMP:
			c = cb.createQuery(PumpLink.class);
			r = c.from(PumpLink.class);
			break;
		case DEWATERING:
			c = cb.createQuery(DewateringLink.class);
			r = c.from(DewateringLink.class);
			break;
		case INVERTED:
			c = cb.createQuery(InvertedLink.class);
			r = c.from(InvertedLink.class);
			break;
		case OBSERVE:
			c = cb.createQuery(ObserveLink.class);
			r = c.from(ObserveLink.class);
			break;
		case DEFORM:
			c = cb.createQuery(DeformLink.class);
			r = c.from(DeformLink.class);
			break;
		default:
			break;
		}
		
		c.select(r);
		c.distinct(true);
		r.fetch("gateway", JoinType.LEFT);
		r.fetch("point", JoinType.LEFT);

		List<Predicate> criteList = new ArrayList<Predicate>();
		criteList.add(cb.equal(r.get("point").get("id"), cid));  
		if (criteList.size() == 1) {  
			c.where(criteList.get(0));  
		} else {  
			c.where(cb.and(criteList.toArray(new Predicate[0])));  
		}
		TypedQuery q = getEntityManager().createQuery(c);  
	    return q.getResultList();
	}
}
