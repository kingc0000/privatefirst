package com.kekeinfo.core.business.point.dao;

import java.util.List;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.model.PointEnumType;
import com.kekeinfo.core.business.point.model.BasepointLink;


public interface PointLinkDao extends KekeinfoEntityDao<Long, BasepointLink<?>> {
	
	/**
	 * 
	 * @param cid
	 * @param type
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("rawtypes")
	public List<BasepointLink> getListByCid(Long cid, PointEnumType type) throws ServiceException;
}
