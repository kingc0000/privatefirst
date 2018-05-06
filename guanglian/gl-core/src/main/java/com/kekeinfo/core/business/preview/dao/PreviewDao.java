package com.kekeinfo.core.business.preview.dao;

import java.util.List;
import java.util.Set;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.preview.model.Preview;


public interface PreviewDao extends KekeinfoEntityDao<Long,Preview> {	
	
	public List<Object[]> getTotalInfo(Long cid) throws ServiceException;
	
	public Preview getBypid(Long cid) throws ServiceException;
	
	public Preview withProject(Long cid) throws ServiceException;
	
	/**
	 * 获取项目id数组的评论汇总集合
	 * @param cids
	 * @param limit
	 * @param offset
	 * @return
	 * @throws ServiceException
	 */
	public List<Object[]> getTotalInfo(Set<Long> cids, Integer limit, Integer offset) throws ServiceException;

}
