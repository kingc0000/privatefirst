package com.kekeinfo.core.business.user.dao;

import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.model.QGroup;
import com.kekeinfo.core.business.user.model.QPermission;

@Repository("groupDao")
public class GroupDaoImpl extends KekeinfoEntityDaoImpl<Integer, Group> implements
		GroupDao {

	@SuppressWarnings("rawtypes")
	@Override
	public List<Group> getGroupsListBypermissions(Set permissionIds) {
		StringBuilder qs = new StringBuilder();
		qs.append("select g from Group as g ");
		qs.append("join fetch g.permissions perms ");
		qs.append("where perms.id in (:cid) ");



    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);

    	q.setParameter("cid", permissionIds);
	
    	@SuppressWarnings("unchecked")
		List<Group> groups =  q.getResultList();

    	
    	return groups;
	}
	
	@Override
	public List<Group> listGroupByIds(Set<Integer> ids) {
		
		StringBuilder qs = new StringBuilder();
		qs.append("select distinct g from Group as g ");
		qs.append("join fetch g.permissions perms ");
		qs.append("where g.id in (:gid) ");
		
    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);

    	q.setParameter("gid", ids);
	
    	@SuppressWarnings("unchecked")
		List<Group> groups =  q.getResultList();

    	
    	return groups;
		
	}

	@Override
	public Group getByName(String name) {
		// TODO Auto-generated method stub
		QGroup qGroup = QGroup.group;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qGroup)
			.where(qGroup.groupName.equalsIgnoreCase(name));
		
		return query.singleResult(qGroup);
	}

	@Override
	public List<Group> listGroupWithPermission() {
		// TODO Auto-generated method stub
		QGroup qGroup = QGroup.group;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qGroup)
			 .leftJoin(qGroup.permissions).fetch()
			//.where(qGroup.groupName.notEqualsIgnoreCase("SUPERADMIN"))
			.orderBy(qGroup.id.asc());
		
		return query.distinct().list(qGroup);
	}

	/**
	 * 根据权限组名称和权限组类型，获取对应的权限组集合
	 * @param type 0:一般权限组，1：部门项目权限组，对应PNODE表关联
	 */
	@Override
	public List<Group> listGroupByPermissionName(String name,int type) {
		// TODO Auto-generated method stub
		QGroup qGroup = QGroup.group;
		QPermission qpPermission = QPermission.permission;
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qGroup)
			 .leftJoin(qGroup.permissions,qpPermission).fetch();
		
			BooleanBuilder pBuilder = new BooleanBuilder();
			pBuilder.and(qpPermission.permissionName.equalsIgnoreCase(name));
			pBuilder.and(qGroup.grouptype.eq(type));
			query.where(pBuilder);
		
		return query.distinct().list(qGroup);
	}

}
