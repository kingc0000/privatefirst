package com.kekeinfo.web.services.controller.system;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kekeinfo.core.business.csite.service.CSiteService;
import com.kekeinfo.core.business.data.service.HdeWellDataService;
import com.kekeinfo.core.business.data.service.HeMonitorDataService;
import com.kekeinfo.core.business.data.service.HiWellDataService;
import com.kekeinfo.core.business.data.service.HoWellDataService;
import com.kekeinfo.core.business.data.service.HpWellDataService;
import com.kekeinfo.core.business.deformmonitor.model.Deformmonitor;
import com.kekeinfo.core.business.dewatering.model.Dewatering;
import com.kekeinfo.core.business.dewatering.service.DewateringService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.model.PointEnumType;
import com.kekeinfo.core.business.invertedwell.model.Invertedwell;
import com.kekeinfo.core.business.invertedwell.service.InvertedwellService;
import com.kekeinfo.core.business.monitordata.model.HdewellData;
import com.kekeinfo.core.business.monitordata.model.HemonitorData;
import com.kekeinfo.core.business.monitordata.model.HiwellData;
import com.kekeinfo.core.business.monitordata.model.HowellData;
import com.kekeinfo.core.business.monitordata.model.HpwellData;
import com.kekeinfo.core.business.observewell.model.Observewell;
import com.kekeinfo.core.business.observewell.service.ObservewellService;
import com.kekeinfo.core.business.point.model.Basepoint;
import com.kekeinfo.core.business.point.model.BasepointLink;
import com.kekeinfo.core.business.point.service.PointService;
import com.kekeinfo.core.business.pumpwell.model.Pumpwell;
import com.kekeinfo.core.business.pumpwell.service.PumpwellService;
import com.kekeinfo.core.business.user.model.AppUser;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.UnderWater;
import com.kekeinfo.web.utils.FormulaService;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.base.DataModal;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.ip.tcp.TcpSlaveServer;
import com.serotonin.modbus4j.sero.util.ResultConstant;

/**
 * modbus监听类
 * 监听网关的握手请求，保留socket链接
 * 定时遍历测点数据，根据测点映射关系，通过socket连接，向网关获取数据
 * @author sam
 *
 */
@Component
public class ModbusListener {
//	private static final Log LOG = LogFactory.getLog(ModbusListener.class);
	private static final Logger LOG = LoggerFactory.getLogger(ModbusListener.class);
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	
	@Autowired HpWellDataService hpWellDataService;
	@Autowired HdeWellDataService hdeWellDataService;
	@Autowired HiWellDataService hiWellDataService;
	@Autowired HoWellDataService hoWellDataService;
	@Autowired HeMonitorDataService heMonitorDataService;
	@Autowired PointService pointService;
	@Autowired CSiteService csiteService;
	@Autowired PNodeUtils pNodeUtils;
	@Autowired PumpwellService pumpwellService;
	@Autowired DewateringService dwellService;
	@Autowired ObservewellService owellService;
	@Autowired InvertedwellService iwellService;
//	private static TcpHandSlave listener = start();
	private static TcpSlaveServer listener = start();
	//private static int power_interval = 0; //断电监测间隔计数
	//private static int gather_interval = 0; //数据采集间隔计数
	Map<Long, Integer> powerTimeMap = new HashMap<Long, Integer>(); //定义断电监测间隔计数Map
	Map<Long, Integer> gatherTimeMap = new HashMap<Long, Integer>(); //定义数据采集间隔计数Map
	
	private boolean projectasync = true; //是否对项目处理采用异步方式处理
	private boolean async = false; //是否对项目的测点采集监控采用异步方式处理

	/**
	 * 系统启动，启动网关握手监听进程
	 */
	private static synchronized TcpSlaveServer start() {
		if (listener==null) {
			IpParameters params = new IpParameters();
			params.setHost("localhost");
			params.setPort(5000);
			
			ModbusFactory modbusFactory = new ModbusFactory();
			listener = (TcpSlaveServer) modbusFactory.createTcpSlaveServer(5000, false);
			//启动线程，进行网关的握手监听
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						System.out.println("开始启动");
						listener.start();
					}
					catch (ModbusInitException e) {
						e.printStackTrace();
						LOG.error("启动modbus service监听进程失败！！", e);
					}
				}
			}).start();
			return listener;
		} else return listener;
	}
	
	@PreDestroy
	public void destory() {
		if (listener!=null) {
			listener.stop();
			listener = null;
			System.gc(); //手动触发回收垃圾
		}
	}
	
	/**
	 * 重新启动监听进程
	 * 为避免长时间有存在无效的连接，因此每个晚上0点，重新启动监听进程
	 */
	public void restart() {
		LOG.info("begin restarting modbus listener");
		try {
			destory();
			Thread.sleep(30000);
			listener = start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		LOG.info("finish restarting modbus listener");
	}

	/**
	 * 对测点进行启停控制
	 * @param point
	 * @return
	 * 0 成功；-11 失败； -12 没有网关连接； -13 连接失败； -14  没有配置网关信息
	 */
	@SuppressWarnings("rawtypes")
	public int modifyStart(Basepoint point,PointEnumType type) {
		LOG.debug("开始测点启停控制");
		BasepointLink link = point.getPointLink();
		int result = 0;
		if (link!=null&&link.getGateway()!=null && link.getChannel3()!=null && link.getNode3()!=null) {
			//修改测点的启停状态
			DataModal data = new DataModal();
			data.setBusinessName(point.getName());
			data.setBusinessId((Long) point.getId());
			data.setSerialNo(point.getPointLink().getGateway().getSerialno());
			data.setNode1(link.getNode3());
			data.setChannel1(link.getChannel3());
			
			int note = 41280; //0XA140
			int offset = data.getChannel1()*16*16;
			if (point.getPowerStatus().intValue()==0) { //启动 0XA140 0XFFFF
				LOG.debug("设备开启请求");
				result = listener.updateStartStatus(data, new short[]{(short) (note+offset),(short) 65535});
//				result = listener.updateStartStatus(data, new short[]{0XA1, 0X40, 0XFF, 0XFF});
			} else { //关闭 0XA140 0X0000 
				LOG.debug("设备关闭请求");
				result = listener.updateStartStatus(data, new short[]{(short) (note+offset),0});
			}
			LOG.debug("设备更新状态结果(0 成功；-11 失败； -12 没有网关连接； -13 连接失败； -14  没有配置网关信息)为"+result);
			//操作结果为0则保持状态
			if(result==ResultConstant.RESPONSE_STATUS_SUCCESS){
				try {
				switch (type){
				case PUMP:
						pumpwellService.update((Pumpwell)point);
						LOG.debug("设备状态入库成功，状态："+point.getPowerStatus());
					break;
				case DEWATERING:
					dwellService.update((Dewatering)point);
					break;
				case INVERTED:
					iwellService.update((Invertedwell)point);
					break;
				case OBSERVE:
					owellService.update((Observewell)point);
					break;
				default:
						break;
				}
				
				
				} catch (ServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
			}
			return result;
		}
		LOG.debug("结束测点启停控制");
		//没有配置网关，无法远程更改测点状态
		return ResultConstant.RESPONSE_STATUS_NOCONFIG;
	}
	
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
							flag = this.modifyStart(p,PointEnumType.PUMP);
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
								 flag = this.modifyStart(p,PointEnumType.DEWATERING);
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
								 flag = this.modifyStart(p,PointEnumType.OBSERVE);
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
								flag = this.modifyStart(p,PointEnumType.INVERTED);
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
	
	/**
	 * 根据项目分开进行自动化采集监听操作
	 * 根据读取各自项目配置间隔时间进行定时操作
	 * @throws ServiceException 
	 */
	public void autoControllByProjects() throws ServiceException {
		@SuppressWarnings("unchecked")
		List<UnderWater> cs =(List<UnderWater> )webCacheUtils.getFromCache(Constants.WATER_CSITE);
		//Set<ConstructionSite> cstieSet = waterDept.getcSites();
		for (UnderWater uw : cs) {
			if (projectasync) {
				new Thread(new Runnable() {

					@SuppressWarnings("unchecked")
					@Override
					public void run() {
						//处理断电监控
						Integer powerTime = uw.getMonitorPower();
						Integer power_int = powerTimeMap.get(uw.getId());
						if (power_int==null) {
							power_int = 0;
						}
						if (powerTime==null) {
							powerTime = 10;
						}
						LOG.info("断电监测循环，项目："+uw.getName()+"，采集频率powerTime="+powerTime+",计数器：power_int="+power_int);
						if (power_int==powerTime) {
							power_int=0;
							if (async) { //异步处理
								new Thread(new Runnable() {
									@Override
									public void run() {
										startMonitorPowerByCsite(uw);
									}
								}).start();
							} else {
								startMonitorPowerByCsite(uw);
							}
						} if (power_int>powerTime) {
							power_int=1; //复原
						} else  {
							power_int++;
						}
						powerTimeMap.put(uw.getId(), power_int);
						
						//处理数据采集
						Integer gatherTime = uw.getGatherData();
						Integer gather_int = gatherTimeMap.get(uw.getId());
						if (gather_int==null) {
							gather_int = 0;
						}
						if (gatherTime==null) {
							gatherTime = 10;
						}
						LOG.info("数据采集循环，项目："+uw.getName()+"，采集频率gatherTime="+gatherTime+",计数器：gather_int="+gather_int);
						if (gather_int==gatherTime) {
							gather_int=0;
							if (async) { //异步处理
								new Thread(new Runnable() {
									@Override
									public void run() {
										Set<AppUser> auses=null;
										try {
											HashMap<Long, Set<AppUser>> umaps =(HashMap<Long, Set<AppUser>>)webCacheUtils.getFromCache(Constants.USER_WARNING);
											auses=umaps.get(uw.getPid());
										} catch (ServiceException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
										startGatherByCsite(uw,auses);
									}
								}).start();
							} else {
								Set<AppUser> auses=null;
								try {
									HashMap<Long, Set<AppUser>> umaps =(HashMap<Long, Set<AppUser>>)webCacheUtils.getFromCache(Constants.USER_WARNING);
									auses=umaps.get(uw.getPid());
								} catch (ServiceException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								startGatherByCsite(uw,auses);
							}
						} if (gather_int>gatherTime) {
							gather_int=1;
						} else  {
							gather_int++;
						}
						gatherTimeMap.put(uw.getId(), gather_int);
					}
					
				}).start();
			}
			
		}
	}
	/**
	 * 采集指定项目下的测点数据信息
	 * @param csite
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void startGatherByCsite(UnderWater uw,Set<AppUser> aus) {
		LOG.debug("开始["+uw.getName()+"]定时处理");
		try {
			this.openOrShut();
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (ServiceException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		LOG.debug("开始["+uw.getName()+"]自动化数据采集");
		try {
			//获取所有的自动开启关闭相关的测点的id
			Map<PointEnumType,List<Long>> mbps =this.getAutoDeep();
			Map<PointEnumType,List<Object>> oms = new HashMap<>();
			for (PointEnumType c : PointEnumType.values()) {  
				//获取每种测点类型配置的网关节点信息
				List<Basepoint> plist = pointService.getListForGather(uw.getId(), c);
				//采集测点监控数据
				List presult = readDatas(plist, c);
				//保存采集到的测点监控数据
				List<Object> os=saveDatas(presult, c,uw.getName(),aus,mbps.get(c));
				if(c.getType()!="DEFORM"){
					oms.put(c, os);
				}
	        } 
			//自动开启设置
			for(PointEnumType e:PointEnumType.values()){
				if(e.getType()!="DEFORM"){
					List<Basepoint> bps =(List<Basepoint>)webCacheUtils.getFromCache("autodeep"+e.toString().toLowerCase());
					if(bps!=null && bps.size()>0){
						for(Basepoint b:bps){
							List<Object> ob=oms.get(b.getAtype());
							if(ob!=null && ob.size()>0){
								for (Object obj : ob) {
									pNodeUtils.wellOpenShut(b,obj,e);
								}
							}
						}
					}
				}
			}
		} catch (ServiceException e1) {
			e1.printStackTrace();
			LOG.error("数据采集失败:"+e1.getMessage());
		}
		LOG.debug("结束["+uw.getName()+"]自动化数据采集");
	}

	/**
	 * 监听指定项目下的测点断电情况
	 * @param csite
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void startMonitorPowerByCsite(UnderWater uw) {
		LOG.debug("开始监听["+uw.getName()+"]降水井、疏干井、回灌井断电告警信息");
		//从缓存获取所有的告警通知用户
		 Set<AppUser> auses=null;
		try {
			HashMap<Long, Set<AppUser>> umaps =(HashMap<Long, Set<AppUser>>)webCacheUtils.getFromCache(Constants.USER_WARNING);
			auses=umaps.get(uw.getPid());
		} catch (ServiceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			//获取所有降水井
			List<Basepoint> plist = pointService.getListForGather(uw.getId(), PointEnumType.PUMP);
			List presult = readPowerStatus(plist);
			if (presult!=null && presult.size()>0) {
				for (Object object : presult) { //更新状态信息（断电、即故障）
//					pointService.update((Basepoint<BasepointLink<?>, BasepointInfo<?>>) object);
					pointService.updatePowerstatus((Basepoint) object, PointEnumType.PUMP,auses,uw.getName());
				}
			}
			
			//获取所有疏干井
			List<Basepoint> dlist = pointService.getListForGather(uw.getId(), PointEnumType.DEWATERING);
			List dresult = readPowerStatus(dlist);
			if (dresult!=null && dresult.size()>0) {
				for (Object object : dresult) { //更新状态信息（断电、即故障）
//					pointService.update((Basepoint<BasepointLink<?>, BasepointInfo<?>>) object);
					pointService.updatePowerstatus((Basepoint) object, PointEnumType.DEWATERING,auses,uw.getName());
				}
			}
			//获取所有回灌井
			List<Basepoint> ilist = pointService.getListForGather(uw.getId(), PointEnumType.INVERTED);
			List iresult = readPowerStatus(ilist);
			if (iresult!=null && iresult.size()>0) {
				for (Object object : dresult) { //更新状态信息（断电、即故障）
//					pointService.update((Basepoint<BasepointLink<?>, BasepointInfo<?>>) object);
					pointService.updatePowerstatus((Basepoint) object, PointEnumType.INVERTED,auses,uw.getName());
				}
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		LOG.debug("结束监听["+uw.getName()+"]降水井、疏干井、回灌井断电告警信息");
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<PointEnumType,List<Long>> getAutoDeep() throws ServiceException{
		Map<PointEnumType,List<Long>> mbps =new HashMap<>();
		for(PointEnumType e:PointEnumType.values()){
			if(e.getType()!="DEFORM"){
				List<Basepoint> bps =(List<Basepoint>)webCacheUtils.getFromCache("autodeep"+e.toString().toLowerCase());
				mbps=this.getAutoClass(mbps, bps);
			}
		}
		return mbps;
	}
	
	@SuppressWarnings({ "rawtypes" })
	private Map<PointEnumType,List<Long>> getAutoClass(Map<PointEnumType,List<Long>>mbps,List<Basepoint> bps){
		if(bps!=null && bps.size()>0){
			for(Basepoint bp:bps){
				switch (bp.getAtype()){
					case PUMP:
						List<Long> blist=mbps.get(PointEnumType.PUMP);
						if(blist==null){
							blist=new ArrayList<>();
						}
						blist.add(bp.getAutoID());
						mbps.put(PointEnumType.PUMP, blist);
						break;
					case DEWATERING:
						List<Long> blist1=mbps.get(PointEnumType.DEWATERING);
						if(blist1==null){
							blist1=new ArrayList<>();
						}						
						blist1.add(bp.getAutoID());
						mbps.put(PointEnumType.DEWATERING, blist1);
						break;
					case INVERTED:
						List<Long> blist2=mbps.get(PointEnumType.INVERTED);
						if(blist2==null){
							blist2=new ArrayList<>();
						}
						blist2.add(bp.getAutoID());
						mbps.put(PointEnumType.INVERTED, blist2);
						break;
					case OBSERVE:
						List<Long> blist3=mbps.get(PointEnumType.OBSERVE);
						if(blist3==null){
							blist3=new ArrayList<>();
						}
						blist3.add(bp.getAutoID());
						mbps.put(PointEnumType.OBSERVE, blist3);
						break;
					default:
						break;
				}
			}
		}
		return mbps;
	}
	/**
	 * 监测数据保存
	 * @param presult
	 * @param type
	 */
	@SuppressWarnings("rawtypes")
	private List<Object> saveDatas(List presult, PointEnumType type,String pname,Set<AppUser> aus,List<Long> ids) {
		List<Object> obs =new ArrayList<>();
		if (presult!=null && presult.size()>0) {
			switch (type) {
			case PUMP:
				for (Object obj : presult) {
					HpwellData data = (HpwellData) obj;
					try {
						hpWellDataService.saveOrUpdate(data, data.getpWell(),pname,aus);
						//更新缓存
						pNodeUtils.setstatusByCid(data.getpWell().getcSite().getId());
						if(ids!=null){
							for(Long id:ids){
								if(id.equals(data.getpWell().getId())){
									obs.add(data);
									break;
								}
							}
						}
					} catch (ServiceException e) {
						e.printStackTrace();
						LOG.error("测点的监控数据保存失败，point.name="+data.getpWell().getName());
					}
				}
				break;
			case DEWATERING:
				for (Object obj : presult) {
					HdewellData data = (HdewellData) obj;
					try {
						hdeWellDataService.saveOrUpdate(data, data.getDeWell(),pname,aus);
						//更新缓存
						pNodeUtils.setstatusByCid(data.getDeWell().getcSite().getId());
						if(ids!=null){
							for(Long id:ids){
								if(id.equals(data.getDeWell().getId())){
									obs.add(data);
									break;
								}
							}
						}
					} catch (ServiceException e) {
						e.printStackTrace();
						LOG.error("测点的监控数据保存失败，point.name="+data.getDeWell().getName());
					}
				}
				break;
			case INVERTED:
				for (Object obj : presult) {
					HiwellData data = (HiwellData) obj;
					try {
						hiWellDataService.saveOrUpdate(data, data.getiWell(),pname,aus);
						//更新缓存
						pNodeUtils.setstatusByCid(data.getiWell().getcSite().getId());
						if(ids!=null){
							for(Long id:ids){
								if(id.equals(data.getiWell().getId())){
									obs.add(data);
									break;
								}
							}
						}
					} catch (ServiceException e) {
						e.printStackTrace();
						LOG.error("测点的监控数据保存失败，point.name="+data.getiWell().getName());
					}
				}
				break;
			case OBSERVE:
				for (Object obj : presult) {
					HowellData data = (HowellData) obj;
					try {
						hoWellDataService.saveOrUpdate(data, data.getoWell(),pname,aus);
						//更新缓存
						pNodeUtils.setstatusByCid(data.getoWell().getcSite().getId());
						if(ids!=null){
							for(Long id:ids){
								if(id.equals(data.getoWell().getId())){
									obs.add(data);
									break;
								}
							}
						}
					} catch (ServiceException e) {
						e.printStackTrace();
						LOG.error("测点的监控数据保存失败，point.name="+data.getoWell().getName());
					}
				}
				break;
			case DEFORM:
				for (Object obj : presult) {
					HemonitorData data = (HemonitorData) obj;
					try {
						heMonitorDataService.saveOrUpdate(data, data.getEmonitor(),pname,aus);//更新缓存
						pNodeUtils.setstatusByCid(data.getEmonitor().getcSite().getId());
					} catch (ServiceException e) {
						e.printStackTrace();
						LOG.error("测点的监控数据保存失败，point.name="+data.getEmonitor().getName());
					}
				}
				break;
			default:
				break;
			}
		}
		return obs;
	}

	/**
     * 获取节点监测数据
     * @param listener
     * @param plist
     * @return
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List readDatas(List<Basepoint> plist, PointEnumType type) {
		if (listener==null) {
			LOG.info("暂时没有modbus连接数");
			return null;
		}
		List result = new ArrayList();
		if (plist!=null && plist.size()>0) {
			switch (type) {
			case PUMP:
				for (Basepoint pwell : plist) {
					Pumpwell pump = (Pumpwell) pwell;
					BasepointLink dataconf = pwell.getPointLink();
					if (dataconf==null || dataconf.getGateway()==null) {
						continue;
					}
					DataModal data = new DataModal();
					data.setBusinessId(pump.getId());
					data.setBusinessName(pump.getName());
					data.setSerialNo(dataconf.getGateway().getSerialno());
					data.setNode1(dataconf.getNode1());
					data.setNode2(dataconf.getNode2());
					data.setChannel1(dataconf.getChannel1());
					data.setChannel2(dataconf.getChannel2());
					LOG.debug(data.getBaseinfo());
					//发送数据采集请求
					BatchResults<String> datas = listener.getDatas(data);
					if (datas==null) continue;
					Long _d1 = (Long) datas.getValue("node1");
					Long _d2 = (Long) datas.getValue("node2");
					Double d1 = null, d2 = null;
					if (_d1!=null) {
						d1 = calValue(_d1, pump.getFormula1());
					}
					
					if (_d2!=null) {
						d2 = calValue(_d2, pump.getFormula2());
					}
					HpwellData pData = new HpwellData();
					//处理测点状态（是否数据告警）
					int status = 0;
					if (d1!=null) {
						pData.setFlow(new BigDecimal(d1));
						if( pData.getFlow().compareTo(pump.getFlow())==1){
							status=1;
						}
					} else {
						pData.setFlow(null);
					}
					if (d2!=null) {
						pData.setWater(new BigDecimal(d2));
						if(pData.getWater().compareTo(pump.getWater())==1 || pData.getWater().compareTo(pump.getWaterDwon())==-1){
							status+=2;
						}
					} else {
						pData.setWater(null);
					}
					pData.setStatus(status);
					pData.setFlowThreshold(pump.getFlow());
					pData.setWaterThreshold(pump.getWater());
					pData.setWaterDown(pump.getWaterDwon());
					pData.setmAuto(true);
					
					pump.setrFlow(pData.getFlow());
					pump.setrWater(pData.getWater());
					pData.setpWell(pump);
					
					result.add(pData);
				}
				break;
			case DEWATERING:
				for (Basepoint pwell : plist) {
					Dewatering well = (Dewatering) pwell;
					BasepointLink dataconf = pwell.getPointLink();
					if (dataconf==null || dataconf.getGateway()==null) {
						continue;
					}
					DataModal data = new DataModal();
					data.setBusinessId(well.getId());
					data.setBusinessName(well.getName());
					data.setSerialNo(dataconf.getGateway().getSerialno());
					data.setNode1(dataconf.getNode1());
					data.setNode2(dataconf.getNode2());
					data.setChannel1(dataconf.getChannel1());
					data.setChannel2(dataconf.getChannel2());
					LOG.debug(data.getBaseinfo());
					//发送数据采集请求
					BatchResults<String> datas = listener.getDatas(data);
					if (datas==null) continue;
					Long _d1 = (Long) datas.getValue("node1");
					Long _d2 = (Long) datas.getValue("node2");
					Double d1 = null, d2 = null;
					if (_d1!=null) {
						d1 = calValue(_d1, well.getFormula1());
					}
					
					if (_d2!=null) {
						d2 = calValue(_d2, well.getFormula2());
					}
					
					HdewellData pData = new HdewellData();
					//处理测点状态（是否数据告警）
					int status = 0;
					if (d1!=null) {
						pData.setFlow(new BigDecimal(d1));
						if( pData.getFlow().compareTo(well.getFlow())==1){
							status=1;
						}
					} else {
						pData.setFlow(null);
					}
					if (d2!=null) {
						pData.setWater(new BigDecimal(d2));
						if(pData.getWater().compareTo(well.getWater())==1 || pData.getWater().compareTo(well.getWaterDwon())==-1){
							status+=2;
						}
					} else {
						pData.setWater(null);
					}
					pData.setStatus(status);
					pData.setFlowThreshold(well.getFlow());
					pData.setWaterThreshold(well.getWater());
					pData.setWaterDown(well.getWaterDwon());
					pData.setmAuto(true);
					
					well.setrFlow(pData.getFlow());
					well.setrWater(pData.getWater());
					pData.setDeWell(well);
					
					result.add(pData);
				}
				break;
			case INVERTED:
				for (Basepoint pwell : plist) {
					Invertedwell well = (Invertedwell) pwell;
					BasepointLink dataconf = pwell.getPointLink();
					if (dataconf==null || dataconf.getGateway()==null) {
						continue;
					}
					DataModal data = new DataModal();
					data.setBusinessId(well.getId());
					data.setBusinessName(well.getName());
					data.setSerialNo(dataconf.getGateway().getSerialno());
					data.setNode1(dataconf.getNode1());
					data.setNode2(dataconf.getNode2());
					data.setChannel1(dataconf.getChannel1());
					data.setChannel2(dataconf.getChannel2());
					LOG.debug(data.getBaseinfo());
					//发送数据采集请求
					BatchResults<String> datas = listener.getDatas(data);
					if (datas==null) continue;
					Long _d1 = (Long) datas.getValue("node1");
					Long _d2 = (Long) datas.getValue("node2");
					Double d1 = null, d2 = null;
					if (_d1!=null) {
						d1 = calValue(_d1, well.getFormula1());
					}
					
					if (_d2!=null) {
						d2 = calValue(_d2, well.getFormula2());
					}
					HiwellData pData = new HiwellData();
					//处理测点状态（是否数据告警）
					
					int status = 0;
					if (d1!=null) {
						pData.setFlow(new BigDecimal(d1));
						if( pData.getFlow().compareTo(well.getFlow())==1){
							status=1;
						}
					} else {
						pData.setFlow(null);
					}
					if (d2!=null) {
						pData.setPressure(new BigDecimal(d2));
						if(pData.getPressure().compareTo(well.getPressure())==1){
							status+=2;
						}
					} else {
						pData.setPressure(null);
					}
					pData.setStatus(status);
					pData.setFlowThreshold(well.getFlow());
					pData.setPressureThreshold(well.getPressure());
					pData.setmAuto(true);
					
					well.setrFlow(pData.getFlow());
					well.setrPressure(pData.getPressure());
					pData.setiWell(well);
					
					result.add(pData);
				}
				break;
			case OBSERVE:
				for (Basepoint pwell : plist) {
					Observewell well = (Observewell) pwell;
					BasepointLink dataconf = pwell.getPointLink();
					if (dataconf==null || dataconf.getGateway()==null) {
						continue;
					}
					DataModal data = new DataModal();
					data.setBusinessId(well.getId());
					data.setBusinessName(well.getName());
					data.setSerialNo(dataconf.getGateway().getSerialno());
					data.setNode1(dataconf.getNode1());
					data.setNode2(dataconf.getNode2());
					data.setChannel1(dataconf.getChannel1());
					data.setChannel2(dataconf.getChannel2());
					LOG.debug(data.getBaseinfo());
					//发送数据采集请求
					BatchResults<String> datas = listener.getDatas(data);
					if (datas==null) continue;
					Long _d1 = (Long) datas.getValue("node1");
					Long _d2 = (Long) datas.getValue("node2");
					Double d1 = null, d2 = null;
					if (_d1!=null) {
						d1 = calValue(_d1, well.getFormula2());
					}
					
					if (_d2!=null) {
						d2 = calValue(_d2, well.getFormula3());
					}
					HowellData pData = new HowellData();
					//处理测点状态（是否数据告警）
					
					int status = 0;
					if (d1!=null) {
						pData.setWater(new BigDecimal(d1));
						if(pData.getWater().compareTo(well.getWaterMeasurement())==1 || pData.getWater().compareTo(well.getWaterDwon())==-1){
							status=1;
						}
					} else {
						pData.setWater(null);
					}
					if (d2!=null) {
						pData.setTemperature(new BigDecimal(d2));
						if(pData.getTemperature().compareTo(well.getWaterTemperature())==1){
							status+=2;
						}
					} else {
						pData.setTemperature(null);
					}
					pData.setStatus(status);
					pData.setWaterThreshold(well.getWaterMeasurement());
					pData.setWaterDwon(well.getWaterDwon());
					pData.setTemperatureThreshold(well.getWaterTemperature());
					pData.setmAuto(true);
					
					well.setrWater(pData.getWater());
					well.setrTemperature(pData.getTemperature());
					pData.setoWell(well);
					result.add(pData);
				}
				break;
			case DEFORM:
				for (Basepoint pwell : plist) {
					Deformmonitor well = (Deformmonitor) pwell;
					BasepointLink dataconf = pwell.getPointLink();
					if (dataconf==null || dataconf.getGateway()==null) {
						continue;
					}
					DataModal data = new DataModal();
					data.setBusinessId(well.getId());
					data.setBusinessName(well.getName());
					data.setSerialNo(dataconf.getGateway().getSerialno());
					data.setNode1(dataconf.getNode1());
					data.setChannel1(dataconf.getChannel1());
					LOG.debug(data.getBaseinfo());
					//发送数据采集请求
					BatchResults<String> datas = listener.getDatas(data);
					if (datas==null) continue;
					Long _d1 = (Long) datas.getValue("node1");
					Double d1 = null;
					if (_d1!=null) {
						d1 = calValue(_d1, well.getFormula1());
					}
					
					HemonitorData pData = new HemonitorData();
					//处理测点状态（是否数据告警）
					
					int status = 0;
					if (d1!=null) {
						pData.setData(new BigDecimal(d1));
						if(pData.getData().compareTo(well.getDeformData())==1){
							status=3;
						}
					} else {
						pData.setData(null);
					}
					pData.setStatus(status);
					pData.setThreshold(well.getDeformData());
					well.setlData(well.getrData());
					well.setrData(pData.getData());
					
					pData.setEmonitor(well);
					result.add(pData);
				}
				break;
			default:
				break;
			}
		} else {
			LOG.info("没有配置测点采集信息，测点类型："+type.getName());
		}
		return result;
	}
	
	private Double calValue(Long d1, String formula) {
		Double result = null;
		Map<String, Object> param = new HashMap<String, Object>();
		//值处理,例如：C1 03 00 01, 0-1字符表示标识，2-3标识小数位数，4-7表示数值，小数点位数不做处理
		String str1 = Long.toHexString(d1);
		int l = str1.length();
//		str1 = PreciseComputeUtils.lpad(8, str1, "0");
		if (l==8) {
//			Integer _precise = Integer.valueOf(str1.substring(2, 4));
			String _val = str1.substring(4);
			Integer val = Integer.parseInt(_val, 16); 
//			Double precise = Math.pow(10, _precise);
//			result = val/precise;
			result = Double.valueOf(val);
			LOG.debug("采集数据值: "+d1+" 16进制表示结果："+str1+"，计算结果值："+result);
			
			if (StringUtils.isNotBlank(formula)) {
				param.put("X", result);
				try {
					result = FormulaService.getInstance().execute(param, formula.toUpperCase()).doubleValue();
					LOG.debug("计算公式表达式"+formula+"，计算公式转换值:"+result);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return result;
		} else {
			LOG.error("采集数据值: "+d1+" 16进制表示结果："+str1+"，采集数据格式不正确");
			return 0d;
		}
	}

	/**
	 * 读取测点断电情况
	 * @param plist
	 * @param type
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List readPowerStatus(List<Basepoint> plist) {
		List result = new ArrayList();
		if (plist!=null && plist.size()>0) {
			for (Basepoint well : plist) {
				BasepointLink dataconf = well.getPointLink();
				if (dataconf!=null && dataconf.getGateway()!=null && dataconf.getNode4()!=null && dataconf.getChannel4()!=null) {
					DataModal data = new DataModal();
					data.setBusinessId((Long) well.getId());
					data.setBusinessName(well.getName());
					data.setSerialNo(dataconf.getGateway().getSerialno());
					data.setNode1(dataconf.getNode4());
					data.setChannel1(dataconf.getChannel4());
					//发送数据采集请求
					BatchResults<String> datas = listener.readPowerStatus(data);
					if (datas!=null) {
						Long _d1 = datas.getLongValue("node1");
						//值处理,例如：A1 40 FF FF, 0-1字符表示标识，4-7表示开启FFFF/关闭0000
						String str1 = Long.toHexString(_d1);
						String _val = str1.substring(4);
						LOG.debug("采集断电值: "+_d1+" 16进制表示结果："+str1);
						
						//判断是否断电（故障）
						if (_val.equalsIgnoreCase("0000")) { //断电(故障）
							if (well.getPowerStatus().intValue()!=1 && well.getAutoStatus().intValue()==0) { //如果测点原本是关闭状态，则不修改其断电
								well.setPowerStatus(2);
							}
						} else if(_val.equalsIgnoreCase("ffff") && well.getAutoStatus().intValue()==0){ //正常
							well.setPowerStatus(0);
						}
						result.add(well);
					}
				}
			}
		}
		return result;
	}
}
