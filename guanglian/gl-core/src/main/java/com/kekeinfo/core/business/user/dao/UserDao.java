package com.kekeinfo.core.business.user.dao;

import java.util.List;

import com.kekeinfo.core.business.common.model.Criteria;
import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.user.model.User;

public interface UserDao extends KekeinfoEntityDao<Long, User> {

	User getByUserName(String userName);

	List<User> listUser();
	
	User getById(Long id);
	
	List<Object[]> getPinYin(String where) throws ServiceException;
	
	List<Object[]> getPinYinUser() throws ServiceException;
	
	Entitites<User> getByCriteria(Criteria criteria) throws ServiceException;
	
	User getWithdepartmentNode(Long id,Integer gid) throws ServiceException;
	
	List<User> getByPermission(List<String> ps) throws ServiceException;
	
	User getName(String name) throws ServiceException;

}
