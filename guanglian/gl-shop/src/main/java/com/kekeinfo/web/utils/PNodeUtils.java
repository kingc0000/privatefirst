package com.kekeinfo.web.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kekeinfo.core.business.camera.model.Camera;
import com.kekeinfo.core.business.common.model.BasedataType;
import com.kekeinfo.core.business.constructionsite.model.ConstructionSite;
import com.kekeinfo.core.business.csite.service.CSiteService;
import com.kekeinfo.core.business.department.model.Department;
import com.kekeinfo.core.business.department.model.DepartmentNode;
import com.kekeinfo.core.business.department.service.DepartmentService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.model.PointEnumType;
import com.kekeinfo.core.business.guard.model.Guard;
import com.kekeinfo.core.business.guard.service.GuardService;
import com.kekeinfo.core.business.job.model.GJob;
import com.kekeinfo.core.business.job.service.GJobService;
import com.kekeinfo.core.business.monitor.model.Monitor;
import com.kekeinfo.core.business.monitor.service.MonitorService;
import com.kekeinfo.core.business.monitordata.model.HdewellData;
import com.kekeinfo.core.business.monitordata.model.HiwellData;
import com.kekeinfo.core.business.monitordata.model.HowellData;
import com.kekeinfo.core.business.monitordata.model.HpwellData;
import com.kekeinfo.core.business.point.model.Basepoint;
import com.kekeinfo.core.business.point.service.PointService;
import com.kekeinfo.core.business.project.model.Project;
import com.kekeinfo.core.business.project.service.ProjectService;
import com.kekeinfo.core.business.user.model.AppUser;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.user.service.UserService;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.BaseEntity;
import com.kekeinfo.web.entity.GuardEntity;
import com.kekeinfo.web.entity.MonitorEntity;
import com.kekeinfo.web.entity.UnderWater;
import com.kekeinfo.web.event.SpringContextUtils;
import com.kekeinfo.web.services.controller.system.ModbusListener;

@Component
public class PNodeUtils {
	
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private CSiteService cSiteService;
	@Autowired
	private MonitorService monitorService;
	@Autowired
	private GuardService guardService;
	@Autowired ProjectService projectService;
	@Autowired UserService userService;
	@Autowired GJobService gJobService;
	@Autowired PointService pointService;
	@Autowired ModbusListener modbusListener;
	private static final Logger LOGGER = LoggerFactory.getLogger(PNodeUtils.class);
	/**
	 * 加载部门+项目+摄像头集合
	 * @param context
	 * @param departmentService
	 * @throws ServiceException
	 */
	public void reloadAllDepartments() throws ServiceException{
		List<Department> list = departmentService.listWithCite();
		webCacheUtils.putInCache(Constants.DEPARTMENTS, list);
		for(Department d:list){
			if(d.getCode().equalsIgnoreCase(Constants.DEPARTMENT_WATER)) {
				webCacheUtils.putInCache(Constants.DEPARTMENT_WATER, d);
			} else if(d.getCode().equalsIgnoreCase(Constants.DEPARTMENT_MONITOR)) {
				webCacheUtils.putInCache(Constants.DEPARTMENT_MONITOR, d);
			}
		}
	}
	
	public void reloadAllUserJobs() throws ServiceException{
		//找到未完成的工作安排
				List<GJob> jobs =gJobService.getEndDate(new Date());
				Map<Long,List<GJob>> ujmaps =new HashMap<>();
				if(jobs!=null && jobs.size()>0){
					for(GJob job:jobs){
						Set<User> users =job.getUsers();
						if(users!=null && users.size()>0){
							for(User user:users){
								List<GJob> js =ujmaps.get(user.getId());
								if(js==null){
									js=new ArrayList<>();
								}
								js.add(job);
								ujmaps.put(user.getId(), js);
							}
						}
					}
				}
				webCacheUtils.putInCache(Constants.USERGJOBS, ujmaps);
	}
	
	@SuppressWarnings("rawtypes")
	public void reloadAllAuto() throws ServiceException{
		//加载自动执行的数据
		for(PointEnumType e:PointEnumType.values()){
			if(e.getType()!="DEFORM"){
				List<Basepoint> bps =pointService.getListForAuto(e);
				if(bps!=null && bps.size()>0){
					//之前在运行的先关掉
					for(Basepoint b:bps){
						if(b.getPowerStatus().intValue()==0){
							b.setContinMin(b.getConMin().longValue());
						}
					}
					webCacheUtils.putInCache("auto"+e.toString().toLowerCase(), bps);
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void reloadAllAutoDeep() throws ServiceException{
		//加载自动执行的数据
		for(PointEnumType e:PointEnumType.values()){
			if(e.getType()!="DEFORM"){
				List<Basepoint> bps =pointService.getListForAutoDeep(e);
				if(bps!=null && bps.size()>0){
					webCacheUtils.putInCache("autodeep"+e.toString().toLowerCase(), bps);
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void comparePoint(Basepoint b,PointEnumType type,BigDecimal dwater){
		boolean change=false;
		switch (type){
			case PUMP:
				//关闭
				if(b.getCloseDepp().compareTo(dwater)==-1 && b.getPowerStatus().intValue()!=1){
					b.setPowerStatus(1);
					change=true;
					//开启	
				}else if(dwater.compareTo(b.getOpenDepp())==1 && b.getPowerStatus().intValue()!=0){
					b.setPowerStatus(0);
					change=true;
				}
				if(change==true){
					try{
						 modbusListener.modifyStart(b,type);
					}catch (Exception e2){
						e2.printStackTrace();
					}
				}
				break;
			case DEWATERING:
				//关闭
				if(b.getCloseDepp().compareTo(dwater)==-1 && b.getPowerStatus().intValue()!=1){
					b.setPowerStatus(1);
					change=true;
					//开启	
				}else if(dwater.compareTo(b.getOpenDepp())==1 && b.getPowerStatus().intValue()!=0){
					b.setPowerStatus(0);
					change=true;
				}
				if(change==true){
					try{
						modbusListener.modifyStart(b,type);
						
					}catch (Exception e2){
						e2.printStackTrace();
					}
				}
				break;
			case INVERTED:
				//关闭
				if(dwater.compareTo(b.getCloseDepp())==-1 && b.getPowerStatus().intValue()!=1){
					b.setPowerStatus(1);
					change=true;
					//开启	
				}else if(dwater.compareTo(b.getOpenDepp())==1 && b.getPowerStatus().intValue()!=0){
					b.setPowerStatus(0);
					change=true;
				}
				if(change){
					try{
						 modbusListener.modifyStart(b,type);
						
					}catch (Exception e3){
						e3.printStackTrace();
					}
				}
				break;
			case OBSERVE:
				//关闭
				if(b.getCloseDepp().compareTo(dwater)==-1 && b.getPowerStatus().intValue()!=1){
					b.setPowerStatus(1);
					change=true;
					//开启	
				}else if(dwater.compareTo(b.getOpenDepp())==1 && b.getPowerStatus().intValue()!=0){
					b.setPowerStatus(0);
					change=true;
				}
				if(change){
					try{
						  modbusListener.modifyStart(b,type);
					}catch (Exception e2){
						e2.printStackTrace();
					}
				}
				break;
			default:
				break;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void wellOpenShut(Basepoint b,Object obj,PointEnumType e){
		switch (b.getAtype()){
		case PUMP:
			HpwellData data = (HpwellData) obj;
			if(data.getpWell().getId().equals(b.getAutoID())){
				this.comparePoint(b, e, data.getWater());
			}
			break;
		case DEWATERING:
			HdewellData ddata = (HdewellData) obj;
			if(ddata.getDeWell().getId().equals(b.getAutoID())){
				this.comparePoint(b, e, ddata.getWater());
			}
			break;
		case INVERTED:
			HiwellData idata = (HiwellData) obj;
			if(idata.getiWell().getId().equals(b.getAutoID())){
				this.comparePoint(b, e, idata.getPressure());
			}
			break;
		case OBSERVE:
			HowellData odata = (HowellData) obj;
			if(odata.getoWell().getId().equals(b.getAutoID())){
				LOGGER.debug("oooooooo"+odata.getoWell().getId());
				this.comparePoint(b, e, odata.getWater());
			}
			break;
		default:
			break;
	}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void OpenWithDeep(Long pid,Object obj,PointEnumType type) throws ServiceException{
		for(PointEnumType e:PointEnumType.values()){
			if(e.getType()!="DEFORM"){
				List<Basepoint> bps =(List<Basepoint>)webCacheUtils.getFromCache("autodeep"+e.toString().toLowerCase());
				if(bps!=null && bps.size()>0){
					for(Basepoint b:bps){
						if(b.getAtype().equals(type) && b.getAutoID().equals(pid)){
							this.wellOpenShut(b,obj,e);
							break;
						}
					}
				}
			}
		}
	}
	@SuppressWarnings("unchecked")
	public void reloadUserJobs(Long uid) throws ServiceException{
		//找到未完成的工作安排
				List<GJob> jobs =gJobService.getEndDate(new Date(), uid);
				Map<Long,List<GJob>> ujmaps =  (Map<Long, List<GJob>>) webCacheUtils.getFromCache(Constants.USERGJOBS);
				if(jobs!=null && jobs.size()>0){
					List<GJob> js =ujmaps.get(uid);
					if(js==null){
						js=new ArrayList<>();
					}
					for(GJob job:jobs){
						Set<User> users =job.getUsers();
						if(users!=null && users.size()>0){
							for(User user:users){
								if(user.getId().equals(uid)){
									//看有没有重复的
									boolean found=false;
									for(GJob djob:js){
										if(djob.getId().equals(job.getId())){
											found=true;
											break;
										}
									}
									if(!found){
										js.add(job);
									}
									break;
								}
							}
						}
					}
					ujmaps.put(uid, js);
				}
				webCacheUtils.putInCache(Constants.USERGJOBS, ujmaps);
	}
	/**
	 * 获取地下水项目对象
	 * @param cid
	 * @param cSiteService
	 * @return
	 * @throws ServiceException
	 */
	public UnderWater getByCid(Long cid) throws ServiceException{
		ConstructionSite c = cSiteService.getByIdWithDepartment(cid);
		UnderWater uw = copyUnderwater(c);
		return uw;
	}

	public UnderWater copyUnderwater(ConstructionSite c) {
		UnderWater uw = new UnderWater();
		uw.setCity(c.getProject().getCity());
		uw.setLatitude(c.getLatitude());
		uw.setLongitude(c.getLongitude());
		uw.setRail(c.getRail());
		uw.setSstatus(c.getProject().getSstatus());
		uw.setStatus(c.getStatus());
		uw.setId(c.getId());
		uw.setName(c.getProject().getName());
		uw.setPid(c.getProject().getId());
		uw.setDid(c.getProject().getDepartment().getId());
		uw.setZone(c.getProject().getZone().getName());
		uw.setCtype(c.getProject().getFeatures());
		uw.setRunstatus(c.getRunstatus());
		uw.setMonitorPower(c.getMonitorPower());
		uw.setGatherData(c.getGatherData());
		uw.setFeatures(c.getProject().getFeatures());
		if(c.getJianGong()!=null){
			uw.setJiangong(c.getJianGong());
		}else{
			uw.setJiangong(false);
		}
		return uw;
	}
	/**
	 * 获取监测项目
	 * @param mid
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public MonitorEntity getByMid(Long mid) throws ServiceException {
		List<MonitorEntity> list = (List<MonitorEntity>) webCacheUtils.getFromCache(Constants.PROEJCT_MONITOR);
		for(MonitorEntity m:list){
			if(m.getId().equals(mid)){
				return m;
			}
		}
		return null;
	}
	
	/**
	 * 获取监护项目
	 * @param gid
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public GuardEntity getByGid(Long gid) throws ServiceException {
		List<GuardEntity> list = (List<GuardEntity>) webCacheUtils.getFromCache(Constants.PROEJCT_GUARD);
		for(GuardEntity m:list){
			if(m.getId().equals(gid)){
				return m;
			}
		}
		return null;
	}
	
	/**
	 * 获取地下水项目对象
	 * @param cid
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	public UnderWater getByCid(Long cid, HttpServletRequest request) throws ServiceException{
		//加载项目集合
		HashMap<String, Set<UnderWater>> set  = getWaterCsitesZone(request);
		if(set!=null && set.size()>0){
			for(Set<UnderWater> sun:set.values()){
				for(UnderWater un:sun){
					if(un.getId().equals(cid)){
							return un;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 修改项目的运行状态
	 * @param cid
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	public void setstatusByCid(Long cid) throws ServiceException{
		@SuppressWarnings("unchecked")
		List<UnderWater> ps = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
		for(UnderWater p:ps){
			if(p.getId().equals(cid)){
				ConstructionSite csite =cSiteService.getByCid(cid);
				p.setRunstatus(csite.getRunstatus());
				break;
			}
		}
		
	}
	
	/**
	 * 加载所有的项目评论负责人，项目告警负责人
	 * @throws ServiceException 
	 */
	
	@SuppressWarnings("unchecked")
	public void reloadGroupUser(User user) throws ServiceException{
		HashMap<Long, Set<AppUser>>  cmaps =(HashMap<Long, Set<AppUser>>)webCacheUtils.getFromCache(Constants.USER_COMMENT);
		for (Entry<Long, Set<AppUser>> entry : cmaps.entrySet()) {
			Long key=entry.getKey();
			 Set<AppUser> us=entry.getValue();
			for(AppUser u:us){
				if(u.getId().equals(user.getId())){
					us.remove(u);
					us.add(this.setUser(user));
					break;
				}
			}
			cmaps.put(key, us);
		}
		webCacheUtils.putInCache(Constants.USER_COMMENT, cmaps);
		HashMap<Long, Set<AppUser>>  umaps =(HashMap<Long, Set<AppUser>>)webCacheUtils.getFromCache(Constants.USER_WARNING);
		for (Entry<Long, Set<AppUser>> entry : umaps.entrySet()) {
			Long key=entry.getKey();
			 Set<AppUser> us=entry.getValue();
			for(AppUser u:us){
				if(u.getId().equals(user.getId())){
					us.remove(u);
					us.add(this.setUser(user));
					break;
				}
			}
			umaps.put(key, us);
		}
		webCacheUtils.putInCache(Constants.USER_WARNING, umaps);
	}
	public void relaodAllGroupUser() throws ServiceException{
		HashMap<Long, Set<AppUser>>  cmaps =new HashMap<>();
		HashMap<Long, Set<AppUser>>  wmaps =new HashMap<>();
		List<Project> ap =projectService.withGroupUser();
		
		for(Project p:ap){
			//项目负责人
			User u=null;
			if(p.getProjectOwnerid()!=null){
				u=userService.getById(p.getProjectOwnerid());
			}			
			//评论负责人
			if(p.getcUsers()!=null && p.getcUsers().size()>0){
				Set<AppUser> cusers =new HashSet<AppUser>();
				for(User user:p.getcUsers()){
					cusers.add(this.setUser(user));
				}
				if(u!=null){
					cusers.add(this.setUser(u));
				}
				cmaps.put(p.getId(), cusers);
			}
			//告警负责人
			if(p.getwUsers()!=null && p.getwUsers().size()>0){
				Set<AppUser> cusers =new HashSet<AppUser>();
				for(User user:p.getwUsers()){
					cusers.add(this.setUser(user));
				}
				if(u!=null){
					cusers.add(this.setUser(u));
				}
				wmaps.put(p.getId(), cusers);
			}
		}
		webCacheUtils.putInCache(Constants.USER_COMMENT, cmaps); 
		webCacheUtils.putInCache(Constants.USER_WARNING, wmaps);
	}
	
	private AppUser setUser(User user){
		AppUser fuser = new AppUser();
		fuser.setDevice_token(user.getDevice_token());
		fuser.setId(user.getId());
		fuser.setuAgent(user.getAtype());
		return fuser;
	}
	
	/**
	 * 加载所有地下水部门的项目
	 * @param context
	 * @param request
	 * @param cSiteService
	 * @throws ServiceException
	 */
	public void reloadAllCsite(HttpServletRequest request) throws ServiceException{
		//保存所有的工地信息到内存
		List<ConstructionSite> cs = cSiteService.withDepartment();
		List<UnderWater> uws = new ArrayList<>();
		for(ConstructionSite c:cs){
			UnderWater uw = copyUnderwater(c);
			uws.add(uw);
		}
		webCacheUtils.putInCache(Constants.WATER_CSITE, uws); //地下水部门信息保存到cache
		if(request !=null){
			request.getSession().removeAttribute(Constants.USER_WATER_CSITE);
			request.getSession().removeAttribute(Constants.USER_ZONE_CSITE);
		}
	}
	/**
	 * 加载所有监控部门项目
	 * @param context
	 * @param request
	 * @param monitorService
	 * @throws ServiceException
	 */
	public void reloadAllMonitor() throws ServiceException{
		//保存所有的工地信息到内存
		List<Monitor> cs = monitorService.withProject();
		List<MonitorEntity> uws = new ArrayList<>();
		for(Monitor c:cs){
			MonitorEntity uw = new MonitorEntity();
			uw.setId(c.getId());
			uw.setName(c.getProject().getName());
			uw.setDid(c.getProject().getDepartment().getId());
			uw.setPid(c.getProject().getId());
			uw.setZone(c.getProject().getZone().getName());
			uw.setCtype(c.getProject().getFeatures());
			uw.setUid(c.getProject().getProjectOwnerid());
			uw.setFeatures(c.getProject().getFeatures());
			uws.add(uw);
		}
		webCacheUtils.putInCache(Constants.PROEJCT_MONITOR, uws);
	}
	
	/**
	 * 加载所有监护部门项目
	 * @param context
	 * @param request
	 * @param guardService
	 * @throws ServiceException
	 */
	public void reloadAllGuard() throws ServiceException{
		//保存所有的工地信息到内存
		List<Guard> cs = guardService.withProject();
		List<GuardEntity> uws = new ArrayList<>();
		for(Guard c:cs){
			GuardEntity uw = new GuardEntity();
			uw.setId(c.getId());
			uw.setName(c.getProject().getName());
			uw.setDid(c.getProject().getDepartment().getId());
			uw.setPid(c.getProject().getId());
			uw.setStation(c.getStation());
			uw.setAddress(c.getProject().getAddress());
			uw.setZone(c.getProject().getZone().getName());
			uw.setStatus(c.getStatus());
			uw.setOwner(c.getProject().getProjectOwner());
			uw.setCtype(c.getProject().getFeatures());
			uw.setUid(c.getProject().getProjectOwnerid());
			uw.setFeatures(c.getProject().getFeatures());
			uws.add(uw);
		}
		webCacheUtils.putInCache(Constants.PROEJCT_GUARD, uws); 
	}
	
	/**
	 * 判断用户是否有对应地下水部门的权限（查看，编辑）
	 */
	public boolean hasProjectRight(Set<DepartmentNode> departmentNodes,Department water){
		if(water.getCode().contentEquals(Constants.DEPARTMENT_WATER)){
			for(DepartmentNode dn: departmentNodes){
				if(dn.getDepartmentid().longValue()==-1 && dn.isAll()){
					return true;
				} else if (dn.getDepartmentid().equals(water.getId()) && dn.getType().equalsIgnoreCase("department")) {
					return true;
				} else if (dn.getDepartmentid().equals(water.getId()) && dn.getType().equalsIgnoreCase("features")) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * 判断用户是否有对应地下水部门的权限（查看，编辑）
	 * @throws ServiceException 
	 */
	@SuppressWarnings("unchecked")
	public boolean hasProjectRightMoiniter(Set<DepartmentNode> departmentNodes) throws ServiceException{
		Department water=(Department) webCacheUtils.getFromCache(Constants.DEPARTMENT_MONITOR);
			for(DepartmentNode dn: departmentNodes){
				if(dn.getDepartmentid().longValue()==-1 && dn.isAll()){
					return true;
				} else if (dn.getDepartmentid().equals(water.getId()) && dn.getType().equalsIgnoreCase("department")) {
					return true;
				} else if (dn.getDepartmentid().equals(water.getId()) && dn.getType().equalsIgnoreCase("features")) {
					return true;
				}else if (dn.getType().equalsIgnoreCase("csite")){
					List<MonitorEntity> list = (List<MonitorEntity>) webCacheUtils.getFromCache(Constants.PROEJCT_MONITOR);
					MonitorEntity u=null;
					for(MonitorEntity uw:list){
						if(dn.getDepartmentid().equals(uw.getPid())){
							u=uw;
							break;
						}
					}
					//ConstructionSite csite = cSiteService.getById(pnode.getDepartmentid());
					if(u!=null){
						if (u.getDid().equals(water.getId())) { //属于指定部门下的项目
							return true;
						}
					}
				}
			}
		
		return false;
	}
	/**
	 * 判断用户是否有对应地下水部门的权限（查看，编辑）
	 * @throws ServiceException 
	 */
	@SuppressWarnings("unchecked")
	public boolean hasProjectRightGuard(Set<DepartmentNode> departmentNodes) throws ServiceException{
		Department water=(Department) webCacheUtils.getFromCache(Constants.DEPARTMENT_MONITOR);
		//if(water.getCode().contentEquals(Constants.DEPARTMENT_MONITOR)){
			for(DepartmentNode dn: departmentNodes){
				if(dn.getDepartmentid().longValue()==-1 && dn.isAll()){
					return true;
				} else if (dn.getDepartmentid().equals(water.getId()) && dn.getType().equalsIgnoreCase("department")) {
					return true;
				} else if (dn.getDepartmentid().equals(water.getId()) && dn.getType().equalsIgnoreCase("features")) {
					return true;
				}else if (dn.getType().equalsIgnoreCase("csite")){
					List<GuardEntity> list = (List<GuardEntity>) webCacheUtils.getFromCache(Constants.PROEJCT_GUARD);
					GuardEntity u=null;
					for(GuardEntity uw:list){
						if(dn.getDepartmentid().equals(uw.getPid())){
							u=uw;
							break;
						}
					}
					//ConstructionSite csite = cSiteService.getById(pnode.getDepartmentid());
					if (u!=null && u.getDid().equals(water.getId())) { //属于指定部门下的项目
						return true;
					}
				}
			}
		//}
		
		return false;
	}
	
	/**
	 * 判断用户是否有对地下水部门的对应权限组权限
	 * @param cSiteService
	 * @param departmentNodes 用户权限组
	 * @param egroups 权限组（可以是编辑，或者是查看）
	 * @param water 地下水部门
	 * @return
	 * @throws ServiceException 
	 */
	@SuppressWarnings("unchecked")
	public boolean hasProjectRight(Set<DepartmentNode> departmentNodes, List<Group> egroups, Department water) throws ServiceException{
		if(water.getCode().contentEquals(Constants.DEPARTMENT_WATER)){
			for(DepartmentNode pnode: departmentNodes){
				if(pnode.getDepartmentid().longValue()==-1 && pnode.isAll() || 
						pnode.getDepartmentid().equals(water.getId()) && pnode.getType().equalsIgnoreCase("department") && pnode.isAll()){
					for(Group g:egroups){
						if(g.getId()==pnode.getGroupid()){
							return true;
						}
					}
				} else if (pnode.getType().equalsIgnoreCase("features") && StringUtils.isNotBlank(pnode.getFeatures()) 
						&& pnode.getDepartmentid().equals(water.getId()) && pnode.isAll()) {
					//工程特性
					for(Group g:egroups){
						if(g.getId()==pnode.getGroupid()){
							return true;
						}
					}
				} else if (pnode.getType().equalsIgnoreCase("csite")){
					List<UnderWater> ps = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
					UnderWater u=null;
					for(UnderWater uw:ps){
						if(pnode.getDepartmentid().equals(uw.getPid())){
							u=uw;
							break;
						}
					}
					//ConstructionSite csite = cSiteService.getById(pnode.getDepartmentid());
					if (u.getDid().equals(water.getId())) { //属于指定部门下的项目
						//项目类型 判断
						for(Group g:egroups){
							if(g.getId()==pnode.getGroupid()){
								return true;
							}
						}
					}
				}
			}
		}
		
		return false;
	}
	
	/**
	 * 判断用户对项目是否有编辑权限
	 * @param request
	 * @param egroups 具有部门项目编辑类型的权限组集合 
	 * @param cSite
	 * @return
	 */
	public boolean hasProjectRight(HttpServletRequest request,List<Group> egroups, Project csite){
		if(request.isUserInRole("ADMIN") || request.isUserInRole("SUPERADMIN")){
			return true;
		}
		User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
		Set<DepartmentNode> departmentNodes = user.getpNodes(); //用户所拥有的部门项目权限组
		if(departmentNodes!=null && departmentNodes.size()>0){
			for(DepartmentNode pnode:departmentNodes){
				if(pnode.getDepartmentid().longValue()==-1 && pnode.isAll() ||//所有权限
						pnode.getDepartmentid().equals(csite.getDepartment().getId()) && pnode.isAll() && pnode.getType().equalsIgnoreCase("department")){ //查看是否具有部门权限
					for(Group g:egroups){
						if(g.getId()==pnode.getGroupid()){
							return true;
						}
					}
				} else if (pnode.getType().equalsIgnoreCase("features") && StringUtils.isNotBlank(pnode.getFeatures()) 
						&& pnode.getDepartmentid().equals(csite.getDepartment().getId()) && pnode.isAll() && pnode.getFeatures().equals(csite.getFeatures())) {
					//工程特性，该特性下的项目都有权限
					for(Group g:egroups){
						if(g.getId()==pnode.getGroupid()){
							return true;
						}
					}
				} else if (pnode.getType().equalsIgnoreCase("csite") && pnode.getDepartmentid().equals(csite.getId()) ){
					//项目类型 判断
					for(Group g:egroups){
						if(g.getId()==pnode.getGroupid()){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 判断用户对项目是否有编辑权限
	 * @param request
	 * @param cSite
	 * @return
	 */
	public boolean hasProjectRight(HttpServletRequest request,List<Group> egroups, BaseEntity csite){
		if(request.isUserInRole("ADMIN") || request.isUserInRole("SUPERADMIN")){
			return true;
		}
		User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
		if(user!=null){
			Set<DepartmentNode> departmentNodes = user.getpNodes(); //用户所拥有的部门项目权限组
			if(departmentNodes!=null && departmentNodes.size()>0){
				for(DepartmentNode pnode:departmentNodes){
					if(pnode.getDepartmentid().longValue()==-1 && pnode.isAll()||//所有权限
							pnode.getDepartmentid().equals(csite.getDid()) && pnode.isAll() && pnode.getType().equalsIgnoreCase("department")){ //查看是否具有部门权限
						for(Group g:egroups){
							if(g.getId()==pnode.getGroupid()){
								return true;
							}
						}
					} else if (pnode.getType().equalsIgnoreCase("features") && StringUtils.isNotBlank(pnode.getFeatures()) 
							&& pnode.getDepartmentid().equals(csite.getDid()) && pnode.isAll() && pnode.getFeatures().equals(csite.getCtype())) {
						//工程特性，该特性下的项目都有权限
						for(Group g:egroups){
							if(g.getId()==pnode.getGroupid()){
								return true;
							}
						}
					} else if (pnode.getType().equalsIgnoreCase("csite") && pnode.getDepartmentid().equals(csite.getPid()) ){
						//项目类型 判断
						for(Group g:egroups){
							if(g.getId()==pnode.getGroupid()){
								return true;
							}
						}
					}
				}
			}
		}
		
		return false;
	}
	
	/**
	 * 根据部门ID，查找当前用户所能查看和编辑的项目ID集合
	 * @param request
	 * @param deparmentId
	 * @return 返回where条件数组， String[0]=id, String[1]=项目id字符串（逗号分隔符）
	 * 0:地下水
	 * 1:监护
	 * 2:监控
	 * @throws ServiceException 
	 */
	@SuppressWarnings("unchecked")
	public List<String>  getCids(HttpServletRequest request, Long deparmentId,int type) throws ServiceException{
		List<BaseEntity> cs = null;
			switch (type) {
			case 0:
				cs=(List<BaseEntity> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
				break;
			case 1:
				cs=(List<BaseEntity> ) webCacheUtils.getFromCache(Constants.PROEJCT_MONITOR);
				break;
			case 2:
				cs=(List<BaseEntity> ) webCacheUtils.getFromCache(Constants.PROEJCT_GUARD);
				break;
			default:
				break;
			}
		Set<Long> idSet = new HashSet<Long>();
		
		if(request.isUserInRole("ADMIN") || request.isUserInRole("SUPERADMIN")){
			return null;
		} else {
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			Set<DepartmentNode> departmentNodes = user.getpNodes();
			if(departmentNodes!=null && departmentNodes.size()>0){
				for(DepartmentNode pnode:departmentNodes){
					if (pnode.getDepartmentid().equals(-1L) && pnode.isAll() || pnode.getDepartmentid().equals(deparmentId) && pnode.isAll()) {
						//拥有部门下所有项目的权限
						return null;
					} else if (pnode.getType().equalsIgnoreCase("features") && pnode.isAll() && pnode.getDepartmentid().equals(deparmentId)) {
						//该部门下的对应工程特性的所有项目
						for (BaseEntity csite : cs) {
							if (csite.getFeatures().equals(pnode.getFeatures())) {
								idSet.add(csite.getPid());
							}
						}
					} else if(pnode.getType().equalsIgnoreCase("csite")){
						for (BaseEntity csite : cs) {
							if (csite.getPid().equals(pnode.getDepartmentid())) {
								idSet.add(csite.getPid());
							}
						}
					}
				}
			}
		}
		
		if(idSet.isEmpty()){
			List<String> where = new ArrayList<String>();
			where.add("project.id");
			where.add("-1");
			return where;
		}else{
			List<String> where = new ArrayList<String>();
			where.add("project.id");
			StringBuilder sbd = new StringBuilder("");
			for (Long cid : idSet) {
				sbd.append(cid.toString()).append(",");
			}
			sbd.deleteCharAt(sbd.lastIndexOf(","));
			where.add(sbd.toString());
			return where;
		}
		
	}
	
	public List<String>  getPids(HttpServletRequest request, Long deparmentId) throws ServiceException{
		
		Set<Long> idSet = new HashSet<Long>();
		
		List<Project> projects =null;
		if(request.isUserInRole("ADMIN") || request.isUserInRole("SUPERADMIN")){
			return null;
		} else {
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			Set<DepartmentNode> departmentNodes = user.getpNodes();
			if(departmentNodes!=null && departmentNodes.size()>0){
				for(DepartmentNode pnode:departmentNodes){
					if (pnode.getDepartmentid().equals(-1L) && pnode.isAll() || pnode.getDepartmentid().equals(deparmentId) && pnode.isAll()) {
						//拥有部门下所有项目的权限
						return null;
					} else if (pnode.getType().equalsIgnoreCase("features") && pnode.isAll() && pnode.getDepartmentid().equals(deparmentId)) {
						//该部门下的对应工程特性的所有项目
						if(projects==null) projects=projectService.list();
						for (Project csite : projects) {
							if (csite.getFeatures().equals(pnode.getFeatures())) {
								idSet.add(csite.getId());
							}
						}
					} else if(pnode.getType().equalsIgnoreCase("csite")){
						if(projects==null) projects=projectService.list();
						for (Project csite : projects) {
							if (csite.getId().equals(pnode.getDepartmentid())) {
								idSet.add(csite.getId());
							}
						}
					}
				}
			}
		}
		
		if(idSet.isEmpty()){
			List<String> where = new ArrayList<String>();
			where.add("project.id");
			where.add("-1");
			return where;
		}else{
			List<String> where = new ArrayList<String>();
			where.add("project.id");
			StringBuilder sbd = new StringBuilder("");
			for (Long cid : idSet) {
				sbd.append(cid.toString()).append(",");
			}
			sbd.deleteCharAt(sbd.lastIndexOf(","));
			where.add(sbd.toString());
			return where;
		}
		
	}
	
	/**
	 * 获取用户所拥有权限（查看和编辑）的部门id字符串
	 * @param request
	 * @return 返回null，则说明拥有所有部门的权限，返回List，则为部门id字符串（逗号分隔符）
	 * @throws ServiceException 
	 */
	@SuppressWarnings("unchecked")
	public List<String>  getDids(HttpServletRequest request) throws ServiceException{
		List<Department> departments = (List<Department>) webCacheUtils.getFromCache(Constants.DEPARTMENTS); //所有部门
		Set<Long> idSet = new HashSet<Long>();
		
		if(request.isUserInRole("ADMIN") || request.isUserInRole("SUPERADMIN")){
			return null;
		} else {
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			Set<DepartmentNode> departmentNodes = user.getpNodes();
			if(departmentNodes!=null && departmentNodes.size()>0){
				for(DepartmentNode pnode:departmentNodes){
					if (pnode.getDepartmentid().equals(-1L) && pnode.isAll()) {
						return null;
					} else if (pnode.getType().equalsIgnoreCase("department")) {
						for (Department d:departments) {
							if (d.getId().equals(pnode.getDepartmentid())) {
								idSet.add(d.getId());
							}
						}
					} 
				}
			}
		}
		
		if(idSet.isEmpty()){
			List<String> where = new ArrayList<String>();
			where.add("id");
			where.add("-1");
			return where;
		}else{
			List<String> where = new ArrayList<String>();
			where.add("id");
			StringBuilder sbd = new StringBuilder("");
			for (Long cid : idSet) {
				sbd.append(cid.toString()).append(",");
			}
			sbd.deleteCharAt(sbd.lastIndexOf(","));
			where.add(sbd.toString());
			return where;
		}
	}
	
	/**
	 * 判断该用户对部门集合的操作权限（编辑）
	 * @param request
	 * @param egroups 用户拥有的部门项目编辑权限组
	 * @param dps 用户所拥有的部门集合
	 * @return
	 */
	public List<Department> setEditDepartment (HttpServletRequest request,List<Group> egroups,List<Department> dps){
		
		Set<Long> idSet = new HashSet<Long>();
		
		if(request.isUserInRole("ADMIN") || request.isUserInRole("SUPERADMIN")){
			for (Department d:dps) {
				d.setSstatus(1);
			}
			return dps;
		} else {
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			Set<DepartmentNode> departmentNodes = user.getpNodes();
			if(departmentNodes!=null && departmentNodes.size()>0){
				for(DepartmentNode pnode:departmentNodes){
					if (pnode.getDepartmentid().equals(-1L) && pnode.isAll()) {
						for (Group group : egroups) { //判断是否属于编辑权限组中
							if (group.getId().equals(pnode.getGroupid())) {
								for (Department d:dps) {
									d.setSstatus(1);
								}
								return dps;
							}
						}
					} else if (pnode.getType().equalsIgnoreCase("department") && pnode.isAll()) {
						for (Group group : egroups) { //判断是否属于编辑权限组中
							if (group.getId().equals(pnode.getGroupid())) {
								idSet.add(pnode.getDepartmentid());
								break;
							}
						}
					} 
				}
			}
		}
		
		if(!idSet.isEmpty()){
			for(Department d:dps){
				for (Long id : idSet) {
					if (id.equals(d.getId())) {
						d.setSstatus(1);
						break;
					}
				}
			}
		}
		return dps;
	}
	
	/**
	 * 对应地下水项目，设置可编辑的csite的编辑状态
	 * @param request
	 * @param egroups 用户拥有的部门项目编辑权限组
	 * @param csits
	 * @return
	 */
	public List<ConstructionSite> setEditCiste (HttpServletRequest request,List<Group> egroups,List<ConstructionSite> csits){
		Set<Long> idSet = new HashSet<Long>();
		
		if(request.isUserInRole("ADMIN") || request.isUserInRole("SUPERADMIN")){
			for (ConstructionSite d:csits) {
				d.setSstatus(1);
			}
			return csits;
		} else {
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			Set<DepartmentNode> departmentNodes = user.getpNodes();
			if(departmentNodes!=null && departmentNodes.size()>0){
				for(DepartmentNode pnode:departmentNodes){
					if (pnode.getDepartmentid().equals(-1L) && pnode.isAll()) {
						for (Group group : egroups) { //判断是否属于编辑权限组中
							if (group.getId().equals(pnode.getGroupid())) {
								for (ConstructionSite d:csits) {
									d.setSstatus(1);
								}
								return csits;
							}
						}
					} else if (pnode.getType().equalsIgnoreCase("department") && pnode.isAll()) {
						for (Group group : egroups) { //判断是否属于编辑权限组中
							if (group.getId().equals(pnode.getGroupid())) {
								for (ConstructionSite d:csits) {
									d.setSstatus(1);
								}
								return csits;
							}
						}
					} else if (pnode.getType().equalsIgnoreCase("features") && pnode.isAll()) {
						for (Group group : egroups) { //判断是否属于编辑权限组中
							if (group.getId().equals(pnode.getGroupid())) {
								for (ConstructionSite d:csits) {
									if (d.getProject().getFeatures().equals(pnode.getFeatures())) {
										idSet.add(d.getId());
									}
								}
								break;
							}
						}
					} else if(pnode.getType().equalsIgnoreCase("csite")){
						for (Group group : egroups) { //判断是否属于编辑权限组中
							if (group.getId().equals(pnode.getGroupid())) {
								idSet.add(pnode.getDepartmentid());
							}
						}
					}
				}
			}
		}
		
		if(!idSet.isEmpty()){
			for (ConstructionSite c:csits) {
				for (Long id : idSet) {
					if (id.equals(c.getId())) {
						c.setSstatus(1);
						break;
					}
				}
			}
		}
		return csits;
	}
	
	/**
	 * 对应监测项目，设置可编辑的monitor项目的编辑状态
	 * @param request
	 * @param egroups 用户拥有的部门项目编辑权限组
	 * @param csits
	 * @return
	 */
	public List<Monitor> setEditMonitor (HttpServletRequest request,List<Group> egroups,List<Monitor> monitors){
		Set<Long> idSet = new HashSet<Long>();
		
		if(request.isUserInRole("ADMIN") || request.isUserInRole("SUPERADMIN")){
			for (Monitor d:monitors) {
				d.setSstatus(1);
			}
			return monitors;
		} else {
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			Set<DepartmentNode> departmentNodes = user.getpNodes();
			if(departmentNodes!=null && departmentNodes.size()>0){
				for(DepartmentNode pnode:departmentNodes){
					if (pnode.getDepartmentid().equals(-1L) && pnode.isAll()) {
						for (Group group : egroups) { //判断是否属于编辑权限组中
							if (group.getId().equals(pnode.getGroupid())) {
								for (Monitor d:monitors) {
									d.setSstatus(1);
								}
								return monitors;
							}
						}
					} else if (pnode.getType().equalsIgnoreCase("department") && pnode.isAll()) {
						for (Group group : egroups) { //判断是否属于编辑权限组中
							if (group.getId().equals(pnode.getGroupid())) {
								for (Monitor d:monitors) {
									d.setSstatus(1);
								}
								return monitors;
							}
						}
					} else if (pnode.getType().equalsIgnoreCase("features") && pnode.isAll()) {
						for (Group group : egroups) { //判断是否属于编辑权限组中
							if (group.getId().equals(pnode.getGroupid())) {
								for (Monitor d:monitors) {
									if (d.getProject().getFeatures().equals(pnode.getFeatures())) {
										idSet.add(d.getId());
									}
								}
								break;
							}
						}
					} else if(pnode.getType().equalsIgnoreCase("csite")){
						for (Group group : egroups) { //判断是否属于编辑权限组中
							if (group.getId().equals(pnode.getGroupid())) {
								idSet.add(pnode.getDepartmentid());
							}
						}
					}
				}
			}
		}
		
		if(!idSet.isEmpty()){
			for (Monitor c:monitors) {
				for (Long id : idSet) {
					if (id.equals(c.getId())) {
						c.setSstatus(1);
						break;
					}
				}
			}
		}
		return monitors;
	}
	
	/**
	 * 对应监护项目，设置可编辑的guard项目的编辑状态
	 * @param request
	 * @param egroups 用户拥有的部门项目编辑权限组
	 * @param csits
	 * @return
	 */
	public List<Guard> setEditGuard (HttpServletRequest request,List<Group> egroups,List<Guard> guards){
		Set<Long> idSet = new HashSet<Long>();
		
		if(request.isUserInRole("ADMIN") || request.isUserInRole("SUPERADMIN")){
			for (Guard d:guards) {
				d.setSstatus(1);
			}
			return guards;
		} else {
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			Set<DepartmentNode> departmentNodes = user.getpNodes();
			if(departmentNodes!=null && departmentNodes.size()>0){
				for(DepartmentNode pnode:departmentNodes){
					if (pnode.getDepartmentid().equals(-1L) && pnode.isAll()) {
						for (Group group : egroups) { //判断是否属于编辑权限组中
							if (group.getId().equals(pnode.getGroupid())) {
								for (Guard d:guards) {
									d.setSstatus(1);
								}
								return guards;
							}
						}
					} else if (pnode.getType().equalsIgnoreCase("department") && pnode.isAll()) {
						for (Group group : egroups) { //判断是否属于编辑权限组中
							if (group.getId().equals(pnode.getGroupid())) {
								for (Guard d:guards) {
									d.setSstatus(1);
								}
								return guards;
							}
						}
					} else if (pnode.getType().equalsIgnoreCase("features") && pnode.isAll()) {
						for (Group group : egroups) { //判断是否属于编辑权限组中
							if (group.getId().equals(pnode.getGroupid())) {
								for (Guard d:guards) {
									if (d.getProject().getFeatures().equals(pnode.getFeatures())) {
										idSet.add(d.getId());
									}
								}
								break;
							}
						}
					} else if(pnode.getType().equalsIgnoreCase("csite")){
						for (Group group : egroups) { //判断是否属于编辑权限组中
							if (group.getId().equals(pnode.getGroupid())) {
								idSet.add(pnode.getDepartmentid());
							}
						}
					}
				}
			}
		}
		
		if(!idSet.isEmpty()){
			for (Guard c:guards) {
				for (Long id : idSet) {
					if (id.equals(c.getId())) {
						c.setSstatus(1);
						break;
					}
				}
			}
		}
		return guards;
	}
	/**
	 * 针对其他项目，设置可编辑的project的编辑状态
	 * @param request
	 * @param egroups
	 * @param csits
	 * @return
	 */
	public List<Project> setEditProject (HttpServletRequest request,List<Group> egroups,List<Project> csits){
		Set<Long> idSet = new HashSet<Long>();
		
		if(request.isUserInRole("ADMIN") || request.isUserInRole("SUPERADMIN")){
			for (Project d:csits) {
				d.setSstatus(1);
			}
			return csits;
		} else {
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			Set<DepartmentNode> departmentNodes = user.getpNodes();
			if(departmentNodes!=null && departmentNodes.size()>0){
				for(DepartmentNode pnode:departmentNodes){
					if (pnode.getDepartmentid().equals(-1L) && pnode.isAll()) {
						for (Group group : egroups) { //判断是否属于编辑权限组中
							if (group.getId().equals(pnode.getGroupid())) {
								for (Project d:csits) {
									d.setSstatus(1);
								}
								return csits;
							}
						}
					} else if (pnode.getType().equalsIgnoreCase("department") && pnode.isAll()) {
						for (Group group : egroups) { //判断是否属于编辑权限组中
							if (group.getId().equals(pnode.getGroupid())) {
								for (Project d:csits) {
									d.setSstatus(1);
								}
								return csits;
							}
						}
					} else if (pnode.getType().equalsIgnoreCase("features") && pnode.isAll()) {
						for (Group group : egroups) { //判断是否属于编辑权限组中
							if (group.getId().equals(pnode.getGroupid())) {
								for (Project d:csits) {
									if (d.getFeatures().equals(pnode.getFeatures())) {
										idSet.add(d.getId());
									}
								}
								break;
							}
						}
					} else if(pnode.getType().equalsIgnoreCase("csite")){
						for (Group group : egroups) { //判断是否属于编辑权限组中
							if (group.getId().equals(pnode.getGroupid())) {
								idSet.add(pnode.getDepartmentid());
							}
						}
					}
				}
			}
		}
		
		if(!idSet.isEmpty()){
			for (Project c:csits) {
				for (Long id : idSet) {
					if (id.equals(c.getId())) {
						c.setSstatus(1);
						break;
					}
				}
			}
		}
		return csits;
		
	}
	
	/**
	 * 获取用户所能查看的地下水部门项目集合，从用户session中读取，如果没有，则需要重新加载，以地区区分
	 * @param request
	 * @param cSiteService
	 * @return
	 * @throws ServiceException 
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Set<UnderWater>> getWaterCsitesZone (HttpServletRequest request) throws ServiceException{
		HashMap<String, Set<UnderWater>> scite = (HashMap<String, Set<UnderWater>> )request.getSession().getAttribute(Constants.USER_ZONE_CSITE);
		
		if (scite!=null && scite.size()>0 ) {
			return scite;
		}
		List<UnderWater> cs = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
		
		scite = new HashMap<String, Set<UnderWater>>();
		if (request.isUserInRole("ADMIN") || request.isUserInRole("SUPERADMIN")) {
			scite = setZoneCsite(scite, cs);
		} else {
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			Department waterDept = getWaterDepartment();
			Set<DepartmentNode> departmentNodes = user.getpNodes();
			if(departmentNodes!=null && departmentNodes.size()>0){
				Set<UnderWater> csiteSet = new HashSet<>();
				//全选状态
				for(DepartmentNode pnode:departmentNodes){
					if(pnode.getDepartmentid().longValue()==-1 && pnode.isAll() || //拥有所有部门权限，因此直接返回地下水部门下的所有项目
							pnode.getDepartmentid().equals(waterDept.getId()) && pnode.getType().equalsIgnoreCase("department") && pnode.isAll()){ //拥有地下水部门权限，因此直接返回地下水部门下的所有项目 
						scite = setZoneCsite(scite, cs);
						request.getSession().setAttribute(Constants.USER_ZONE_CSITE, scite);
						return scite;
					} else if (pnode.getType().equalsIgnoreCase("features") &&  StringUtils.isNotBlank(pnode.getFeatures()) && pnode.getDepartmentid().equals(waterDept.getId()) && pnode.isAll()) {
						//工程特性，该特性下的项目都有权限
						String features = pnode.getFeatures();
						for(UnderWater csite:cs){
							if (csite.getCtype().equals(features)) {
								csiteSet.add(csite);
							}
						}
					} else if (pnode.getType().equalsIgnoreCase("csite")){
						//项目类型
						for(UnderWater csite:cs){
							if (csite.getPid().equals(pnode.getDepartmentid())) {
								csiteSet.add(csite);
								break;
							}
						}
					}
				}
				if(csiteSet.size()>0){
					scite = setZoneCsite(scite, csiteSet);
				}
			}
		}
		request.getSession().setAttribute(Constants.USER_ZONE_CSITE, scite);
		return scite;
	}
	
	/**
	 * 获取用户所能查看的地下水部门项目集合，从用户session中读取，如果没有，则需要重新加载，以地区区分
	 * @param request
	 * @param cSiteService
	 * @return
	 * @throws ServiceException 
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Set<GuardEntity>> getGuardZone (HttpServletRequest request) throws ServiceException{
		HashMap<String, Set<GuardEntity>> scite = (HashMap<String, Set<GuardEntity>> )request.getSession().getAttribute(Constants.USER_ZONE_GUARD);
		
		if (scite!=null && scite.size()>0 ) {
			return scite;
		}
		List<GuardEntity> cs = (List<GuardEntity> ) webCacheUtils.getFromCache(Constants.PROEJCT_GUARD);
		
		scite = new HashMap<String, Set<GuardEntity>>();
		if (request.isUserInRole("ADMIN") || request.isUserInRole("SUPERADMIN")) {
			scite = setZoneGuard(scite, cs);
		} else {
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			Department waterDept = (Department)webCacheUtils.getFromCache(Constants.DEPARTMENT_MONITOR);
			Set<DepartmentNode> departmentNodes = user.getpNodes();
			if(departmentNodes!=null && departmentNodes.size()>0){
				List<GuardEntity> csiteSet = new ArrayList<GuardEntity>();
				//全选状态
				for(DepartmentNode pnode:departmentNodes){
					if(pnode.getDepartmentid().longValue()==-1 && pnode.isAll() || //拥有所有部门权限，因此直接返回地下水部门下的所有项目
							pnode.getDepartmentid().equals(waterDept.getId()) && pnode.getType().equalsIgnoreCase("department") && pnode.isAll()){ //拥有地下水部门权限，因此直接返回地下水部门下的所有项目 
						scite = setZoneGuard(scite, cs);
						request.getSession().setAttribute(Constants.USER_ZONE_GUARD, scite);
						return scite;
					}  else if (pnode.getType().equalsIgnoreCase("csite")){
						//项目类型
						for(GuardEntity csite:cs){
							if (csite.getPid().equals(pnode.getDepartmentid())) {
								csiteSet.add(csite);
								break;
							}
						}
					}
				}
				if(csiteSet.size()>0){
					scite = setZoneGuard(scite, csiteSet);
				}
			}
		}
		request.getSession().setAttribute(Constants.USER_ZONE_GUARD, scite);
		return scite;
	}
	
	/**
	 * 获取用户所能查看的地下水部门项目集合，从用户session中读取，如果没有，则需要重新加载，以地区区分
	 * @param request
	 * @param cSiteService
	 * @return
	 * @throws ServiceException 
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Set<MonitorEntity>> getMonitorZone (HttpServletRequest request) throws ServiceException{
		HashMap<String, Set<MonitorEntity>> scite = (HashMap<String, Set<MonitorEntity>> )request.getSession().getAttribute(Constants.USER_ZONE_MONITOR);
		
		if (scite!=null && scite.size()>0 ) {
			return scite;
		}
		List<MonitorEntity> cs = (List<MonitorEntity> ) webCacheUtils.getFromCache(Constants.PROEJCT_MONITOR);
		
		scite = new HashMap<String, Set<MonitorEntity>>();
		if (request.isUserInRole("ADMIN") || request.isUserInRole("SUPERADMIN")) {
			scite = setZoneMonitor(scite, cs);
		} else {
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			Department waterDept = (Department)webCacheUtils.getFromCache(Constants.DEPARTMENT_MONITOR);;
			Set<DepartmentNode> departmentNodes = user.getpNodes();
			if(departmentNodes!=null && departmentNodes.size()>0){
				List<MonitorEntity> csiteSet = new ArrayList<MonitorEntity>();
				//全选状态
				for(DepartmentNode pnode:departmentNodes){
					if(pnode.getDepartmentid().longValue()==-1 && pnode.isAll() || //拥有所有部门权限，因此直接返回地下水部门下的所有项目
							pnode.getDepartmentid().equals(waterDept.getId()) && pnode.getType().equalsIgnoreCase("department") && pnode.isAll()){ //拥有地下水部门权限，因此直接返回地下水部门下的所有项目 
						scite = setZoneMonitor(scite, cs);
						request.getSession().setAttribute(Constants.USER_ZONE_MONITOR, scite);
						return scite;
					}  else if (pnode.getType().equalsIgnoreCase("csite")){
						//项目类型
						for(MonitorEntity csite:cs){
							if (csite.getPid().equals(pnode.getDepartmentid())) {
								csiteSet.add(csite);
								break;
							}
						}
					}
				}
				if(csiteSet.size()>0){
					scite = setZoneMonitor(scite, csiteSet);
				}
			}
		}
		request.getSession().setAttribute(Constants.USER_ZONE_MONITOR, scite);
		return scite;
	}
	
	private HashMap<String, Set<UnderWater>> setZoneCsite(HashMap<String, Set<UnderWater>> zcsite,
			Set<UnderWater> csiteSet) {
		if(zcsite==null) zcsite = new HashMap<String, Set<UnderWater>>();
		for(UnderWater csite:csiteSet){
			Set<UnderWater> cs = zcsite.get(csite.getZone());
			if(cs !=null){
				cs.add(csite);
			}else{
				cs =new HashSet<>();
				cs.add(csite);
				zcsite.put(csite.getZone(), cs);
			}
		}
		return zcsite;
	}
	private HashMap<String, Set<GuardEntity>> setZoneGuard(HashMap<String, Set<GuardEntity>> zcsite,
			List<GuardEntity> csiteSet) {
		if(zcsite==null) zcsite = new HashMap<String, Set<GuardEntity>>();
		for(GuardEntity csite:csiteSet){
			Set<GuardEntity> cs = zcsite.get(csite.getZone());
			if(cs !=null){
				cs.add(csite);
			}else{
				cs =new HashSet<>();
				cs.add(csite);
				zcsite.put(csite.getZone(), cs);
			}
		}
		return zcsite;
	}
	private HashMap<String, Set<MonitorEntity>> setZoneMonitor(HashMap<String, Set<MonitorEntity>> zcsite,
			List<MonitorEntity> csiteSet) {
		if(zcsite==null) zcsite = new HashMap<String, Set<MonitorEntity>>();
		for(MonitorEntity csite:csiteSet){
			Set<MonitorEntity> cs = zcsite.get(csite.getZone());
			if(cs !=null){
				cs.add(csite);
			}else{
				cs =new HashSet<>();
				cs.add(csite);
				zcsite.put(csite.getZone(), cs);
			}
		}
		return zcsite;
	}
	
	private HashMap<String, Set<UnderWater>> setZoneCsite(HashMap<String, Set<UnderWater>> zcsite,List<UnderWater> csiteSet){
		if(zcsite==null) zcsite = new HashMap<String, Set<UnderWater>>();
		for(UnderWater csite:csiteSet){
			Set<UnderWater> cs = zcsite.get(csite.getZone());
			if(cs !=null){
				cs.add(csite);
			}else{
				cs =new HashSet<>();
				cs.add(csite);
				zcsite.put(csite.getZone(), cs);
			}
		}
		return zcsite;
	}
	
	/**
	 * 获取用户所能查看的地下水部门项目集合，从用户session中读取，如果没有，则需要重新加载
	 * @param request
	 * @param cSiteService
	 * @return
	 * @throws ServiceException 
	 */
	@SuppressWarnings("unchecked")
	public List<UnderWater> getWaterCsites (HttpServletRequest request) throws ServiceException{
		List<UnderWater> csiteList = (List<UnderWater>)request.getSession().getAttribute(Constants.USER_WATER_CSITE);
		if (csiteList!=null && csiteList.size()>0 ) {
			return csiteList;
		}
		List<UnderWater> cs = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);

		csiteList = new ArrayList<UnderWater>();
		if (request.isUserInRole("ADMIN") || request.isUserInRole("SUPERADMIN")) {
			csiteList =cs;
		} else {
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			if(user!=null){
				Department waterDept = getWaterDepartment();
				Set<DepartmentNode> departmentNodes = user.getpNodes();
				if(departmentNodes!=null && departmentNodes.size()>0){
					Set<UnderWater> csiteSet = new HashSet<>();
					//全选状态
					for(DepartmentNode pnode:departmentNodes){

						if(pnode.getDepartmentid().longValue()==-1 && pnode.isAll()){ //拥有所有部门权限，因此直接返回地下水部门下的所有项目
							csiteList = cs;
							break;
						} else if(pnode.getDepartmentid().longValue()==waterDept.getId().longValue() && pnode.getType().equalsIgnoreCase("department") && pnode.isAll()){
							//拥有地下水部门权限，因此直接返回地下水部门下的所有项目
							csiteList = cs;
							break;
						} else if (pnode.getType().equalsIgnoreCase("features") &&  StringUtils.isNotBlank(pnode.getFeatures()) && pnode.getDepartmentid().equals(waterDept.getId()) && pnode.isAll()) {
							//工程特性，该特性下的项目都有权限
							String features = pnode.getFeatures();
							for(UnderWater csite:cs){
								if (csite.getCtype().equals(features)) {
									csiteSet.add(csite);
								}
							}
						} else if (pnode.getType().equalsIgnoreCase("csite")){
							//项目类型
							for(UnderWater csite:cs){
								if (csite.getPid().equals(pnode.getDepartmentid())) {
									csiteSet.add(csite);
									break;
								}
							}
						}
					}
					//判断csiteList是否不为0
					if (csiteList.isEmpty()&&!csiteSet.isEmpty()) {
						csiteList = new ArrayList<UnderWater>(csiteSet); 
					}
				}
			}
			request.getSession().setAttribute(Constants.USER_WATER_CSITE, csiteList);
			return csiteList;
			}
		return null;
			
	}
	
	/**
	 * 获取用户所能编辑的地下水部门项目集合，从用户session中读取，如果没有，则需要重新加载
	 * @param request
	 * @param egroups 用户所拥有的项目编辑权限的权限组
	 * @param cSiteService
	 * @return
	 * @throws ServiceException 
	 */
	public List<ConstructionSite> getWaterCsites (HttpServletRequest request, List<Group> egroups) throws ServiceException{
		List<ConstructionSite> csiteList = new ArrayList<ConstructionSite>();
		User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
		Department waterDept = getWaterDepartment();
		List<ConstructionSite> cs = cSiteService.withDepartment();
		if (request.isUserInRole("ADMIN") || request.isUserInRole("SUPERADMIN")) {
			csiteList = cs;
		} else {
			Set<DepartmentNode> departmentNodes = user.getpNodes();
			Set<ConstructionSite> csiteSet = new HashSet<ConstructionSite>();
			if(departmentNodes!=null && departmentNodes.size()>0){
				//全选状态
				for(DepartmentNode pnode:departmentNodes){
					
					if(pnode.getDepartmentid().equals(-1L) && pnode.isAll()){ //拥有所有部门权限，因此直接返回地下水部门下的所有项目
						for(Group g:egroups){
							if(g.getId()==pnode.getGroupid()){
								csiteList = cs;
								break;
							}
						}
					} else if(pnode.getDepartmentid().equals(waterDept.getId()) && pnode.getType().equalsIgnoreCase("department") && pnode.isAll()){
						//拥有地下水部门权限，因此直接返回地下水部门下的所有项目
						for(Group g:egroups){
							if(g.getId()==pnode.getGroupid()){
								csiteList = cs;
								break;
							}
						}
					} else if (pnode.getType().equalsIgnoreCase("features") && pnode.isAll() && pnode.getDepartmentid().equals(waterDept.getId())) {
						for (Group group : egroups) { //判断是否属于编辑权限组中
							if (group.getId().equals(pnode.getGroupid())) {
								for (ConstructionSite d:cs) {
									if (d.getProject().getFeatures().equals(pnode.getFeatures())) {
										csiteSet.add(d);
									}
								}
								break;
							}
						}
					} else if (pnode.getType().equalsIgnoreCase("csite")) {
						for (Group group : egroups) { //判断是否属于编辑权限组中
							if (group.getId().equals(pnode.getGroupid())) {
								for (ConstructionSite d:cs) {
									if (d.getId().equals(pnode.getDepartmentid())) {
										csiteSet.add(d);
									}
								}
								break;
							}
						}
					}
				}
				//判断csiteList是否不为0
				if (csiteList.isEmpty()&&!csiteSet.isEmpty()) {
					csiteList = new ArrayList<ConstructionSite>(csiteSet); 
				}
			}
		}
		request.getSession().setAttribute(Constants.USER_WATER_CSITE, csiteList);
		return csiteList;
	}

	public Department getWaterDepartment() throws ServiceException {
		return (Department) webCacheUtils.getFromCache(Constants.DEPARTMENT_WATER);
//		Department waterDept =null;
//		List<Department> departments = (List<Department>) webCacheUtils.getFromCache(Constants.DEPARTMENTS); //所有部门
//		for(Department d:departments){
//			if(d.getCode().equalsIgnoreCase(Constants.DEPARTMENT_WATER))
//				waterDept=d;
//		}
//		return waterDept;
	}
	
	public Department getMonitorDepartment() throws ServiceException {
		return (Department) webCacheUtils.getFromCache(Constants.DEPARTMENT_MONITOR);
	}
	
	/**
	 * 获取工程特性对应的名称Map
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getFeaturesNames() {
		Map<String, String> result = new HashMap<String, String>();
		List<BasedataType> basedataList = (List<BasedataType>) SpringContextUtils.getServletContext().getAttribute(Constants.BD_PROJECT_TYPE); //工程特性基础数据集合
		if (basedataList!=null) {
			for (BasedataType basedataType : basedataList) {
				result.put(basedataType.getValue(), basedataType.getName());
			}
		}
		return result;
	}
	
	/**
	 * 获取用户所能查看到的所有项目及其对应的摄像头信息
	 * @param request
	 * @param flag 是否显示没有摄像头的项目，如果true，则只获取带有摄像头的项目，如果false，则获取所有项目
	 * @return
	 * @throws ServiceException 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getProjectCameras (HttpServletRequest request, Boolean flag) throws ServiceException{
		List<Department> departments = (List<Department>) webCacheUtils.getFromCache(Constants.DEPARTMENTS); //所有部门

		List<Map<String, Object>> projectList = new ArrayList<Map<String, Object>>();
		if (request.isUserInRole("ADMIN") || request.isUserInRole("SUPERADMIN")) {
			for (Department p : departments) {
				projectList.add(loadProjectsByDepartments(p, flag));
			}
		} else {
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			Set<DepartmentNode> departmentNodes = user.getpNodes();
			if(departmentNodes!=null && departmentNodes.size()>0){
				Set<Project> projectSet = new HashSet<Project>();
				//全选状态
				for(DepartmentNode pnode:departmentNodes){

					if(pnode.getDepartmentid().equals(-1L) && pnode.isAll()){ //拥有所有部门权限，因此直接返回地下水部门下的所有项目
						for (Department p : departments) {
							projectList.add(loadProjectsByDepartments(p, flag));
						}
						break;
					} else if(pnode.getType().equalsIgnoreCase("department") && pnode.isAll()){
						for (Department p : departments) {
							if (p.getId().equals(pnode.getDepartmentid())) { //拥有对应部门下所有项目的权限
								for (Project project : p.getcSites()) {
									if (flag) {
										if (!project.getCamera().isEmpty()) {
											projectSet.add(project);		
										}
									} else {
										projectSet.add(project);
									}
								}
								break;
							}
						}
					} else if (pnode.getType().equalsIgnoreCase("features") &&  StringUtils.isNotBlank(pnode.getFeatures()) && pnode.isAll()) {
						//工程特性，该特性下的项目都有权限
						for (Department p : departments) {
							if (p.getId().equals(pnode.getDepartmentid())) { //拥有对应部门下对应同样工程特性的项目的权限
								for (Project project : p.getcSites()) {
									if (project.getFeatures().equals(pnode.getFeatures())) {
										if (flag) {
											if (!project.getCamera().isEmpty()) {
												projectSet.add(project);		
											}
										} else {
											projectSet.add(project);
										}
									}
								}
								break;
							}
						}
					} else if (pnode.getType().equalsIgnoreCase("csite")){
						//项目类型
						for (Department p : departments) {
							for (Project project : p.getcSites()) {
								if (project.getId().equals(pnode.getDepartmentid())) {
									if (flag) {
										if (!project.getCamera().isEmpty()) {
											projectSet.add(project);		
										}
									} else {
										projectSet.add(project);
									}
									break;
								}
							}
							break;
						}
					}
				}
				//判断projectList是否不为0
				if (projectList.isEmpty()&&!projectSet.isEmpty()) {
					Map<Long, Map<String, Object>> map = new HashMap<Long, Map<String, Object>>();
					for (Project project : projectSet) {
						Map<String, Object> entry = new HashMap<String, Object>();
						Map<String, Object> entry_csite = extractProject(project); //项目
						Department department = project.getDepartment();
						if (map.get(department.getId())!=null) {
							Map<String, Object> entry_map = map.get(department.getId());
							List<Map<String, Object>> csiteList = (List<Map<String, Object>>) entry_map.get("cSites");
							csiteList.add(entry_csite);
						} else {
							entry.put("id", department.getId());
							entry.put("name", department.getName());
							List<Map<String, Object>> csiteList = new ArrayList<Map<String, Object>>();
							csiteList.add(entry_csite);
							entry.put("cSites", csiteList);
							map.put(department.getId(), entry);
						}
					}
					projectList.addAll(map.values());
				}
			}
		}
		return projectList;
	}
	
	private Map<String, Object> loadProjectsByDepartments(Department p, Boolean flag) {
		Map<String, Object> entry = new HashMap<String, Object>();
		entry.put("id", p.getId());
		entry.put("name", p.getName());
		List<Map<String, Object>> csiteList = new ArrayList<Map<String, Object>>();
		if(!p.getcSites().isEmpty()) {
			for (Project csite : p.getcSites()) {
				if (flag) {
					if (!csite.getCamera().isEmpty()) {
						Map<String, Object> entry_csite = extractProject(csite);
						csiteList.add(entry_csite);
					}
				} else {
					Map<String, Object> entry_csite = extractProject(csite);
					csiteList.add(entry_csite);
				}
			}
		}
		
		entry.put("cSites", csiteList);
		return entry;
	}

	private Map<String, Object> extractProject(Project csite) {
		Map<String, Object> entry_csite = new HashMap<String, Object>();
		entry_csite.put("id", csite.getId());
		entry_csite.put("name", csite.getName());
		// 处理摄像头
		if(csite.getCamera()!=null && csite.getCamera().size()>0){
			List<Map<String, Object>> cameraList = new ArrayList<Map<String, Object>>();
			for(Camera c:csite.getCamera()){
				if (c.isStatus()) {
					Map<String, Object> entry_camera = new HashMap<String, Object>();
					entry_camera.put("id", c.getId());
					entry_camera.put("name", c.getNote());
					entry_camera.put("path", c.getPath());
					cameraList.add(entry_camera);
				}
			}
			entry_csite.put("cameras", cameraList);
		}
		return entry_csite;
	}

}
