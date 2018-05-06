package com.kekeinfo.core.business.department.service;


import java.util.List;

import com.kekeinfo.core.business.department.model.Department;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.user.model.User;

public interface DepartmentService extends KekeinfoEntityService<Long, Department>{

	public Department getMerchantStore(String merchantStoreCode)
			throws ServiceException;
	
	public Department getByName(String code) throws ServiceException ;

	void saveOrUpdate(Department store) throws ServiceException;
	
	List<Object[]> getPinYin() throws ServiceException;
	
	public List<Department> listWithCite() throws ServiceException;
	
	void saveOrUpdateWithUser(Department store,User user) throws ServiceException;
	
	void deleteWithPermission(Department department) throws ServiceException;
	
	/**
	 * 通过csiteid
	 * @return
	 * @throws ServiceException
	 */
	public List<Department> listByCite(List<Long> cids) throws ServiceException;
	
	public Department getByIDWithCsite(Long id) throws ServiceException;
}
