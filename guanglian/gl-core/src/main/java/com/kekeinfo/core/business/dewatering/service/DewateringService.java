package com.kekeinfo.core.business.dewatering.service;

import java.util.List;

import com.kekeinfo.core.business.dewatering.model.Dewatering;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;

public interface DewateringService extends KekeinfoEntityService<Long, Dewatering> {
	
	Dewatering getByIdWithCSite(Long pid) throws ServiceException;
	Dewatering getByIdWithPointLink(Long pid) throws ServiceException;
	
	List<Dewatering> getWarningData(List<Long> ids) throws ServiceException;

	public void saveStatus(Dewatering pwell) throws ServiceException;
	public void saveQcode(Dewatering pwell) throws ServiceException;
	void saveOrUpdate(Dewatering pwell,String delids) throws ServiceException;
	
	void remove(Dewatering pwell) throws ServiceException;
	List<Dewatering> getByCid(Long cid) throws ServiceException;
	/**
	 * 根据项目id和抽水井状态，获取抽水井集合
	 * @param pid
	 * @param status
	 * @return
	 * @throws ServiceException
	 */
	List<Dewatering> getListByPidAndPowerStatus(Long pid, Integer[] status) throws ServiceException;
	public List<Dewatering> getByIds(List<Long> ids) throws ServiceException ;
}
	
