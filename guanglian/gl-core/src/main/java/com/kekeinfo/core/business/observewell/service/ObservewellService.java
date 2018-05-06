package com.kekeinfo.core.business.observewell.service;

import java.util.List;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.observewell.model.Observewell;

public interface ObservewellService extends KekeinfoEntityService<Long, Observewell> {
	
	Observewell getByIdWithCSite(Long pid) throws ServiceException;
	Observewell getByIdWithPointLink(Long pid) throws ServiceException;
	public void saveStatus(Observewell owell) throws ServiceException;
	public List<Observewell> getWarningData(List<Long> ids) throws ServiceException;
	List<Observewell> getBycid(Long cid) throws ServiceException;
	
	public void saveQcode(Observewell pwell) throws ServiceException;
	void saveOrUpdate(Observewell owell,String delids) throws ServiceException;
	
	void remove(Observewell owell) throws ServiceException;
	
	/**
	 * 根据项目id和观测井状态，获取观测井集合
	 * @param pid
	 * @param status
	 * @return
	 * @throws ServiceException
	 */
	public List<Observewell> getListByPidAndPowerStatus(Long pid, Integer[] status) throws ServiceException;
	
	public int switchWell(Long wid) throws ServiceException;
	
	public List<Observewell> getByIds(List<Long> ids) throws ServiceException;
}
	
