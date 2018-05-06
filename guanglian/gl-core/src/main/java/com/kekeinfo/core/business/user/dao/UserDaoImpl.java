package com.kekeinfo.core.business.user.dao;

import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.common.model.Criteria;
import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.department.model.QDepartmentNode;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.user.model.QGroup;
import com.kekeinfo.core.business.user.model.QPermission;
import com.kekeinfo.core.business.user.model.QUser;
import com.kekeinfo.core.business.user.model.User;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("userDao")
public class UserDaoImpl extends KekeinfoEntityDaoImpl<Long, User> implements
		UserDao {
	
	@Override
	public User getByUserName(String userName) {
		
		
		QUser qUser = QUser.user;
		QGroup qGroup = QGroup.group;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qUser)
			.innerJoin(qUser.groups, qGroup).fetch()
			.leftJoin(qUser.pNodes).fetch()
			.where(qUser.adminName.eq(userName));
		
		

		User user = query.uniqueResult(qUser);
		return user;
	}
	
	@Override
	public User getById(Long id) {
		
		
		QUser qUser = QUser.user;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qUser)
			.leftJoin(qUser.pNodes).fetch()
			.leftJoin(qUser.groups).fetch()
			.where(qUser.id.eq(id));
		
		

		User user = query.uniqueResult(qUser);
		return user;
	}

	@Override
	public List<User> listUser() {
		QUser qUser = QUser.user;
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qUser)
			.orderBy(qUser.id.asc());
		
		return query.distinct().list(qUser);
				//listDistinct(qUser);
	}

	@Override
	public Entitites<User> getByCriteria(Criteria criteria) throws ServiceException {
		// TODO Auto-generated method stub
		QUser qUser = QUser.user;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qUser);
		
		//多字段模糊匹配
		BooleanBuilder pBuilder = new BooleanBuilder();
		
		if(!StringUtils.isEmpty(criteria.getCode())) {
			pBuilder.andAnyOf(
					qUser.adminEmail.like(new StringBuilder().append("%").append(criteria.getCode()).append("%").toString()),
					qUser.adminName.like(new StringBuilder().append("%").append(criteria.getCode()).append("%").toString()),
					qUser.firstName.like(new StringBuilder().append("%").append(criteria.getCode()).append("%").toString())
					);
		}
		
		
		if(pBuilder!=null) {
			query.where(pBuilder);
		}
		query.orderBy(qUser.auditSection.dateModified.desc() );
		Entitites<User> elist =new Entitites<User>();
		elist.setTotalCount((int) query.count());
		elist.setEntites(query.list(qUser));
		return elist;
	}

	@Override
	public List<Object[]> getPinYin(String where) throws ServiceException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				StringBuilder qs = new StringBuilder();
				qs.append("select USER.USER_ID as id,ADMIN_FIRST_NAME as name,IFNULL(ELT(INTERVAL(CONV(HEX(left(CONVERT(ADMIN_FIRST_NAME USING gbk),1)),16,10), ");
				qs.append("0xB0A1,0xB0C5,0xB2C1,0xB4EE,0xB6EA,0xB7A2,0xB8C1,0xB9FE,0xBBF7,0xBFA6,0xC0AC,0xC2E8,0xC4C3,0xC5B6,0xC5BE,0xC6DA,0xC8BB,0xC8F6,0xCBFA,0xCDDA,0xCEF4,0xD1B9,0xD4D1),");
				qs.append("'A','B','C','D','E','F','G','H','J','K','L','M','N','O','P',");
				qs.append(" 'Q','R','S','T','W','X','Y','Z'),LEFT(ADMIN_FIRST_NAME,1)) as pinyin  from USER");
				if(where!=null){
					qs.append(where);
				}
				qs.append(" order by pinyin,name ");
				
				Query q  =super.getEntityManager().createNativeQuery(qs.toString());
		    	
		    	@SuppressWarnings("unchecked")
				List<Object[]> counts =  q.getResultList();
		    	
		    	return counts;
	}

	@Override
	public User getWithdepartmentNode(Long id,Integer gid) throws ServiceException {
		// TODO Auto-generated method stub
		QUser qUser = QUser.user;
		QDepartmentNode qpNode =QDepartmentNode.departmentNode;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qUser)
		.leftJoin(qUser.pNodes,qpNode).fetch();
		if(gid!=null){
			BooleanBuilder pBuilder = new BooleanBuilder();
			pBuilder.and(qpNode.groupid.eq(gid));
			query.where(pBuilder);
		}
		User user = query.uniqueResult(qUser);
		return user;
	}

	/**
	 * ps 为不等于
	 */
	@Override
	public List<User> getByPermission(List<String> ps) throws ServiceException {
		// TODO Auto-generated method stub
		
		QUser qUser = QUser.user;
		QGroup qGroup = QGroup.group;
		QPermission qPermission = QPermission.permission;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qUser)
		.leftJoin(qUser.groups,qGroup)
		.leftJoin(qGroup.permissions,qPermission);
		
		if(ps !=null && ps.size()>0){
			BooleanBuilder pBuilder = new BooleanBuilder();
			for(String s:ps){
				pBuilder.and(qPermission.permissionName.notEqualsIgnoreCase(s));
			}
			query.where(pBuilder);
		}
		
		return query.distinct().list(qUser);
	}

	@Override
	public User getName(String name) throws ServiceException {
		// TODO Auto-generated method stub
		QUser qUser = QUser.user;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qUser)
			.where(qUser.adminName.eq(name));
		
		

		User user = query.uniqueResult(qUser);
		return user;
	}

	@Override
	public List<Object[]> getPinYinUser() throws ServiceException {
		// TODO Auto-generated method stub
		StringBuilder qs = new StringBuilder();
		qs.append("select USER_ID as id,ADMIN_FIRST_NAME as name,IFNULL(ELT(INTERVAL(CONV(HEX(left(CONVERT(ADMIN_FIRST_NAME USING gbk),1)),16,10), ");
		qs.append("0xB0A1,0xB0C5,0xB2C1,0xB4EE,0xB6EA,0xB7A2,0xB8C1,0xB9FE,0xBBF7,0xBFA6,0xC0AC,0xC2E8,0xC4C3,0xC5B6,0xC5BE,0xC6DA,0xC8BB,0xC8F6,0xCBFA,0xCDDA,0xCEF4,0xD1B9,0xD4D1),");
		qs.append("'A','B','C','D','E','F','G','H','J','K','L','M','N','O','P',");
		qs.append(" 'Q','R','S','T','W','X','Y','Z'),LEFT(ADMIN_FIRST_NAME,1)) as pinyin,TELEPHONE as tel from USER order by pinyin,name ");
		
		Query q  =super.getEntityManager().createNativeQuery(qs.toString());
    	
    	@SuppressWarnings("unchecked")
		List<Object[]> counts =  q.getResultList();

    	
    	return counts;
	}
	

}
