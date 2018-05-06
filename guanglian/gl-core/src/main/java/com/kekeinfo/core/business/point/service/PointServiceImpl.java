package com.kekeinfo.core.business.point.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.deformmonitor.model.Deformmonitor;
import com.kekeinfo.core.business.dewatering.model.Dewatering;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.model.PointEnumType;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.invertedwell.model.Invertedwell;
import com.kekeinfo.core.business.monitordata.model.WarningData;
import com.kekeinfo.core.business.monitordata.model.WdewellData;
import com.kekeinfo.core.business.monitordata.model.WemonitorData;
import com.kekeinfo.core.business.monitordata.model.WiwellData;
import com.kekeinfo.core.business.monitordata.model.WowellData;
import com.kekeinfo.core.business.monitordata.model.WpwellData;
import com.kekeinfo.core.business.observewell.model.Observewell;
import com.kekeinfo.core.business.point.dao.PointDao;
import com.kekeinfo.core.business.point.model.Basepoint;
import com.kekeinfo.core.business.point.model.BasepointInfo;
import com.kekeinfo.core.business.point.model.BasepointLink;
import com.kekeinfo.core.business.pumpwell.model.Pumpwell;
import com.kekeinfo.core.business.user.model.AppUser;

@Service("pointService")
public class PointServiceImpl extends KekeinfoEntityServiceImpl<Long, Basepoint<BasepointLink<?>, BasepointInfo<?>>> implements PointService {
	
	private PointDao pointDao;
	
	@Autowired private WarningService warningService;
	
	@Autowired
	public PointServiceImpl(PointDao pointDao) {
		super(pointDao);
		this.pointDao = pointDao;
	}

	/**
	 * 获取指定网关的对应所有测点（项目未结束），如果网关id为null，则获取所有网关不为空的对应测点
	 * @param gateway
	 * @param type 测点类型
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("rawtypes")
	public List<Basepoint> getListByGateway(Long gateway, PointEnumType type) throws ServiceException {
		return pointDao.getListByGateway(gateway, type);
	}
	
	/**
	 * 获取项目下面所对应的测点集合
	 * @param cid
	 * @param type 测点类型
	 * @param visible 是否获取地图可见的测点，如果为null，则都获取
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("rawtypes")
	public List<Basepoint> getListByCid(Long cid, PointEnumType type, Boolean visible) throws ServiceException {
		return pointDao.getListByCid(cid, type, visible);
	}
	
	/**
	 * 为处理测点数据采集监控，获取未结束的项目下面所对应的测点集合
	 * @param cid
	 * @param type
	 * @param closed 项目是否已经结束
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("rawtypes")
	public List<Basepoint> getListForGather(Long cid, PointEnumType type) throws ServiceException {
		return pointDao.getListForGather(cid, type);
	}

	/**
	 * 获取测点最新采集数据
	 * @param wellId 测点ID
	 * @param type 测点类型
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Object getLastDataByCid(Long wellId, PointEnumType type) throws ServiceException {
		return pointDao.getLastDataByCid(wellId, type);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void updatePowerstatus(Basepoint point, PointEnumType type,Set<AppUser> ausers,String pname) throws ServiceException {
		if (point.getPowerStatus().intValue()==2) { //断电
			//获取测点在数据库中的断电状态
			
			Basepoint dbPoint = pointDao.getById((Long) point.getId(), type);
			WarningData warning = createWarningData(type);
			warning.setWarningType(1); //设定为断电告警类型
			warning.setPoint(point);
			if (dbPoint.getPowerStatus().intValue()==2) { //原本就断电了，则更新断电最新时间
				//获取db最新断电告警信息
				WarningData tmp = warningService.findLastWarning((Long)point.getId(), type, 1);
				if (tmp!=null) {
					warning.setId(tmp.getId());
					warning.getAuditSection().setDateCreated(tmp.getAuditSection().getDateCreated());
				}
			}
			flushWarningData(warning, point, type);
			warningService.saveOrUpdate(warning,point.getName(),ausers,pname);
		}
		pointDao.update(point);
	}
	
	@SuppressWarnings("rawtypes")
	private WarningData createWarningData(PointEnumType type) {
		WarningData warning = null;
		switch (type) {
		case PUMP:
			warning = new WpwellData();
			break;
		case DEWATERING:
			warning = new WdewellData();
			break;
		case INVERTED:
			warning = new WiwellData();
			break;
		case OBSERVE:
			warning = new WowellData();
			break;
		case DEFORM:
			warning = new WemonitorData();
			break;
		default:
			break;
		}
		return warning;
	}
	
	@SuppressWarnings("rawtypes")
	private void flushWarningData(WarningData warning, Basepoint point, PointEnumType type) {
		
		switch (type) {
		case PUMP:
			Pumpwell well = (Pumpwell) point;
			warning.setcSite(well.getcSite());
			break;
		case DEWATERING:
			Dewatering dewell = (Dewatering) point;
			warning.setcSite(dewell.getcSite());
			break;
		case INVERTED:
			Invertedwell iwell = (Invertedwell) point;
			warning.setcSite(iwell.getcSite());
			break;
		case OBSERVE:
			Observewell owell = (Observewell) point;
			warning.setcSite(owell.getcSite());
			break;
		case DEFORM:
			Deformmonitor dwell = (Deformmonitor) point;
			warning.setcSite(dwell.getcSite());
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Basepoint> getListForAuto(PointEnumType type) throws ServiceException {
		// TODO Auto-generated method stub
		return pointDao.getListForAuto(type);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Basepoint getById(Long wellId, PointEnumType type) {
		// TODO Auto-generated method stub
		return pointDao.getById(wellId, type);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Basepoint> getListNameByCid(Long cid, PointEnumType type) throws ServiceException {
		// TODO Auto-generated method stub
		return pointDao.getListNameByCid(cid, type);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Basepoint> getListForAutoDeep(PointEnumType type) throws ServiceException {
		// TODO Auto-generated method stub
		return pointDao.getListForAutoDeep(type);
	}
	
}
