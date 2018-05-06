package com.kekeinfo.core.business.mreport.service;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.monitor.data.service.SurfaceDataService;
import com.kekeinfo.core.business.monitor.model.MPointEnumType;
import com.kekeinfo.core.business.monitor.statistical.model.Mstatistical;
import com.kekeinfo.core.business.monitor.statistical.service.MstatisticalService;
import com.kekeinfo.core.business.monitor.surface.service.SurfaceService;
import com.kekeinfo.core.business.monitoreqip.model.MpointEquip;
import com.kekeinfo.core.business.monitoreqip.service.MpointEquipService;
import com.kekeinfo.core.business.mreport.dao.RMreportDao;
import com.kekeinfo.core.business.mreport.model.PointInfo;
import com.kekeinfo.core.business.mreport.model.RMreport;

@Service("rmreportService")
public class RMreportServiceImpl extends KekeinfoEntityServiceImpl<Long, RMreport> implements RMreportService {
	private RMreportDao rmreportDao;
	@Autowired MstatisticalService statisticalService;
	@Autowired SurfaceService surfaceService;
	@Autowired SurfaceDataService surfaceDataService;
	@Autowired MpointEquipService mpointEquipService;
	@Autowired
	public RMreportServiceImpl(RMreportDao rmreportDao) {
		super(rmreportDao);
		this.rmreportDao = rmreportDao;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional
	@Override
	public int createReport(RMreport rMreport) throws ServiceException{
		//获取日报
		Mstatistical md =statisticalService.getByDate(rMreport.getThiser());
		if(md==null ){
			return -1;
		}
		 
		rMreport.setMstatistical(md);
		for (MPointEnumType e : MPointEnumType.values()) {
			//System.out.println(e.toString());
			List sf =surfaceService.getByMid(rMreport.getMonitor().getId(), e);
			if(sf!=null){
				try{
					Class catClass = Class.forName("com.kekeinfo.core.business.mreport.model.R"+e.toString()+"Data");
					Set rsds =new HashSet<>();
					for(Object o:sf){
						Object obj=catClass.newInstance();
						Class cClass = Class.forName("com.kekeinfo.core.business.monitor.surface.model."+e.toString());
						Method[] mess=cClass.getMethods();
						Object ob=null;
						List<MpointEquip> mes=mpointEquipService.getByMtype(e.toString());
						PointInfo pointinfo =new PointInfo();
						for(Method me:mess){
							if(me.getName().equals("getId")){
								ob =surfaceDataService.getByDate(rMreport.getThiser(), (Long)me.invoke(o),e);
								break;
							}
						}
						for(Method me:mess){
							if(me.getName().equals("getInitHeight")){
								pointinfo.setfHeight(me.invoke(o)==null?null:(BigDecimal)me.invoke(o));
							}
							if(me.getName().equals("getFrontDisplacement")){
								pointinfo.setsHeight(me.invoke(o)==null?null:(BigDecimal)me.invoke(o));
							}
							if(me.getName().equals("getMarkNO")){
								pointinfo.setMarkNO(me.invoke(o).toString());
							}
							if(me.getName().equals("getMemo")){
								pointinfo.setMemo(me.invoke(o)==null?null:(String)me.invoke(o));
							}
							//支撑轴力，设置支撑类型
							if(e==MPointEnumType.SupAxial){
								if(me.getName().equals("getZhiCheng")){
									catClass.getMethod("setZhiCheng",String.class).invoke(obj,me.invoke(o));
								}
								if(me.getName().equals("getAlarmV")){
									catClass.getMethod("setAlarmV",BigDecimal.class).invoke(obj,me.invoke(o));
								}
							}
						}
						
						Class cpClass = Class.forName("com.kekeinfo.core.business.monitor.data.model."+e.toString()+"Data");
						
						
						if(ob!=null){
							Method[] dess =cpClass.getMethods();
							for(Method me:dess){
								if(me.getName().equals("getCalibration")){
									catClass.getMethod("setCalibration",Date.class).invoke(obj,me.invoke(ob));
								}
								if(me.getName().equals("getInitHeight")){
									catClass.getMethod("setInitHeight",BigDecimal.class).invoke(obj,me.invoke(ob));
								}
								if(me.getName().equals("getCurtHeight")){
									catClass.getMethod("setCurtHeight",BigDecimal.class).invoke(obj,me.invoke(ob));
								}
							}
							
						}
						
						catClass.getMethod("setReport",RMreport.class).invoke(obj,rMreport);
						catClass.getMethod("setPointInfo",PointInfo.class).invoke(obj,pointinfo);
						//添加设备
						if(mes!=null && mes.size()>0){
							for(MpointEquip me:mes){
								if(me.getMpoint().getpType().equalsIgnoreCase(e.toString())){
									pointinfo.seteName(me.getMonitorEqip().getEquip().getName());
									pointinfo.seteNameid(me.getMonitorEqip().getEquip().geteNO());
									break;
								}
							}
						}
						//catClass.getMethod(name, parameterTypes)
						rsds.add(obj);
					}
					Class rclass= RMreport.class;
					rclass.getMethod("sets"+e.toString(), Set.class).invoke(rMreport, rsds);
				}catch (Exception e1){
					e1.toString();
				}
			}
		}
		
		super.save(rMreport);
		return  0;
	}
	
	@Override
	public RMreport getByRid(Long rid) throws ServiceException {
		return this.rmreportDao.getByRid(rid);
	}
}
