package com.kekeinfo.core.business.user.dao;

import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.kekeinfo.core.business.user.model.Permission;
import com.kekeinfo.core.business.user.model.PermissionCriteria;
import com.kekeinfo.core.business.user.model.PermissionList;
import com.kekeinfo.core.business.user.model.QPermission;

@Repository("permissionDao")
public class PermissionDaoImpl extends KekeinfoEntityDaoImpl<Integer, Permission> implements
		PermissionDao {

	@Override
	public List<Permission> listPermission() {
		QPermission qPermission = QPermission.permission;
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qPermission)
			.orderBy(qPermission.id.asc());
		
		return query.distinct().list(qPermission);
				//listDistinct(qPermission);
		}

	@Override
	public Permission getById(Integer permissionId) {
		QPermission qPermission = QPermission.permission;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qPermission)
			.where(qPermission.id.eq(permissionId));
		
		return query.uniqueResult(qPermission);
	}


	@SuppressWarnings("rawtypes")
	@Override
	public List<Permission> getPermissionsListByGroups(Set groupIds) {
		StringBuilder qs = new StringBuilder();
		qs.append("select distinct p from Permission as p ");
		qs.append("join fetch p.groups grous ");
		qs.append("where grous.id in (:cid)");

    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);

    	q.setParameter("cid", groupIds);
    	
    	@SuppressWarnings("unchecked")
		List<Permission> permissions =  q.getResultList();
    	
    	return permissions;
	}

	@Override
	public PermissionList listByCriteria(PermissionCriteria criteria) {
		PermissionList permissionList = new PermissionList();

        
		StringBuilder countBuilderSelect = new StringBuilder();
		countBuilderSelect.append("select count(p) from Permission as p");
		
		StringBuilder countBuilderWhere = new StringBuilder();
		
		
		if(criteria.getGroupIds()!=null && criteria.getGroupIds().size()>0) {
			countBuilderSelect.append(" INNER JOIN p.groups grous");
			countBuilderWhere.append(" where grous.id in (:cid)");
		}
		
	
		Query countQ = super.getEntityManager().createQuery(
				countBuilderSelect.toString() + countBuilderWhere.toString());

		if(criteria.getGroupIds()!=null && criteria.getGroupIds().size()>0) {
			countQ.setParameter("cid", criteria.getGroupIds());
		}
		

		Number count = (Number) countQ.getSingleResult ();

		permissionList.setTotalCount(count.intValue());
		
        if(count.intValue()==0)
        	return permissionList;

		
		StringBuilder qs = new StringBuilder();
		qs.append("select p from Permission as p ");
		qs.append("join fetch p.groups grous ");
		
		if(criteria.getGroupIds()!=null && criteria.getGroupIds().size()>0) {
			qs.append(" where grous.id in (:cid)");
		}
		
		qs.append(" order by p.id asc ");
		
    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);


    	if(criteria.getGroupIds()!=null && criteria.getGroupIds().size()>0) {
    		q.setParameter("cid", criteria.getGroupIds());
    	}
    	
    	if(criteria.getMaxCount()>0) {
    		
    		
	    	q.setFirstResult((int)criteria.getStartIndex());
	    	if(criteria.getMaxCount()<count.intValue()) {
	    		q.setMaxResults((int)criteria.getMaxCount());
	    		permissionList.setTotalCount((int)criteria.getMaxCount());
	    	}
	    	else {
	    		q.setMaxResults(count.intValue());
	    		permissionList.setTotalCount(count.intValue());
	    	}
    	}
    	
    	@SuppressWarnings("unchecked")
		List<Permission> permissions =  q.getResultList();
    	permissionList.setPermissions(permissions);
    	
    	return permissionList;
	}

	@Override
	public List<Permission> listAdminPermission() {
		// TODO Auto-generated method stub
		QPermission qPermission = QPermission.permission;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qPermission)
			.where(qPermission.permissionName.notEqualsIgnoreCase("AUTH_CUSTOMER"));
		
		return query.distinct().list(qPermission);
	}

	@Override
	public Permission getByName(String name) {
		// TODO Auto-generated method stub
		QPermission qPermission = QPermission.permission;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qPermission)
			.where(qPermission.permissionName.endsWithIgnoreCase(name));
		
		return query.uniqueResult(qPermission);
	}

}
