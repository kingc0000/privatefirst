package com.kekeinfo.core.business.csite.service;


import java.util.Date;
import java.util.List;

import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.constructionsite.model.ConstructionSite;
import com.kekeinfo.core.business.constructionsite.model.CsiteCriteria;
import com.kekeinfo.core.business.department.model.DepartmentNode;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.user.model.User;

public interface CSiteService extends KekeinfoEntityService<Long, ConstructionSite>{
	

	void saveOrUpdate(ConstructionSite constructionSite) throws ServiceException;
	
	public ConstructionSite getByUserName(String username) throws ServiceException;
	
	ConstructionSite getByCid(long cid) throws ServiceException;
	
	Entitites<ConstructionSite> getByCriteria(CsiteCriteria criteria) throws ServiceException;
	
	void saveOrUpdateWithRight(ConstructionSite constructionSite,User user,String delids,String[] cuids,String[] wuids,List<DepartmentNode> pNodes) throws ServiceException;
	
	void deleteByCid(Long cid) throws ServiceException;
	
	void deleteWithPermission(Long cid) throws ServiceException;
	
	ConstructionSite getById(long cid) throws ServiceException;
	
	ConstructionSite getByIdWithDepartment(long cid) throws ServiceException;
	ConstructionSite getBypId(long cid) throws ServiceException;
	/*
	 * 不包括已经结束的项目
	 */
	List<ConstructionSite>  withZone() throws ServiceException;
	
	public List<ConstructionSite> shengtong(Date date) throws ServiceException;
	
	List<ConstructionSite>  withDepartment() throws ServiceException;
	
	List<ConstructionSite>  getByPid(Long pid) throws ServiceException;
	/**
	 * 查询指定部门下所有项目的统计信息
	 * @param dept 如果为空，则查询所有部门下的项目
	 * @param csite
	 * @param offset
	 * @return
	 * @throws ServiceException
	 */
	Entitites<ConstructionSite>  getByCsite(ConstructionSite csite,long offset) throws ServiceException;
	
	List<ConstructionSite>  getByIds(List<Long> ids) throws ServiceException;
	
	ConstructionSite getByCidWithWell(long cid) throws ServiceException;
	
	ConstructionSite getByCidWithALLWell(long cid) throws ServiceException;
	
	ConstructionSite getByIdWithImg(long cid) throws ServiceException;
	
	/**
	 * 查询项目下所有测点信息，包括测点的扩展信息
	 * @param cid
	 * @return
	 * @throws ServiceException
	 */
	public ConstructionSite getByCidForwells(long cid) throws ServiceException;
	
	public ConstructionSite withProjectGroup(long cid) throws ServiceException;
	
	public void saveQcode(ConstructionSite csite) throws ServiceException;
}
