package com.kekeinfo.web.init.data;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import com.kekeinfo.core.business.basedata.service.BaseDataService;
import com.kekeinfo.core.business.common.model.BasedataType;
import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.common.model.MetaDescription;
import com.kekeinfo.core.business.content.model.Content;
import com.kekeinfo.core.business.content.service.ContentService;
import com.kekeinfo.core.business.department.model.Department;
import com.kekeinfo.core.business.department.service.DepartmentService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.monitor.model.MPoint;
import com.kekeinfo.core.business.monitor.model.MPointEnumType;
import com.kekeinfo.core.business.monitor.service.MPointEquipService;
import com.kekeinfo.core.business.reference.init.service.InitializationDatabase;
import com.kekeinfo.core.business.system.model.SystemConfiguration;
import com.kekeinfo.core.business.system.service.SystemConfigurationService;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.model.Permission;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.business.user.service.PermissionService;
import com.kekeinfo.core.business.user.service.UserService;
import com.kekeinfo.core.constants.SystemConstants;
import com.kekeinfo.core.utils.CoreConfiguration;
import com.kekeinfo.web.admin.security.WebUserServices;
import com.kekeinfo.web.admin.service.ResetJob;
import com.kekeinfo.web.constants.ApplicationConstants;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.utils.DataUtils;
import com.kekeinfo.web.utils.ReloadMemoryData;




@Component
public class InitializationLoader implements ApplicationContextAware, ServletContextAware, Ordered{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InitializationLoader.class);
	
	@Autowired ReloadMemoryData reloadMemoryData;
	
	@Autowired
	private InitializationDatabase initializationDatabase;
	
	@Autowired
	private SystemConfigurationService systemConfigurationService;
	
	@Autowired
	private WebUserServices userDetailsService;
	
	@Autowired
	protected PermissionService  permissionService;
	
	@Autowired
	protected GroupService   groupService;
	
	@Autowired
	private CoreConfiguration configuration;
	
	@Autowired
	private UserService userService;
	
	@Autowired
    private ResetJob job;
	
	@Autowired
	private BaseDataService baseDataService;
	
	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
	private ContentService contentService;
	
	
	
	@Autowired MPointEquipService mPointEquipService;
	
	@Autowired DataUtils dataUtils;
	/***
	 * 临时增加省份
	 */
	/**
	@Autowired
	private ZonesLoader zonesLoader;
	
	@Autowired
	private ZoneService zoneService;
	/***/
	public void init() {
		LOGGER.info("开始系统初始化信息");
		try {
			/*** 临时增加省份*/
			/***
			 try {

	    		  Map<String,Zone> zonesMap = new HashMap<String,Zone>();
	    		  zonesMap = zonesLoader.loadZones("reference/zoneconfig.json");
	              
	    		  for (Map.Entry<String, Zone> entry : zonesMap.entrySet()) {
	           	   
	            	  Zone value = entry.getValue();
	            	  Zone dbZone = zoneService.getByCode(value.getCode());
	            	  value.setId(dbZone.getId());
	          	      zoneService.saveOrUpdate(value);
	              
	              }

	  		} catch (Exception e) {
	  		    
	  			throw new ServiceException(e);
	  		}
	  		*/
			
			/****/ 
			if (initializationDatabase.isEmpty()) {
				LOGGER.info(String.format("%s : Shopizer database is empty, populate it....", "gl-shop"));
		
				 initializationDatabase.populate("sglshop");
				
				 //security groups and permissions
				  Permission superadmin = new Permission("SUPERADMIN");
				  superadmin.setName("超级管理员");
				  permissionService.create(superadmin);
				  
				  Permission admin = new Permission("ADMIN");
				  admin.setName("管理员");
				  permissionService.create(admin);
				  
				  Permission project = new Permission("VIEW-PROJECT");
				  project.setName("部门/项目查看");
				  project.setType(1);
				  permissionService.create(project);
				  
				  Permission projectedit = new Permission("EDIT-PROJECT");
				  projectedit.setName("部门/项目编辑");
				  projectedit.setType(1);
				  permissionService.create(projectedit);
				  
				  Permission user  = new Permission("USER");
				  user.setName("用户管理");
				  permissionService.create(user);
				  
				  Permission auth = new Permission("AUTH");//Authenticated
				  auth.setName("基本权限");
				  permissionService.create(auth);
				  
				  Group gsuperadmin = new Group("SUPERADMIN");
				  gsuperadmin.setName("超级用户组");
				  Group gadmin = new Group("ADMIN");
				  gadmin.setName("管理员组");
				  

				  gsuperadmin.getPermissions().add(superadmin);
				  gsuperadmin.getPermissions().add(admin);
				  gsuperadmin.getPermissions().add(user);
				  gsuperadmin.getPermissions().add(projectedit);
				  gsuperadmin.getPermissions().add(project);
				  gsuperadmin.getPermissions().add(auth);
				  gsuperadmin.setsGroup(true);
				  groupService.saveOrUpdate(gsuperadmin);
				  
				  gadmin.getPermissions().add(user);
				  gadmin.getPermissions().add(projectedit);
				  gadmin.getPermissions().add(project);
				  gadmin.getPermissions().add(admin);
				  gadmin.getPermissions().add(auth);
				  gadmin.setsGroup(false);
				  groupService.saveOrUpdate(gadmin);
				  
				  //用户管理组
				  Group userg = new Group("USERGROUP");
				  userg.setName("用户管理组");
				  userg.getPermissions().add(user);
				  userg.getPermissions().add(auth);
				  userg.setsGroup(false);
				  groupService.saveOrUpdate(userg);
				  
				  //项目管理组
				  Group projectg = new Group("PROJECTEDITGROUP");
				  projectg.setName("部门/项目编辑组");
				  projectg.getPermissions().add(projectedit);
				  projectg.getPermissions().add(auth);
				  projectg.getPermissions().add(project);
				  projectg.setGrouptype(1);
				  projectg.setsGroup(false);
				  groupService.saveOrUpdate(projectg);
				  
				  //项目组查看
				  Group projectvg = new Group("PROJECTVIEWGROUP");
				  projectvg.setName("部门/项目查看组");
				  projectvg.getPermissions().add(project);
				  projectvg.getPermissions().add(auth);
				  projectg.setGrouptype(1);
				  projectg.setsGroup(true);
				  groupService.saveOrUpdate(projectvg);
				  
			} else {
				/**增加作业权限
				 * 
				 */
				Group jobg =groupService.getByName("GUARDJOB");
				if(jobg==null){
					Permission user  = new Permission("GJOB");
					user.setName("监护项目作业计划管理");
					permissionService.save(user);
					jobg=new Group("GUARDJOB");
					jobg.setName("监护项目作业计划管理");
					Permission auth =permissionService.getByName("AUTH");
					jobg.getPermissions().add(auth);
					jobg.getPermissions().add(user);
					jobg.setsGroup(false);
					groupService.save(jobg);
					List<Group> groups =groupService.listGroupWithPermission();
					for(Group g:groups){
						if(g.getGroupName().equals("SUPERADMIN")){
							g.getPermissions().add(user);
							groupService.saveOrUpdate(g);
						}else if(g.getGroupName().equals("ADMIN")){
							g.getPermissions().add(user);
							groupService.saveOrUpdate(g);
						}
					}
				}
				/**
				 *已有项目升级 处理
				 */
				//升级已完成
				//删除约束
				//删除约束
				try{
					List<String> p_attributes = new ArrayList<String>();
					p_attributes.add("CONSTRAINT_NAME");
					ArrayList<String> delUserGroup = new ArrayList<>();
					List<Object[]> plist = groupService.getBySql(p_attributes, "INFORMATION_SCHEMA.KEY_COLUMN_USAGE"," where TABLE_NAME = 'CONSTRUCTIONSITE' AND REFERENCED_TABLE_NAME='DEPARTMENT'");
					if(plist !=null && plist.size()>0){
						for(Object c:plist){
							String con1 ="ALTER TABLE CONSTRUCTIONSITE DROP FOREIGN KEY "+c.toString();
							delUserGroup.add(con1);
						}
					}
					StringBuffer nsql =new StringBuffer();
					 nsql.append("ALTER TABLE CONSTRUCTIONSITE DROP COLUMN DEPARTMENT_ID");
					delUserGroup.add(nsql.toString());
					groupService.excuteByNativeSql(delUserGroup);
				}catch(Exception e){
					
				}
				
			}
			
			if(userService.count()==0)
				userDetailsService.createDefaultAdmin();
			//初始化基础数据
			initBasedataType();
			
			//初始化部门，针对该广联项目，初始化地下工程部门
			Entitites<Department> departlist = departmentService.getListByAttributes(new String[]{"code"}, new String[]{Constants.DEPARTMENT_WATER}, null);
			if (departlist.getTotalCount()==0) {
				Department department = new Department();
				department.setName("地下水综合治理事业部");
				department.setCode(Constants.DEPARTMENT_WATER);
				departmentService.saveOrUpdate(department);
			}
			
			Entitites<Department> departlist1 = departmentService.getListByAttributes(new String[]{"code"}, new String[]{Constants.DEPARTMENT_MONITOR}, null);
			if (departlist1.getTotalCount()==0) {
				Department department = new Department();
				department.setName("岩土工程事业部（监测所）");
				department.setCode(Constants.DEPARTMENT_MONITOR);
				departmentService.saveOrUpdate(department);
			}
			//初始化报告编制内容模板
			List<Content> contents = contentService.listByType(Constants.CTN_REPORT_TYPE);
			if (contents.isEmpty()) {
				Content content = new Content();
				content.setContentType(Constants.CTN_REPORT_TYPE);
				content.setCode("report_model_0");
				MetaDescription description = new MetaDescription();
				description.setName("报告编制模板一");
				StringBuilder sbd = new StringBuilder();
				sbd.append("<h1>1概述</h1>").append("<h2>1.1背景</h2>")
				.append("<p>&nbsp;&nbsp;&nbsp;&nbsp;描述整个项目的信息内容。</p>").append("<h2>1.2目的</h2>")
				.append("<p>&nbsp;&nbsp;&nbsp;&nbsp;本文档的目的，提供什么样的解决方案。</p>").append("<p>本文档适合的阅读对象包括但不限于XXX等。</p>")
				.append("<h2>1.3范围</h2>").append("<p>&nbsp;&nbsp;&nbsp;&nbsp;项目范围。</p>")
				.append("<h1>2需求</h1>").append("<h2>2.1功能需求</h2>")
				.append("<p>&nbsp;&nbsp;&nbsp;&nbsp;描述整个项目的功能需求内容。</p>");
				description.setDescription(sbd.toString());
				content.setDescription(description);
				contentService.save(content);
			}
			
			//初始化项目日志模板
			List<Content> dailys = contentService.listByType(Constants.CTN_DAILY_TYPE);
			if (dailys.isEmpty()) {
				Content content = new Content();
				content.setContentType(Constants.CTN_DAILY_TYPE);
				content.setCode("daily_model_0");
				MetaDescription description = new MetaDescription();
				description.setName("项目日志模板一");
				StringBuilder sbd = new StringBuilder();
				sbd.append("描述项目的整体进展情况，今日的工作情况和后续的工作计划。");
				description.setDescription(sbd.toString());
				content.setDescription(description);
				contentService.save(content);
			}
			
			//初始化监控测点
			if(mPointEquipService.count()==0){
				for (MPointEnumType e : MPointEnumType.values()) {  
					MPoint mpoint = new MPoint();
					mpoint.setpName(e.getName());
					mpoint.setpType(e.toString());
					mPointEquipService.save(mpoint);  
				} 
			}
			loadData();

		} catch (Exception e) {
			LOGGER.error("Error in the init method",e);
		}
		LOGGER.info("成功完成系统初始化信息");
	}
		
	private void initBasedataType() throws ServiceException {
		/**
		//规范库基础数据
		Entitites<BasedataType> tmp = baseDataService.getListByAttributes(new String[]{"type"}, new String[]{Constants.BD_DOC_TYPE}, null);
		if(tmp==null || tmp.getEntites()==null || tmp.getEntites().size()<1) {
			
			BasedataType doc_1 = new BasedataType("地下水标准", 1, Constants.BD_DOC_TYPE, "water");
			BasedataType doc_2 = new BasedataType("隧道标准", 2, Constants.BD_DOC_TYPE, "tunnel");
			BasedataType doc_3 = new BasedataType("土建标准", 3, Constants.BD_DOC_TYPE, "ground");
			
			baseDataService.create(doc_1);
			baseDataService.create(doc_2);
			baseDataService.create(doc_3);
			
		}*/
		//系统基础数据
		Entitites<BasedataType> sys = baseDataService.getListByAttributes(new String[]{"type"}, new String[]{Constants.FRESHTIME}, null);
		if(sys==null || sys.getEntites()==null || sys.getEntites().size()<1) {
			//刷新事件
			BasedataType rTime  = new BasedataType("实时数据刷新时间（单位：秒）",1,Constants.FRESHTIME,"600");
			rTime.setSys(true);
			baseDataService.create(rTime);
		}
		Entitites<BasedataType> fre = baseDataService.getListByAttributes(new String[]{"type"}, new String[]{Constants.KEEPTIME}, null);
		if(fre==null || fre.getEntites()==null || fre.getEntites().size()<1) {
			BasedataType cTime  = new BasedataType("实时数据清理时间（单位：天）",2,Constants.KEEPTIME,"7");
			cTime.setSys(true);
			baseDataService.create(cTime);
		}
		
		Entitites<BasedataType> mon = baseDataService.getListByAttributes(new String[]{"type"}, new String[]{Constants.DATA_MONITOR_POWER}, null);
		if(mon==null || mon.getEntites()==null || mon.getEntites().size()<1) {
			BasedataType mTime  = new BasedataType("测点断电监测周期（单位：分钟）",3,Constants.DATA_MONITOR_POWER,"10");
			mTime.setSys(true);
			baseDataService.create(mTime);
		}
		
		Entitites<BasedataType> gather = baseDataService.getListByAttributes(new String[]{"type"}, new String[]{Constants.DATA_GATHER_DATA}, null);
		if(gather==null || gather.getEntites()==null || gather.getEntites().size()<1) {
			BasedataType gTime  = new BasedataType("测点数据采集周期（单位：分钟）",4,Constants.DATA_GATHER_DATA,"10");
			gTime.setSys(true);
			baseDataService.create(gTime);
		}
		
		Entitites<BasedataType> pst = baseDataService.getListByAttributes(new String[]{"type"}, new String[]{Constants.PROJECT_STATUS}, null);
		if(pst==null || pst.getEntites()==null || pst.getEntites().size()<1) {
			BasedataType cTime  = new BasedataType("成井",1,Constants.PROJECT_STATUS,"0");
			BasedataType cT  = new BasedataType("降压运行",2,Constants.PROJECT_STATUS,"1");
			BasedataType cTs  = new BasedataType("疏干运行",3,Constants.PROJECT_STATUS,"2");
			BasedataType cTi  = new BasedataType("结束",4,Constants.PROJECT_STATUS,"-1");
			cTime.setSys(true);
			cT.setSys(true);
			cTs.setSys(true);
			cTi.setSys(true);
			baseDataService.create(cTs);
			baseDataService.create(cTime);
			baseDataService.create(cT);
			baseDataService.create(cTi);
		}
		
		//项目等级
		Entitites<BasedataType> projects = baseDataService.getListByAttributes(new String[]{"type"}, new String[]{Constants.BD_PROJ_RANK}, null);
		if(projects==null || projects.getEntites()==null || projects.getEntites().size()<1) {
			
			BasedataType bd_1 = new BasedataType("一级", 1, Constants.BD_PROJ_RANK, "1");
			BasedataType bd_2 = new BasedataType("二级", 2, Constants.BD_PROJ_RANK, "2");
			bd_1.setSys(true);
			bd_2.setSys(true);
			baseDataService.create(bd_1);
			baseDataService.create(bd_2);
		}
		
		//项目等级
		Entitites<BasedataType> mprojects = baseDataService.getListByAttributes(new String[]{"type"}, new String[]{Constants.MO_PROJ_RANK}, null);
		if(mprojects==null || mprojects.getEntites()==null || mprojects.getEntites().size()<1) {
			
			BasedataType bd_1 = new BasedataType("一级", 1, Constants.MO_PROJ_RANK, "1");
			BasedataType bd_2 = new BasedataType("二级", 2, Constants.MO_PROJ_RANK, "2");
			bd_1.setSys(true);
			bd_2.setSys(true);
			baseDataService.create(bd_1);
			baseDataService.create(bd_2);
		}
		
		//项目等级
		Entitites<BasedataType> evns = baseDataService.getListByAttributes(new String[]{"type"}, new String[]{Constants.BD_ENV_RANK}, null);
		if(evns==null || evns.getEntites()==null || evns.getEntites().size()<1) {
			
			BasedataType bd_1 = new BasedataType("一类", 1, Constants.BD_ENV_RANK, "A");
			BasedataType bd_2 = new BasedataType("二类", 2, Constants.BD_ENV_RANK, "B");
			BasedataType bd_3 = new BasedataType("三类", 3, Constants.BD_ENV_RANK, "C");
			baseDataService.create(bd_1);
			baseDataService.create(bd_2);
			baseDataService.create(bd_3);
		}
		
		//基坑开挖深度
		Entitites<BasedataType> depth = baseDataService.getListByAttributes(new String[]{"type"}, new String[]{Constants.BD_DEPTH_RANK}, null);
		if(depth==null || depth.getEntites()==null || depth.getEntites().size()<1) {
			
			BasedataType bd_1 = new BasedataType("》40m", 1, Constants.BD_DEPTH_RANK, "A");
			BasedataType bd_2 = new BasedataType("》25m", 2, Constants.BD_DEPTH_RANK, "B");
			BasedataType bd_3 = new BasedataType("》16m", 3, Constants.BD_DEPTH_RANK, "C");
			BasedataType bd_4 = new BasedataType("《16m", 4, Constants.BD_DEPTH_RANK, "D");
			baseDataService.create(bd_1);
			baseDataService.create(bd_2);
			baseDataService.create(bd_3);
			baseDataService.create(bd_4);
		}
		
		//工程特性度
		Entitites<BasedataType> ptype = baseDataService.getListByAttributes(new String[]{"type"}, new String[]{Constants.BD_PROJECT_TYPE}, null);
		if(ptype==null || ptype.getEntites()==null || ptype.getEntites().size()<1) {
			
			BasedataType bd_1 = new BasedataType("地铁", 1, Constants.BD_PROJECT_TYPE, "A");
			BasedataType bd_2 = new BasedataType("民建", 2, Constants.BD_PROJECT_TYPE, "B");
			BasedataType bd_3 = new BasedataType("市政", 3, Constants.BD_PROJECT_TYPE, "C");
			BasedataType bd_4 = new BasedataType("其他", 4, Constants.BD_PROJECT_TYPE, "D");
			baseDataService.create(bd_1);
			baseDataService.create(bd_2);
			baseDataService.create(bd_3);
			baseDataService.create(bd_4);
		}
		
		//围护特征
		Entitites<BasedataType> features = baseDataService.getListByAttributes(new String[]{"type"}, new String[]{Constants.BD_SURROUND_FEATURES}, null);
		if(features==null || features.getEntites()==null || features.getEntites().size()<1) {
			
			BasedataType bd_1 = new BasedataType("悬挂式", 1, Constants.BD_SURROUND_FEATURES, "A");
			BasedataType bd_2 = new BasedataType("落底式", 2, Constants.BD_SURROUND_FEATURES, "B");
			BasedataType bd_3 = new BasedataType("落顶式", 3, Constants.BD_SURROUND_FEATURES, "C");
			baseDataService.create(bd_1);
			baseDataService.create(bd_2);
			baseDataService.create(bd_3);
		}
		
		//围护形式
		Entitites<BasedataType> style = baseDataService.getListByAttributes(new String[]{"type"}, new String[]{Constants.BD_SURROUND_STYLE}, null);
		if(style==null || style.getEntites()==null || style.getEntites().size()<1) {
			
			BasedataType bd_1 = new BasedataType("地下连续墙", 1, Constants.BD_SURROUND_STYLE, "A");
			BasedataType bd_2 = new BasedataType("TRD", 2, Constants.BD_SURROUND_STYLE, "B");
			BasedataType bd_3 = new BasedataType("双排桩", 3, Constants.BD_SURROUND_STYLE, "C");
			BasedataType bd_4 = new BasedataType("钢板桩", 4, Constants.BD_SURROUND_STYLE, "D");
			BasedataType bd_5 = new BasedataType("其他", 3, Constants.BD_SURROUND_STYLE, "E");
			baseDataService.create(bd_1);
			baseDataService.create(bd_2);
			baseDataService.create(bd_3);
			baseDataService.create(bd_4);
			baseDataService.create(bd_5);
		}
		
		//布井方式
		Entitites<BasedataType> pattern = baseDataService.getListByAttributes(new String[]{"type"}, new String[]{Constants.BD_PATTERN}, null);
		if(pattern==null || pattern.getEntites()==null || pattern.getEntites().size()<1) {
			
			BasedataType bd_1 = new BasedataType("坑内降水", 1, Constants.BD_PATTERN, "A");
			BasedataType bd_2 = new BasedataType("坑外降水", 2, Constants.BD_PATTERN, "B");
			baseDataService.create(bd_1);
			baseDataService.create(bd_2);
		}
		
		//紧急程度
		Entitites<BasedataType> degree = baseDataService.getListByAttributes(new String[]{"type"}, new String[]{Constants.MON_PROBLEM_DEGREE}, null);
		if(degree==null || degree.getEntites()==null || degree.getEntites().size()<1) {
			
			BasedataType bd_1 = new BasedataType("重要", 1, Constants.MON_PROBLEM_DEGREE, "A");
			BasedataType bd_2 = new BasedataType("一般", 2, Constants.MON_PROBLEM_DEGREE, "B");
			baseDataService.create(bd_1);
			baseDataService.create(bd_2);
		}
		
		//降水类型
		Entitites<BasedataType> dtype = baseDataService.getListByAttributes(new String[]{"type"}, new String[]{Constants.DD_TYPE}, null);
		if(dtype==null || dtype.getEntites()==null || dtype.getEntites().size()<1) {
			
			BasedataType bd_1 = new BasedataType("标准", 1, Constants.DD_TYPE, "A");
			BasedataType bd_2 = new BasedataType("论文", 2, Constants.DD_TYPE, "B");
			BasedataType bd_3 = new BasedataType("期刊", 3, Constants.DD_TYPE, "C");
			baseDataService.create(bd_1);
			baseDataService.create(bd_2);
			baseDataService.create(bd_3);
		}
		//降水类型
		Entitites<BasedataType> pptype = baseDataService.getListByAttributes(new String[]{"type"}, new String[]{Constants.BD_TYPE}, null);
		if(pptype==null || pptype.getEntites()==null || pptype.getEntites().size()<1) {
			
			BasedataType bd_1 = new BasedataType("降压", 1, Constants.BD_TYPE, "A");
			BasedataType bd_2 = new BasedataType("疏干", 2, Constants.BD_TYPE, "B");
			baseDataService.create(bd_1);
			baseDataService.create(bd_2);
		}
		
		//降压幅度
		Entitites<BasedataType> prange = baseDataService.getListByAttributes(new String[]{"type"}, new String[]{Constants.BD_RANGE}, null);
		if(prange==null || prange.getEntites()==null || prange.getEntites().size()<1) {
			
			BasedataType bd_1 = new BasedataType("》20m", 1, Constants.BD_RANGE, "A");
			BasedataType bd_2 = new BasedataType("》15m", 2, Constants.BD_RANGE, "B");
			BasedataType bd_3 = new BasedataType("》10m", 3, Constants.BD_RANGE, "C");
			BasedataType bd_4 = new BasedataType("》5m", 4, Constants.BD_RANGE, "D");
			BasedataType bd_5 = new BasedataType("《5m", 5, Constants.BD_RANGE, "E");
			BasedataType bd_6 = new BasedataType("无", 6, Constants.BD_RANGE, "F");
			baseDataService.create(bd_1);
			baseDataService.create(bd_2);
			baseDataService.create(bd_3);
			baseDataService.create(bd_4);
			baseDataService.create(bd_5);
			baseDataService.create(bd_6);
		}
		
		//降压幅度
		Entitites<BasedataType> Layer = baseDataService.getListByAttributes(new String[]{"type"}, new String[]{Constants.BD_LAYER}, null);
		if(Layer==null || Layer.getEntites()==null || Layer.getEntites().size()<1) {
			
			BasedataType bd_1 = new BasedataType("圆砾层", 1, Constants.BD_LAYER, "A");
			BasedataType bd_2 = new BasedataType("粗砂", 2, Constants.BD_LAYER, "B");
			BasedataType bd_3 = new BasedataType("中砂", 3, Constants.BD_LAYER, "C");
			BasedataType bd_4 = new BasedataType("细砂", 4, Constants.BD_LAYER, "D");
			BasedataType bd_5 = new BasedataType("粉砂", 5, Constants.BD_LAYER, "E");
			BasedataType bd_6 = new BasedataType("砂质粉土", 6, Constants.BD_LAYER, "F");
			BasedataType bd_9 = new BasedataType("粉土", 7, Constants.BD_LAYER, "G");
			BasedataType bd_10 = new BasedataType("粉质粘土", 8, Constants.BD_LAYER, "H");
			BasedataType bd_7 = new BasedataType("粘土", 9, Constants.BD_LAYER, "I");
			BasedataType bd_8 = new BasedataType("淤泥质土", 10, Constants.BD_LAYER, "J");
			baseDataService.create(bd_1);
			baseDataService.create(bd_2);
			baseDataService.create(bd_3);
			baseDataService.create(bd_4);
			baseDataService.create(bd_5);
			baseDataService.create(bd_6);
			baseDataService.create(bd_7);
			baseDataService.create(bd_8);
			baseDataService.create(bd_9);
			baseDataService.create(bd_10);
		}
		//承压水分类
		Entitites<BasedataType> confined = baseDataService.getListByAttributes(new String[]{"type"}, new String[]{Constants.BD_CONFINED}, null);
		if(confined==null || confined.getEntites()==null || confined.getEntites().size()<1) {
			
			BasedataType bd_1 = new BasedataType("微承压水", 1, Constants.BD_CONFINED, "A");
			BasedataType bd_2 = new BasedataType("第I承压水", 2, Constants.BD_CONFINED, "B");
			BasedataType bd_3 = new BasedataType("第II承压水", 3, Constants.BD_CONFINED, "C");
			BasedataType bd_4 = new BasedataType("第III承压水", 4, Constants.BD_CONFINED, "D");
			BasedataType bd_5 = new BasedataType("第IV承压水", 5, Constants.BD_CONFINED, "E");
			baseDataService.create(bd_1);
			baseDataService.create(bd_2);
			baseDataService.create(bd_3);
			baseDataService.create(bd_4);
			baseDataService.create(bd_5);
		}
		//承压水分类
		Entitites<BasedataType> shentong = baseDataService.getListByAttributes(new String[]{"type"}, new String[]{"shentong"}, null);
		if(shentong==null || shentong.getEntites()==null || shentong.getEntites().size()<1) {
			
			BasedataType bd_1 = new BasedataType("申通令牌", 1,"shentong", "IKJimnZTch");
			baseDataService.create(bd_1);
		}
		//承压水分类
		Entitites<BasedataType> precipitation = baseDataService.getListByAttributes(new String[]{"type"}, new String[]{Constants.BD_PRECIPITATION}, null);
		if(precipitation==null || precipitation.getEntites()==null || precipitation.getEntites().size()<1) {
			
			BasedataType bd_1 = new BasedataType("○23", 1, Constants.BD_PRECIPITATION, "A");
			BasedataType bd_2 = new BasedataType("○42", 2, Constants.BD_PRECIPITATION, "B");
			BasedataType bd_3 = new BasedataType("○52", 3, Constants.BD_PRECIPITATION, "C");
			BasedataType bd_4 = new BasedataType("○53-2", 4, Constants.BD_PRECIPITATION, "D");
			BasedataType bd_5 = new BasedataType("○71", 5, Constants.BD_PRECIPITATION, "E");
			BasedataType bd_6 = new BasedataType("○72", 1, Constants.BD_PRECIPITATION, "F");
			BasedataType bd_7 = new BasedataType("○82", 2, Constants.BD_PRECIPITATION, "G");
			BasedataType bd_8 = new BasedataType("○91", 3, Constants.BD_PRECIPITATION, "H");
			BasedataType bd_9 = new BasedataType("○92", 4, Constants.BD_PRECIPITATION, "I");
			BasedataType bd_10 = new BasedataType("○10j", 5, Constants.BD_PRECIPITATION, "J");
			BasedataType bd_11 = new BasedataType("○11", 3, Constants.BD_PRECIPITATION, "K");
			BasedataType bd_12 = new BasedataType("○13", 4, Constants.BD_PRECIPITATION, "L");
			BasedataType bd_13 = new BasedataType("其他", 5, Constants.BD_PRECIPITATION, "M");
			baseDataService.create(bd_1);
			baseDataService.create(bd_2);
			baseDataService.create(bd_3);
			baseDataService.create(bd_4);
			baseDataService.create(bd_5);
			baseDataService.create(bd_6);
			baseDataService.create(bd_7);
			baseDataService.create(bd_8);
			baseDataService.create(bd_9);
			baseDataService.create(bd_10);
			baseDataService.create(bd_11);
			baseDataService.create(bd_12);
			baseDataService.create(bd_13);
		}
		//报表报送公司
		Entitites<BasedataType> reportTo = baseDataService.getListByAttributes(new String[]{"type"}, new String[]{Constants.RP_COMPANY}, null);
		if(reportTo==null || reportTo.getEntites()==null || reportTo.getEntites().size()<1) {
			
			BasedataType bd_1 = new BasedataType("上海轨道交通18号线发展有限公司", 1, Constants.RP_COMPANY, "CA1");
			BasedataType bd_2 = new BasedataType("上海建浩工程顾问有限公司", 2, Constants.RP_COMPANY, "CA2");
			BasedataType bd_3 = new BasedataType("中铁十五局集团有限公司", 3, Constants.RP_COMPANY, "CA3");
			baseDataService.create(bd_1);
			baseDataService.create(bd_2);
			baseDataService.create(bd_3);
		}
		//管线配置
		Entitites<BasedataType> gline = baseDataService.getListByAttributes(new String[]{"type"}, new String[]{Constants.BD_MONITOR_LINE}, null);
		if(gline==null || gline.getEntites()==null || gline.getEntites().size()<1) {
			
			BasedataType bd_2 = new BasedataType("上水", 2, Constants.BD_MONITOR_LINE, "SS");
			BasedataType bd_3 = new BasedataType("燃气", 3, Constants.BD_MONITOR_LINE, "RQ");
			BasedataType bd_4 = new BasedataType("雨水", 3, Constants.BD_MONITOR_LINE, "YS");
			BasedataType bd_5 = new BasedataType("污水", 3, Constants.BD_MONITOR_LINE, "WS");
			BasedataType bd_6 = new BasedataType("信息", 3, Constants.BD_MONITOR_LINE, "XX");
			BasedataType bd_7 = new BasedataType("军通", 3, Constants.BD_MONITOR_LINE, "JT");
			BasedataType bd_8 = new BasedataType("电力", 3, Constants.BD_MONITOR_LINE, "DL");
			baseDataService.create(bd_2);
			baseDataService.create(bd_3);
			baseDataService.create(bd_4);
			baseDataService.create(bd_5);
			baseDataService.create(bd_6);
			baseDataService.create(bd_7);
			baseDataService.create(bd_8);
		}
		//支撑类型
		Entitites<BasedataType> zcheng = baseDataService.getListByAttributes(new String[]{"type"}, new String[]{Constants.BD_MONITOR_ZHICHENG}, null);
		if(zcheng==null || zcheng.getEntites()==null || zcheng.getEntites().size()<1) {
			
			BasedataType bd_2 = new BasedataType("钢支撑", 1, Constants.BD_MONITOR_ZHICHENG, "S");
			BasedataType bd_3 = new BasedataType("砼支撑", 2, Constants.BD_MONITOR_ZHICHENG, "X");
			baseDataService.create(bd_2);
			baseDataService.create(bd_3);
		}
		//监护类型上行，下行
		Entitites<BasedataType> guardtype = baseDataService.getListByAttributes(new String[]{"type"}, new String[]{Constants.BD_GUARD_TYPE}, null);
		if(guardtype==null || guardtype.getEntites()==null || guardtype.getEntites().size()<1) {
			
			BasedataType bd_2 = new BasedataType("上行线", 1, Constants.BD_GUARD_TYPE, "S");
			BasedataType bd_3 = new BasedataType("下行线", 2, Constants.BD_GUARD_TYPE, "X");
			baseDataService.create(bd_2);
			baseDataService.create(bd_3);
		}
		//问题类型
		Entitites<BasedataType> protype = baseDataService.getListByAttributes(new String[]{"type"}, new String[]{Constants.BD_GUARD_PTYPE}, null);
		if(protype==null || protype.getEntites()==null || protype.getEntites().size()<1) {
			
			BasedataType bd_2 = new BasedataType("设备问题", 1, Constants.BD_GUARD_PTYPE, "SHEBEI");
			BasedataType bd_3 = new BasedataType("人员问题", 2, Constants.BD_GUARD_PTYPE, "RENYUAN");
			baseDataService.create(bd_2);
			baseDataService.create(bd_3);
		}
		//监护项目结构
		Entitites<BasedataType> pstruct = baseDataService.getListByAttributes(new String[]{"type"}, new String[]{Constants.BD_GUARD_STRUCTURE}, null);
		if(pstruct==null || pstruct.getEntites()==null || pstruct.getEntites().size()<1) {
			BasedataType bd_1 = new BasedataType("高架", 1, Constants.BD_GUARD_STRUCTURE, "1");
			BasedataType bd_2 = new BasedataType("隧道", 1, Constants.BD_GUARD_STRUCTURE, "2");
			BasedataType bd_3 = new BasedataType("地下车站", 2, Constants.BD_GUARD_STRUCTURE, "3");
			baseDataService.create(bd_1);
			baseDataService.create(bd_2);
			baseDataService.create(bd_3);
		}
		//监护项目状态
		Entitites<BasedataType> gstatus = baseDataService.getListByAttributes(new String[]{"type"}, new String[]{Constants.BD_GUARD_STATUS}, null);
		if(gstatus==null || gstatus.getEntites()==null || gstatus.getEntites().size()<1) {
			BasedataType bd_1 = new BasedataType("桩基施工", 1, Constants.BD_GUARD_STATUS, "1");
			BasedataType bd_2 = new BasedataType("土体加固", 2, Constants.BD_GUARD_STATUS, "2");
			BasedataType bd_3 = new BasedataType("围护结构", 3, Constants.BD_GUARD_STATUS, "3");
			BasedataType bd_4 = new BasedataType("降水", 4, Constants.BD_GUARD_STATUS, "4");
			BasedataType bd_5 = new BasedataType("土方开挖", 5, Constants.BD_GUARD_STATUS, "5");
			BasedataType bd_6 = new BasedataType("地下结构回筑", 6, Constants.BD_GUARD_STATUS, "6");
			BasedataType bd_7 = new BasedataType("地上结构施工", 7, Constants.BD_GUARD_STATUS, "7");
			BasedataType bd_8 = new BasedataType("沟槽开挖(管线改排)", 8, Constants.BD_GUARD_STATUS, "8");
			BasedataType bd_9 = new BasedataType("竣工后", 9, Constants.BD_GUARD_STATUS, "0");
			bd_9.setSys(true);
			baseDataService.create(bd_2);
			baseDataService.create(bd_3);
			baseDataService.create(bd_1);
			baseDataService.create(bd_4);
			baseDataService.create(bd_5);
			baseDataService.create(bd_6);
			baseDataService.create(bd_7);
			baseDataService.create(bd_8);
			baseDataService.create(bd_9);
		}
		//监护项目定时提醒时间
		Entitites<BasedataType> alarmtime = baseDataService.getListByAttributes(new String[]{"type"}, new String[]{Constants.BD_GUARD_ALARMTIME}, null);
		if(alarmtime==null || alarmtime.getEntites()==null || alarmtime.getEntites().size()<1) {
			
			BasedataType bd_2 = new BasedataType("监护项目定时提醒时间(例如:13:30:00)", 1, Constants.BD_GUARD_ALARMTIME, "11:30:00");
			baseDataService.create(bd_2);
		}else{
			String [] strs =alarmtime.getEntites().get(0).getValue().split(":");
			//0 30 11 * * ?
			StringBuffer sd =new StringBuffer();
			if(strs.length>=3){
				sd.append(strs[2]);
			}else{
				sd.append("0");
			}
			if(strs.length>=2){
				sd.append(" "+strs[1]);
			}else{
				sd.append(" 0");
			}
			if(strs.length>=1){
				sd.append(" "+strs[0]).append(" * * ?");
				job.restJob(sd.toString(), "autoSendTrigger");
			}
		}
	}
	
	private void loadData() throws ServiceException {
		
		String loadTestData = configuration.getProperty(ApplicationConstants.POPULATE_TEST_DATA);
		boolean loadData =  !StringUtils.isBlank(loadTestData) && loadTestData.equals(SystemConstants.CONFIG_VALUE_TRUE);
		
		if(loadData) {
			SystemConfiguration configuration = systemConfigurationService.getByKey(ApplicationConstants.TEST_DATA_LOADED);
		
			if(configuration!=null) {
					if(configuration.getKey().equals(ApplicationConstants.TEST_DATA_LOADED)) {
						if(configuration.getValue().equals(SystemConstants.CONFIG_VALUE_TRUE)) {
							return;		
						}
					}		
			}
			configuration = new SystemConfiguration();
			configuration.getAuditSection().setModifiedBy(SystemConstants.SYSTEM_USER);
			configuration.setKey(ApplicationConstants.TEST_DATA_LOADED);
			configuration.setValue(SystemConstants.CONFIG_VALUE_TRUE);
			systemConfigurationService.create(configuration);
		}
	}

	/**
	 * 定义加载bean的优先级，值越大，优先级越高
	 * @return
	 */
	@Override
	public int getOrder() {
		return 99;
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		reloadMemoryData.setContext(servletContext);
		reloadMemoryData.reload();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		init();
	}
}
