package com.kekeinfo.core.business.generic.dao;

import java.io.Serializable;
import java.util.Map;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.metamodel.SingularAttribute;

import com.kekeinfo.core.business.generic.util.GenericEntityUtils;
import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;

/**
 * @param <T> entity type
 */
public abstract class KekeinfoEntityDaoImpl<K extends Serializable & Comparable<K>, E extends KekeinfoEntity<K, ?>>
		extends KekeinfoJpaDaoSupport
		implements KekeinfoEntityDao<K, E> {
	
	private Class<E> objectClass;
	
	@SuppressWarnings("unchecked")
	public KekeinfoEntityDaoImpl() {
		this.objectClass = (Class<E>) GenericEntityUtils.getGenericEntityClassFromComponentDefinition(getClass());
	}
	
	protected final Class<E> getObjectClass() {
		return objectClass;
	}
	
	
	public E getEntity(Class<? extends E> clazz, K id) {
		return super.getEntity(getObjectClass(), id);
	}
	
	
	public E getById(K id) {
		return super.getEntity(getObjectClass(), id);
	}
	
	
	public <V> E getByField(SingularAttribute<? super E, V> attribute, V fieldValue) {
		return super.getByField(getObjectClass(), attribute, fieldValue);
	}
	
	public <V> Entitites<E> getPageListByFields(List<SingularAttribute<? super E, V>> attributes, List<V> fieldValues, Integer limit, Integer offset, Map<String, String> orderby) {
		return super.getPageListByFields(getObjectClass(), attributes, fieldValues, limit, offset, orderby);
	}
	public <V> Entitites<E> getPageListByAttributes(List<String> attributes, List<V> fieldValues, Integer limit, Integer offset, Map<String, String> orderby) {
		return super.getPageListByAttributes(getObjectClass(), attributes, fieldValues, limit, offset, orderby);
	}
	public <V> Entitites<E> getPageListByAttributesLike(List<String> attributes, String fieldValue, Integer limit, Integer offset, Map<String, String> orderby) {
		return super.getPageListByAttributesLike(getObjectClass(), attributes, fieldValue, limit, offset, orderby);
	}
	public <V> Entitites<E> getPageListByAttributesLike(List<String> attributes, String fieldValue, Integer limit, Integer offset, Map<String, String> orderby,List<String> where,List<String> jions ){
		return super.getPageListByAttributesLike(getObjectClass(), attributes, fieldValue, limit, offset, orderby, where,jions);
	}
	/**
	 * 根据多个查询条件，返回查询集合，查询匹配规则为like
	 * @param clazz
	 * @param attributes
	 * @param fieldValues
	 * @param limit 获取的集合数，可以理解为每页的数量，如果为空，则不限制获取数量
	 * @param offset 偏移量，可以理解为从第几条数据获取，如果为空，则从第0条数据获取
	 * @param orderby 排序，指定查询语句的排序方式 Map<attribute, "asc/desc">
	 * @param  where 条件 特定条件,Object[3], String[0]为属性，Object[1]为条件值，String[2]为比较条件（目前默认采用0：equal，1：大于gt，2：大于等于ge，3：小于，4：小于等于）
	 * @param fetches 联接，Map<联接属性名称，联接类型>
	 * @param cascade 联接是否支持级联
	 * @return
	 */
	public <V> Entitites<E> getPageListByAttributesLike(List<String> attributes, String fieldValue, Integer limit, Integer offset, 
			Map<String, String> orderby, List<Object[]> where, Map<String, String> fetches, boolean cascade) {
		return super.getPageListByAttributesLike(getObjectClass(), attributes, fieldValue, limit, offset, orderby, where, fetches, cascade);
	}
	public List<Object[]> getBySql(List<String> attributes,String table, String where){
		return super.getBySql(attributes,table, where);
	}
	public int updateBySql (List<String> attributes,String table, String where){
		return super.updateBySql(attributes, table, where);
	}
	public void update(E entity) throws ServiceException{
		super.update(entity);
	}
	
	
	public void save(E entity) {
		super.save(entity);
	}
	
	
	public void delete(E entity) {
		super.delete(entity);
	}
	
	public void deleteById(Object id) {
		EntityManager manager = super.getEntityManager();
		manager.remove(manager.getReference(getObjectClass(), id));
	}
	
	
	public E refresh(E entity) {
		return super.refresh(entity);
	}
	
	
	public List<E> list() {
		return super.listEntity(getObjectClass());
	}
	
	
	public <V> List<E> listByField(SingularAttribute<? super E, V> attribute, V fieldValue) {
		return super.listEntityByField(getObjectClass(), attribute, fieldValue);
	}
	
	
	public <T extends E> List<T> list(Class<T> objectClass, Expression<Boolean> filter, Integer limit, Integer offset, Order... orders) {
		return super.listEntity(objectClass, filter, limit, offset, orders);
	}
	
	
	public Long count() {
		return super.countEntity(getObjectClass());
	}
	
	
	public <V> Long countByField(SingularAttribute<? super E, V> attribute, V fieldValue) {
		return super.countEntityByField(getObjectClass(), attribute, fieldValue);
	}
	
	
	public Long count(Expression<Boolean> filter) {
		return super.countEntity(getObjectClass(), filter);
	}
	
	
	@Override
	public EntityManager getEntityManager() {
		return super.getEntityManager();
	}
}
