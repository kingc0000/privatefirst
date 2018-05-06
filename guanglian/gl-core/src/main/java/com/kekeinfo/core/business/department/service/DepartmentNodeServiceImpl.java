package com.kekeinfo.core.business.department.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.department.dao.DepartmentNodeDao;
import com.kekeinfo.core.business.department.model.DepartmentNode;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;

@Service("departmentNodeService")
public class DepartmentNodeServiceImpl extends KekeinfoEntityServiceImpl<Long, DepartmentNode> 
		implements DepartmentNodeService {
	

		
	
		
	
	private DepartmentNodeDao departmentNodeDao;
	
	@Autowired
	public DepartmentNodeServiceImpl(DepartmentNodeDao departmentNodeDao) {
		super(departmentNodeDao);
		this.departmentNodeDao = departmentNodeDao;
	}

	@Override
	public List<DepartmentNode> getByGroupID(int groupid,Long userid) throws ServiceException {
		// TODO Auto-generated method stub
		return departmentNodeDao.getByGroupID(groupid,userid);
	}

	@Override
	public List<DepartmentNode> getBytype(String type,Long userid) throws ServiceException {
		// TODO Auto-generated method stub
		return departmentNodeDao.getBytype(type,userid);
	}

	@Override
	public List<DepartmentNode> getByTypePName(String type, Long userid, String pnmae) throws ServiceException {
		// TODO Auto-generated method stub
		return departmentNodeDao.getByTypePName(type, userid, pnmae);
	}

	@Override
	public List<DepartmentNode> getByDepartmentAndType(String type, Long pid) throws ServiceException {
		// TODO Auto-generated method stub
		return departmentNodeDao.getByDepartmentAndType(type, pid);
	}

	@Override
	public List<DepartmentNode> getByTypePID(Long pid) throws ServiceException {
		// TODO Auto-generated method stub
		return departmentNodeDao.getByTypePID(pid);
	}
	

}
