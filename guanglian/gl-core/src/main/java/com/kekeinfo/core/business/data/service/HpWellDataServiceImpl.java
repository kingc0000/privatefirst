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
import com.kekeinfo.core.business.data.dao.HpWellDataDao;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.model.PointEnumType;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.monitordata.model.HpwellData;
import com.kekeinfo.core.business.monitordata.model.PwellData;
import com.kekeinfo.core.business.monitordata.model.WarningData;
import com.kekeinfo.core.business.monitordata.model.WpwellData;
import com.kekeinfo.core.business.point.service.PointService;
import com.kekeinfo.core.business.point.service.WarningService;
import com.kekeinfo.core.business.pumpwell.model.Pumpwell;
import com.kekeinfo.core.business.pumpwell.service.PumpwellService;
import com.kekeinfo.core.business.user.model.AppUser;

@Service("hpWellDataService")
public class HpWellDataServiceImpl extends KekeinfoEntityServiceImpl<Long, HpwellData> implements HpWellDataService {
	
	private HpWellDataDao hpwellDataDao;
	
	@Autowired PumpwellService pumpwellService;
	
	@Autowired PWellDataService pWellDataService;
	
	private @Autowired PointService pointService;

	private @Autowired WarningService warningService;
	
	@Autowired CSiteService cSiteService;
	@Autowired
	public HpWellDataServiceImpl(HpWellDataDao hpwellDataDao) {
		super(hpwellDataDao);
		this.hpwellDataDao = hpwellDataDao;
	}

	/**
	 * 新增观测点数据，操作历史表和当前表，
	 * @param pwell, 如果pwell不为空，则需要同时更新测点的最新状态信息
	 */
	@SuppressWarnings({ "rawtypes" })
	@Transactional
	@Override
	public void saveOrUpdate(HpwellData pData, Pumpwell pwell,String pname,Set<AppUser> aus) throws ServiceException {
		//获取此前最新一条采集数据记录
		HpwellData lastData = (HpwellData) pointService.getLastDataByCid(pwell.getId(), PointEnumType.PUMP);
		boolean update = true;
		if (pData.getId()==null) {
			update = false;
		}
		super.saveOrUpdate(pData);
		super.flush();
		if (pwell!=null) { //如果pwell不为空，则需要更新pwell的最新状态
			pumpwellService.saveStatus(pwell);
		}
		
		try {
			PwellData now = new PwellData();
			PropertyUtils.copyProperties(now, pData);
			now.setHid(pData.getId());
			if (update) { //更新操作
				Entitites<PwellData> dbList = pWellDataService.getListByAttributes(new String[]{"hid"}, new Long[]{now.getHid()}, null);
				if (dbList.getTotalCount()==1) {
					now.setId(dbList.getEntites().get(0).getId());
				}
			}
			pWellDataService.saveOrUpdate(now);
			
			//处理告警数据
			if (pData.getStatus()>0) { //本采集数据为告警数据
				WpwellData wdata = null;
				wdata = new WpwellData();
				PropertyUtils.copyProperties(wdata, pData);
				wdata.setAuditSection(new AuditSection());
				if (lastData!=null && lastData.getStatus()>0) { //上一条采集数据为告警数据，则更新告警数据的对应信息
					WpwellData tmp = (WpwellData) warningService.findLastWarning(pwell.getId(), PointEnumType.PUMP, 0);
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
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 数据删除，先删除历史表，后删除当前表，判断删除的是否是最新的观察值，如果是，则需要更新测点表；
	 */
	@Override
	public Long deleteBydId(Object id) {
		HpwellData entity = hpwellDataDao.getById((Long) id);
		Long cid=null;
		try {
			super.delete(entity);
			List<String> attributes = new ArrayList<String>();
			attributes.add("pWell.id");
			List<Object> values = new ArrayList<Object>();
			values.add(entity.getpWell().getId());
			Map<String, String> orderby = new HashMap<String, String>();
			orderby.put("auditSection.dateCreated", "desc");
			Entitites<PwellData> latest = pWellDataService.getPageListByAttributes(attributes, values, 2, 0, orderby); //获取该井最新的两条记录
			
			PwellData latestDb = null; //最新的数据
			PwellData delDb = null; //当前表需要删除的数据
			if (latest.getTotalCount()>0) {
				latestDb = latest.getEntites().get(0);
			}
			Entitites<PwellData> dbList = pWellDataService.getListByAttributes(new String[]{"hid"}, new Long[]{entity.getId()}, null);
			if (dbList.getTotalCount()>0) {
				delDb = dbList.getEntites().get(0);
				pWellDataService.delete(delDb); //删除对应的当前表记录
			}
			
			if (latestDb!=null&&delDb!=null&&latestDb.getId().equals(delDb.getId())) { //如果删除的是最新的数据，需要更新测点的信息，测点记录次新的数据信息
				Pumpwell pwell = pumpwellService.getByIdWithCSite(entity.getpWell().getId());
				int laststatus=pwell.getDataStatus();
				if (latest.getEntites().size()>1) { //数据记录至少有2条以上数据
					PwellData secondDb = latest.getEntites().get(1);
					pwell.setrFlow(secondDb.getFlow());
					pwell.setrWater(secondDb.getWater());
				} else { //数据记录只有一条数据，则将状态恢复为正常、故障、关闭之一
					pwell.setrFlow(null);
					pwell.setrWater(null);
				}
				pwell.updateDataStatusByData(pwell.getrFlow(), pwell.getrWater()); //更新状态
				if(laststatus==0){
					//++
					if(pwell.getDataStatus()>0){
						ConstructionSite scite = pwell.getcSite();
						scite.setRunstatus(scite.getRunstatus()+1);
						cid =scite.getId();
						cSiteService.update(scite);
					}
				}else{
					//--
					if(pwell.getDataStatus()==0){
						ConstructionSite scite = pwell.getcSite();
						int runstatus=scite.getRunstatus();
						if(runstatus>0){
							scite.setRunstatus(scite.getRunstatus()-1);
							cid =scite.getId();
							cSiteService.update(scite);
						}
					}
				}
				pumpwellService.update(pwell);
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return cid;
	}
}
