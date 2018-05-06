package com.kekeinfo.core.business.department.dao;

import java.util.List;

import com.kekeinfo.core.business.department.model.Department;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.generic.exception.ServiceException;
public interface DepartmentDao extends KekeinfoEntityDao<Long, Department> {
	
	public Department getMerchantStore(Long merchantStoreId);

	public Department getMerchantStore(String code) throws ServiceException;
	public Department getMerchantCode(String code) throws ServiceException;
	
	List<Object[]> getPinYin() throws ServiceException;
	
	public List<Department> listWithCite() throws ServiceException;
	
	public List<Department> listByCite(List<Long> cids) throws ServiceException;
	
}
