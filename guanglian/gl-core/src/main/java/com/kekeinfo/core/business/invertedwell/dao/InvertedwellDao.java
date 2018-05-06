package com.kekeinfo.core.business.invertedwell.dao;

import java.util.List;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.invertedwell.model.Invertedwell;


public interface InvertedwellDao extends KekeinfoEntityDao<Long, Invertedwell> {
	Invertedwell getByIdWithCSite(Long pid) throws ServiceException;
	Invertedwell getByIdWithPointLink(Long pid) throws ServiceException;
	public List<Invertedwell> getWarningData(List<Long> ids) throws ServiceException;
	public List<Invertedwell> getBycid(Long cid) throws ServiceException;
	
	/**
	 * 根据项目id和抽水井状态，获取抽水井集合
	 * @param pid
	 * @param status
	 * @return
	 * @throws ServiceException
	 */
	List<Invertedwell> getListByPidAndPowerStatus(Long pid, Integer[] status) throws ServiceException;
	
	public List<Invertedwell> getByIds(List<Long> ids) throws ServiceException ;
}
