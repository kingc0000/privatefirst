package com.kekeinfo.core.business.generic.service;

import java.io.Serializable;
import java.util.Map;
import java.util.List;

import javax.persistence.metamodel.SingularAttribute;

import com.kekeinfo.core.business.common.model.Criteria;
import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;

/**
 * <p>Service racine pour la gestion des entités.</p>
 *
 * @param <T> type d'entité
 */
public interface KekeinfoEntityService<K extends Serializable & Comparable<K>, E extends KekeinfoEntity<K, ?>> extends TransactionalAspectAwareService{

	void saveOrUpdate(E entity) throws ServiceException;
	/**
	 * Crée l'entité dans la base de données. Mis à part dans les tests pour faire des sauvegardes simples, utiliser
	 * create() car il est possible qu'il y ait des listeners sur la création d'une entité.
	 * 
	 * @param entity entité
	 */
	void save(E entity) throws ServiceException;
	
	/**
	 * Met à jour l'entité dans la base de données.
	 * 
	 * @param entity entité
	 */
	void update(E entity) throws ServiceException;
	
	/**
	 * Crée l'entité dans la base de données.
	 * 
	 * @param entity entité
	 */
	void create(E entity) throws ServiceException;

	/**
	 * Supprime l'entité de la base de données
	 * 
	 * @param entity entité
	 */
	void delete(E entity) throws ServiceException;
	
	void deleteById(Object id) throws ServiceException;
	
	/**
	 * Rafraîchit l'entité depuis la base de données
	 * 
	 * @param entity entité
	 */
	E refresh(E entity);
	

	/**
	 * Retourne une entité à partir de son id.
	 * 
	 * @param id identifiant
	 * @return entité
	 */
	E getById(K id);
	
	/**
	 * Renvoie la liste de l'ensemble des entités de ce type.
	 * 
	 * @return liste d'entités
	 */
	List<E> list();
	
	/**
	 * Retourne une entité à partir de sa classe et son id.
	 * 
	 * @param clazz classe
	 * @param id identifiant
	 * @return entité
	 */
	E getEntity(Class<? extends E> clazz, K id);
	
	/**
	 * Compte le nombre d'entités de ce type présentes dans la base.
	 * 
	 * @return nombre d'entités
	 */
	Long count();
	
	/**
	 * Flushe la session.
	 */
	void flush();

	void clear();
	
	/**
	 * 获取datatable查询集合
	 * @param criteria
	 * @return
	 * @throws ServiceException
	 */
	public Entitites<E> getByCriteria(Criteria criteria) throws ServiceException;
	
	/**
	 * 根据多个查询条件，返回查询集合，查询匹配规则为equal
	 * 不支持子对象属性查询
	 * @param attributes
	 * @param fieldValues
	 * @param limit 获取的集合数，可以理解为每页的数量，如果为空，则不限制获取数量
	 * @param offset 偏移量，可以理解为从第几条数据获取，如果为空，则从第0条数据获取
	 * @return
	 */
	<V> Entitites<E> getPageListByFields(List<SingularAttribute<? super E, V>> attributes, List<V> fieldValues, Integer limit, Integer offset, Map<String, String> orderby);
	
	/**
	 * 根据多个查询条件，返回查询集合，查询匹配规则为equal
	 * 支持子对象属性查询
	 * @param attributes
	 * @param fieldValues
	 * @param limit 获取的集合数，可以理解为每页的数量，如果为空，则不限制获取数量
	 * @param offset 偏移量，可以理解为从第几条数据获取，如果为空，则从第0条数据获取
	 * @return
	 */
	<V> Entitites<E> getPageListByAttributes(List<String> attributes, List<V> fieldValues, Integer limit, Integer offset, Map<String, String> orderby);
	Entitites<E> getListByAttributes(String[] attributes, Object[] fieldValues, Map<String, String> orderby);
	/**
	 * 根据多个查询条件，返回查询集合，查询匹配规则为like
	 * 支持子对象属性查询
	 * @param attributes
	 * @param fieldValue
	 * @param limit 获取的集合数，可以理解为每页的数量，如果为空，则不限制获取数量
	 * @param offset 偏移量，可以理解为从第几条数据获取，如果为空，则从第0条数据获取
	 * @return
	 */
	<V> Entitites<E> getPageListByAttributesLike(List<String> attributes, String fieldValue, Integer limit, Integer offset, Map<String, String> orderby);
	
	/**
	 * 根据多个查询条件，返回查询集合，查询匹配规则为like，指定了特定条件
	 * 支持子对象属性查询
	 * @param attributes
	 * @param fieldValue
	 * @param limit 获取的集合数，可以理解为每页的数量，如果为空，则不限制获取数量
	 * @param offset 偏移量，可以理解为从第几条数据获取，如果为空，则从第0条数据获取
	 * @param  where 条件 特定条件
	 * @return
	 */
	<V> Entitites<E> getPageListByAttributesLike(List<String> attributes, String fieldValue, Integer limit, Integer offset, Map<String, String> orderby ,List<String> where ,List<String> jions);
	
	/**
	 * 根据多个查询条件，返回查询集合，查询匹配规则为like
	 * @param attributes
	 * @param fieldValues
	 * @param limit 获取的集合数，可以理解为每页的数量，如果为空，则不限制获取数量
	 * @param offset 偏移量，可以理解为从第几条数据获取，如果为空，则从第0条数据获取
	 * @param orderby 排序，指定查询语句的排序方式 Map<attribute, "asc/desc">
	 * @param  where 条件 特定条件,Object[3], String[0]为属性，String[1]为条件值，Object[2]为比较条件（目前默认采用0：equal，1：大于gt，2：大于等于ge，3：小于，4：小于等于）
	 * @param fetches 联接，Map<联接属性名称，联接类型>
	 * @param cascade 联接是否支持级联
	 * @return
	 */
	<V> Entitites<E> getPageListByAttributesLike(List<String> attributes, String fieldValue, Integer limit, Integer offset, 
			Map<String, String> orderby, List<Object[]> where, Map<String, String> fetches, boolean cascade);
	
	/**
	 * 
	 * @param attributes 返回的属性
	 * @param where 条件
	 * @return
	 */
	List<Object[]> getBySql(List<String> attributes,String table, String where);
	
	/**
	 * 
	 * @param attributes 设置的条件
	 * @param table
	 * @param where
	 * 0:执行成功
	 * -1：执行失败
	 */
	int updateBySql (List<String> attributes,String table, String where);
	
	/**
	 * 
	 * @param attributes 设置的条件
	 * @param table
	 * @param 查询的条件
	 * list有一个就执行几次语句，有事物处理的
	 *
	 */
	void updateBySql (List<String> attributes,String table, List<String> where);
	
	/**
	 * 执行原生Sql语句
	 * @param nativeSqlList
	 */
	public void excuteByNativeSql(List<String> nativeSqlList);
}
