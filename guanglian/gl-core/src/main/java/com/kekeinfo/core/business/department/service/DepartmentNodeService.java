package com.kekeinfo.core.business.department.service;


import java.util.List;

import com.kekeinfo.core.business.department.model.DepartmentNode;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;

public interface DepartmentNodeService extends KekeinfoEntityService<Long, DepartmentNode>{

	List<DepartmentNode> getByGroupID(int groupid,Long userid) throws ServiceException ;
	List<DepartmentNode> getBytype(String type,Long userid) throws ServiceException ;
	/**
	 * 
	 * @param type  not Eq
	 * @param userid
	 * @param pnmae
	 * @return
	 * @throws ServiceException
	 */
	List<DepartmentNode> getByTypePName(String type,Long userid,String pnmae) throws ServiceException ;
	
	/**
	 * 
	 * @param type not Eq
	 * @param pid departmentid or projectid
	 * @return
	 * @throws ServiceException
	 */
	List<DepartmentNode> getByDepartmentAndType(String type,Long pid) throws ServiceException ;
	
	/**
	 * 通过项目ID查找
	 * @param pid
	 * @return
	 */
	List<DepartmentNode> getByTypePID(Long pid) throws ServiceException ;
}
