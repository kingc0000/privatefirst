package com.kekeinfo.core.business.csite.dao;

import java.util.Date;
import java.util.List;

import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.constructionsite.model.ConstructionSite;
import com.kekeinfo.core.business.constructionsite.model.CsiteCriteria;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.generic.exception.ServiceException;

public interface CSiteDao extends KekeinfoEntityDao<Long, ConstructionSite> {
	
	ConstructionSite getByUserName(String username) throws ServiceException;
	
	ConstructionSite getByCid(long cid) throws ServiceException;
	
	ConstructionSite getByCidWithWell(long cid) throws ServiceException;
	
	ConstructionSite getByCidWithALLWell(long cid) throws ServiceException;
	
	ConstructionSite getById(long cid) throws ServiceException;
	ConstructionSite getBypId(long cid) throws ServiceException;
	
	ConstructionSite getByIdWithImg(long cid) throws ServiceException;
	
	ConstructionSite getByIdWithDepartment(long cid) throws ServiceException;
	
	Entitites<ConstructionSite> getByCriteria(CsiteCriteria criteria) throws ServiceException;
	
	Entitites<ConstructionSite>  getByCsite(ConstructionSite csite,long offset) throws ServiceException;
	
	List<ConstructionSite>  getByIds(List<Long> ids) throws ServiceException;
	
	List<ConstructionSite>  getByPid(Long pid) throws ServiceException;
	
	List<ConstructionSite>  withZone() throws ServiceException;
	
	List<ConstructionSite>  withDepartment() throws ServiceException;
	
	public List<ConstructionSite> shengtong(Date date) throws ServiceException;
	/**
	 * 查询项目下所有测点信息，包括测点的扩展信息
	 * @param cid
	 * @return
	 * @throws ServiceException
	 */
	public ConstructionSite getByCidForwells(long cid) throws ServiceException;
	
	public ConstructionSite withProjectGroup(long cid) throws ServiceException;
	
}
