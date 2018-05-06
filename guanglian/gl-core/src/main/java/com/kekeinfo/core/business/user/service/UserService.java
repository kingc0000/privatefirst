package com.kekeinfo.core.business.user.service;

import java.util.List;
import java.util.Set;

import com.kekeinfo.core.business.common.model.Criteria;
import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.department.model.DepartmentNode;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.user.model.User;

public interface UserService extends KekeinfoEntityService<Long, User> {

	User getByUserName(String userName) throws ServiceException;

	List<User> listUser() throws ServiceException;
	
	/**
	 * Create or update a User
	 * @param user
	 * @throws ServiceException
	 */
	void saveOrUpdate(User user,Set<DepartmentNode> delnode) throws ServiceException;
	
	void saveOrUpdateWithRight(User user) throws ServiceException;
	
	void deleteUser(User user) throws ServiceException;
	
	
	User getByUserId(long id);
	
	List<Object[]> getPinYin( String where) throws ServiceException;

	Entitites<User> getByCriteria(Criteria criteria) throws ServiceException;

	User getWithdepartmentNode(Long id,Integer gid) throws ServiceException;
	
	/**
	 * ps 为不等于
	 * @param ps
	 * @return
	 * @throws ServiceException
	 */
	List<User> getByPermission(List<String> ps) throws ServiceException;
	
	User getName(String name) throws ServiceException;
	
	List<Object[]> getPinYinUser() throws ServiceException;
}
