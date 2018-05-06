package com.kekeinfo.core.business.department.dao;

import java.util.List;

import com.kekeinfo.core.business.department.model.DepartmentNode;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.generic.exception.ServiceException;
public interface DepartmentNodeDao extends KekeinfoEntityDao<Long, DepartmentNode> {
	
	List<DepartmentNode> getByGroupID(int groupid,Long userid) ;
	
	List<DepartmentNode> getBytype(String type,Long userid) ;
	

	List<DepartmentNode> getByTypePName(String type,Long userid,String pnmae) ;
	
	/**
	 * 通过项目ID查找
	 * @param pid
	 * @return
	 */
	List<DepartmentNode> getByTypePID(Long pid) ;
	/**
	 * 
	 * @param type
	 * @param pid departmentid or projectid
	 * @return
	 * @throws ServiceException
	 */
	List<DepartmentNode> getByDepartmentAndType(String type,Long pid) throws ServiceException ;
}
