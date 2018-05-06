package com.kekeinfo.core.business.observewell.dao;

import java.util.List;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.monitordata.model.HpwellData;
import com.kekeinfo.core.business.monitordata.model.WpwellData;
import com.kekeinfo.core.business.observewell.model.Observewell;


public interface ObservewellDao extends KekeinfoEntityDao<Long, Observewell> {
	Observewell getByIdWithCSite(Long pid) throws ServiceException;
	Observewell getByIdWithPointLink(Long pid) throws ServiceException;
	List<Observewell> getWarningData(List<Long> ids) throws ServiceException;
	List<Observewell> getBycid(Long cid) throws ServiceException;
	/**
	 * 根据项目id和观测井状态，获取观测井集合
	 * @param pid
	 * @param status
	 * @return
	 * @throws ServiceException
	 */
	public List<Observewell> getListByPidAndPowerStatus(Long pid, Integer[] status) throws ServiceException;
	/**
	 * 批量将观测井采集数据转化成抽水井采集数据（历史数据和当前数据）
	 * @param well
	 * @throws ServiceException
	 */
	void batchSavePwellData(List<HpwellData> hpwellList) throws ServiceException;
	
	public List<Observewell> getByIds(List<Long> ids) throws ServiceException;
	void batchSaveWpwellData(List<WpwellData> wpwellDataList);
}
