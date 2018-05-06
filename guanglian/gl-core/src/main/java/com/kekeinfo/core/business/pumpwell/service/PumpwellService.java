package com.kekeinfo.core.business.pumpwell.service;

import java.util.List;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.pumpwell.model.Pumpwell;

public interface PumpwellService extends KekeinfoEntityService<Long, Pumpwell> {
	
	Pumpwell getByIdWithCSite(Long pid) throws ServiceException;
	Pumpwell getByIdWithPointLink(Long pid) throws ServiceException;
	
	List<Pumpwell> getWarningData(List<Long> ids) throws ServiceException;

	public void saveStatus(Pumpwell pwell) throws ServiceException;
	
	public void saveQcode(Pumpwell pwell) throws ServiceException;
	
	void saveOrUpdate(Pumpwell pwell,String delids) throws ServiceException;
	
	void remove(Pumpwell pwell) throws ServiceException;
	
	List<Pumpwell> getByCid(Long cid) throws ServiceException;
	
	/**
	 * 根据项目id和抽水井状态，获取抽水井集合
	 * @param pid
	 * @param status
	 * @return
	 * @throws ServiceException
	 */
	List<Pumpwell> getListByPidAndPowerStatus(Long pid, Integer[] status) throws ServiceException;
	
	/**
	 * 获取指定网关的所有抽水井测点，如果网关id为null，则获取所有网关不为空的抽水井测点
	 * @param gateway
	 * @return
	 * @throws ServiceException
	 */
	List<Pumpwell> getListByGateway(Long gateway) throws ServiceException;
	
	public int switchWell(Long wid) throws ServiceException;
	public List<Pumpwell> getByIds(List<Long> ids) throws ServiceException;
}
	
