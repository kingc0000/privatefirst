package com.kekeinfo.core.business.point.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.model.PointEnumType;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.point.dao.PointLinkDao;
import com.kekeinfo.core.business.point.model.BasepointLink;

@Service("pointLinkService")
public class PointLinkServiceImpl extends KekeinfoEntityServiceImpl<Long, BasepointLink<?>> implements PointLinkService {
	
	private PointLinkDao pointLinkDao;
	@Autowired
	public PointLinkServiceImpl(PointLinkDao pointLinkDao) {
		super(pointLinkDao);
		this.pointLinkDao = pointLinkDao;
	}
	
	/**
	 * 获取项目下所对应测点类型的所有测点数据配置集合
	 * @param cid 项目id
	 * @param type
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("rawtypes")
	public List<BasepointLink> getListByCid(Long cid, PointEnumType type) throws ServiceException{
		return pointLinkDao.getListByCid(cid, type);
	}

}
