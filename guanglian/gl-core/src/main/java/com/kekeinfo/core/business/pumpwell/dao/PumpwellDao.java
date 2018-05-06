package com.kekeinfo.core.business.pumpwell.dao;

import java.util.List;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.monitordata.model.HowellData;
import com.kekeinfo.core.business.monitordata.model.WowellData;
import com.kekeinfo.core.business.pumpwell.model.Pumpwell;


public interface PumpwellDao extends KekeinfoEntityDao<Long, Pumpwell> {
	Pumpwell getByIdWithCSite(Long pid) throws ServiceException;
	
	Pumpwell getByIdWithPointLink(Long pid) throws ServiceException;
	
	List<Pumpwell> getWarningData(List<Long> ids) throws ServiceException;
	
	List<Pumpwell> getByCid(Long cid) throws ServiceException;
	
	/**
	 * 根据项目id和降水井状态，获取降水井集合
	 * @param pid
	 * @param status
	 * @return
	 * @throws ServiceException
	 */
	List<Pumpwell> getListByPidAndPowerStatus(Long pid, Integer[] status) throws ServiceException;
	
	/**
	 * 获取指定网关的所有降水井测点，如果网关id为null，则获取所有网关不为空的降水井测点
	 * @param gateway
	 * @return
	 * @throws ServiceException
	 */
	List<Pumpwell> getListByGateway(Long gateway) throws ServiceException;

	/**
	 * 批量将降水井采集数据转化成观测井采集数据（历史数据和当前数据）
	 * @param well
	 * @throws ServiceException
	 */
	void batchSaveOwellData(List<HowellData> howellList) throws ServiceException;
	
	public List<Pumpwell> getByIds(List<Long> ids) throws ServiceException;
	
	/**
	 * 批量将降水井告警数据转化成观测井告警数据
	 * @param well
	 * @throws ServiceException
	 */
	void batchSaveWowellData(List<WowellData> wowellList) throws ServiceException;
}
