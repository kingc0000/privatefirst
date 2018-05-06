package com.kekeinfo.web.utils;

import java.text.ParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.kekeinfo.core.business.dewatering.model.Dewatering;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.model.PointEnumType;
import com.kekeinfo.core.business.invertedwell.model.Invertedwell;
import com.kekeinfo.core.business.observewell.model.Observewell;
import com.kekeinfo.core.business.pumpwell.model.Pumpwell;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.services.controller.system.ModbusListener;

public class AutoOpenAndShut {
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;	
	@Autowired ModbusListener modbusListener;
	private static final Logger LOG = LoggerFactory.getLogger(AutoOpenAndShut.class);
	@SuppressWarnings("unchecked")
	public void openOrShut() throws ParseException, ServiceException{
		
		//抽水井
		List<Pumpwell> pwells=(List<Pumpwell>) webCacheUtils.getFromCache(Constants.AUTOPWELL);
		if(pwells!=null && pwells.size()>0){
			for(Pumpwell p:pwells){
				boolean isSave =false;
				Integer pstatus=p.getPowerStatus();
				long con=p.getContinMin();
				//开启的，查看是否关闭
				if(p.getPowerStatus().intValue()==0){
					//查看持续时间
					if(p.getContinMin()>=p.getConMin().longValue() ){
						isSave=true;
						p.setPowerStatus(1);
						p.setContinMin(1l);
					}
				}else{
					if(p.getContinMin()>=p.getSpaceMin()){
						isSave=true;
						p.setPowerStatus(0);
						p.setContinMin(1l);
					}
				}
				if(isSave){
					try{
						
						int flag=-999999;
						while(flag==-999999){
							flag = modbusListener.modifyStart(p,PointEnumType.PUMP);
							LOG.debug("操作结果："+p.getId()+"状态"+flag);
							//自动操作失败
							 if(flag!=-999999 && flag!=0){
								p.setPowerStatus(pstatus);
								p.setContinMin(con);
							}
						}
						
					}catch (Exception e){
						LOG.debug(e.getMessage());
					}
				}else{
					p.setContinMin(p.getContinMin()+1);
				}
			}
		}
		//sugan
			List<Dewatering> dewells=(List<Dewatering>) webCacheUtils.getFromCache(Constants.AUTODEWELL);
			if(dewells!=null && dewells.size()>0){
				for(Dewatering p:dewells){
					boolean isSave =false;
					Integer pstatus=p.getPowerStatus();
					long con=p.getContinMin();
					//开启的，查看是否关闭
					if(p.getPowerStatus().intValue()==0){
						//没有余数
						if(p.getConMin().longValue()<=p.getContinMin() ){
							isSave=true;
							p.setPowerStatus(1);
							p.setContinMin(1l);
						}
					}else{
						if(p.getSpaceMin().longValue()<=p.getContinMin() ){
							isSave=true;
							p.setPowerStatus(0);
							p.setContinMin(1l);
						}
					}
					if(isSave){
						try{
							System.out.println("dededddddde"+p.getId()+"状态"+p.getPowerStatus());
							int flag=-999999;
							while(flag==-999999){
								 flag = modbusListener.modifyStart(p,PointEnumType.DEWATERING);
								//自动操作失败
								if(flag!=-999999 && flag!=0){
									p.setPowerStatus(pstatus);
									p.setContinMin(con);
								}
							}
						}catch (Exception e){
							LOG.debug(e.getMessage());
						}
					}else{
						p.setContinMin(p.getContinMin()+1);
					}
				}
			}
			//sugan
			List<Observewell> owells=(List<Observewell>) webCacheUtils.getFromCache(Constants.AUTOOWELL);
			if(owells!=null && owells.size()>0){
				for(Observewell p:owells){
					boolean isSave =false;
					Integer pstatus=p.getPowerStatus();
					long con=p.getContinMin();
					//开启的，查看是否关闭
					if(p.getPowerStatus().intValue()==0){
						//没有余数
						if(p.getConMin().longValue()>=p.getContinMin() ){
							isSave=true;
							p.setPowerStatus(1);
							p.setContinMin(1l);
						}
					}else{
						if(p.getSpaceMin().longValue()>=p.getContinMin() ){
							isSave=true;
							p.setPowerStatus(0);
							p.setContinMin(1l);
						}
					}
					if(isSave){
						try{
							System.out.println("ooooooooo"+p.getId()+"状态"+p.getPowerStatus());
							int flag=-999999;
							while(flag==-999999){
								 flag = modbusListener.modifyStart(p,PointEnumType.OBSERVE);
								if(flag!=-999999 && flag!=0){
									p.setPowerStatus(pstatus);
									p.setContinMin(con);
								}
							}
							
							
						}catch (Exception e){
							LOG.debug(e.getMessage());
						}
					}else{
						p.setContinMin(p.getContinMin()+1);
					}
				}
			}
			//sugan
			List<Invertedwell> iwells=(List<Invertedwell>) webCacheUtils.getFromCache(Constants.AUTOIWELL);
			if(iwells!=null && iwells.size()>0){
				for(Invertedwell p:iwells){
					boolean isSave =false;
					Integer pstatus=p.getPowerStatus();
					long con=p.getContinMin();
					//开启的，查看是否关闭
					if(p.getPowerStatus().intValue()==0){
						//没有余数
						if(p.getConMin().longValue()>=p.getContinMin()){
							isSave=true;
							p.setPowerStatus(1);
							p.setContinMin(1l);
						}
					}else{
						if(p.getSpaceMin().longValue()>=p.getContinMin()){
							isSave=true;
							p.setPowerStatus(0);
							p.setContinMin(1l);
						}
					}
					if(isSave){
						try{
							System.out.println("iiiiiiiiiiiiii"+p.getId()+"状态"+p.getPowerStatus());
							int flag=-999999;
							while(flag==-999999){
								flag = modbusListener.modifyStart(p,PointEnumType.INVERTED);
								 if(flag!=-999999 && flag!=0){
									p.setPowerStatus(pstatus);
									p.setContinMin(con);
								}
							}
						}catch (Exception e){
							LOG.debug(e.getMessage());
						}
					}else{
						p.setContinMin(p.getContinMin()+1);
					}
				}
			}	
	}

}
