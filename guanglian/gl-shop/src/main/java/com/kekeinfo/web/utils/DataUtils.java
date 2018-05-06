package com.kekeinfo.web.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.constructionsite.model.ConstructionSite;
import com.kekeinfo.core.business.csite.service.CSiteService;
import com.kekeinfo.core.business.data.service.DeWellDataService;
import com.kekeinfo.core.business.data.service.EMonitorDataService;
import com.kekeinfo.core.business.data.service.HdeWellDataService;
import com.kekeinfo.core.business.data.service.HeMonitorDataService;
import com.kekeinfo.core.business.data.service.HiWellDataService;
import com.kekeinfo.core.business.data.service.HoWellDataService;
import com.kekeinfo.core.business.data.service.HpWellDataService;
import com.kekeinfo.core.business.data.service.IWellDataService;
import com.kekeinfo.core.business.data.service.OWellDataService;
import com.kekeinfo.core.business.data.service.PWellDataService;
import com.kekeinfo.core.business.deformmonitor.model.Deformmonitor;
import com.kekeinfo.core.business.deformmonitor.service.DeformmonitorService;
import com.kekeinfo.core.business.dewatering.model.Dewatering;
import com.kekeinfo.core.business.dewatering.service.DewateringService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.model.PointEnumType;
import com.kekeinfo.core.business.invertedwell.model.Invertedwell;
import com.kekeinfo.core.business.invertedwell.service.InvertedwellService;
import com.kekeinfo.core.business.monitordata.model.DewellData;
import com.kekeinfo.core.business.monitordata.model.EmonitorData;
import com.kekeinfo.core.business.monitordata.model.HdewellData;
import com.kekeinfo.core.business.monitordata.model.HemonitorData;
import com.kekeinfo.core.business.monitordata.model.HiwellData;
import com.kekeinfo.core.business.monitordata.model.HowellData;
import com.kekeinfo.core.business.monitordata.model.HpwellData;
import com.kekeinfo.core.business.monitordata.model.IwellData;
import com.kekeinfo.core.business.monitordata.model.OwellData;
import com.kekeinfo.core.business.monitordata.model.PwellData;
import com.kekeinfo.core.business.observewell.model.Observewell;
import com.kekeinfo.core.business.observewell.service.ObservewellService;
import com.kekeinfo.core.business.point.model.Basepoint;
import com.kekeinfo.core.business.point.service.PointService;
import com.kekeinfo.core.business.pumpwell.model.Pumpwell;
import com.kekeinfo.core.business.pumpwell.service.PumpwellService;
import com.kekeinfo.web.entity.UnderWater;

@Component
public class DataUtils {
	
	@Autowired private PointService pointService;
	@Autowired PumpwellService pumpwellService;
	@Autowired InvertedwellService invertedwellService;
	@Autowired ObservewellService observewellService;
	@Autowired DeformmonitorService deformmonitorService;
	@Autowired DewateringService dewateringService;
	
	@Autowired PWellDataService pWellDataService;
	@Autowired IWellDataService iWellDataService;
	@Autowired OWellDataService oWellDataService;
	@Autowired EMonitorDataService eMonitorDataService;
	@Autowired DeWellDataService deWellDataService;
	
	@Autowired HpWellDataService hpWellDataService;
	@Autowired HiWellDataService hiWellDataService;
	@Autowired HoWellDataService hoWellDataService;
	@Autowired HeMonitorDataService heMonitorDataService;
	@Autowired HdeWellDataService hdeWellDataService;
	 
	@Autowired CSiteService cSiteService;
	private static Integer mod = 300;
	
	public Map <String, Object> getWarningData(Map<String, Object> dataMap,List<Long> ids,List<UnderWater> cs) throws ServiceException{
		if(dataMap ==null) {
			dataMap = new HashMap<String, Object>();
		}
		//降水井
		List<Pumpwell> pwellList = pumpwellService.getWarningData(ids);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		UnderWater csite =null;
		for(Pumpwell p:pwellList){
			Map<String, Object> entry_camera = new HashMap<String, Object>();
			if(csite==null || !csite.getId().equals(p.getcSite().getId())){
				for(UnderWater c:cs){
					if(c.getId().equals(p.getcSite().getId())){
						csite=c;
						break;
					}
				}
			}
			entry_camera.put("csite", csite.getName());
			entry_camera.put("cid", p.getcSite().getId());
			entry_camera.put("name", p.getName());
//			entry_camera.put("status", p.getStatus());
			entry_camera.put("dataStatus", p.getDataStatus());
			entry_camera.put("powerStatus", p.getPowerStatus());
			resultList.add(entry_camera);
			
		}
		dataMap.put("wpwell", resultList);
		
		//降水井
		List<Dewatering> dewellList = dewateringService.getWarningData(ids);
		resultList = new ArrayList<Map<String, Object>>();
		for(Dewatering p:dewellList){
			Map<String, Object> entry_camera = new HashMap<String, Object>();
			if(csite==null || !csite.getId().equals(p.getcSite().getId())){
				for(UnderWater c:cs){
					if(c.getId().equals(p.getcSite().getId())){
						csite=c;
						break;
					}
				}
			}
			entry_camera.put("csite", csite.getName());
			entry_camera.put("cid", p.getcSite().getId());
			entry_camera.put("name", p.getName());
			entry_camera.put("dataStatus", p.getDataStatus());
			entry_camera.put("powerStatus", p.getPowerStatus());
			resultList.add(entry_camera);
		}
		dataMap.put("wdewell", resultList);
		
		List<Observewell> owellList =observewellService.getWarningData(ids);
		resultList = new ArrayList<Map<String, Object>>();
		for(Observewell o:owellList){
			Map<String, Object> entry_camera = new HashMap<String, Object>();
			if(csite==null || !csite.getId().equals(o.getcSite().getId())){
				for(UnderWater c:cs){
					if(c.getId().equals(o.getcSite().getId())){
						csite=c;
						break;
					}
				}
			}
			entry_camera.put("csite", csite.getName());
			entry_camera.put("cid", o.getcSite().getId());
			entry_camera.put("name", o.getName());
			entry_camera.put("dataStatus", o.getDataStatus());
			entry_camera.put("powerStatus", o.getPowerStatus());
			resultList.add(entry_camera);
			
		}
		dataMap.put("wowell", resultList);
		
		List<Invertedwell> iwellList =invertedwellService.getWarningData(ids);
		resultList = new ArrayList<Map<String, Object>>();
		for(Invertedwell i:iwellList){
			Map<String, Object> entry_camera = new HashMap<String, Object>();
			if(csite==null || !csite.getId().equals(i.getcSite().getId())){
				for(UnderWater c:cs){
					if(c.getId().equals(i.getcSite().getId())){
						csite=c;
						break;
					}
				}
			}
			entry_camera.put("csite", csite.getName());
			entry_camera.put("cid", i.getcSite().getId());
			entry_camera.put("name", i.getName());
			entry_camera.put("dataStatus", i.getDataStatus());
			entry_camera.put("powerStatus", i.getPowerStatus());
			resultList.add(entry_camera);
			
		}
		dataMap.put("wiwell", resultList);
		
		List<Deformmonitor> ewellList = deformmonitorService.getWarningData(ids);
		resultList = new ArrayList<Map<String, Object>>();
		for(Deformmonitor d:ewellList){
			Map<String, Object> entry_camera = new HashMap<String, Object>();
			if(csite==null || !csite.getId().equals(d.getcSite().getId())){
				for(UnderWater c:cs){
					if(c.getId().equals(d.getcSite().getId())){
						csite=c;
						break;
					}
				}
			}
			entry_camera.put("csite", csite.getName());
			entry_camera.put("cid", d.getcSite().getId());
			entry_camera.put("name", d.getName());
			entry_camera.put("dataStatus", d.getDataStatus());
			entry_camera.put("powerStatus", d.getPowerStatus());
			resultList.add(entry_camera);
			
		}
		dataMap.put("wewell", resultList);
		
		return dataMap;
	}
	
	public Map <String, Object> getStardingInfo(Map<String, Object> dataMap,List<Long> ids) throws ServiceException{
		List<ConstructionSite> cs = cSiteService.getByIds(ids);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for(ConstructionSite csite:cs){
			Map<String, Object> entry_camera = new HashMap<String, Object>();
			entry_camera.put("id",csite.getId());
			
			//if(csite.getSowell()!=null && csite.getSowell().getrWater()!=null){
				//entry_camera.put("rwater",csite.getSowell().getrWater());
			//}else{
				entry_camera.put("rwater","没有采集到");
		//	}
			
			resultList.add(entry_camera);
		}
		dataMap.put("rowell", resultList);
		return dataMap;
	}
	
	/**
	 * 获取对应项目下面的测点，指定是否要获取可见的测点，还是所有的测点
	 * @param cid
	 * @param visible 是否获取地图可见的测点，如果为null，则都获取
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, Object> getWellDataByCid(long cid, Boolean visible) throws ServiceException{
		Map<String, Object> dataMap = new HashMap<String, Object>();
		//降水井
		PointEnumType pointType = PointEnumType.getType(Integer.valueOf(1));
		List<Basepoint> plist = pointService.getListByCid(cid, pointType, visible);
//		Entitites<Pumpwell> pwellList = pumpwellService.getListByAttributes(new String[]{"cSite.id"}, new Object[]{cid}, null);
		dataMap.put("pwell", plist);
		//疏干井
		PointEnumType pointType2 = PointEnumType.getType(Integer.valueOf(2));
		List<Basepoint> plist2 = pointService.getListByCid(cid, pointType2, visible);
//		Entitites<Dewatering> dewellList = dewateringService.getListByAttributes(new String[]{"cSite.id"}, new Object[]{cid}, null);
		dataMap.put("dewell", plist2);
		//观测井
		PointEnumType pointType4 = PointEnumType.getType(Integer.valueOf(4));
		List<Basepoint> plist4 = pointService.getListByCid(cid, pointType4, visible);
//		Entitites<Observewell> owellList = observewellService.getListByAttributes(new String[]{"cSite.id"}, new Object[]{cid}, null);
		dataMap.put("owell", plist4);
		//回灌井
		PointEnumType pointType3 = PointEnumType.getType(Integer.valueOf(3));
		List<Basepoint> plist3 = pointService.getListByCid(cid, pointType3, visible);
//		Entitites<Invertedwell> iwellList = invertedwellService.getListByAttributes(new String[]{"cSite.id"}, new Object[]{cid}, null);
		dataMap.put("iwell", plist3);
		//环境监控
		PointEnumType pointType5 = PointEnumType.getType(Integer.valueOf(5));
		List<Basepoint> plist5 = pointService.getListByCid(cid, pointType5, visible);
//		Entitites<Deformmonitor> dwellList = deformmonitorService.getListByAttributes(new String[]{"cSite.id"}, new Object[]{cid}, null);
		dataMap.put("ewell", plist5);
		
		return dataMap;
	}
	
	/**
	 * 获取测点的经纬度信息
	 * @param cid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getWellSitesByCid(long cid){
		Map<String, Object> dataMap = new HashMap<String, Object>();
		//降水井
		Entitites<Pumpwell> pwellList = pumpwellService.getListByAttributes(new String[]{"cSite.id"}, new Object[]{cid}, null);
		JSONArray array = new JSONArray();
		for (Pumpwell well : pwellList.getEntites()) {
			JSONObject o = new JSONObject();
			o.put("name",well.getName());
			o.put("longitude", well.getLongitude());
			o.put("latitude", well.getLatitude());
			array.add(o);
		}
		dataMap.put("pwell", array);
		//降水井
		Entitites<Dewatering> dewellList = dewateringService.getListByAttributes(new String[]{"cSite.id"}, new Object[]{cid}, null);
		JSONArray array1 = new JSONArray();
		for (Dewatering well : dewellList.getEntites()) {
			JSONObject o = new JSONObject();
			o.put("name",well.getName());
			o.put("longitude", well.getLongitude());
			o.put("latitude", well.getLatitude());
			array1.add(o);
		}
		dataMap.put("dewell", array1);
		//观测井
		Entitites<Observewell> owellList = observewellService.getListByAttributes(new String[]{"cSite.id"}, new Object[]{cid}, null);
		JSONArray array2 = new JSONArray();
		for (Observewell well : owellList.getEntites()) {
			JSONObject o = new JSONObject();
			o.put("name",well.getName());
			o.put("longitude", well.getLongitude());
			o.put("latitude", well.getLatitude());
			array2.add(o);
		}
		dataMap.put("owell", array2);
		//回灌井
		Entitites<Invertedwell> iwellList = invertedwellService.getListByAttributes(new String[]{"cSite.id"}, new Object[]{cid}, null);
		JSONArray array3 = new JSONArray();
		for (Invertedwell well : iwellList.getEntites()) {
			JSONObject o = new JSONObject();
			o.put("name",well.getName());
			o.put("longitude", well.getLongitude());
			o.put("latitude", well.getLatitude());
			array3.add(o);
		}
		dataMap.put("iwell", array3);
		//环境监控
		Entitites<Deformmonitor> dwellList = deformmonitorService.getListByAttributes(new String[]{"cSite.id"}, new Object[]{cid}, null);
		JSONArray array4 = new JSONArray();
		for (Deformmonitor well : dwellList.getEntites()) {
			JSONObject o = new JSONObject();
			o.put("name",well.getName());
			o.put("longitude", well.getLongitude());
			o.put("latitude", well.getLatitude());
			array4.add(o);
		}
		dataMap.put("ewell", array4);
		
		return dataMap;
	}
	
	public List<Map<String, String>> getDataByCid(long cid){
		List<Map<String, String>> dataResult = new ArrayList<Map<String, String>>();
		//获取降水井的最新值
		Entitites<Pumpwell> pwellList = pumpwellService.getListByAttributes(new String[]{"cSite.id"}, new Object[]{cid}, null);
		Map<String, String> orderby = new HashMap<String, String>();
		orderby.put("auditSection.dateCreated", "desc");
		for (Pumpwell pwell : pwellList.getEntites()) {
			Entitites<PwellData> pwellDataList = pWellDataService.getPageListByAttributes(Arrays.asList("pWell.id"), Arrays.asList(pwell.getId()), 1, 0,  orderby);
			Map<String, String> tmp = new HashMap<String, String>();
			tmp.put("id", pwell.getId().toString());
			tmp.put("name", pwell.getName());
			tmp.put("lng", pwell.getLongitude().toString());
			tmp.put("lat", pwell.getLatitude().toString());
			tmp.put("type", "pwell");
			if (pwellDataList.getTotalCount()>0) {
				PwellData pwellData = pwellDataList.getEntites().get(0);
				
				tmp.put("stauts", String.valueOf(pwellData.getStatus()));
				tmp.put("data1",pwellData.getFlow().toString());
				tmp.put("data2", pwellData.getWater().toString());
			} 
			dataResult.add(tmp);
		}
		//获取观测井
		Entitites<Observewell> owellList = observewellService.getListByAttributes(new String[]{"cSite.id"}, new Object[]{cid}, null);
		for (Observewell owell : owellList.getEntites()) {
			Entitites<OwellData> owellDataList = oWellDataService.getPageListByAttributes(Arrays.asList("oWell.id"), Arrays.asList(owell.getId()), 1, 0,  orderby);
			Map<String, String> tmp = new HashMap<String, String>();
			tmp.put("id", owell.getId().toString());
			tmp.put("name", owell.getName());
			tmp.put("lng", owell.getLongitude().toString());
			tmp.put("lat", owell.getLatitude().toString());
			tmp.put("type", "owell");
			if (owellDataList.getTotalCount()>0) {
				OwellData owellData = owellDataList.getEntites().get(0);
				
				tmp.put("stauts", String.valueOf(owellData.getStatus()));
				tmp.put("data1",owellData.getWater().toString());
				tmp.put("data2", owellData.getTemperature().toString());
			} 
			dataResult.add(tmp);
		}
		//获取回灌井
		Entitites<Invertedwell> iwellList = invertedwellService.getListByAttributes(new String[]{"cSite.id"}, new Object[]{cid}, null);
		for (Invertedwell iwell : iwellList.getEntites()) {
			Entitites<IwellData> iwellDataList = iWellDataService.getPageListByAttributes(Arrays.asList("iWell.id"), Arrays.asList(iwell.getId()), 1, 0,  orderby);
			Map<String, String> tmp = new HashMap<String, String>();
			tmp.put("id", iwell.getId().toString());
			tmp.put("name", iwell.getName());
			tmp.put("lng", iwell.getLongitude().toString());
			tmp.put("lat", iwell.getLatitude().toString());
			tmp.put("type", "iwell");
			if (iwellDataList.getTotalCount()>0) {
				IwellData iwellData = iwellDataList.getEntites().get(0);
				
				tmp.put("stauts", String.valueOf(iwellData.getStatus()));
				tmp.put("data1",iwellData.getFlow().toString());
				tmp.put("data2", iwellData.getPressure().toString());
			}
			dataResult.add(tmp);
		}
		//获取环境监控
		Entitites<Deformmonitor> dwellList = deformmonitorService.getListByAttributes(new String[]{"cSite.id"}, new Object[]{cid}, null);
		for (Deformmonitor dwell : dwellList.getEntites()) {
			Entitites<EmonitorData> dwellDataList = eMonitorDataService.getPageListByAttributes(Arrays.asList("emonitor.id"), Arrays.asList(dwell.getId()), 1, 0,  orderby);
			Map<String, String> tmp = new HashMap<String, String>();
			tmp.put("id", dwell.getId().toString());
			tmp.put("name", dwell.getName());
			tmp.put("lng", dwell.getLongitude().toString());
			tmp.put("lat", dwell.getLatitude().toString());
			tmp.put("type", "emonitor");
			if (dwellDataList.getTotalCount()>0) {
				EmonitorData dwellData = dwellDataList.getEntites().get(0);
				
				tmp.put("stauts", String.valueOf(dwellData.getStatus()));
				tmp.put("data",dwellData.getData().toString());
			}
			dataResult.add(tmp);
		}
		
		return dataResult;
	}

	/**
	 * 获取降水井、疏干井和回灌井的数据统计信息，显示当天的柱状图和抽灌比的饼图
	 * 显示观测井的水位情况（包括水位上限和水位下限）
	 * 柱状图只显示正常和告警状态的测点，故障和关闭测点不显示
	 * @param cid
	 * @return
	 */
	public Map<String, Object> getCheckpoints(long cid){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, String>> pwellDataResult = new ArrayList<Map<String, String>>(); //降水井柱状结果数据
		List<Map<String, String>> iwellDataResult = new ArrayList<Map<String, String>>(); //回灌井柱状结果数据
		List<Map<String, String>> dewellDataResult = new ArrayList<Map<String, String>>(); //疏干井柱状结果数据
		List<Map<String, String>> owellDataResult = new ArrayList<Map<String, String>>(); //观测井曲线结果数据
		try {
			
			BigDecimal pwellListFlows = new BigDecimal(0); //所有降水井当日总流量
			BigDecimal iwellListFlows = new BigDecimal(0); //所有回灌井当日总流量
//			BigDecimal dewellListFlows = new BigDecimal(0); //所有梳干井当日总流量
			
			//获取降水井的最新值
			//Entitites<Pumpwell> pwellList = pumpwellService.getListByAttributes(new String[]{"cSite.id"}, new Object[]{cid}, null);
			List<Pumpwell> pwellList = pumpwellService.getListByPidAndPowerStatus(cid, new Integer[]{0});
			
			Map<String, String> orderby = new HashMap<String, String>();
			orderby.put("auditSection.dateCreated", "desc");
			for (Pumpwell pwell : pwellList) { 
				Map<String, String> tmp = new HashMap<String, String>(); //降水井当前观测值
				tmp.put("id", pwell.getId().toString());
				tmp.put("name", pwell.getName());
				tmp.put("data1", pwell.getrFlow()==null?"0":pwell.getrFlow().toString());
				tmp.put("data2", pwell.getrWater()==null?"0":pwell.getrWater().toString());
				pwellDataResult.add(tmp);
				//计算所有降水井最新流量数据之和
				if (pwell.getrFlow()!=null) {
					pwellListFlows = pwellListFlows.add(pwell.getrFlow());
				}
				//处理降水井当天流量总和，旧处理方法
				/*BigDecimal pwellFlows = new BigDecimal(0); //单个降水井当日总流量
				List<Object[]> whereList = new ArrayList<Object[]>();
				whereList.add(new String[]{"pWell.id", pwell.getId().toString(), "0"});
				whereList.add(new Object[]{"auditSection.dateCreated", calendar.getTime(), "2"});
				Entitites<PwellData> pwellDataList = pWellDataService.getPageListByAttributesLike(null, null, null, null, orderby, whereList, null, false);
				if (pwellDataList.getTotalCount()>0) {
					for (PwellData pwellDate : pwellDataList.getEntites()) {
						BigDecimal flow = pwellDate.getFlow();
						if (flow!=null) {
							pwellFlows = pwellFlows.add(flow);
						}
					}
					//求出单井当天的流量平均值，并统计到所有井当日总流量中
					pwellListFlows = pwellListFlows.add(pwellFlows.divide(new BigDecimal(pwellDataList.getTotalCount()), 2, BigDecimal.ROUND_HALF_UP)); 
				}*/ 
			}
			//疏干井
			List<Dewatering> dewellList = dewateringService.getListByPidAndPowerStatus(cid, new Integer[]{0});
			for (Dewatering dewell : dewellList) {
				Map<String, String> tmp = new HashMap<String, String>();
				tmp.put("id", dewell.getId().toString());
				tmp.put("name", dewell.getName());
				tmp.put("data1", dewell.getrFlow()==null?"0":dewell.getrFlow().toString());
				tmp.put("data2", dewell.getrWater()==null?"0":dewell.getrWater().toString());
				dewellDataResult.add(tmp);
				
			}
			//获取回灌井的最新值
			//Entitites<Invertedwell> iwellList = invertedwellService.getListByAttributes(new String[]{"cSite.id"}, new Object[]{cid}, null);
			List<Invertedwell> iwellList = invertedwellService.getListByPidAndPowerStatus(cid, new Integer[]{0});
			for (Invertedwell iwell : iwellList) {
				Map<String, String> tmp = new HashMap<String, String>();
				tmp.put("id", iwell.getId().toString());
				tmp.put("name", iwell.getName());
				tmp.put("data1", iwell.getrFlow()==null?"0":iwell.getrFlow().toString());
				tmp.put("data2", iwell.getrPressure()==null?"0":iwell.getrPressure().toString());
				iwellDataResult.add(tmp);
				
				//计算所有回灌井最新流量数据之和
				if (iwell.getrFlow()!=null) {
					iwellListFlows = iwellListFlows.add(iwell.getrFlow());
				}
				
				//处理回灌井当天流量总和，旧处理方法
				/*BigDecimal iwellFlows = new BigDecimal(0); //单个回灌井当日总流量
				List<Object[]> whereList = new ArrayList<Object[]>();
				whereList.add(new String[]{"iWell.id", iwell.getId().toString(), "0"});
				whereList.add(new Object[]{"auditSection.dateCreated", calendar.getTime(), "2"});
				Entitites<IwellData> iwellDataList = iWellDataService.getPageListByAttributesLike(null, null, null, null, orderby, whereList, null, false);
				if (iwellDataList.getTotalCount()>0) {
					for (IwellData iwellDate : iwellDataList.getEntites()) {
						BigDecimal flow = iwellDate.getFlow();
						if (flow!=null) {
							iwellFlows = iwellFlows.add(flow);
						}
					}
					//求出单井当天的流量平均值，并统计到所有井当日总流量中
					iwellListFlows = iwellListFlows.add(iwellFlows.divide(new BigDecimal(iwellDataList.getTotalCount()), 2, BigDecimal.ROUND_HALF_UP)); 
				} */
			}
			
			List<Object> checkpoints = new ArrayList<Object>();
			Map<String, Object> pcheckpoint = new HashMap<String, Object>();
			pcheckpoint.put("chkTitle", "降水井实时监控");
			pcheckpoint.put("dataName1", "流量(m³/h)");
			pcheckpoint.put("dataName2", "水位(m)");
			pcheckpoint.put("dataset", pwellDataResult);
			
			Map<String, Object> decheckpoint = new HashMap<String, Object>();
			decheckpoint.put("chkTitle", "疏干井实时监控");
			decheckpoint.put("dataName1", "流量(m³/h)");
			decheckpoint.put("dataName2", "水位(m)");
			decheckpoint.put("dataset", dewellDataResult);
			
			Map<String, Object> icheckpoint = new HashMap<String, Object>();
			icheckpoint.put("chkTitle", "回灌井实时监控");
			icheckpoint.put("dataName1", "流量(m³/h)");
			icheckpoint.put("dataName2", "井内水位(MPa)");
			icheckpoint.put("dataset", iwellDataResult);
			
			checkpoints.add(pcheckpoint);
			checkpoints.add(decheckpoint);
			checkpoints.add(icheckpoint);
			
			//处理观测井曲线图
			List<Observewell> owellList = observewellService.getListByPidAndPowerStatus(cid, new Integer[]{0});
			for (Observewell owell : owellList) {
				Map<String, String> tmp = new HashMap<String, String>();
				tmp.put("id", owell.getId().toString());
				tmp.put("name", owell.getName());
				tmp.put("data1", owell.getrWater()==null?"0":owell.getrWater().toString());
				tmp.put("data2", owell.getWaterMeasurement()==null?"0":owell.getWaterMeasurement().toString());
				tmp.put("data3", owell.getWaterDwon()==null?"0":owell.getWaterDwon().toString());
				owellDataResult.add(tmp);
			}
			Map<String, Object> owelllines = new HashMap<String, Object>();
			owelllines.put("chkTitle", "观测井水位监控");
			owelllines.put("dataName1", "当前水位(m)");
			owelllines.put("dataName2", "水位上限(m)");
			owelllines.put("dataName3", "水位下限(m)");
			owelllines.put("dataset", owellDataResult);
			
			result.put("status", 0);
			result.put("checkpoints", checkpoints);
			result.put("pwellListFlows", pwellListFlows);
//			result.put("dewellListFlows", dewellListFlows);
			result.put("iwellListFlows", iwellListFlows);
			result.put("owelllines", owelllines);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	/**
	 * 读取当前表，获取时间范围内测点的数据记录，提供前台页面展示曲线图
	 * @param cid 测点id
	 * @param type 测点类型，0降水井，1观测井，2回灌井，3环境监测
	 * @param begintm 开始时间
	 * @param endtm 截止时间
	 * @return
	 * 最近60天的
	 */
	public Map<String, Object> getLines60(long cid, String type){
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> orderby = new HashMap<String, String>();
		orderby.put("auditSection.dateCreated", "desc");
		
		List<Object[]> whereList = new ArrayList<Object[]>();
		
		
		Map<String, Object> linesMap = new HashMap<String, Object>();
		List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
		switch (Integer.valueOf(type)) {
		case 0:
			Pumpwell well = pumpwellService.getById(cid);
			if (well==null) {
				result.put("status", -1);
				result.put("message", "测点不存在，cid="+cid);
				return result;
			}
			whereList.add(new String[]{"pWell.id", String.valueOf(cid), "0"});
			Entitites<PwellData> dataList = pWellDataService.getPageListByAttributesLike(null, null, 60, null, orderby, whereList, null, false);
			if (dataList.getTotalCount()>0) {
				for (PwellData data : dataList.getEntites()) {
					Map<String, Object> datas = new HashMap<String, Object>();
					datas.put("xTime", DateFormatUtils.format(data.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
					datas.put("data1", data.getFlow());
					datas.put("data2", data.getWater());
					dataset.add(datas);
				}
			}
			linesMap.put("dataName1", "流量(m³/h)");
			linesMap.put("dataName2", "水位(m)");
			linesMap.put("dataset", dataset);
			
			result.put("chkTitle", "降水井曲线图-"+well.getName());
			break;
		case 1:
			Observewell well1 = observewellService.getById(cid);
			if (well1==null) {
				result.put("status", -1);
				result.put("message", "测点不存在，cid="+cid);
				return result;
			}
			whereList.add(new String[]{"oWell.id", String.valueOf(cid), "0"});
			Entitites<OwellData> dataList1 = oWellDataService.getPageListByAttributesLike(null, null, 60, null, orderby, whereList, null, false);
			if (dataList1.getTotalCount()>0) {
				for (OwellData data : dataList1.getEntites()) {
					Map<String, Object> datas = new HashMap<String, Object>();
					datas.put("xTime", DateFormatUtils.format(data.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
					datas.put("data1", data.getWater());
					datas.put("data2", data.getTemperature());
					dataset.add(datas);
				}
			}
			linesMap.put("dataName1", "水位(m)");
			linesMap.put("dataName2", "温度(°C)");
			linesMap.put("dataset", dataset);
			
			result.put("chkTitle", "观测井曲线图-"+well1.getName());
			break;
		case 2:
			Invertedwell well2 = invertedwellService.getById(cid);
			if (well2==null) {
				result.put("status", -1);
				result.put("message", "测点不存在，cid="+cid);
				return result;
			}
			whereList.add(new String[]{"iWell.id", String.valueOf(cid), "0"});
			Entitites<IwellData> dataList2 = iWellDataService.getPageListByAttributesLike(null, null, 60, null, orderby, whereList, null, false);
			if (dataList2.getTotalCount()>0) {
				for (IwellData data : dataList2.getEntites()) {
					Map<String, Object> datas = new HashMap<String, Object>();
					datas.put("xTime", DateFormatUtils.format(data.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
					datas.put("data1", data.getFlow());
					datas.put("data2", data.getPressure());
					dataset.add(datas);
				}
			}
			linesMap.put("dataName1", "流量(m³/h)");
			linesMap.put("dataName2", "井内水位(MPa)");
			linesMap.put("dataset", dataset);
			
			result.put("chkTitle", "回灌井曲线图-"+well2.getName());
			break;
		case 3:
			Deformmonitor well3 = deformmonitorService.getById(cid);
			if (well3==null) {
				result.put("status", -1);
				result.put("message", "测点不存在，cid="+cid);
				return result;
			}
			whereList.add(new String[]{"emonitor.id", String.valueOf(cid), "0"});
			Entitites<EmonitorData> dataList3 = eMonitorDataService.getPageListByAttributesLike(null, null, 60, null, orderby, whereList, null, false);
			if (dataList3.getTotalCount()>0) {
				for (EmonitorData data : dataList3.getEntites()) {
					Map<String, Object> datas = new HashMap<String, Object>();
					datas.put("xTime", DateFormatUtils.format(data.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
					datas.put("data1", data.getData());
					dataset.add(datas);
				}
			}
			linesMap.put("dataName1", "数据");
			linesMap.put("dataset", dataset);
			
			result.put("chkTitle", "环境监测曲线图-"+well3.getName());
			break;
		case 4:
			Dewatering well4 = dewateringService.getById(cid);
			if (well4==null) {
				result.put("status", -1);
				result.put("message", "测点不存在，cid="+cid);
				return result;
			}
			whereList.add(new String[]{"deWell.id", String.valueOf(cid), "0"});
			Entitites<DewellData> dataList4 = deWellDataService.getPageListByAttributesLike(null, null, 60, null, orderby, whereList, null, false);
			if (dataList4.getTotalCount()>0) {
				for (DewellData data : dataList4.getEntites()) {
					Map<String, Object> datas = new HashMap<String, Object>();
					datas.put("xTime", DateFormatUtils.format(data.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
					datas.put("data1", data.getFlow());
					datas.put("data2", data.getWater());
					dataset.add(datas);
				}
			}
			linesMap.put("dataName1", "流量(m³/h)");
			linesMap.put("dataName2", "水位(m)");
			linesMap.put("dataset", dataset);
			
			result.put("chkTitle", "疏干井曲线图-"+well4.getName());
			break;
		}
		
		result.put("status", 0);
		result.put("linesMap", linesMap);
		
		return result;
	}
	/**
	 * 读取当前表，获取时间范围内测点的数据记录，提供前台页面展示曲线图
	 * @param cid 测点id
	 * @param type 测点类型，0降水井，1观测井，2回灌井，3环境监测
	 * @param begintm 开始时间
	 * @param endtm 截止时间
	 * @return
	 */
	public Map<String, Object> getLines(long cid, String type, Date begintm, Date endtm){
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> orderby = new HashMap<String, String>();
		orderby.put("auditSection.dateCreated", "asc");
		
		List<Object[]> whereList = new ArrayList<Object[]>();
		if (begintm!=null) {
			whereList.add(new Object[]{"auditSection.dateCreated", begintm, "2"});
		}
		if (endtm!=null) {
			whereList.add(new Object[]{"auditSection.dateCreated", endtm, "4"});
		}
		
		Map<String, Object> linesMap = new HashMap<String, Object>();
		List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
		switch (Integer.valueOf(type)) {
		case 0:
			Pumpwell well = pumpwellService.getById(cid);
			if (well==null) {
				result.put("status", -1);
				result.put("message", "测点不存在，cid="+cid);
				return result;
			}
			whereList.add(new String[]{"pWell.id", String.valueOf(cid), "0"});
			Entitites<PwellData> dataList = pWellDataService.getPageListByAttributesLike(null, null, null, null, orderby, whereList, null, false);
			if (dataList.getTotalCount()>0) {
				for (PwellData data : dataList.getEntites()) {
					Map<String, Object> datas = new HashMap<String, Object>();
					datas.put("xTime", DateFormatUtils.format(data.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
					datas.put("data1", data.getFlow());
					datas.put("data2", data.getWater());
					dataset.add(datas);
				}
			}
			linesMap.put("dataName1", "流量(m³/h)");
			linesMap.put("dataName2", "水位(m)");
			linesMap.put("dataset", dataset);
			
			result.put("chkTitle", "降水井曲线图-"+well.getName());
			break;
		case 1:
			Observewell well1 = observewellService.getById(cid);
			if (well1==null) {
				result.put("status", -1);
				result.put("message", "测点不存在，cid="+cid);
				return result;
			}
			whereList.add(new String[]{"oWell.id", String.valueOf(cid), "0"});
			Entitites<OwellData> dataList1 = oWellDataService.getPageListByAttributesLike(null, null, null, null, orderby, whereList, null, false);
			if (dataList1.getTotalCount()>0) {
				for (OwellData data : dataList1.getEntites()) {
					Map<String, Object> datas = new HashMap<String, Object>();
					datas.put("xTime", DateFormatUtils.format(data.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
					datas.put("data1", data.getWater());
					datas.put("data2", data.getTemperature());
					dataset.add(datas);
				}
			}
			linesMap.put("dataName1", "水位(m)");
			linesMap.put("dataName2", "温度(°C)");
			linesMap.put("dataset", dataset);
			
			result.put("chkTitle", "观测井曲线图-"+well1.getName());
			break;
		case 2:
			Invertedwell well2 = invertedwellService.getById(cid);
			if (well2==null) {
				result.put("status", -1);
				result.put("message", "测点不存在，cid="+cid);
				return result;
			}
			whereList.add(new String[]{"iWell.id", String.valueOf(cid), "0"});
			Entitites<IwellData> dataList2 = iWellDataService.getPageListByAttributesLike(null, null, null, null, orderby, whereList, null, false);
			if (dataList2.getTotalCount()>0) {
				for (IwellData data : dataList2.getEntites()) {
					Map<String, Object> datas = new HashMap<String, Object>();
					datas.put("xTime", DateFormatUtils.format(data.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
					datas.put("data1", data.getFlow());
					datas.put("data2", data.getPressure());
					dataset.add(datas);
				}
			}
			linesMap.put("dataName1", "流量(m³/h)");
			linesMap.put("dataName2", "井内水位(MPa)");
			linesMap.put("dataset", dataset);
			
			result.put("chkTitle", "回灌井曲线图-"+well2.getName());
			break;
		case 3:
			Deformmonitor well3 = deformmonitorService.getById(cid);
			if (well3==null) {
				result.put("status", -1);
				result.put("message", "测点不存在，cid="+cid);
				return result;
			}
			whereList.add(new String[]{"emonitor.id", String.valueOf(cid), "0"});
			Entitites<EmonitorData> dataList3 = eMonitorDataService.getPageListByAttributesLike(null, null, null, null, orderby, whereList, null, false);
			if (dataList3.getTotalCount()>0) {
				for (EmonitorData data : dataList3.getEntites()) {
					Map<String, Object> datas = new HashMap<String, Object>();
					datas.put("xTime", DateFormatUtils.format(data.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
					datas.put("data1", data.getData());
					dataset.add(datas);
				}
			}
			linesMap.put("dataName1", "数据");
			linesMap.put("dataset", dataset);
			
			result.put("chkTitle", "环境监测曲线图-"+well3.getName());
			break;
		case 4:
			Dewatering well4 = dewateringService.getById(cid);
			if (well4==null) {
				result.put("status", -1);
				result.put("message", "测点不存在，cid="+cid);
				return result;
			}
			whereList.add(new String[]{"deWell.id", String.valueOf(cid), "0"});
			Entitites<DewellData> dataList4 = deWellDataService.getPageListByAttributesLike(null, null, null, null, orderby, whereList, null, false);
			if (dataList4.getTotalCount()>0) {
				for (DewellData data : dataList4.getEntites()) {
					Map<String, Object> datas = new HashMap<String, Object>();
					datas.put("xTime", DateFormatUtils.format(data.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
					datas.put("data1", data.getFlow());
					datas.put("data2", data.getWater());
					dataset.add(datas);
				}
			}
			linesMap.put("dataName1", "流量(m³/h)");
			linesMap.put("dataName2", "水位(m)");
			linesMap.put("dataset", dataset);
			
			result.put("chkTitle", "疏干井曲线图-"+well4.getName());
			break;
		}
		
		result.put("status", 0);
		result.put("linesMap", linesMap);
		
		return result;
	}
	
	/**
	 * 读取历史表，获取时间范围内测点的数据记录，提供前台页面展示曲线图
	 * @param cid 测点id
	 * @param type 测点类型，0降水井，1观测井，2回灌井，3环境监测,4疏干井
	 * @param begintm 开始时间
	 * @param endtm 截止时间
	 * @return
	 */
	public Map<String, Object> getHistoryLines(long cid, String type, Date begintm, Date endtm){
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> orderby = new HashMap<String, String>();
		orderby.put("auditSection.dateCreated", "asc");
		
		List<Object[]> whereList = new ArrayList<Object[]>();
		if (begintm!=null) {
			whereList.add(new Object[]{"auditSection.dateCreated", begintm, "2"});
		}
		if (endtm!=null) {
			whereList.add(new Object[]{"auditSection.dateCreated", endtm, "4"});
		}
		
		Map<String, Object> linesMap = new HashMap<String, Object>();
		List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
		switch (Integer.valueOf(type)) {
		case 0:
			Pumpwell well = pumpwellService.getById(cid);
			if (well==null) {
				result.put("status", -1);
				result.put("message", "测点不存在，cid="+cid);
				return result;
			}
			whereList.add(new String[]{"pWell.id", String.valueOf(cid), "0"});
			Entitites<HpwellData> dataList = hpWellDataService.getPageListByAttributesLike(null, null, null, null, orderby, whereList, null, false);
			if (dataList.getTotalCount()>0) {
				for (HpwellData data : dataList.getEntites()) {
					Map<String, Object> datas = new HashMap<String, Object>();
					datas.put("xTime", DateFormatUtils.format(data.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
					datas.put("data1", data.getFlow());
					datas.put("data2", data.getWater());
					dataset.add(datas);
				}
			}
			linesMap.put("dataName1", "流量(m³/h)");
			linesMap.put("dataName2", "水位(m)");
			linesMap.put("dataset", dataset);
			
			result.put("chkTitle", "降水井曲线图-"+well.getName());
			break;
		case 1:
			Observewell well1 = observewellService.getById(cid);
			if (well1==null) {
				result.put("status", -1);
				result.put("message", "测点不存在，cid="+cid);
				return result;
			}
			whereList.add(new String[]{"oWell.id", String.valueOf(cid), "0"});
			Entitites<HowellData> dataList1 = hoWellDataService.getPageListByAttributesLike(null, null, null, null, orderby, whereList, null, false);
			if (dataList1.getTotalCount()>0) {
				for (HowellData data : dataList1.getEntites()) {
					Map<String, Object> datas = new HashMap<String, Object>();
					datas.put("xTime", DateFormatUtils.format(data.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
					datas.put("data1", data.getWater());
					datas.put("data2", data.getTemperature());
					dataset.add(datas);
				}
			}
			linesMap.put("dataName1", "水位(m)");
			linesMap.put("dataName2", "温度(°C)");
			linesMap.put("dataset", dataset);
			
			result.put("chkTitle", "观测井曲线图-"+well1.getName());
			break;
		case 2:
			Invertedwell well2 = invertedwellService.getById(cid);
			if (well2==null) {
				result.put("status", -1);
				result.put("message", "测点不存在，cid="+cid);
				return result;
			}
			whereList.add(new String[]{"iWell.id", String.valueOf(cid), "0"});
			Entitites<HiwellData> dataList2 = hiWellDataService.getPageListByAttributesLike(null, null, null, null, orderby, whereList, null, false);
			if (dataList2.getTotalCount()>0) {
				for (HiwellData data : dataList2.getEntites()) {
					Map<String, Object> datas = new HashMap<String, Object>();
					datas.put("xTime", DateFormatUtils.format(data.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
					datas.put("data1", data.getFlow());
					datas.put("data2", data.getPressure());
					dataset.add(datas);
				}
			}
			linesMap.put("dataName1", "流量(m³/h)");
			linesMap.put("dataName2", "井内水位(MPa)");
			linesMap.put("dataset", dataset);
			
			result.put("chkTitle", "回灌井曲线图-"+well2.getName());
			break;
		case 3:
			Deformmonitor well3 = deformmonitorService.getById(cid);
			if (well3==null) {
				result.put("status", -1);
				result.put("message", "测点不存在，cid="+cid);
				return result;
			}
			whereList.add(new String[]{"emonitor.id", String.valueOf(cid), "0"});
			Entitites<HemonitorData> dataList3 = heMonitorDataService.getPageListByAttributesLike(null, null, null, null, orderby, whereList, null, false);
			if (dataList3.getTotalCount()>0) {
				for (HemonitorData data : dataList3.getEntites()) {
					Map<String, Object> datas = new HashMap<String, Object>();
					datas.put("xTime", DateFormatUtils.format(data.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
					datas.put("data1", data.getData());
					dataset.add(datas);
				}
			}
			linesMap.put("dataName1", "数据");
			linesMap.put("dataset", dataset);
			
			result.put("chkTitle", "环境监测曲线图-"+well3.getName());
			break;
		case 4:
			Dewatering well4 = dewateringService.getById(cid);
			if (well4==null) {
				result.put("status", -1);
				result.put("message", "测点不存在，cid="+cid);
				return result;
			}
			whereList.add(new String[]{"deWell.id", String.valueOf(cid), "0"});
			Entitites<HdewellData> dataList4 = hdeWellDataService.getPageListByAttributesLike(null, null, null, null, orderby, whereList, null, false);
			if (dataList4.getTotalCount()>0) {
				for (HdewellData data : dataList4.getEntites()) {
					Map<String, Object> datas = new HashMap<String, Object>();
					datas.put("xTime", DateFormatUtils.format(data.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
					datas.put("data1", data.getFlow());
					datas.put("data2", data.getWater());
					dataset.add(datas);
				}
			}
			linesMap.put("dataName1", "流量(m³/h)");
			linesMap.put("dataName2", "水位(m)");
			linesMap.put("dataset", dataset);
			
			result.put("chkTitle", "疏干井曲线图-"+well4.getName());
			break;
		default:
			break;
		}
		
		result.put("status", 0);
		result.put("linesMap", linesMap);
		
		return result;
	}
	
	/**
	 * 多测点当前表曲线比较
	 * @param cid
	 * @param type 01降水井流量比较，02降水井水位比较，11观测井流量比较，12观测井水温比较，21回灌井流量比较，22回灌井水位比较，31变形监测数值比较,41疏干井流量比较，42疏干井水位比较，
	 * @param begintm
	 * @param endtm
	 * @return
	 */
	public Map<String, Object> getLines(String[] cid, String type, Date begintm, Date endtm){
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> orderby = new HashMap<String, String>();
		orderby.put("auditSection.dateCreated", "asc");
		
		List<Object[]> whereList = new ArrayList<Object[]>();
		if (begintm!=null) {
			whereList.add(new Object[]{"auditSection.dateCreated", begintm, "2"});
		}
		if (endtm!=null) {
			whereList.add(new Object[]{"auditSection.dateCreated", endtm, "4"});
		}
		
		List<Map<String, Object>> comparedataset = new ArrayList<Map<String, Object>>();
		switch (type) {
		case "01":
			for (int i = 0; i < cid.length; i++) {
				Map<String, Object> linesMap = new HashMap<String, Object>();
				List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
				Pumpwell well = pumpwellService.getById(Long.valueOf(cid[i]));
				if (well==null) {
					result.put("status", -1);
					result.put("message", "测点不存在，cid="+cid);
					return result;
				}
				List<Object[]> tmpList = new ArrayList<Object[]>();
				tmpList.add(new String[]{"pWell.id", String.valueOf(cid[i]), "0"});
				tmpList.addAll(whereList);
				Entitites<PwellData> dataList = pWellDataService.getPageListByAttributesLike(null, null, null, null, orderby, tmpList, null, false);
				if (dataList.getTotalCount()>0) {
					List<PwellData> list = dataList.getEntites();
					int interval = getIntervalPoint(list.size());
					for (int j = 0; j < list.size();) {
						PwellData data = list.get(j);
						Map<String, Object> datas = new HashMap<String, Object>();
						datas.put("xTime", DateFormatUtils.format(data.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
						datas.put("data", data.getFlow());
						dataset.add(datas);
						j += interval;
					}
				}
				linesMap.put("dataset", dataset);
				linesMap.put("dataName", well.getName());
				comparedataset.add(linesMap);
			}
			result.put("chkTitle", "降水井曲线图-流量(m³/h)比较");
			break;
		case "02":
			for (int i = 0; i < cid.length; i++) {
				Map<String, Object> linesMap = new HashMap<String, Object>();
				List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
				Pumpwell well = pumpwellService.getById(Long.valueOf(cid[i]));
				if (well==null) {
					result.put("status", -1);
					result.put("message", "测点不存在，cid="+cid);
					return result;
				}
				List<Object[]> tmpList = new ArrayList<Object[]>();
				tmpList.add(new String[]{"pWell.id", String.valueOf(cid[i]), "0"});
				tmpList.addAll(whereList);
				Entitites<PwellData> dataList = pWellDataService.getPageListByAttributesLike(null, null, null, null, orderby, tmpList, null, false);
				if (dataList.getTotalCount()>0) {
					List<PwellData> list = dataList.getEntites();
					int interval = getIntervalPoint(list.size());
					for (int j = 0; j < list.size();) {
						PwellData data = list.get(j);
						Map<String, Object> datas = new HashMap<String, Object>();
						datas.put("xTime", DateFormatUtils.format(data.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
						datas.put("data", data.getWater());
						dataset.add(datas);
						j += interval;
					}
				}
				linesMap.put("dataset", dataset);
				linesMap.put("dataName", well.getName());
				comparedataset.add(linesMap);
			}
			result.put("chkTitle", "降水井曲线图-水位(m)比较");
			break;
		case "11":
			for (int i = 0; i < cid.length; i++) {
				Map<String, Object> linesMap = new HashMap<String, Object>();
				List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
				Observewell well = observewellService.getById(Long.valueOf(cid[i]));
				if (well==null) {
					result.put("status", -1);
					result.put("message", "测点不存在，cid="+cid);
					return result;
				}
				List<Object[]> tmpList = new ArrayList<Object[]>();
				tmpList.add(new String[]{"oWell.id", String.valueOf(cid[i]), "0"});
				tmpList.addAll(whereList);
				Entitites<OwellData> dataList = oWellDataService.getPageListByAttributesLike(null, null, null, null, orderby, tmpList, null, false);
				if (dataList.getTotalCount()>0) {
					List<OwellData> list = dataList.getEntites();
					int interval = getIntervalPoint(list.size());
					for (int j = 0; j < list.size();) {
						OwellData data = list.get(j);
						Map<String, Object> datas = new HashMap<String, Object>();
						datas.put("xTime", DateFormatUtils.format(data.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
						datas.put("data", data.getWater());
						dataset.add(datas);
						j += interval;
					}
				}
				linesMap.put("dataset", dataset);
				linesMap.put("dataName", well.getName());
				comparedataset.add(linesMap);
			}
			result.put("chkTitle", "观测井曲线图-水位(m)比较");
			break;
		case "12":
			for (int i = 0; i < cid.length; i++) {
				Map<String, Object> linesMap = new HashMap<String, Object>();
				List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
				Observewell well = observewellService.getById(Long.valueOf(cid[i]));
				if (well==null) {
					result.put("status", -1);
					result.put("message", "测点不存在，cid="+cid);
					return result;
				}
				List<Object[]> tmpList = new ArrayList<Object[]>();
				tmpList.add(new String[]{"oWell.id", String.valueOf(cid[i]), "0"});
				tmpList.addAll(whereList);
				Entitites<OwellData> dataList = oWellDataService.getPageListByAttributesLike(null, null, null, null, orderby, tmpList, null, false);
				if (dataList.getTotalCount()>0) {
					List<OwellData> list = dataList.getEntites();
					int interval = getIntervalPoint(list.size());
					for (int j = 0; j < list.size();) {
						OwellData data = list.get(j);
						Map<String, Object> datas = new HashMap<String, Object>();
						datas.put("xTime", DateFormatUtils.format(data.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
						datas.put("data", data.getTemperature());
						dataset.add(datas);
						j += interval;
					}
				}
				linesMap.put("dataset", dataset);
				linesMap.put("dataName", well.getName());
				comparedataset.add(linesMap);
			}
			result.put("chkTitle", "观测井曲线图-温度(°C)比较");
			break;
		case "21":
			for (int i = 0; i < cid.length; i++) {
				Map<String, Object> linesMap = new HashMap<String, Object>();
				List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
				Invertedwell well = invertedwellService.getById(Long.valueOf(cid[i]));
				if (well==null) {
					result.put("status", -1);
					result.put("message", "测点不存在，cid="+cid);
					return result;
				}
				List<Object[]> tmpList = new ArrayList<Object[]>();
				tmpList.add(new String[]{"iWell.id", String.valueOf(cid[i]), "0"});
				tmpList.addAll(whereList);
				Entitites<IwellData> dataList = iWellDataService.getPageListByAttributesLike(null, null, null, null, orderby, tmpList, null, false);
				if (dataList.getTotalCount()>0) {
					List<IwellData> list = dataList.getEntites();
					int interval = getIntervalPoint(list.size());
					for (int j = 0; j < list.size();) {
						IwellData data = list.get(j);
						Map<String, Object> datas = new HashMap<String, Object>();
						datas.put("xTime", DateFormatUtils.format(data.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
						datas.put("data", data.getFlow());
						dataset.add(datas);
						j += interval;
					}
				}
				linesMap.put("dataset", dataset);
				linesMap.put("dataName", well.getName());
				comparedataset.add(linesMap);
			}
			result.put("chkTitle", "回灌井曲线图-流量(m³/h)比较");
			break;
		case "22":
			for (int i = 0; i < cid.length; i++) {
				Map<String, Object> linesMap = new HashMap<String, Object>();
				List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
				Invertedwell well = invertedwellService.getById(Long.valueOf(cid[i]));
				if (well==null) {
					result.put("status", -1);
					result.put("message", "测点不存在，cid="+cid);
					return result;
				}
				List<Object[]> tmpList = new ArrayList<Object[]>();
				tmpList.add(new String[]{"iWell.id", String.valueOf(cid[i]), "0"});
				tmpList.addAll(whereList);
				Entitites<IwellData> dataList = iWellDataService.getPageListByAttributesLike(null, null, null, null, orderby, tmpList, null, false);
				if (dataList.getTotalCount()>0) {
					List<IwellData> list = dataList.getEntites();
					int interval = getIntervalPoint(list.size());
					for (int j = 0; j < list.size();) {
						IwellData data = list.get(j);
						Map<String, Object> datas = new HashMap<String, Object>();
						datas.put("xTime", DateFormatUtils.format(data.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
						datas.put("data", data.getPressure());
						dataset.add(datas);
						j += interval;
					}
				}
				linesMap.put("dataset", dataset);
				linesMap.put("dataName", well.getName());
				comparedataset.add(linesMap);
			}
			result.put("chkTitle", "回灌井曲线图-井内水位(MPa)比较");
			break;
		case "31":
			for (int i = 0; i < cid.length; i++) {
				Map<String, Object> linesMap = new HashMap<String, Object>();
				List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
				Deformmonitor well = deformmonitorService.getById(Long.valueOf(cid[i]));
				if (well==null) {
					result.put("status", -1);
					result.put("message", "测点不存在，cid="+cid);
					return result;
				}
				List<Object[]> tmpList = new ArrayList<Object[]>();
				tmpList.add(new String[]{"emonitor.id", String.valueOf(cid[i]), "0"});
				tmpList.addAll(whereList);
				Entitites<EmonitorData> dataList = eMonitorDataService.getPageListByAttributesLike(null, null, null, null, orderby, tmpList, null, false);
				if (dataList.getTotalCount()>0) {
					List<EmonitorData> list = dataList.getEntites();
					int interval = getIntervalPoint(list.size());
					for (int j = 0; j < list.size();) {
						EmonitorData data = list.get(j);
						Map<String, Object> datas = new HashMap<String, Object>();
						datas.put("xTime", DateFormatUtils.format(data.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
						datas.put("data", data.getData());
						dataset.add(datas);
						j += interval;
					}
				}
				linesMap.put("dataset", dataset);
				linesMap.put("dataName", well.getName());
				comparedataset.add(linesMap);
			}
			result.put("chkTitle", "环境监测曲线图-数据比较");
			break;
		case "41":
			for (int i = 0; i < cid.length; i++) {
				Map<String, Object> linesMap = new HashMap<String, Object>();
				List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
				Dewatering well = dewateringService.getById(Long.valueOf(cid[i]));
				if (well==null) {
					result.put("status", -1);
					result.put("message", "测点不存在，cid="+cid);
					return result;
				}
				List<Object[]> tmpList = new ArrayList<Object[]>();
				tmpList.add(new String[]{"deWell.id", String.valueOf(cid[i]), "0"});
				tmpList.addAll(whereList);
				Entitites<DewellData> dataList = deWellDataService.getPageListByAttributesLike(null, null, null, null, orderby, tmpList, null, false);
				if (dataList.getTotalCount()>0) {
					List<DewellData> list = dataList.getEntites();
					int interval = getIntervalPoint(list.size());
					for (int j = 0; j < list.size();) {
						DewellData data = list.get(j);
						Map<String, Object> datas = new HashMap<String, Object>();
						datas.put("xTime", DateFormatUtils.format(data.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
						datas.put("data", data.getFlow());
						dataset.add(datas);
						j += interval;
					}
				}
				linesMap.put("dataset", dataset);
				linesMap.put("dataName", well.getName());
				comparedataset.add(linesMap);
			}
			result.put("chkTitle", "疏干井曲线图-流量(m³/h)比较");
			break;
		case "42":
			for (int i = 0; i < cid.length; i++) {
				Map<String, Object> linesMap = new HashMap<String, Object>();
				List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
				Dewatering well = dewateringService.getById(Long.valueOf(cid[i]));
				if (well==null) {
					result.put("status", -1);
					result.put("message", "测点不存在，cid="+cid);
					return result;
				}
				List<Object[]> tmpList = new ArrayList<Object[]>();
				tmpList.add(new String[]{"deWell.id", String.valueOf(cid[i]), "0"});
				tmpList.addAll(whereList);
				Entitites<DewellData> dataList = deWellDataService.getPageListByAttributesLike(null, null, null, null, orderby, tmpList, null, false);
				if (dataList.getTotalCount()>0) {
					List<DewellData> list = dataList.getEntites();
					int interval = getIntervalPoint(list.size());
					for (int j = 0; j < list.size();) {
						DewellData data = list.get(j);
						Map<String, Object> datas = new HashMap<String, Object>();
						datas.put("xTime", DateFormatUtils.format(data.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
						datas.put("data", data.getWater());
						dataset.add(datas);
						j += interval;
					}
				}
				linesMap.put("dataset", dataset);
				linesMap.put("dataName", well.getName());
				comparedataset.add(linesMap);
			}
			result.put("chkTitle", "疏干井曲线图-水位(m)比较");
			break;
		default:
			break;
		}
		result.put("status", 0);
		result.put("comparedataset", comparedataset);
		return result;
	}
	
	/**
	 * 多测点历史曲线比较
	 * @param cid
	 * @param type 01降水井流量比较，02降水井水位比较，11观测井流量比较，12观测井水温比较，21回灌井流量比较，22回灌井水位比较，31变形监测数值比较,41疏干井流量比较，42疏干井水位比较，
	 * @param begintm
	 * @param endtm
	 * @return
	 */
	public Map<String, Object> getHistoryLines(String[] cid, String type, Date begintm, Date endtm){
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> orderby = new HashMap<String, String>();
		orderby.put("auditSection.dateCreated", "asc");
		
		List<Object[]> whereList = new ArrayList<Object[]>();
		if (begintm!=null) {
			whereList.add(new Object[]{"auditSection.dateCreated", begintm, "2"});
		}
		if (endtm!=null) {
			whereList.add(new Object[]{"auditSection.dateCreated", endtm, "4"});
		}
		
		List<Map<String, Object>> comparedataset = new ArrayList<Map<String, Object>>();
		switch (type) {
		case "01":
			for (int i = 0; i < cid.length; i++) {
				Map<String, Object> linesMap = new HashMap<String, Object>();
				List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
				Pumpwell well = pumpwellService.getById(Long.valueOf(cid[i]));
				if (well==null) {
					result.put("status", -1);
					result.put("message", "测点不存在，cid="+cid);
					return result;
				}
				List<Object[]> tmpList = new ArrayList<Object[]>();
				tmpList.add(new String[]{"pWell.id", String.valueOf(cid[i]), "0"});
				tmpList.addAll(whereList);
				Entitites<HpwellData> dataList = hpWellDataService.getPageListByAttributesLike(null, null, null, null, orderby, tmpList, null, false);
				if (dataList.getTotalCount()>0) {
					List<HpwellData> list = dataList.getEntites();
					int interval = getIntervalPoint(list.size());
					for (int j = 0; j < list.size();) {
						HpwellData data = list.get(j);
						Map<String, Object> datas = new HashMap<String, Object>();
						datas.put("xTime", DateFormatUtils.format(data.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
						datas.put("data", data.getFlow());
						dataset.add(datas);
						j += interval;
					}
				}
				linesMap.put("dataset", dataset);
				linesMap.put("dataName", well.getName());
				comparedataset.add(linesMap);
			}
			result.put("chkTitle", "降水井曲线图-流量(m³/h)比较");
			break;
		case "02":
			for (int i = 0; i < cid.length; i++) {
				Map<String, Object> linesMap = new HashMap<String, Object>();
				List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
				Pumpwell well = pumpwellService.getById(Long.valueOf(cid[i]));
				if (well==null) {
					result.put("status", -1);
					result.put("message", "测点不存在，cid="+cid);
					return result;
				}
				List<Object[]> tmpList = new ArrayList<Object[]>();
				tmpList.add(new String[]{"pWell.id", String.valueOf(cid[i]), "0"});
				tmpList.addAll(whereList);
				Entitites<HpwellData> dataList = hpWellDataService.getPageListByAttributesLike(null, null, null, null, orderby, tmpList, null, false);
				if (dataList.getTotalCount()>0) {
					List<HpwellData> list = dataList.getEntites();
					int interval = getIntervalPoint(list.size());
					for (int j = 0; j < list.size();) {
						HpwellData data = list.get(j);
						Map<String, Object> datas = new HashMap<String, Object>();
						datas.put("xTime", DateFormatUtils.format(data.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
						datas.put("data", data.getWater());
						dataset.add(datas);
						j += interval;
					}
				}
				linesMap.put("dataset", dataset);
				linesMap.put("dataName", well.getName());
				comparedataset.add(linesMap);
			}
			result.put("chkTitle", "降水井曲线图-水位(m)比较");
			break;
		case "11":
			for (int i = 0; i < cid.length; i++) {
				Map<String, Object> linesMap = new HashMap<String, Object>();
				List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
				Observewell well = observewellService.getById(Long.valueOf(cid[i]));
				if (well==null) {
					result.put("status", -1);
					result.put("message", "测点不存在，cid="+cid);
					return result;
				}
				List<Object[]> tmpList = new ArrayList<Object[]>();
				tmpList.add(new String[]{"oWell.id", String.valueOf(cid[i]), "0"});
				tmpList.addAll(whereList);
				Entitites<HowellData> dataList = hoWellDataService.getPageListByAttributesLike(null, null, null, null, orderby, tmpList, null, false);
				if (dataList.getTotalCount()>0) {
					List<HowellData> list = dataList.getEntites();
					int interval = getIntervalPoint(list.size());
					for (int j = 0; j < list.size();) {
						HowellData data = list.get(j);
						Map<String, Object> datas = new HashMap<String, Object>();
						datas.put("xTime", DateFormatUtils.format(data.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
						datas.put("data", data.getWater());
						dataset.add(datas);
						j += interval;
					}
				}
				linesMap.put("dataset", dataset);
				linesMap.put("dataName", well.getName());
				comparedataset.add(linesMap);
			}
			result.put("chkTitle", "观测井曲线图-水位(m)比较");
			break;
		case "12":
			for (int i = 0; i < cid.length; i++) {
				Map<String, Object> linesMap = new HashMap<String, Object>();
				List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
				Observewell well = observewellService.getById(Long.valueOf(cid[i]));
				if (well==null) {
					result.put("status", -1);
					result.put("message", "测点不存在，cid="+cid);
					return result;
				}
				List<Object[]> tmpList = new ArrayList<Object[]>();
				tmpList.add(new String[]{"oWell.id", String.valueOf(cid[i]), "0"});
				tmpList.addAll(whereList);
				Entitites<HowellData> dataList = hoWellDataService.getPageListByAttributesLike(null, null, null, null, orderby, tmpList, null, false);
				if (dataList.getTotalCount()>0) {
					List<HowellData> list = dataList.getEntites();
					int interval = getIntervalPoint(list.size());
					for (int j = 0; j < list.size();) {
						HowellData data = list.get(j);
						Map<String, Object> datas = new HashMap<String, Object>();
						datas.put("xTime", DateFormatUtils.format(data.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
						datas.put("data", data.getTemperature());
						dataset.add(datas);
						j += interval;
					}
				}
				linesMap.put("dataset", dataset);
				linesMap.put("dataName", well.getName());
				comparedataset.add(linesMap);
			}
			result.put("chkTitle", "观测井曲线图-温度(°C)比较");
			break;
		case "21":
			for (int i = 0; i < cid.length; i++) {
				Map<String, Object> linesMap = new HashMap<String, Object>();
				List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
				Invertedwell well = invertedwellService.getById(Long.valueOf(cid[i]));
				if (well==null) {
					result.put("status", -1);
					result.put("message", "测点不存在，cid="+cid);
					return result;
				}
				List<Object[]> tmpList = new ArrayList<Object[]>();
				tmpList.add(new String[]{"iWell.id", String.valueOf(cid[i]), "0"});
				tmpList.addAll(whereList);
				Entitites<HiwellData> dataList = hiWellDataService.getPageListByAttributesLike(null, null, null, null, orderby, tmpList, null, false);
				if (dataList.getTotalCount()>0) {
					List<HiwellData> list = dataList.getEntites();
					int interval = getIntervalPoint(list.size());
					for (int j = 0; j < list.size();) {
						HiwellData data = list.get(j);
						Map<String, Object> datas = new HashMap<String, Object>();
						datas.put("xTime", DateFormatUtils.format(data.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
						datas.put("data", data.getFlow());
						dataset.add(datas);
						j += interval;
					}
				}
				linesMap.put("dataset", dataset);
				linesMap.put("dataName", well.getName());
				comparedataset.add(linesMap);
			}
			result.put("chkTitle", "回灌井曲线图-流量(m³/h)比较");
			break;
		case "22":
			for (int i = 0; i < cid.length; i++) {
				Map<String, Object> linesMap = new HashMap<String, Object>();
				List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
				Invertedwell well = invertedwellService.getById(Long.valueOf(cid[i]));
				if (well==null) {
					result.put("status", -1);
					result.put("message", "测点不存在，cid="+cid);
					return result;
				}
				List<Object[]> tmpList = new ArrayList<Object[]>();
				tmpList.add(new String[]{"iWell.id", String.valueOf(cid[i]), "0"});
				tmpList.addAll(whereList);
				Entitites<HiwellData> dataList = hiWellDataService.getPageListByAttributesLike(null, null, null, null, orderby, tmpList, null, false);
				if (dataList.getTotalCount()>0) {
					List<HiwellData> list = dataList.getEntites();
					int interval = getIntervalPoint(list.size());
					for (int j = 0; j < list.size();) {
						HiwellData data = list.get(j);
						Map<String, Object> datas = new HashMap<String, Object>();
						datas.put("xTime", DateFormatUtils.format(data.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
						datas.put("data", data.getPressure());
						dataset.add(datas);
						j += interval;
					}
				}
				linesMap.put("dataset", dataset);
				linesMap.put("dataName", well.getName());
				comparedataset.add(linesMap);
			}
			result.put("chkTitle", "回灌井曲线图-井内水位(MPa)比较");
			break;
		case "31":
			for (int i = 0; i < cid.length; i++) {
				Map<String, Object> linesMap = new HashMap<String, Object>();
				List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
				Deformmonitor well = deformmonitorService.getById(Long.valueOf(cid[i]));
				if (well==null) {
					result.put("status", -1);
					result.put("message", "测点不存在，cid="+cid);
					return result;
				}
				List<Object[]> tmpList = new ArrayList<Object[]>();
				tmpList.add(new String[]{"emonitor.id", String.valueOf(cid[i]), "0"});
				tmpList.addAll(whereList);
				Entitites<HemonitorData> dataList = heMonitorDataService.getPageListByAttributesLike(null, null, null, null, orderby, tmpList, null, false);
				if (dataList.getTotalCount()>0) {
					List<HemonitorData> list = dataList.getEntites();
					int interval = getIntervalPoint(list.size());
					for (int j = 0; j < list.size();) {
						HemonitorData data = list.get(j);
						Map<String, Object> datas = new HashMap<String, Object>();
						datas.put("xTime", DateFormatUtils.format(data.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
						datas.put("data", data.getData());
						dataset.add(datas);
						j += interval;
					}
				}
				linesMap.put("dataset", dataset);
				linesMap.put("dataName", well.getName());
				comparedataset.add(linesMap);
			}
			result.put("chkTitle", "环境监测曲线图-数据比较");
			break;
		case "41":
			for (int i = 0; i < cid.length; i++) {
				Map<String, Object> linesMap = new HashMap<String, Object>();
				List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
				Dewatering dewell = dewateringService.getById(Long.valueOf(cid[i]));
				if (dewell==null) {
					result.put("status", -1);
					result.put("message", "测点不存在，cid="+cid);
					return result;
				}
				List<Object[]> tmpList = new ArrayList<Object[]>();
				tmpList.add(new String[]{"deWell.id", String.valueOf(cid[i]), "0"});
				tmpList.addAll(whereList);
				Entitites<HdewellData> dataList = hdeWellDataService.getPageListByAttributesLike(null, null, null, null, orderby, tmpList, null, false);
				if (dataList.getTotalCount()>0) {
					List<HdewellData> list = dataList.getEntites();
					int interval = getIntervalPoint(list.size());
					for (int j = 0; j < list.size();) {
						HdewellData data = list.get(j);
						Map<String, Object> datas = new HashMap<String, Object>();
						datas.put("xTime", DateFormatUtils.format(data.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
						datas.put("data", data.getFlow());
						dataset.add(datas);
						j += interval;
					}
				}
				linesMap.put("dataset", dataset);
				linesMap.put("dataName", dewell.getName());
				comparedataset.add(linesMap);
			}
			result.put("chkTitle", "疏干井曲线图-流量(m³/h)比较");
			break;	
		case "42":
			for (int i = 0; i < cid.length; i++) {
				Map<String, Object> linesMap = new HashMap<String, Object>();
				List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
				Dewatering dewell = dewateringService.getById(Long.valueOf(cid[i]));
				if (dewell==null) {
					result.put("status", -1);
					result.put("message", "测点不存在，cid="+cid);
					return result;
				}
				List<Object[]> tmpList = new ArrayList<Object[]>();
				tmpList.add(new String[]{"deWell.id", String.valueOf(cid[i]), "0"});
				tmpList.addAll(whereList);
				Entitites<HdewellData> dataList = hdeWellDataService.getPageListByAttributesLike(null, null, null, null, orderby, tmpList, null, false);
				if (dataList.getTotalCount()>0) {
					List<HdewellData> list = dataList.getEntites();
					int interval = getIntervalPoint(list.size());
					for (int j = 0; j < list.size();) {
						HdewellData data = list.get(j);
						Map<String, Object> datas = new HashMap<String, Object>();
						datas.put("xTime", DateFormatUtils.format(data.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
						datas.put("data", data.getWater());
						dataset.add(datas);
						j += interval;
					}
				}
				linesMap.put("dataset", dataset);
				linesMap.put("dataName", dewell.getName());
				comparedataset.add(linesMap);
			}
			result.put("chkTitle", "疏干井曲线图-水位(m)比较");
			break;
		default:
			break;
		}
		result.put("status", 0);
		result.put("comparedataset", comparedataset);
		return result;
	}
	
	/**
	 * 求时间曲线图显示节点的个数，如果size>mod,则间隔为1+size/mod
	 * @param size
	 * @return
	 */
	private Integer getIntervalPoint(Integer size) {
		if (size>mod) {
			int interval = size/mod;
			return size%mod>0?interval+1:interval;
		} else {
			return 1;
		}
	}
}
