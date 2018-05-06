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
import com.kekeinfo.core.business.data.dao.HiWellDataDao;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.model.PointEnumType;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.invertedwell.model.Invertedwell;
import com.kekeinfo.core.business.invertedwell.service.InvertedwellService;
import com.kekeinfo.core.business.monitordata.model.HiwellData;
import com.kekeinfo.core.business.monitordata.model.IwellData;
import com.kekeinfo.core.business.monitordata.model.WarningData;
import com.kekeinfo.core.business.monitordata.model.WiwellData;
import com.kekeinfo.core.business.point.service.PointService;
import com.kekeinfo.core.business.point.service.WarningService;
import com.kekeinfo.core.business.user.model.AppUser;

@Service("hiWellDataService")
public class HiWellDataServiceImpl extends KekeinfoEntityServiceImpl<Long, HiwellData> implements HiWellDataService {
	
	private HiWellDataDao hiwellDataDao;
	
	@Autowired InvertedwellService invertedwellService;
	
	@Autowired IWellDataService iWellDataService;
	
	private @Autowired PointService pointService;

	private @Autowired WarningService warningService;
	@Autowired CSiteService cSiteService;
	@Autowired
	public HiWellDataServiceImpl(HiWellDataDao hiwellDataDao) {
		super(hiwellDataDao);
		this.hiwellDataDao = hiwellDataDao;
	}
	
	/**
	 * 新增观测点数据，操作历史表和当前表，
	 * @param pwell, 如果pwell不为空，则需要同时更新测点的最新状态信息
	 */
	@SuppressWarnings({ "rawtypes" })
	@Transactional
	@Override
	public void saveOrUpdate(HiwellData pData, Invertedwell pwell,String pname,Set<AppUser> aus) throws ServiceException {
		//获取此前最新一条采集数据记录
		HiwellData lastData = (HiwellData) pointService.getLastDataByCid(pwell.getId(), PointEnumType.INVERTED);
		boolean update = true;
		if (pData.getId()==null) {
			update = false;
		}
		super.saveOrUpdate(pData);
		super.flush();
		if (pwell!=null) { //如果pwell不为空，则需要更新pwell的最新状态
			invertedwellService.saveStatus(pwell);
		}
		
		IwellData now = new IwellData();
		try {
//			BeanUtils.copyProperties(now, pData);
			PropertyUtils.copyProperties(now, pData);
			now.setHid(pData.getId());
			if (update) { //更新操作
				Entitites<IwellData> dbList = iWellDataService.getListByAttributes(new String[]{"hid"}, new Long[]{now.getHid()}, null);
				if (dbList.getTotalCount()==1) {
					now.setId(dbList.getEntites().get(0).getId());
				}
			}
			
			//处理告警数据
			if (pData.getStatus()>0) { //本采集数据为告警数据
				WiwellData wdata = null;
				wdata = new WiwellData();
				PropertyUtils.copyProperties(wdata, pData);
				wdata.setAuditSection(new AuditSection());
				if (lastData!=null && lastData.getStatus()>0) { //上一条采集数据为告警数据，则更新告警数据的对应信息
					WiwellData tmp = (WiwellData) warningService.findLastWarning(pwell.getId(), PointEnumType.INVERTED, 0);
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
		iWellDataService.saveOrUpdate(now);
	}
	
	/**
	 * 数据删除，先删除历史表，后删除当前表，判断删除的是否是最新的观察值，如果是，则需要更新测点表；
	 */
	@Override
	public Long deleteBydId(Object id) {
		HiwellData entity = hiwellDataDao.getById((Long) id);
		Long cid=null;
		try {
			super.delete(entity);
			List<String> attributes = new ArrayList<String>();
			attributes.add("iWell.id");
			List<Object> values = new ArrayList<Object>();
			values.add(entity.getiWell().getId());
			Map<String, String> orderby = new HashMap<String, String>();
			orderby.put("auditSection.dateCreated", "desc");
			Entitites<IwellData> latest = iWellDataService.getPageListByAttributes(attributes, values, 2, 0, orderby); //获取最新的两条记录
			
			IwellData latestDb = null; //最新的数据
			IwellData delDb = null; //当前表需要删除的数据
			if (latest.getTotalCount()>0) {
				latestDb = latest.getEntites().get(0);
			}
			Entitites<IwellData> dbList = iWellDataService.getListByAttributes(new String[]{"hid"}, new Long[]{entity.getId()}, null);
			if (dbList.getTotalCount()>0) { 
				delDb = dbList.getEntites().get(0);
				iWellDataService.delete(delDb); //删除对应的当前表记录
			}
			
			if (latestDb!=null&&delDb!=null&&latestDb.getId().equals(delDb.getId())) { //如果删除的是最新的数据，需要更新测点的信息，测点记录次新的数据信息
				Invertedwell pwell = invertedwellService.getByIdWithCSite(entity.getiWell().getId());
				int laststatus=pwell.getDataStatus();
				if (latest.getEntites().size()>1) { //数据记录至少有2条以上数据
					IwellData secondDb = latest.getEntites().get(1);
					pwell.setrFlow(secondDb.getFlow());
					pwell.setrPressure(secondDb.getPressure());
				} else { //数据记录只有一条数据，则将状态恢复为正常、故障、关闭之一
					pwell.setrFlow(null);
					pwell.setrPressure(null);;
				}
				pwell.updateDataStatusByData(pwell.getrFlow(), pwell.getrPressure()); //更新状态
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
				invertedwellService.update(pwell);
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return cid;
	}


}
