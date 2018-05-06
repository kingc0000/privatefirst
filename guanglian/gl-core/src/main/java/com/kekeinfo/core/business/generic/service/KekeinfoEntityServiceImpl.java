package com.kekeinfo.core.business.generic.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

import javax.persistence.metamodel.SingularAttribute;

import org.springframework.transaction.annotation.Transactional;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.generic.util.GenericEntityUtils;
import com.kekeinfo.core.business.common.model.Criteria;
import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;

/**
 * @param <T> entity type
 */
public abstract class KekeinfoEntityServiceImpl<K extends Serializable & Comparable<K>, E extends KekeinfoEntity<K, ?>>
	implements KekeinfoEntityService<K, E> {
	
	/**
	 * Classe de l'entité, déterminé à partir des paramètres generics.
	 */
	private Class<E> objectClass;

	private KekeinfoEntityDao<K, E> genericDao;

	@SuppressWarnings("unchecked")
	public KekeinfoEntityServiceImpl(KekeinfoEntityDao<K, E> genericDao) {
		this.genericDao = genericDao;
		
		this.objectClass = (Class<E>) GenericEntityUtils.getGenericEntityClassFromComponentDefinition(getClass());
	}
	
	protected final Class<E> getObjectClass() {
		return objectClass;
	}

	
	public E getEntity(Class<? extends E> clazz, K id) {
		return genericDao.getEntity(clazz, id);
	}

	
	public E getById(K id) {
		return genericDao.getById(id);
	}

	/**
	 * @param fieldName condition field
	 * @param fieldValue field value
	 * @return entity
	 */
	protected <V> E getByField(SingularAttribute<? super E, V> fieldName, V fieldValue) {
		return genericDao.getByField(fieldName, fieldValue);
	}
	
	/**
	 * 根据多个查询条件，返回查询集合，查询匹配规则为equal
	 * 不支持子对象属性查询
	 * @param attributes
	 * @param fieldValues
	 * @param limit 获取的集合数，可以理解为每页的数量，如果为空，则不限制获取数量
	 * @param offset 偏移量，可以理解为从第几条数据获取，如果为空，则从第0条数据获取
	 * @return
	 */
	public <V> Entitites<E> getPageListByFields(List<SingularAttribute<? super E, V>> attributes, List<V> fieldValues, Integer limit, Integer offset, Map<String, String> orderby) {
		return genericDao.getPageListByFields(attributes, fieldValues, limit, offset, orderby);
	}
	/**
	 * 根据多个查询条件，返回查询集合，查询匹配规则为equal
	 * 支持子对象属性查询
	 * @param attributes
	 * @param fieldValues
	 * @param limit 获取的集合数，可以理解为每页的数量，如果为空，则不限制获取数量
	 * @param offset 偏移量，可以理解为从第几条数据获取，如果为空，则从第0条数据获取
	 * @return
	 */
	public <V> Entitites<E> getPageListByAttributes(List<String> attributes, List<V> fieldValues, Integer limit, Integer offset, Map<String, String> orderby) {
		return genericDao.getPageListByAttributes(attributes, fieldValues, limit, offset, orderby);
	}
	public Entitites<E> getListByAttributes(String[] attributes, Object[] fieldValues, Map<String, String> orderby) {
		List<String> attributesList = new ArrayList<String>();
		List<Object> fieldValuesList = new ArrayList<Object>();
		for (String attr : attributes) {
			attributesList.add(attr);
		}
		for (Object field : fieldValues) {
			fieldValuesList.add(field);
		}
		return getPageListByAttributes(attributesList, fieldValuesList, null, null, orderby);
	}
	
	/**
	 * 根据多个查询条件，返回查询集合，查询匹配规则为like
	 * 支持子对象属性查询
	 * @param attributes
	 * @param fieldValue
	 * @param limit 获取的集合数，可以理解为每页的数量，如果为空，则不限制获取数量
	 * @param offset 偏移量，可以理解为从第几条数据获取，如果为空，则从第0条数据获取
	 * @return
	 */
	public <V> Entitites<E> getPageListByAttributesLike(List<String> attributes, String fieldValue, Integer limit, Integer offset, Map<String, String> orderby) {
		return genericDao.getPageListByAttributesLike(attributes, fieldValue, limit, offset, orderby);
	}
	
	/**
	 * 根据多个查询条件，返回查询集合，查询匹配规则为like
	 * 支持子对象属性查询
	 * @param attributes
	 * @param fieldValue
	 * @param limit 获取的集合数，可以理解为每页的数量，如果为空，则不限制获取数量
	 * @param offset 偏移量，可以理解为从第几条数据获取，如果为空，则从第0条数据获取
	 * @param  where 条件 特定条件
	 * @return
	 */
	public <V> Entitites<E> getPageListByAttributesLike(List<String> attributes, String fieldValue, Integer limit, Integer offset, Map<String, String> orderby,List<String> where,List<String> jions ) {
		return genericDao.getPageListByAttributesLike(attributes, fieldValue, limit, offset, orderby, where,jions);
	}
	
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
	public <V> Entitites<E> getPageListByAttributesLike(List<String> attributes, String fieldValue, Integer limit, Integer offset, 
			Map<String, String> orderby, List<Object[]> where, Map<String, String> fetches, boolean cascade) {
		return genericDao.getPageListByAttributesLike(attributes, fieldValue, limit, offset, orderby, where, fetches, cascade);
	}
	
	@Override
    public void saveOrUpdate(E entity) throws ServiceException
    {

        // save or update (persist and attach entities
        if ( entity.getId() != null) {
            update( entity );
        } else {
            save( entity );
        }
    }
	
	public void save(E entity) throws ServiceException {
		genericDao.save(entity);
	}
	
	
	public void create(E entity) throws ServiceException {
		createEntity(entity);
	}
	
	
	protected void createEntity(E entity) throws ServiceException {
		save(entity);
	}
	
	
	public final void update(E entity) throws ServiceException {
		try {
			updateEntity(entity);
		} catch (Exception e) {
			// TODO: handle exception
			throw new ServiceException(e); 
		}
	}
	
	protected void updateEntity(E entity) throws ServiceException {
		genericDao.update(entity);
	}
	
	
	public void delete(E entity) throws ServiceException {
		genericDao.delete(entity);
	}
	
	public void deleteById(Object id) throws ServiceException{
		genericDao.deleteById(id);
	}
	
	public void flush() {
		genericDao.flush();
	}
	
	
	public void clear() {
		genericDao.clear();
	}
	
	
	public E refresh(E entity) {
		return genericDao.refresh(entity);
	}

	
	public List<E> list() {
		return genericDao.list();
	}
	
	/**
	 * Renvoie la liste des entités dont le champ donné en paramètre a la bonne valeur.
	 * 
	 * @param fieldName le champ sur lequel appliquer la condition
	 * @param fieldValue valeur du champ
	 * @return liste d'entités
	 */
	protected <V> List<E> listByField(SingularAttribute<? super E, V> fieldName, V fieldValue) {
		return genericDao.listByField(fieldName, fieldValue);
	}
	
	
	public Long count() {
		return genericDao.count();
	}
	
	public Entitites<E> getByCriteria(Criteria criteria) throws ServiceException {
		return null;
	}
	public List<Object[]> getBySql(List<String> attributes,String table, String where) {
		return genericDao.getBySql(attributes, table,where);
	}
	public int  updateBySql (List<String> attributes,String table, String where){
		return genericDao.updateBySql(attributes, table, where);
	}
	/**
	 * 
	 * @param attributes 设置的条件
	 * @param table
	 * @param 查询的条件
	 * list有一个就执行几次语句，有事物处理的
	 * 
	 */
	@Transactional
	public void updateBySql (List<String> attributes,String table, List<String> where){
		for(int i=0;i<attributes.size();i++){
			List<String> at = new ArrayList<String>();
			at.add(attributes.get(i));
			genericDao.updateBySql(at, table, where.get(i));
		}
	}
	
	/**
	 * 执行原生sql语句
	 * @param nativeSqlList
	 */
	public void excuteByNativeSql(List<String> nativeSqlList){
		for(int i=0;i<nativeSqlList.size();i++){
			genericDao.excuteBySql(nativeSqlList.get(i));
		}
	}
}