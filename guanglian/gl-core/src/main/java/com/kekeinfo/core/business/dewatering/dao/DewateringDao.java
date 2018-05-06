package com.kekeinfo.core.business.dewatering.dao;

import java.util.List;

import com.kekeinfo.core.business.dewatering.model.Dewatering;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.generic.exception.ServiceException;


public interface DewateringDao extends KekeinfoEntityDao<Long, Dewatering> {
	Dewatering getByIdWithCSite(Long pid) throws ServiceException;
	Dewatering getByIdWithPointLink(Long pid) throws ServiceException;
	List<Dewatering> getWarningData(List<Long> ids) throws ServiceException;
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
