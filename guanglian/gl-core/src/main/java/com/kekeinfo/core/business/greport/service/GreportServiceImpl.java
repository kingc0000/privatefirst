package com.kekeinfo.core.business.greport.service;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.greport.dao.GreportDao;
import com.kekeinfo.core.business.greport.model.Greport;
import com.kekeinfo.core.business.guard.data.service.DiameterConvertDataService;
import com.kekeinfo.core.business.guard.model.GPointEnumType;
import com.kekeinfo.core.business.guard.point.service.DiameterConvertService;

@Service("greportService")
public class GreportServiceImpl extends KekeinfoEntityServiceImpl<Long, Greport> implements GreportService {
	@SuppressWarnings("unused")
	private GreportDao greportDao;
	
	@Autowired DiameterConvertService diameterConvertService;
	@Autowired DiameterConvertDataService diameterConvertDataService;
	@Autowired
	public GreportServiceImpl(GreportDao greportDao) {
		super(greportDao);
		this.greportDao = greportDao;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void createReport(Greport report) throws ServiceException {
		// TODO Auto-generated method stub
		for (GPointEnumType e : GPointEnumType.values()) {
			//System.out.println(e.toString());
			List sf =diameterConvertService.getByMid(report.getGuard().getId(), e);
			if(sf!=null){
				try{
					Class catClass = Class.forName("com.kekeinfo.core.business.greport.model.R"+e.toString()+"Data");
					Set rsds =new HashSet<>();
					for(Object o:sf){
						Object obj=catClass.newInstance();
						Class cClass = Class.forName("com.kekeinfo.core.business.guard.point.model."+e.toString());
						Method[] mess=cClass.getMethods();
						Object ob=null;
						
						for(Method me:mess){
							if(me.getName().equals("getId")){
								ob =diameterConvertDataService.getByDate(report.getThiser(), (Long)me.invoke(o),e);
								break;
							}
						}
						BigDecimal init =new BigDecimal(0);
						Class cpClass = Class.forName("com.kekeinfo.core.business.guard.data.model."+e.toString()+"Data");
						if(ob!=null){
							for(Method me:mess){
								if(me.getName().equals("getMarkNO")){
									String markNO=(String)me.invoke(o);
									catClass.getMethod("setMarkNO",String.class).invoke(obj,markNO);
									
								}
								if(me.getName().equals("getInitHeight")){
									Object obj1=me.invoke(o);
									if(obj1!=null){
										init=(BigDecimal)obj1;
									}
								}
							}
							
							Method[] dess =cpClass.getMethods();
							BigDecimal cue=null;
							BigDecimal initb=null;
							//第一遍获取值
							for(Method me:dess){
								if(me.getName().equals("getCurtHeight")){
									cue=(BigDecimal) me.invoke(ob);
									catClass.getMethod("setCurHeight",BigDecimal.class).invoke(obj,cue);
									catClass.getMethod("setSum",BigDecimal.class).invoke(obj,cue.subtract(init));
								}
								if(me.getName().equals("getInitHeight")){
									Object inito= me.invoke(ob);
									if(inito==null){
										initb=new BigDecimal(0);
									}else{
										 initb=(BigDecimal) inito;
										 initb=initb.subtract(init);
									}
									catClass.getMethod("setLast",BigDecimal.class).invoke(obj,initb);
								}
							}
							//设置同greport的关联
							catClass.getMethod("setGreport",Greport.class).invoke(obj,report);
							rsds.add(obj);
						}
						
					}
					Class rclass= Greport.class;
					rclass.getMethod("setr"+e.toString(), Set.class).invoke(report, rsds);
				}catch (Exception e1){
					e1.toString();
				}
			}
		}
		super.save(report);
	}
}
