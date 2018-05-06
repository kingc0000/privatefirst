package com.kekeinfo.core.business.department.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.department.model.DepartmentNode;
import com.kekeinfo.core.business.department.model.QDepartmentNode;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.user.model.QGroup;
import com.kekeinfo.core.business.user.model.QPermission;
import com.kekeinfo.core.business.user.model.QUser;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;



@Repository("departmentNodeDao")
public class DepartmentNodeDaoImpl extends KekeinfoEntityDaoImpl<Long, DepartmentNode> implements DepartmentNodeDao {

	public DepartmentNodeDaoImpl() {
		super();
	}

	@Override
	public List<DepartmentNode> getByGroupID(int groupid,Long userid) {
		// TODO Auto-generated method stub
		QDepartmentNode qProjectNode = QDepartmentNode.departmentNode;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qProjectNode)
		.leftJoin(qProjectNode.user).fetch();
		
		BooleanBuilder pBuilder = new BooleanBuilder();
		pBuilder.and(qProjectNode.groupid.eq(groupid));
		
		if(userid!=null){
			pBuilder.and(qProjectNode.user.id.eq(userid));
		}
		query.where(pBuilder);

		return query.distinct().list(qProjectNode);
	}

	@Override
	public List<DepartmentNode> getBytype(String type,Long userid) {
		// TODO Auto-generated method stub
		QDepartmentNode qProjectNode = QDepartmentNode.departmentNode;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qProjectNode)
		.leftJoin(qProjectNode.user).fetch();
		
		BooleanBuilder pBuilder = new BooleanBuilder();
		pBuilder.and(qProjectNode.type.equalsIgnoreCase(type));
		
		if(userid!=null){
			pBuilder.and(qProjectNode.user.id.eq(userid));
		}
		query.where(pBuilder);
		
		return query.distinct().list(qProjectNode);
	}

	@Override
	public List<DepartmentNode> getByTypePName(String type, Long userid, String pnmae) {
		// TODO Auto-generated method stub
		QDepartmentNode qProjectNode = QDepartmentNode.departmentNode;
		QUser quUser =QUser.user;
		QGroup qGroup = QGroup.group;
		QPermission qPermission = QPermission.permission;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qProjectNode)
		.leftJoin(qProjectNode.user,quUser).fetch()
		.leftJoin(quUser.groups,qGroup).fetch()
		.leftJoin(qGroup.permissions,qPermission).fetch();
		
		BooleanBuilder pBuilder = new BooleanBuilder();
		pBuilder.and(qProjectNode.type.notEqualsIgnoreCase(type));
		pBuilder.and(quUser.id.eq(userid));
		pBuilder.and(qProjectNode.groupid.eq(qGroup.id));
		pBuilder.and(qPermission.permissionName.equalsIgnoreCase(pnmae));
		
		query.where(pBuilder);
		
		return query.distinct().list(qProjectNode);
	}

	@Override
	public List<DepartmentNode> getByDepartmentAndType(String type, Long pid) throws ServiceException {
		// TODO Auto-generated method stub
		QDepartmentNode qProjectNode = QDepartmentNode.departmentNode;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qProjectNode)
		.leftJoin(qProjectNode.user).fetch();
		
		BooleanBuilder pBuilder = new BooleanBuilder();
		pBuilder.and(qProjectNode.departmentid.eq(pid));
		
		if(StringUtils.isNotBlank(type)){
			pBuilder.and(qProjectNode.type.notEqualsIgnoreCase(type));
		}
		
		
		query.where(pBuilder);
		
		return query.distinct().list(qProjectNode);
	}

	@Override
	public List<DepartmentNode> getByTypePID(Long pid) {
		QDepartmentNode qProjectNode = QDepartmentNode.departmentNode;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qProjectNode)
		.leftJoin(qProjectNode.user).fetch()
		.where(qProjectNode.departmentid.eq(pid).and(qProjectNode.type.equalsIgnoreCase("csite")));
		
		return query.distinct().list(qProjectNode);
	}
	
	
	
	
}
