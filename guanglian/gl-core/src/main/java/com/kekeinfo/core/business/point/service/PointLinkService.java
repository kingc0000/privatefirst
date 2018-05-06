package com.kekeinfo.core.business.point.service;

import java.util.List;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.model.PointEnumType;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.point.model.BasepointLink;

public interface PointLinkService extends KekeinfoEntityService<Long, BasepointLink<?>> {
	/**
	 * 获取项目下所对应测点类型的所有测点数据配置集合
	 * @param cid 项目id
	 * @param type
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("rawtypes")
	public List<BasepointLink> getListByCid(Long cid, PointEnumType type) throws ServiceException;
}
	
