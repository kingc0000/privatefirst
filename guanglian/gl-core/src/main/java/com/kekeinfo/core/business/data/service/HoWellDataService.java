package com.kekeinfo.core.business.data.service;

import java.util.Set;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.monitordata.model.HowellData;
import com.kekeinfo.core.business.observewell.model.Observewell;
import com.kekeinfo.core.business.user.model.AppUser;

public interface HoWellDataService extends KekeinfoEntityService<Long, HowellData> {

	/**
	 * 新增观测点数据，操作历史表和当前表，
	 * @param pwell, 如果pwell不为空，则需要同时更新测点的最新状态信息
	 */
	public void saveOrUpdate(HowellData pData, Observewell pwell,String pname,Set<AppUser> aus) throws ServiceException;
	
	public Long  deleteBydId(Object id) throws ServiceException;
}
	
