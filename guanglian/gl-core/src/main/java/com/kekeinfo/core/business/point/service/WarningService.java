package com.kekeinfo.core.business.point.service;

import java.util.Set;

import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.model.PointEnumType;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.monitordata.model.WarningData;
import com.kekeinfo.core.business.point.model.Basepoint;
import com.kekeinfo.core.business.point.model.BasepointInfo;
import com.kekeinfo.core.business.point.model.BasepointLink;
import com.kekeinfo.core.business.user.model.AppUser;

public interface WarningService extends KekeinfoEntityService<Long, WarningData<Basepoint<BasepointLink<?>, BasepointInfo<?>>>> {
	
	@SuppressWarnings("rawtypes")
	public Entitites<WarningData> getListByCid(Long cid, PointEnumType type, Integer warningType, String search, Integer limit, Integer offset) throws ServiceException;

	/**
	 * 获取测点的最新告警数据
	 * @param id
	 * @param warningType 告警数据类型 
	 * 0：数据采集告警
	 * 1：断电告警
	 * 如果为null，则全部查询
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public WarningData findLastWarning(Long pid, PointEnumType type, Integer warningType) throws ServiceException;

	public void deleteById(Long id, PointEnumType pointType) throws ServiceException;
	
	@SuppressWarnings("rawtypes")
	public void saveOrUpdate(WarningData wdata,String name,Set<AppUser> ausers,String pname) throws ServiceException;
}
	
