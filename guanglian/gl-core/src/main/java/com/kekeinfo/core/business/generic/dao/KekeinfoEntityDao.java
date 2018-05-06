package com.kekeinfo.core.business.generic.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.metamodel.SingularAttribute;

import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;


/**
 * @param <E> type entity
 */
public interface KekeinfoEntityDao<K extends Serializable & Comparable<K>, E extends KekeinfoEntity<K, ?>> {

	/**
	 * @param clazz class
	 * @param id id
	 * @return entity
	 */
	E getEntity(Class<? extends E> clazz, K id);
	
	/**
	 * @param id id
	 * @return entity
	 */
	E getById(K id);
	
	/**
	 * @param entity entity
	 */
	void save(E entity);
	
	/**
	 * @param entity entity
	 * @throws ServiceException 
	 */
	void update(E entity) throws ServiceException;
	
	/** 
	 * @param entity entity
	 */
	void delete(E entity);
	
	void deleteById(Object id);
	
	/**
	 * @param entity entity
	 */
	E refresh(E entity);
	
	/**
	 * @return liste d'entitys
	 */
	List<E> list();
	
	/**
	 * @return nombre d'entitys
	 */
	Long count();
	
	void flush();
	
	void clear();

	/**
	 * @param <V> type value
	 * @param attribute
	 * @param fieldValue
	 * @return numbers of entities
	 */
	<V> Long countByField(SingularAttribute<? super E, V> attribute, V fieldValue);

	/**
	 * @param <V> type value
	 * @param attribute
	 * @param fieldValue
	 * @return entities
	 */
	<V> List<E> listByField(SingularAttribute<? super E, V> attribute, V fieldValue);

	/**
	 * 根据多个查询条件，返回查询集合，查询匹配规则为equal
	 * 本方法不支持子对象属性的查询
	 * @param attributes
	 * @param fieldValues
	 * @param limit 获取的集合数，可以理解为每页的数量，如果为空，则不限制获取数量
	 * @param offset 偏移量，可以理解为从第几条数据获取，如果为空，则从第0条数据获取
	 * @return
	 */
	<V> Entitites<E> getPageListByFields(List<SingularAttribute<? super E, V>> attributes, List<V> fieldValues, Integer limit, Integer offset, Map<String, String> orderby);
	
	/**
	 * 根据多个查询条件，返回查询集合，查询匹配规则为equal
	 * 本方法支持子对象属性的查询
	 * @param attributes
	 * @param fieldValues
	 * @param limit 获取的集合数，可以理解为每页的数量，如果为空，则不限制获取数量
	 * @param offset 偏移量，可以理解为从第几条数据获取，如果为空，则从第0条数据获取
	 * @return
	 */
	<V> Entitites<E> getPageListByAttributes(List<String> attributes, List<V> fieldValues, Integer limit, Integer offset, Map<String, String> orderby);
	
	/**
	 * 根据多个查询条件，返回查询集合，查询匹配规则为like
	 * 本方法支持子对象属性的查询
	 * @param attributes
	 * @param fieldValue
	 * @param limit 获取的集合数，可以理解为每页的数量，如果为空，则不限制获取数量
	 * @param offset 偏移量，可以理解为从第几条数据获取，如果为空，则从第0条数据获取
	 * @return
	 */
	<V> Entitites<E> getPageListByAttributesLike(List<String> attributes, String fieldValue, Integer limit, Integer offset, Map<String, String> orderby);
	
	/**
	 * 根据多个查询条件，返回查询集合，查询匹配规则为like
	 * 本方法支持子对象属性的查询
	 * @param attributes
	 * @param fieldValue
	 * @param limit 获取的集合数，可以理解为每页的数量，如果为空，则不限制获取数量
	 * @param offset 偏移量，可以理解为从第几条数据获取，如果为空，则从第0条数据获取
	 * @param  where 条件 特定条件
	 * @return
	 */
	<V> Entitites<E> getPageListByAttributesLike(List<String> attributes, String fieldValue, Integer limit, Integer offset, Map<String, String> orderby, List<String> where,List<String> jions );
	
	/**
	 * 根据多个查询条件，返回查询集合，查询匹配规则为like
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
	<V> Entitites<E> getPageListByAttributesLike(List<String> attributes, String fieldValue, Integer limit, Integer offset, 
			Map<String, String> orderby, List<Object[]> where, Map<String, String> fetches, boolean cascade);
	/**
	 * @param <V>
	 * @param attribute
	 * @param fieldValue
	 * @return
	 * @throws NoResultException
	 * @throws {@link NonUniqueResultException}
	 */
	<V> E getByField(SingularAttribute<? super E, V> attribute, V fieldValue);

	<T extends E> List<T> list(Class<T> objectClass, Expression<Boolean> filter, Integer limit, Integer offset, Order... orders);

	Long count(Expression<Boolean> filter);
	
	EntityManager getEntityManager();

	List<Object[]> getBySql(List<String> attributes,String table, String where);
	
	/**
	 * 
	 *
	 * @param attributes 设置的条件
	 * @param table
	 * @param where
	 * 0:执行成功
	 * -1：执行失败
	 */
	int updateBySql (List<String> attributes,String table, String where);
	
	public int excuteBySql(String nativeSql);

}
