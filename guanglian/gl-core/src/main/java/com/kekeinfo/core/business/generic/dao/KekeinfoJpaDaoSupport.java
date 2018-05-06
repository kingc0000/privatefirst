package com.kekeinfo.core.business.generic.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.generic.exception.ServiceException;

@Repository("entityDao")
@Transactional
public class KekeinfoJpaDaoSupport {

	@PersistenceContext
	private EntityManager entityManager;
	
	/**
	 * Cree la requete et applique les conditions de limite / offset et retourne la {@link TypedQuery}
	 * correspondante.
	 * 
	 * @param <T> le type de l'entite retournee
	 * @param criteria
	 * @param limit null si pas de limite
	 * @param offset null si pas d'offset
	 * @return la {@link TypedQuery} avec limite et offset le cas echeant
	 */
	protected <T> TypedQuery<T> buildTypedQuery(CriteriaQuery<T> criteria, Integer limit, Integer offset) {
		TypedQuery<T> query = getEntityManager().createQuery(criteria);
		if (offset != null) {
			query.setFirstResult(offset);
		}
		if (limit != null) {
			query.setMaxResults(limit);
		}
		
		return query;
		
	}
	
	protected void filterCriteriaQuery(CriteriaQuery<?> criteria, Expression<Boolean> filter) {
		if (filter != null) {
			criteria.where(filter);
		}
	}
	
	protected <T> Root<T> rootCriteriaQuery(CriteriaBuilder builder, CriteriaQuery<?> criteria, Class<T> objectClass) {
		return criteria.from(objectClass);
	}
	
	public <T, K> T getEntity(Class<T> clazz, K id) {
		return getEntityManager().find(clazz, id);
	}
	
	public <T, V> T getByField(Class<T> clazz, SingularAttribute<? super T, V> attribute, V fieldValue) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> criteria = builder.createQuery(clazz);
		Root<T> root = criteria.from(clazz);
		criteria.where(builder.equal(root.get(attribute), fieldValue));
		
		try {
			return buildTypedQuery(criteria, null, null).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	/**
	 * 
	 * @param attributes
	 * @param where
	 * @return
	 */
	public List<Object[]> getBySql(List<String> attributes,String table, String where){
		if(attributes !=null && attributes.size()>0 && !StringUtils.isBlank(table)){
			StringBuilder qs = new StringBuilder();
			qs.append("select ");
			for(String s:attributes){
				qs.append(s).append(",  ");
			}
			//去掉最后一个逗号
			qs.deleteCharAt(qs.lastIndexOf(","));
			qs.append("from").append("  ").append(table).append("   ");
			if(!StringUtils.isBlank(where)){
				qs.append(where);
			}
	    	String hql = qs.toString();
			Query q = getEntityManager().createNativeQuery(hql);
	    	
	    	@SuppressWarnings("unchecked")
			List<Object[]> counts =  q.getResultList();
	    	return counts;
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param attributes 设置的条件
	 * @param table
	 * @param where
	 * 0:执行成功
	 * -1：执行失败
	 */
	int updateBySql (List<String> attributes,String table, String where){
		if(attributes !=null && attributes.size()>0 && !StringUtils.isBlank(table)){
			StringBuilder qs = new StringBuilder();
			qs.append("UPDATE ").append(table).append("  ");
			for(String s:attributes){
				qs.append(s).append(",  ");
			}
			//去掉最后一个逗号
			qs.deleteCharAt(qs.lastIndexOf(","));
			if(!StringUtils.isBlank(where)){
				qs.append("  ").append(where);
			}
			
			String hql = qs.toString();
			Query q = getEntityManager().createNativeQuery(hql);
			q.executeUpdate();
			return 0;
		}
		
		
		return -1;
	}
	
	/**
	 * 执行原生sql语句
	 * @param nativeSql
	 * @return
	 */
	public int excuteBySql(String nativeSql){
		Query q = getEntityManager().createNativeQuery(nativeSql);
		q.executeUpdate();
		return 0;
	}
	/**
	 * 根据多个查询条件，返回查询集合，查询匹配规则为equal
	 * @param clazz
	 * @param attributes
	 * @param fieldValues
	 * @param limit 获取的集合数，可以理解为每页的数量，如果为空，则不限制获取数量
	 * @param offset 偏移量，可以理解为从第几条数据获取，如果为空，则从第0条数据获取
	 * @param orderby 排序，指定查询语句的排序方式 Map<attribute, "asc/desc">
	 * @return
	 */
	public <T, V> Entitites<T> getPageListByFields(Class<T> clazz, List<SingularAttribute<? super T, V>> attributes, List<V> fieldValues, Integer limit, Integer offset, Map<String, String> orderby) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> criteria = builder.createQuery(clazz);
		Root<T> root = criteria.from(clazz);
		if (attributes != null) {
			Predicate p = null;
			for (int i = 0; i < attributes.size(); i++) {
				if (p != null) {
					p = builder.and(p, builder.equal(root.get(attributes.get(i)), fieldValues.get(i)));
				} else {
					p = builder.equal(root.get(attributes.get(i)), fieldValues.get(i));
					
				}
			}
			criteria.where(p);
		}
		if (orderby!=null && orderby.size()>0) {
			List<Order> orders = new ArrayList<Order>();
			Iterator<Entry<String, String>> iter = orderby.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
				String key = entry.getKey();
				String value = entry.getValue();
				String[] atts = key.split("\\.");
				@SuppressWarnings("rawtypes")
				Path path = null;
				for (String att : atts) {
					if (path != null) {
						path = path.get(att);
					} else {
						path = root.get(att);
					}
				}
				if (value.equalsIgnoreCase("asc"))
					orders.add(builder.asc(path));
				else if (value.equalsIgnoreCase("desc"))
					orders.add(builder.desc(path));
			}
			criteria.orderBy(orders);
		}
		Entitites<T> result = new Entitites<T>();
		result.setEntites(buildTypedQuery(criteria, limit, offset).getResultList());
		//如果limit为空，则不需要获取结果总数
		if (limit==null) {
			result.setTotalCount(result.getEntites().size()); 	
		} else {
			result.setTotalCount(countEntityByCriteria(criteria, clazz).intValue()); ////获取结果总数
		}

		return result;
	}
	
	/**
	 * 根据多个查询条件，返回查询集合，查询匹配规则为equal
	 * @param clazz
	 * @param attributes
	 * @param fieldValues
	 * @param limit 获取的集合数，可以理解为每页的数量，如果为空，则不限制获取数量
	 * @param offset 偏移量，可以理解为从第几条数据获取，如果为空，则从第0条数据获取
	 * @param orderby 排序，指定查询语句的排序方式 Map<attribute, "asc/desc">
	 * @return
	 */
	public <T, V> Entitites<T> getPageListByAttributes(Class<T> clazz, List<String> attributes, List<V> fieldValues, Integer limit, Integer offset, Map<String, String> orderby) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> criteria = builder.createQuery(clazz);
		Root<T> root = criteria.from(clazz);
		if (attributes != null) {
			Predicate p = null;
			for (int i = 0; i < attributes.size(); i++) {
				String[] atts = attributes.get(i).split("\\.");
				@SuppressWarnings("rawtypes")
				Path path = null;
				for (String att : atts) {
					if (path != null) {
						path = path.get(att);
					} else {
						path = root.get(att);
					}
				}
				if (p != null) {
					p = builder.and(p, builder.equal(path, fieldValues.get(i)));
				} else {
					p = builder.equal(path, fieldValues.get(i));
				}
			}
			criteria.where(p);
		}
		if (orderby!=null && orderby.size()>0) {
			List<Order> orders = new ArrayList<Order>();
			Iterator<Entry<String, String>> iter = orderby.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
				String key = entry.getKey();
				String value = entry.getValue();
				String[] atts = key.split("\\.");
				@SuppressWarnings("rawtypes")
				Path path = null;
				for (String att : atts) {
					if (path != null) {
						path = path.get(att);
					} else {
						path = root.get(att);
					}
				}
				if (value.equalsIgnoreCase("asc"))
					orders.add(builder.asc(path));
				else if (value.equalsIgnoreCase("desc"))
					orders.add(builder.desc(path));
			}
			criteria.orderBy(orders);
		}
		Entitites<T> result = new Entitites<T>();
		result.setEntites(buildTypedQuery(criteria, limit, offset).getResultList());
		//如果limit为空，则不需要获取结果总数
		if (limit==null) {
			result.setTotalCount(result.getEntites().size()); 	
		} else {
			result.setTotalCount(countEntityByCriteria(criteria, clazz).intValue()); ////获取结果总数
		}
		return result;
//		return buildTypedQuery(criteria, limit, offset).getResultList();
	}
	
	/**
	 * 根据多个查询条件，返回查询集合，查询匹配规则为like
	 * @param clazz
	 * @param attributes
	 * @param fieldValues
	 * @param limit 获取的集合数，可以理解为每页的数量，如果为空，则不限制获取数量
	 * @param offset 偏移量，可以理解为从第几条数据获取，如果为空，则从第0条数据获取
	 * @param orderby 排序，指定查询语句的排序方式 Map<attribute, "asc/desc">
	 * @return
	 */
	public <T, V> Entitites<T> getPageListByAttributesLike(Class<T> clazz, List<String> attributes, String fieldValue, Integer limit, Integer offset, Map<String, String> orderby) {
		return getPageListByAttributesLike(clazz, attributes, fieldValue, limit, offset, orderby, null, null, false);
	}
	
	/**
	 * 根据多个查询条件，返回查询集合，查询匹配规则为like
	 * @param clazz
	 * @param attributes
	 * @param fieldValues
	 * @param limit 获取的集合数，可以理解为每页的数量，如果为空，则不限制获取数量
	 * @param offset 偏移量，可以理解为从第几条数据获取，如果为空，则从第0条数据获取
	 * @param orderby 排序，指定查询语句的排序方式 Map<attribute, "asc/desc">
	 * @param  where 条件 特定条件
	 * @param joins 关联
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T, V> Entitites<T> getPageListByAttributesLike(Class<T> clazz, List<String> attributes, String fieldValue, Integer limit, Integer offset, Map<String, String> orderby,List<String> where,List<String> jions) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		
		CriteriaQuery<T> criteria = builder.createQuery(clazz);
		Root<T> root = criteria.from(clazz);
		//root.fetch(attributeName, jt)
		if (attributes != null && !StringUtils.isBlank(fieldValue)) {
			Predicate p = null;
			for (int i = 0; i < attributes.size(); i++) {
				String[] atts = attributes.get(i).split("\\.");
				Path path = null;
				for (String att : atts) {
					if (path != null) {
						path = path.get(att);
					} else {
						path = root.get(att);
					}
				}
				if (p != null) {
					p = builder.or(p, builder.like(builder.lower(path), "%"+fieldValue.toLowerCase()+"%"));
				} else {
					p = builder.like(builder.lower(path), "%"+fieldValue.toString().toLowerCase()+"%");
					
				}
			}
			if(where !=null){
				for(int j=0;j<where.size();j++){
					String[] atts = where.get(j).split("\\.");
					Path path = null;
					for (String att : atts) {
						if (path != null) {
							path = path.get(att);
						} else {
							path = root.get(att);
						}
					}
					//Predicate p =builder.and(builder.equal(path, where.get(1)));
					String condition = where.get(j+1);
					//数组用in
					if(condition.indexOf(",")!=-1){
						String []spid = condition.split(",");
						List<Long> ids= new ArrayList<Long>();
						for(int i=0;i<spid.length;i++){
							ids.add(Long.parseLong(spid[i]));
						}
						
						if(p!=null){
							p=builder.and(p,builder.in(path).value(ids));
						}else{
							p=builder.in(path).value(ids);
						}
					}else{
						if(p!=null){
							p=builder.and(p,builder.equal(path, where.get(j+1)));
						}else{
							p=builder.equal(path, where.get(j+1));
						}
					}
					j++;
				}
				
			}
			criteria.where(p);
			
		}else{
			if(where !=null){
				Predicate p = null;
				for(int j=0;j<where.size();j++){
					String[] atts = where.get(j).split("\\.");
					Path path = null;
					for (String att : atts) {
						if (path != null) {
							path = path.get(att);
						} else {
							path = root.get(att);
						}
					}
					//Predicate p =builder.and(builder.equal(path, where.get(1)));
					String condition = where.get(j+1);
					//数组用in
					if(condition.indexOf(",")!=-1){
						String []spid = condition.split(",");
						List<Long> ids= new ArrayList<>();
						for(int i=0;i<spid.length;i++){
							ids.add(Long.parseLong(spid[i]));
						}
						
						if(p!=null){
							p=builder.and(p,builder.in(path).value(ids));
						}else{
							p=builder.in(path).value(ids);
						}
					}else{
						if(p!=null)	{
							p=builder.and(p,builder.equal(path, where.get(j+1)));
						}else{
							p=builder.equal(path, where.get(j+1));
						}
						
					}
					j++;
				}
				if(p!=null){
					criteria.where(p);
				}
				
			}
		}
		if(jions!=null){
			for(String s:jions){
				root.fetch(s, JoinType.LEFT);
			}
			
			criteria.select(root);
		}
		if (orderby!=null && orderby.size()>0) {
			List<Order> orders = new ArrayList<Order>();
			Iterator<Entry<String, String>> iter = orderby.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
				String key = entry.getKey();
				String value = entry.getValue();
				String[] atts = key.split("\\.");
				Path path = null;
				for (String att : atts) {
					if (path != null) {
						path = path.get(att);
					} else {
						path = root.get(att);
					}
				}
				if (value.equalsIgnoreCase("asc"))
					orders.add(builder.asc(path));
				else if (value.equalsIgnoreCase("desc"))
					orders.add(builder.desc(path));
			}
			criteria.orderBy(orders);
		}
		Entitites<T> result = new Entitites<T>();
		criteria.select(root).distinct(true);
		result.setEntites(buildTypedQuery(criteria, limit, offset).getResultList());
		//如果limit为空，则不需要获取结果总数
		if (limit==null) {
			result.setTotalCount(result.getEntites().size()); 	
		} else {
			result.setTotalCount(countEntityByCriteria(criteria, clazz).intValue()); ////获取结果总数
		}
		return result;
	}
	
	/**
	 * 根据多个查询条件，返回查询集合，查询匹配规则为like
	 * @param clazz
	 * @param attributes
	 * @param fieldValues
	 * @param limit 获取的集合数，可以理解为每页的数量，如果为空，则不限制获取数量
	 * @param offset 偏移量，可以理解为从第几条数据获取，如果为空，则从第0条数据获取
	 * @param orderby 排序，指定查询语句的排序方式 Map<attribute, "asc/desc">
	 * @param  where 条件 特定条件,String[3], String[0]为属性，String[1]为条件值，String[2]为比较条件（目前默认采用0：equal，1：大于gt，2：大于等于ge，3：小于，4：小于等于 5：like）
	 * @param fetches 联接，Map<联接属性名称，联接类型>，联接类型（left，right，inner）
	 * @param cascade 联接是否支持级联
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T, V> Entitites<T> getPageListByAttributesLike(Class<T> clazz, List<String> attributes, String fieldValue, Integer limit, Integer offset, 
			Map<String, String> orderby, List<Object[]> where, Map<String, String> fetches, boolean cascade) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		
		CriteriaQuery<T> criteria = builder.createQuery(clazz);
		Root<T> root = criteria.from(clazz);
		//root.fetch(attributeName, jt)
		Predicate p = null;
		if (attributes != null && !StringUtils.isBlank(fieldValue)) {
			for (int i = 0; i < attributes.size(); i++) {
				String[] atts = attributes.get(i).split("\\.");
				Path path = null;
				for (String att : atts) {
					if (path != null) {
						path = path.get(att);
					} else {
						path = root.get(att);
					}
				}
				if (p != null) {
					p = builder.or(p, builder.like(builder.lower(path), "%"+fieldValue.toLowerCase()+"%"));
				} else {
					p = builder.like(builder.lower(path), "%"+fieldValue.toString().toLowerCase()+"%");
				}
			}
		} 
		//where限定条件处理
		if (where!=null && where.size()>0) {
			for (Object[] condArray : where) {
				String[] atts = condArray[0].toString().split("\\.");
				Path path = null;
				for (String att : atts) {
					if (path != null) {
						path = path.get(att);
					} else {
						path = root.get(att);
					}
				}
				
				if (condArray.length<=2||condArray.length>2&&(condArray[2]==null||condArray[2].equals("0"))) { //采用等于条件
					boolean flag = condArray[1] instanceof String; //是否是字符串
					if(flag && condArray[1].toString().indexOf(",")!=-1){ //如果是字符串，并且有“，”分隔符，则采用in条件处理
						String []spid = condArray[1].toString().split(",");
						if(p!=null){
							p=builder.and(p,builder.in(path).value(Arrays.asList(spid)));
						}else{
							p=builder.in(path).value(Arrays.asList(spid));
						}
					}else{
						if (p!=null){
							p = builder.and(p,builder.equal(path, condArray[1]));
						}else {
							p = builder.and(builder.equal(path, condArray[1]));
						}
					}
				} else if(condArray[2].equals("1")){ //大于条件
					if (p!=null){
						p = builder.and(p, builder.greaterThan(path, (Comparable)condArray[1]));
					}else {
						p = builder.and(builder.greaterThan(path, (Comparable)condArray[1]));
					}
				} else if(condArray[2].equals("2")){ //大于等于条件
					if (p!=null){
						p = builder.and(p, builder.greaterThanOrEqualTo(path, (Comparable)condArray[1]));
					}else {
						p = builder.and(builder.greaterThanOrEqualTo(path, (Comparable)condArray[1]));
					}
				} else if(condArray[2].equals("3")){ //小于条件
					if (p!=null){
						p = builder.and(p, builder.lessThan(path, (Comparable)condArray[1]));
					}else {
						p = builder.and(builder.lessThan(path, (Comparable)condArray[1]));
					}
				} else if(condArray[2].equals("4")){ //小于等于条件
					if (p!=null){
						p = builder.and(p, builder.lessThanOrEqualTo(path, (Comparable)condArray[1]));
					}else {
						p = builder.and(builder.lessThanOrEqualTo(path, (Comparable)condArray[1]));
					}
				}else if(condArray[2].equals("5")){ //小于等于条件
					if (p!=null){
						p = builder.and(p,  builder.like(builder.lower(path), condArray[1].toString().toLowerCase()+"%"));
					}else {
						p = builder.and(builder.like(builder.lower(path), condArray[1].toString().toLowerCase()+"%"));
					}
				}
				
			}
		}
		if (p!=null) {
			criteria.where(p);
		}
		//处理联接定义
		if(fetches!=null && fetches.size()>0){
			if (cascade) { //级联联接
				Fetch<T, Object> fetch = null;
				Iterator<Map.Entry<String, String>> iter = fetches.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry<String, String> object = (Map.Entry<String, String>) iter.next();
					String s = object.getKey(); //联接属性名称
					String t = object.getValue(); //联接类型
					if (fetch == null) {
						fetch = root.fetch(s, JoinType.valueOf(t.toUpperCase())); 
					} else {
						fetch = fetch.fetch(s, JoinType.valueOf(t.toUpperCase()));
					}
				}
			} else {
				Iterator<Map.Entry<String, String>> iter = fetches.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry<String, String> object = (Map.Entry<String, String>) iter.next();
					String s = object.getKey(); //联接属性名称
					String t = object.getValue(); //联接类型
					root.fetch(s, JoinType.valueOf(t.toUpperCase()));
				}
			}
			
			criteria.select(root).distinct(true);//使用distinct()设置删除任何重复
		}
		if (orderby!=null && orderby.size()>0) {
			List<Order> orders = new ArrayList<Order>();
			Iterator<Entry<String, String>> iter = orderby.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
				String key = entry.getKey();
				String value = entry.getValue();
				String[] atts = key.split("\\.");
				Path path = null;
				for (String att : atts) {
					if (path != null) {
						path = path.get(att);
					} else {
						path = root.get(att);
					}
				}
				if (value.equalsIgnoreCase("asc"))
					orders.add(builder.asc(path));
				else if (value.equalsIgnoreCase("desc"))
					orders.add(builder.desc(path));
			}
			criteria.orderBy(orders);
		}
		Entitites<T> result = new Entitites<T>();
		result.setEntites(buildTypedQuery(criteria, limit, offset).getResultList());
		//如果limit为空，则不需要获取结果总数
		if (limit==null) {
			result.setTotalCount(result.getEntites().size()); 	
		} else {
			result.setTotalCount(countEntityByCriteria(criteria, clazz).intValue()); ////获取结果总数
		}
		return result;
	}
	protected <T> void update(T entity)  throws ServiceException{
		try {
			if (!getEntityManager().contains(entity)) {
				getEntityManager().merge(entity);
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		//TODO: http://blog.xebia.com/2009/03/23/jpa-implementation-patterns-saving-detached-entities/
	}
	
	protected <T> void save(T entity) {
		getEntityManager().persist(entity);
	}
	
	protected <T> void delete(T entity) {
		if (!getEntityManager().contains(entity)) {
			//getEntityManager().merge(entity);
			getEntityManager().remove(getEntityManager().merge(entity));
			//throw new PersistenceException("Failed to delete a detached entity");
		}else{
			getEntityManager().remove(entity);
		}
	}
	
	protected <T> T refresh(T entity) {
		getEntityManager().refresh(entity);
		
		return entity;
	}
	
	public void flush() {
		getEntityManager().flush();
	}
	
	public void clear() {
		getEntityManager().clear();
	}
	
	protected <T> List<T> listEntity(Class<T> objectClass, Expression<Boolean> filter, Integer limit, Integer offset, Order... orders) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> criteria = builder.createQuery(objectClass);
		rootCriteriaQuery(builder, criteria, objectClass);
		
		if (filter != null) {
			filterCriteriaQuery(criteria, filter);
		}
		if (orders != null && orders.length > 0) {
			criteria.orderBy(orders);
		}
		TypedQuery<T> query = buildTypedQuery(criteria, limit, offset);
		
		List<T> entities = query.getResultList();
		
		if (orders == null || orders.length == 0) {
			sort(entities);
		}
		
		return entities;
	}
	
	protected <T> List<T> listEntity(Class<T> objectClass) {
		return listEntity(objectClass, null, null, null);
	}
	
	protected <T> List<T> listEntity(Class<T> objectClass, Expression<Boolean> filter) {
		return listEntity(objectClass, filter, null, null);
	}
	
	protected <T, V> List<T> listEntityByField(Class<T> objectClass, SingularAttribute<? super T, V> attribute, V fieldValue) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> criteria = builder.createQuery(objectClass);
		
		Root<T> root = rootCriteriaQuery(builder, criteria, objectClass);
		criteria.where(builder.equal(root.get(attribute), fieldValue));
		
		List<T> entities = buildTypedQuery(criteria, null, null).getResultList();
		
		sort(entities);
		
		return entities;
	}
	
	protected <T> Long countEntity(Class<T> clazz) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<T> root = rootCriteriaQuery(builder, criteria, clazz);
		
		criteria.select(builder.count(root));
		
		return buildTypedQuery(criteria, null, null).getSingleResult();
	}
	
	/**
	 * 获取查询匹配的结果数量
	 * @param criteriaQuery
	 * @param clazz
	 * @return
	 */
	protected <T> Long countEntityByCriteria(CriteriaQuery<T> criteriaQuery, Class<T> clazz) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> countCriteriaQuery = builder.createQuery(Long.class);
		Root<T> root = rootCriteriaQuery(builder, countCriteriaQuery, clazz);
		
		countCriteriaQuery.select(builder.count(root));
		if (criteriaQuery.getGroupList() != null) {
            countCriteriaQuery.groupBy(criteriaQuery.getGroupList());
        }
        if (criteriaQuery.getGroupRestriction() != null) {
            countCriteriaQuery.having(criteriaQuery.getGroupRestriction());
        }
        if (criteriaQuery.getRestriction() != null) {
            countCriteriaQuery.where(criteriaQuery.getRestriction());
        }
		
		return buildTypedQuery(countCriteriaQuery, null, null).getSingleResult();
	}
	
	protected <T, V> Long countEntityByField(Class<T> clazz, SingularAttribute<? super T, V> attribute, V fieldValue) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		
		Root<T> root = rootCriteriaQuery(builder, criteria, clazz);
		criteria.select(builder.count(root));
		
		Expression<Boolean> filter = builder.equal(root.get(attribute), fieldValue);
		filterCriteriaQuery(criteria, filter);
		
		return buildTypedQuery(criteria, null, null).getSingleResult();
	}
	
	protected <T> Long countEntity(Class<T> clazz, Expression<Boolean> filter) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		
		Root<T> root = rootCriteriaQuery(builder, criteria, clazz);
		criteria.select(builder.count(root));
		
		filterCriteriaQuery(criteria, filter);
		
		return buildTypedQuery(criteria, null, null).getSingleResult();
	}
	
	protected <E> E getSingleResultOrNull(CriteriaQuery<E> cq) {
		try {
			return getEntityManager().createQuery(cq).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	protected <E> E getSingleResultOrNull(TypedQuery<E> tq) {
		try {
			return tq.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	protected EntityManager getEntityManager() {
		return entityManager;
	}
	
	@SuppressWarnings("unchecked")
	protected <T> void sort(List<T> entities) {
		Object[] a = entities.toArray();
		Arrays.sort(a);
		ListIterator<T> i = entities.listIterator();
		for (int j = 0; j < a.length; j++) {
			i.next();
			i.set((T) a[j]);
		}
	}

}
