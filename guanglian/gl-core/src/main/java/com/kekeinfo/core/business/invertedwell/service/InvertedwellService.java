package com.kekeinfo.core.business.invertedwell.service;

import java.util.List;


import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.invertedwell.model.Invertedwell;

public interface InvertedwellService extends KekeinfoEntityService<Long, Invertedwell> {
	Invertedwell getByIdWithCSite(Long pid) throws ServiceException;
	Invertedwell getByIdWithPointLink(Long pid) throws ServiceException;
	
	public void saveStatus(Invertedwell iwell) throws ServiceException;
	List<Invertedwell> getWarningData(List<Long> ids) throws ServiceException;
	List<Invertedwell> getBycid(Long cid) throws ServiceException;
	public void saveQcode(Invertedwell iwell) throws ServiceException;

	void saveOrUpdate(Invertedwell iwell,String delids) throws ServiceException;
	
	void remove(Invertedwell iwell) throws ServiceException;
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
	
