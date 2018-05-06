package com.kekeinfo.core.business.point.dao;

import java.util.List;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.model.PointEnumType;
import com.kekeinfo.core.business.point.model.Basepoint;
import com.kekeinfo.core.business.point.model.BasepointInfo;
import com.kekeinfo.core.business.point.model.BasepointLink;


public interface PointDao extends KekeinfoEntityDao<Long, Basepoint<BasepointLink<?>, BasepointInfo<?>>> {
	
	@SuppressWarnings("rawtypes")
	Basepoint getById(Long wellId, PointEnumType type);
	/**
	 * 获取指定网关的对应所有测点（项目未结束），如果网关id为null，则获取所有网关不为空的对应测点
	 * @param gateway
	 * @param type 测点类型
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("rawtypes")
	List<Basepoint> getListByGateway(Long gateway, PointEnumType type) throws ServiceException;
	
	/**
	 * 获取项目下面所对应的测点集合
	 * @param cid
	 * @param type 测点类型
	 * @param visible 是否获取地图可见的测点，如果为null，则都获取
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("rawtypes")
	public List<Basepoint> getListByCid(Long cid, PointEnumType type, Boolean visible) throws ServiceException;
	
	/**
	 * 获取项目下面所对应的测点集合
	 * @param cid
	 * @param type 测点类型
	 * @param visible 是否获取地图可见的测点，如果为null，则都获取
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("rawtypes")
	public List<Basepoint> getListNameByCid(Long cid, PointEnumType type) throws ServiceException;
	
	/**
	 * 为处理测点数据采集监控，获取未结束的项目下面所对应的测点集合
	 * @param cid
	 * @param type
	 * @param closed 项目是否已经结束
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("rawtypes")
	public List<Basepoint> getListForGather(Long cid, PointEnumType type) throws ServiceException;
	
	/**
	 * 获取测点最新采集数据
	 * @param wellId 测点ID
	 * @param type 测点类型
	 * @return
	 * @throws ServiceException
	 */
	public Object getLastDataByCid(Long wellId, PointEnumType type);
	
	/**
	 * 获取定时开关机的测点
	 * @param type
	 * @param closed 项目是否已经结束
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("rawtypes")
	public List<Basepoint> getListForAuto(PointEnumType type) throws ServiceException;
	
	/**
	 * 获取定时开关机的测点
	 * @param type
	 * @param closed 项目是否已经结束
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("rawtypes")
	public List<Basepoint> getListForAutoDeep(PointEnumType type) throws ServiceException;
}
