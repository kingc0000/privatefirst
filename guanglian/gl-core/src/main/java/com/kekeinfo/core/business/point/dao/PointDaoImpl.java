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

import com.kekeinfo.core.business.deformmonitor.model.Deformmonitor;
import com.kekeinfo.core.business.dewatering.model.Dewatering;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.model.PointEnumType;
import com.kekeinfo.core.business.invertedwell.model.Invertedwell;
import com.kekeinfo.core.business.monitordata.model.HdewellData;
import com.kekeinfo.core.business.monitordata.model.HemonitorData;
import com.kekeinfo.core.business.monitordata.model.HiwellData;
import com.kekeinfo.core.business.monitordata.model.HowellData;
import com.kekeinfo.core.business.monitordata.model.HpwellData;
import com.kekeinfo.core.business.observewell.model.Observewell;
import com.kekeinfo.core.business.point.model.Basepoint;
import com.kekeinfo.core.business.point.model.BasepointInfo;
import com.kekeinfo.core.business.point.model.BasepointLink;
import com.kekeinfo.core.business.pumpwell.model.Pumpwell;

@Repository("pointDao")
public class PointDaoImpl extends KekeinfoEntityDaoImpl<Long, Basepoint<BasepointLink<?>, BasepointInfo<?>>> implements PointDao {

	public PointDaoImpl() {
		super();
	}
	
	@SuppressWarnings("rawtypes")
	private Class getClassType(PointEnumType type) {
		Class clazz = null;
		switch (type) {
		case PUMP:
			clazz = Pumpwell.class;
			break;
		case DEWATERING:
			clazz = Dewatering.class;
			break;
		case INVERTED:
			clazz = Invertedwell.class;
			break;
		case OBSERVE:
			clazz = Observewell.class;
			break;
		case DEFORM:
			clazz = Deformmonitor.class;
			break;
		default:
			break;
		}
		return clazz;
	}
	
	@SuppressWarnings("rawtypes")
	private Class getDataClassType(PointEnumType type) {
		Class clazz = null;
		switch (type) {
		case PUMP:
			clazz = HpwellData.class;
			break;
		case DEWATERING:
			clazz = HdewellData.class;
			break;
		case INVERTED:
			clazz = HiwellData.class;
			break;
		case OBSERVE:
			clazz = HowellData.class;
			break;
		case DEFORM:
			clazz = HemonitorData.class;
			break;
		default:
			break;
		}
		return clazz;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Basepoint getById(Long wellId, PointEnumType type) {
		Class clazz = getClassType(type);
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery c = cb.createQuery(clazz);
		Root root = c.from(clazz);
		c.select(root);
		c.distinct(true);
		
		c.where(cb.equal(root.get("id"), wellId));
		TypedQuery q = getEntityManager().createQuery(c);  
	    return (Basepoint) q.getSingleResult();
	}
	/**
	 * 获取指定网关的对应所有测点（项目未结束），如果网关id为null，则获取所有网关不为空的对应测点
	 * @param gateway
	 * @param type 测点类型
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Basepoint> getListByGateway(Long gateway, PointEnumType type) throws ServiceException {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		Class clazz = getClassType(type);
		CriteriaQuery c = cb.createQuery(clazz);
		Root r = c.from(clazz);
		
		c.select(r);
		c.distinct(true);
		r.fetch("pointLink").fetch("gateway", JoinType.LEFT);
		r.fetch("cSite", JoinType.LEFT);

		List<Predicate> criteList = new ArrayList<Predicate>();
		if (gateway != null) {
			criteList.add(cb.equal(r.get("pointLink").get("gateway").get("id"), gateway));  
		} else {
			criteList.add(cb.gt(r.get("pointLink").get("gateway").get("id"), 0));
		}
//		criteList.add(cb.notEqual(r.get("status"), 1)); //关闭状态测点不进行数据采集
		criteList.add(cb.notEqual(r.get("cSite").get("status"), -1)); //项目结束的测点不进行数据采集
		c.where(cb.and(criteList.toArray(new Predicate[0])));  
		TypedQuery q = getEntityManager().createQuery(c);  
	    return q.getResultList();
	}
	
	/**
	 * 获取项目下面所对应的测点集合
	 * @param cid
	 * @param type 测点类型
	 * @param visible 是否获取地图可见的测点，如果为null，则都获取
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Basepoint> getListByCid(Long cid, PointEnumType type, Boolean visible) throws ServiceException {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		Class clazz = getClassType(type);
		CriteriaQuery c = cb.createQuery(clazz);
		Root r = c.from(clazz);
		
		c.select(r);
		c.distinct(true);
		r.fetch("pointLink", JoinType.LEFT).fetch("gateway", JoinType.LEFT);
		r.fetch("cSite", JoinType.LEFT);
		//r.fetch("pointInfo", JoinType.LEFT);

		List<Predicate> criteList = new ArrayList<Predicate>();
		criteList.add(cb.equal(r.get("cSite").get("id"), cid));  
		if (visible!=null) { //测点在地图是否可见的条件处理
			criteList.add(cb.equal(r.get("visible"), visible));
		}
		if (criteList.size() == 1) {  
			c.where(criteList.get(0));  
		} else {  
			c.where(cb.and(criteList.toArray(new Predicate[0])));  
		}
		TypedQuery q = getEntityManager().createQuery(c);  
	    return q.getResultList();
	}
	
	/**
	 * 为处理测点数据采集监控，获取未结束的项目下面所对应的测点集合
	 * @param cid
	 * @param type
	 * @param closed 项目是否已经结束
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Basepoint> getListForGather(Long cid, PointEnumType type) throws ServiceException {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		Class clazz = getClassType(type);
		CriteriaQuery c = cb.createQuery(clazz);
		Root r = c.from(clazz);
		
		c.select(r);
		c.distinct(true);
		r.fetch("pointLink", JoinType.LEFT).fetch("gateway", JoinType.LEFT);
		r.fetch("cSite", JoinType.LEFT);
		//r.fetch("pointInfo", JoinType.LEFT);

		List<Predicate> criteList = new ArrayList<Predicate>();
		criteList.add(cb.equal(r.get("cSite").get("id"), cid));  
//		criteList.add(cb.notEqual(r.get("status"), 1)); //关闭状态测点不进行数据采集
		criteList.add(cb.notEqual(r.get("cSite").get("status"), -1)); //项目结束的测点不进行数据采集
		criteList.add(cb.notEqual(r.get("dataStatus"), 4)); //封井的测点不采集
		if (criteList.size() == 1) {  
			c.where(criteList.get(0));  
		} else {  
			c.where(cb.and(criteList.toArray(new Predicate[0])));  
		}
		TypedQuery q = getEntityManager().createQuery(c);  
	    return q.getResultList();
	}
	
	/**
	 * 获取测点最新采集数据
	 * @param wellId 测点ID
	 * @param type 测点类型
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object getLastDataByCid(Long wellId, PointEnumType type) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		Class clazz = getDataClassType(type);
		CriteriaQuery c = cb.createQuery(clazz);
		Root r = c.from(clazz);
		
		c.select(r);
		c.distinct(true);

		List<Predicate> criteList = new ArrayList<Predicate>();
		String well = "";
		switch (type) {
		case PUMP:
			well = "pWell";
			break;
		case DEWATERING:
			well = "deWell";
			break;
		case INVERTED:
			well = "iWell";
			break;
		case OBSERVE:
			well = "oWell";
			break;
		case DEFORM:
			well = "emonitor";
			break;
		default:
			break;
		}
		criteList.add(cb.equal(r.get(well).get("id"), wellId));  
		
		c.where(criteList.get(0));  
		c.orderBy(cb.desc(r.get("auditSection").get("dateCreated")));

		List result = buildTypedQuery(c, 1, 0).getResultList();
		if (result.isEmpty()) {
			return null;
		} else return result.get(0);
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Basepoint> getListForAuto(PointEnumType type) throws ServiceException {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		Class clazz = getClassType(type);
		CriteriaQuery c = cb.createQuery(clazz);
		Root r = c.from(clazz);
		r.fetch("pointLink", JoinType.LEFT).fetch("gateway", JoinType.LEFT);
		c.select(r);
		c.distinct(true);
		List<Predicate> criteList = new ArrayList<Predicate>();
		criteList.add(cb.notEqual(r.get("dataStatus"), 4)); //封井的测点不采集
		criteList.add(cb.equal(r.get("autoStatus"), 2)); 
		criteList.add(cb.notEqual(r.get("cSite").get("status"), -1)); //项目结束的测点不进行数据采集
		if (criteList.size() == 1) {  
			c.where(criteList.get(0));  
		} else {  
			c.where(cb.and(criteList.toArray(new Predicate[0])));  
		}
		TypedQuery q = getEntityManager().createQuery(c);  
	    return q.getResultList();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Basepoint> getListNameByCid(Long cid, PointEnumType type) throws ServiceException {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		Class clazz = getClassType(type);
		CriteriaQuery c = cb.createQuery(clazz);
		Root r = c.from(clazz);
		
		c.select(r);
		c.distinct(true);
		//r.fetch("pointInfo", JoinType.LEFT);

		List<Predicate> criteList = new ArrayList<Predicate>();
		criteList.add(cb.equal(r.get("cSite").get("id"), cid));  
		if (criteList.size() == 1) {  
			c.where(criteList.get(0));  
		} else {  
			c.where(cb.and(criteList.toArray(new Predicate[0])));  
		}
		TypedQuery q = getEntityManager().createQuery(c);  
	    return q.getResultList();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Basepoint> getListForAutoDeep(PointEnumType type) throws ServiceException {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		Class clazz = getClassType(type);
		CriteriaQuery c = cb.createQuery(clazz);
		Root r = c.from(clazz);
		r.fetch("pointLink", JoinType.LEFT).fetch("gateway", JoinType.LEFT);
		c.select(r);
		c.distinct(true);
		List<Predicate> criteList = new ArrayList<Predicate>();
		criteList.add(cb.notEqual(r.get("dataStatus"), 4)); //封井的测点不采集
		criteList.add(cb.equal(r.get("autoStatus"), 1)); 
		criteList.add(cb.notEqual(r.get("cSite").get("status"), -1)); //项目结束的测点不进行数据采集
		if (criteList.size() == 1) {  
			c.where(criteList.get(0));  
		} else {  
			c.where(cb.and(criteList.toArray(new Predicate[0])));  
		}
		TypedQuery q = getEntityManager().createQuery(c);  
	    return q.getResultList();
	}
}
