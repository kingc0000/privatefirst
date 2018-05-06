package com.kekeinfo.core.business.data.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.common.model.audit.AuditSection;
import com.kekeinfo.core.business.constructionsite.model.ConstructionSite;
import com.kekeinfo.core.business.csite.service.CSiteService;
import com.kekeinfo.core.business.data.dao.HoWellDataDao;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.model.PointEnumType;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.monitordata.model.HowellData;
import com.kekeinfo.core.business.monitordata.model.OwellData;
import com.kekeinfo.core.business.monitordata.model.WarningData;
import com.kekeinfo.core.business.monitordata.model.WowellData;
import com.kekeinfo.core.business.observewell.model.Observewell;
import com.kekeinfo.core.business.observewell.service.ObservewellService;
import com.kekeinfo.core.business.point.service.PointService;
import com.kekeinfo.core.business.point.service.WarningService;
import com.kekeinfo.core.business.user.model.AppUser;

@Service("hoWellDataService")
public class HoWellDataServiceImpl extends KekeinfoEntityServiceImpl<Long, HowellData> implements HoWellDataService {
	
	private HoWellDataDao howellDataDao;
	
	@Autowired ObservewellService observewellService;
	
	@Autowired OWellDataService oWellDataService;
	
	@Autowired private PointService pointService;

	@Autowired private WarningService warningService;
	@Autowired CSiteService cSiteService;
	
	@Autowired
	public HoWellDataServiceImpl(HoWellDataDao howellDataDao) {
		super(howellDataDao);
		this.howellDataDao = howellDataDao;
	}

	/**
	 * 新增观测点数据，操作历史表和当前表，
	 * @param pwell, 如果pwell不为空，则需要同时更新测点的最新状态信息
	 */
	@SuppressWarnings({ "rawtypes" })
	@Transactional
	@Override
	public void saveOrUpdate(HowellData pData, Observewell pwell,String pname,Set<AppUser> aus) throws ServiceException {
		//获取此前最新一条告警记录
		HowellData lastData = (HowellData) pointService.getLastDataByCid(pwell.getId(), PointEnumType.OBSERVE);
		boolean update = true;
		if (pData.getId()==null) {
			update = false;
		}
		super.saveOrUpdate(pData);
		super.flush();
		if (pwell!=null) { //如果pwell不为空，则需要更新pwell的最新状态
			observewellService.saveStatus(pwell);
		}
		
		OwellData now = new OwellData();
		try {
			PropertyUtils.copyProperties(now, pData);
			now.setHid(pData.getId());
			if (update) { //更新操作
				Entitites<OwellData> dbList = oWellDataService.getListByAttributes(new String[]{"hid"}, new Long[]{now.getHid()}, null);
				if (dbList.getTotalCount()==1) {
					now.setId(dbList.getEntites().get(0).getId());
				}
			}
			
			//处理告警数据
			if (pData.getStatus()>0) { //本采集数据为告警数据
				WowellData wdata = null;
				wdata = new WowellData();
				PropertyUtils.copyProperties(wdata, pData);
				wdata.setAuditSection(new AuditSection());
				if (lastData!=null && lastData.getStatus()>0) { //上一条采集数据为告警数据，则更新告警数据的对应信息
					WowellData tmp = (WowellData) warningService.findLastWarning(pwell.getId(), PointEnumType.OBSERVE, 0);
					if (tmp!=null) {
						wdata.setId(tmp.getId());
						wdata.getAuditSection().setDateCreated(tmp.getAuditSection().getDateCreated());
					} else {
						wdata.setId(null);
					}
				} else { //新增一条告警记录
					wdata.setId(null);
				}
				wdata.setPoint(pwell);
				wdata.setcSite(pwell.getcSite());
				warningService.saveOrUpdate((WarningData)wdata,pwell.getName(),aus,pname);
				
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		oWellDataService.saveOrUpdate(now);
	}
	
	/**
	 * 数据删除，先删除历史表，后删除当前表，判断删除的是否是最新的观察值，如果是，则需要更新测点表；
	 */
	@Override
	public Long deleteBydId(Object id) {
		HowellData entity = howellDataDao.getById((Long) id);
		Long cid=null;
		try {
			super.delete(entity);
			List<String> attributes = new ArrayList<String>();
			attributes.add("oWell.id");
			List<Object> values = new ArrayList<Object>();
			values.add(entity.getoWell().getId());
			Map<String, String> orderby = new HashMap<String, String>();
			orderby.put("auditSection.dateCreated", "desc");
			Entitites<OwellData> latest = oWellDataService.getPageListByAttributes(attributes, values, 2, 0, orderby); //获取最新的两条记录
			
			OwellData latestDb = null; //最新的数据
			OwellData delDb = null; //当前表需要删除的数据
			if (latest.getTotalCount()>0) {
				latestDb = latest.getEntites().get(0);
			}
			Entitites<OwellData> dbList = oWellDataService.getListByAttributes(new String[]{"hid"}, new Long[]{entity.getId()}, null);
			if (dbList.getTotalCount()>0) { 
				delDb = dbList.getEntites().get(0);
				oWellDataService.delete(delDb); //删除对应的当前表记录
			}
			
			if (latestDb!=null&&delDb!=null&&latestDb.getId().equals(delDb.getId())) { //如果删除的是最新的数据，需要更新测点的信息，测点记录次新的数据信息
				Observewell pwell = observewellService.getByIdWithCSite(entity.getoWell().getId());
				int laststatus=pwell.getDataStatus();
				if (latest.getEntites().size()>1) { //数据记录至少有2条以上数据
					OwellData secondDb = latest.getEntites().get(1);
					pwell.setrWater(secondDb.getWater());
					pwell.setrTemperature(secondDb.getTemperature());
				} else { //数据记录只有一条数据，则将状态恢复为正常、故障、关闭之一
					pwell.setrWater(null);
					pwell.setrTemperature(null);;
				}
				pwell.updateDataStatusByData(pwell.getrWater(), pwell.getrTemperature()); //更新状态
				observewellService.update(pwell);
				if(laststatus==0){
					//++
					if(pwell.getDataStatus()>0){
						ConstructionSite scite = pwell.getcSite();
						cid =scite.getId();
						scite.setRunstatus(scite.getRunstatus()+1);
						cSiteService.update(scite);
					}
				}else{
					//--
					if(pwell.getDataStatus()==0){
						ConstructionSite scite = pwell.getcSite();
						int runstatus=scite.getRunstatus();
						cid =scite.getId();
						if(runstatus>0){
							scite.setRunstatus(scite.getRunstatus()-1);
							cSiteService.update(scite);
						}
					}
				}
			}
			
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return cid;
	}
}

