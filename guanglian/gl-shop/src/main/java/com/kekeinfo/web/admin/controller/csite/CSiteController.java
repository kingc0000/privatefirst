package com.kekeinfo.web.admin.controller.csite;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.google.zxing.WriterException;
import com.kekeinfo.core.business.camera.model.Camera;
import com.kekeinfo.core.business.camera.service.CameraService;
import com.kekeinfo.core.business.common.model.BasedataType;
import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.constructionsite.model.ConstructionSite;
import com.kekeinfo.core.business.content.model.FileContentType;
import com.kekeinfo.core.business.csite.service.CSiteService;
import com.kekeinfo.core.business.daily.model.Daily;
import com.kekeinfo.core.business.daily.service.DailyService;
import com.kekeinfo.core.business.department.model.Department;
import com.kekeinfo.core.business.department.model.DepartmentNode;
import com.kekeinfo.core.business.department.service.DepartmentNodeService;
import com.kekeinfo.core.business.department.service.DepartmentService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.image.model.Images;
import com.kekeinfo.core.business.last.model.CsiteLast;
import com.kekeinfo.core.business.last.service.CsiteLastService;
import com.kekeinfo.core.business.observewell.service.ObservewellService;
import com.kekeinfo.core.business.point.model.Basepoint;
import com.kekeinfo.core.business.project.model.Project;
import com.kekeinfo.core.business.project.service.ProjectService;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.business.user.service.UserService;
import com.kekeinfo.core.business.zone.model.Zone;
import com.kekeinfo.core.business.zone.service.ZoneService;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.admin.security.WebUserServices;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.DataTable;
import com.kekeinfo.web.entity.PinYin;
import com.kekeinfo.web.entity.UnderWater;
import com.kekeinfo.web.entity.filter.PointInfoFilter;
import com.kekeinfo.web.event.SpringContextUtils;
import com.kekeinfo.web.filter.UserFilter;
import com.kekeinfo.web.utils.DataTableParameter;
import com.kekeinfo.web.utils.DataUtils;
import com.kekeinfo.web.utils.ImageFilePathUtils;
import com.kekeinfo.web.utils.ImageUtils;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.PinyinUtils;
import com.kekeinfo.web.utils.QCodeUtils;
import com.kekeinfo.web.utils.RadomSixNumber;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;

@Controller
public class CSiteController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CSiteController.class);
	
	@Autowired
	ZoneService zoneService;
	
	@Autowired CSiteService cSiteService;
	
	@Autowired DepartmentService departmentService;
	
	@Autowired UserService userService;
	
	@Autowired DepartmentNodeService departmentNodeService;
	
	@Autowired GroupService groupService;
	
	@Autowired DataUtils dataUtils;
	
	@Autowired ObservewellService owellService;
	
	@Autowired CameraService cameraService;
	
	@Autowired CsiteLastService csiteLastService;
	
	@Autowired ProjectService projectService;
	
	@Autowired QCodeUtils qCodeUtils;
	
	@Autowired private WebUserServices userDetailsService;
	
	@Autowired
	private DailyService dailyService;
	@Autowired private PNodeUtils pnodeUtils;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/csite/list.html", method=RequestMethod.GET)
	public String displayStores(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
       
		
		setMenu(model,request);
		List<Zone> zones =  zoneService.getAllZone();
		ConstructionSite csite = new ConstructionSite();
		model.addAttribute("csite", csite);
		model.addAttribute("zones", zones);
		
		//判断用户是否有该项目的编辑权限
        boolean hasRight = false;
        try {
        	if (request.isUserInRole("ADMIN") || request.isUserInRole("SUPERADMIN")) {
        		hasRight = true;
        	} else {
        		if (request.isUserInRole("EDIT-PROJECT")) {
        			Department de=(Department) webCacheUtils.getFromCache(Constants.DEPARTMENT_WATER);
        			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
        			hasRight = pnodeUtils.hasProjectRight(user.getpNodes(), de);
        		}
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("hasRight", hasRight);
		return "water-csites";
	}
	
	/**
	 * 地下水部门项目查看
	 * @param model
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/csite/wlist.html", method=RequestMethod.GET)
	public String displayWProjects(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!request.isUserInRole("ADMIN") && !request.isUserInRole("SUPERADMIN")) {
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			Department waterDept = (Department) webCacheUtils.getFromCache(Constants.DEPARTMENT_WATER); //地下水部门
			boolean find = pnodeUtils.hasProjectRight(user.getpNodes(), waterDept);
			if(!find){
				return "redirect:/water/home.html";
			}
		}
		
		setWaterMenu(model,request);
		//加载工程日志内容
		request.setAttribute("daily", new Daily());
		request.setAttribute("nowdate", DateFormatUtils.format(new Date(), "yyyy-MM-dd"));

		//是否某个具体的项目
		String pid = request.getParameter("pid");
		if(!StringUtils.isBlank(pid)){
			
			//ConstructionSite csite =cSiteService.getById(Long.parseLong(pid));
			List<UnderWater> cs = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
			UnderWater csite=null;
			for(UnderWater c:cs){
				if(c.getId().equals(Long.parseLong(pid))){
					csite=c;
					break;
				}
			}
			if(csite==null){
				csite=pnodeUtils.getByCid(Long.parseLong(pid));
			}
			request.setAttribute("activePid", pid); //指定项目
			request.setAttribute("activeZone", csite.getZone()); //指定项目
			request.setAttribute("activeFun", "monitor");  //指定当前操作功能
			model.addAttribute("pid", pid);
			model.addAttribute("csite", csite);
			
			//判断用户是否有该项目的编辑权限
	        boolean hasRight = false;
	        try {
	        	if (request.isUserInRole("ADMIN") || request.isUserInRole("SUPERADMIN")) {
	        		hasRight = true;
	        	} else {
	        		if (request.isUserInRole("EDIT-PROJECT")) {
	        			List<Group> egroups = groupService.listGroupByPermissionName("EDIT-PROJECT",1);
	        			hasRight = pnodeUtils.hasProjectRight(request, egroups, csite);
	        		}
	        	}
			} catch (Exception e) {
				e.printStackTrace();
			}
			model.addAttribute("hasRight", hasRight);
			
			CsiteLast clast = new CsiteLast();
			clast.setCid(csite.getId());
			clast.setDateModified(new Date());
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			clast.setUid(user.getId());
			csiteLastService.saveNew(clast);
		}else {
			//加载项目集合
			HashMap<String, Set<UnderWater>> set  = pnodeUtils.getWaterCsitesZone(request);
			request.setAttribute("projects", set);
			model.addAttribute("pid", -1);
		}
		//加载刷新时间
		List<BasedataType> rlist = (List<BasedataType>) SpringContextUtils.getServletContext().getAttribute(Constants.FRESHTIME);
		if(rlist.isEmpty()){
			model.addAttribute("rfreshtime", "60");
		}else {
			model.addAttribute("rfreshtime", rlist.get(0).getValue());
		}
		return "csite-wlist";
	}
	
	/**
	 * 编辑项目
	 * @param model
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/csite/toEdit.html", method=RequestMethod.GET)
	public String toEdit(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setWaterMenu(model,request);
		String cid = request.getParameter("cid");
		
		ConstructionSite csite =cSiteService.getByCid(Long.parseLong(cid));
		request.setAttribute("activeFun", "toEdit");  //指定当前操作功能
		
		//判断用户是否有该项目的编辑权限
        boolean hasRight = false;
        try {
        	if (request.isUserInRole("ADMIN") || request.isUserInRole("SUPERADMIN")) {
        		hasRight = true;
        	} else {
        		if (request.isUserInRole("EDIT-PROJECT")) {
        			List<Group> egroups = groupService.listGroupByPermissionName("EDIT-PROJECT",1);
        			hasRight = pnodeUtils.hasProjectRight(request, egroups, csite.getProject());
        		}
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("hasRight", hasRight);
		
		model.addAttribute("csite", csite);
		String ctype = request.getParameter("ctype");
		//手机版
		if(StringUtils.isNotBlank(ctype)){
			UnderWater un = pnodeUtils.getByCid(Long.parseLong(cid),request);
			model.addAttribute("project", un);
			return "phone-wedit";
		}
		Project project =projectService.withUserGroup(csite.getProject().getId());
		model.addAttribute("cusers", project.getcUsers());
		model.addAttribute("wusers", project.getwUsers());
		return "csite-wedit";
	}
	
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/csite/server_processing.shtml", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getCsites(@RequestParam String aoData,HttpServletRequest request,
			HttpServletResponse response) {
		AjaxResponse resp = new AjaxResponse();
		DataTableParameter dataTableParam = new DataTableParameter(aoData);
		DataTable<ConstructionSite> dt = new DataTable<ConstructionSite>();
		try {
			//指定根据什么条件进行模糊查询
			List<String> attributes = new ArrayList<String>();
			attributes.add("project.name"); 
			attributes.add("project.projectOwner"); 
			attributes.add("project.phone");
//			attributes.add("memo");
			attributes.add("project.address");
			attributes.add("project.zone.name");
			attributes.add("project.city");
			//HashMap<String, String> orderby = new HashMap<String, String>();
			//orderby.put("project.name", "desc");
			
			List<String> join =new ArrayList<String>();
			join.add("project");
			join.add("pbase");
			//join.add("images");
			Department de=(Department) webCacheUtils.getFromCache(Constants.DEPARTMENT_WATER);
			
			List<String> where = pnodeUtils.getCids(request, de.getId(),0);
			
			Entitites<ConstructionSite> list  = cSiteService.getPageListByAttributesLike(attributes, dataTableParam.getsSearch(), Long.valueOf(dataTableParam.getiDisplayLength()).intValue(), 
					Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), null,where,join);
			
			List<Group> egroups = groupService.listGroupByPermissionName("EDIT-PROJECT",1);
			
			dt.setsEcho(dataTableParam.getsEcho()+1);
			dt.setiTotalDisplayRecords(list.getTotalCount());
	        dt.setiTotalRecords(list.getTotalCount());
	        dt.setAaData(pnodeUtils.setEditCiste(request, egroups, list.getEntites()));
	        if(!request.isUserInRole("ADMIN") && !request.isUserInRole("SUPERADMIN") && !request.isUserInRole("EDIT-PROJECT")){
				dt.setHasRight(false);
			}else{
				dt.setHasRight(true);
			}
	        ObjectMapper mapper = new ObjectMapper();
	        mapper.getSerializationConfig().addMixInAnnotations(User.class, UserFilter.class);
			String json = mapper.writeValueAsString(dt);
			return json;
		} catch (Exception e) {
			LOGGER.error("Error while paging csite", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		return null;
	}
	
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value="/water/csite/active.shtml", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String active(HttpServletRequest request, Locale locale) throws Exception {
		
		String sUserId = request.getParameter("listId");

		AjaxResponse resp = new AjaxResponse();
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		
		try {
			ConstructionSite csite = cSiteService.getById(Long.parseLong(sUserId));
			if(csite!=null){ //成井和结束两种状态切换
				if(csite.getStatus()==0){
					csite.setStatus(-1);
				}else if(csite.getStatus()==-1){
					csite.setStatus(0);
				}
				cSiteService.saveOrUpdate(csite);
				resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
				resp.setStatusMessage("操作成功");
			}
		
		} catch (Exception e) {
			LOGGER.error("Error while deleting product price", e);
			//resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		
		return returnString;
		
	}
	
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value="/water/csite/images.shtml", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String images(HttpServletRequest request, Locale locale) throws Exception {
		
		String sUserId = request.getParameter("cid");

		//AjaxResponse resp = new AjaxResponse();
		//resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		
		try {
			ConstructionSite csite = cSiteService.getByIdWithImg(Long.parseLong(sUserId));
			Map<String, Object> result = new HashMap<>();
			if(csite!=null){
				if(csite.getImages()!=null && csite.getImages().size()>0){
					result.put("img", csite.getImages());
					
				}
				Project project =projectService.withUserGroup(csite.getProject().getId());
				if(project.getcUsers()!=null && project.getcUsers().size()>0){
					List<com.kekeinfo.web.entity.User> cuser = new ArrayList<>();
					for(User cu:project.getcUsers()){
						com.kekeinfo.web.entity.User fuser = new com.kekeinfo.web.entity.User();
						fuser.setId(cu.getId());
						fuser.setName(cu.getFirstName());
						cuser.add(fuser);
					}
					result.put("cusers", cuser);
				}
				if(project.getwUsers()!=null && project.getwUsers().size()>0){
					List<com.kekeinfo.web.entity.User> wuser = new ArrayList<>();
					for(User cu:project.getwUsers()){
						com.kekeinfo.web.entity.User fuser = new com.kekeinfo.web.entity.User();
						fuser.setId(cu.getId());
						fuser.setName(cu.getFirstName());
						wuser.add(fuser);
					}
					result.put("wusers", wuser);
				}
				if(result.size()>0){
					ObjectMapper mapper = new ObjectMapper();
					String json = mapper.writeValueAsString(result);
					return json;
				}	
			}
			
		} catch (Exception e) {
			LOGGER.error("Error while deleting product price", e);
			return "-1";
		}
		
		return "0";
		
	}
	
	@PreAuthorize("hasRole('EDIT-PROJECT')") 
	@RequestMapping(value="/water/csite/save.shtml", method=RequestMethod.POST, produces="application/json; charset=utf-8")
	public @ResponseBody String save(@ModelAttribute("csite") ConstructionSite csite, Model model,@RequestParam(required=false, value="fileupload") List<CommonsMultipartFile> uploadfiles, HttpServletRequest request, HttpServletResponse response)  {
		AjaxResponse resp = new AjaxResponse();
		
		try {
			String delids= request.getParameter("delids");
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			Department waterDept = (Department) webCacheUtils.getFromCache(Constants.DEPARTMENT_WATER); //地下水部门
			if (csite.getProject().getDepartment().getId()==null) {
				csite.getProject().getDepartment().setId(waterDept.getId());; //设定地下水部门
			}
			if(user!=null){
				csite.getProject().getAuditSection().setModifiedBy(user.getId().toString());
				if(csite.getId()==null){
					csite.getProject().getAuditSection().setDateCreated(new Date());
				}else{
					csite.getProject().getAuditSection().setDateModified(new Date());
				}
				//之前的用户如果有修改需要去掉权限
				//附件处理
				if(uploadfiles!=null && !uploadfiles.isEmpty()){
					List<Images> imgs= new ArrayList<>();
					for(CommonsMultipartFile uploadfile:uploadfiles){
						Images img = new Images();
						img.setName(RadomSixNumber.getImageName(uploadfile.getOriginalFilename())); 
						img.setDigital(uploadfile.getInputStream());
						//压缩
						img.setJpeg(RadomSixNumber.getImageName(img.getName()));
						InputStream inputStream = uploadfile.getInputStream();
						img.setJdigital(ImageUtils.ByteArrayOutputStream(inputStream));
						imgs.add(img);
					}
					csite.setImages(imgs);
				}
				User puser =null;
				//部门负责人从用户选择过来的
				Long uid = csite.getProject().getProjectOwnerid();
				if(uid!=null){
					 puser = userService.getById(uid);
					if(puser!=null){
						if (StringUtils.isNotBlank(csite.getProject().getProjectOwner())&&!csite.getProject().getProjectOwner().equals(puser.getFirstName())) { //手动输入的用户姓名，而非系统用户名
							csite.getProject().setProjectOwnerid(null);
						} else {
							csite.getProject().setProjectOwner(puser.getFirstName());
							csite.getProject().setPhone(puser.getTelephone());
						}
					}
				}
				if(csite.getPbase()!=null){
					csite.getPbase().setCsite((csite));
				}
				//获取评论信息
				String[] cuids =request.getParameterValues("cusers");
				String[] wuids =request.getParameterValues("wusers");
				//判断项目是否结束了，如果结束删除相应的权限,并找到相对于的用户
				List<DepartmentNode> pNodes=null;
				if(csite.getStatus()==-1){
					pNodes =departmentNodeService.getByTypePID(csite.getProject().getId());
				}
				cSiteService.saveOrUpdateWithRight(csite,puser,delids,cuids,wuids,pNodes);
				pnodeUtils.reloadAllDepartments();
				pnodeUtils.reloadAllCsite(request);
				pnodeUtils.relaodAllGroupUser();
				if(StringUtils.isNotBlank(csite.getqCode())){
					this.saveQCode(request, csite);
				}
				//设置成功后清除相应用户的session，让其重新登录
				if(pNodes!=null && pNodes.size()>0){
					for(DepartmentNode p:pNodes){
						userDetailsService.removeSession(p.getUser());
					}
				}
			}
			//
		} catch (ServiceException | IOException | WriterException e) {
			LOGGER.error("Error while save banner ", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		return returnString;
	}
	
	
	@RequestMapping(value="/water/csite/checkCsiteCode.shtml", method=RequestMethod.GET, produces="application/json;charset=UTF-8")
	public @ResponseBody String checkValidCode(@RequestParam String id, HttpServletRequest request, HttpServletResponse response) {
		String domain=request.getParameter("name");
		AjaxResponse resp = new AjaxResponse();
		if(StringUtils.isBlank(domain)) {
			resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
			return resp.toJSONString();
		}
		try {
			List<String> attributes = new ArrayList<String>();
			attributes.add("name");
			List<String> fvalues = new ArrayList<String>();
			fvalues.add(domain);
			Entitites<ConstructionSite> csite = cSiteService.getPageListByAttributes(attributes, fvalues, null, null, null);
			if(!StringUtils.isBlank(id)) {
				try {
					Long lid = Long.parseLong(id);
					if(csite == null || (csite!=null && csite.getEntites().size()>0 && csite.getEntites().get(0).getProject().getName().equals(domain) && csite.getEntites().get(0).getId().longValue()==lid)) {
						resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
						return resp.toJSONString();
					} 
				} catch (Exception e) {
					resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
					return resp.toJSONString();
				}
			} else {
				if(csite==null) {
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
					return resp.toJSONString();
				}
			}
			resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
			} catch (Exception e) {
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorMessage(e);
			}
			
			String returnString = resp.toJSONString();
			return returnString;
	} 

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value="/water/csite/remove.shtml", method=RequestMethod.POST, produces="application/json;charset=utf-8")
	public @ResponseBody List<AjaxResponse> remove(@RequestParam String[] listId,HttpServletRequest request, Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId!=null&&listId.length>0) {
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.parseLong(listId[i]);
				try {
					cSiteService.deleteWithPermission(id);
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
					resplist.add(resp);
				} catch (Exception e) {
					if(e.getMessage().startsWith("Cannot delete or update a parent row")){
						resp.setStatus(9997);
					}else{
						resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
					}
					e.printStackTrace();
					resplist.add(resp);
				}
			}
			pnodeUtils.reloadAllCsite(request);
		}
		return resplist;
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/csite/getWellData.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=utf-8")
	public @ResponseBody String WellData(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String cid = request.getParameter("cid");
		if(StringUtils.isNotBlank(cid)){
			try{
				Map<String, Object> dataResult = dataUtils.getWellSitesByCid(Long.parseLong(cid));
				ConstructionSite csite = cSiteService.getById(Long.parseLong(cid));
				dataResult.put("rail", csite.getRail());
				//dataResult.put("csite", csite);
//				var point = new BMap.Point(data.longitude,data.latitude);
				
				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(dataResult);
				return json;
			}catch (Exception e){
				e.printStackTrace();
				LOGGER.error(e.getMessage());
			}
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/csite/getCamera.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=utf-8")
	public @ResponseBody String cameraData(HttpServletRequest request, HttpServletResponse response, Locale locale) throws ServiceException {
		String cid = request.getParameter("pid");
		if(StringUtils.isNotBlank(cid)){
			List<UnderWater> cs = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
			UnderWater csite=null;
			for(UnderWater c:cs){
				if(c.getId().equals(Long.parseLong(cid))){
					csite=c;
					break;
				}
			}
			if(csite==null){
				try {
					csite = pnodeUtils.getByCid(Long.parseLong(cid));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			List<Object[]> where =new ArrayList<>();
			where.add(new Object[]{"project.id", csite.getPid()});
			Entitites<Camera> list = cameraService.getPageListByAttributesLike(null,null, null, null,null, where, null, true);
			if(list!=null && !list.getEntites().isEmpty()){
				List<Camera> cameras = list.getEntites();
				List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
				for (Camera camera : cameras) {
					Map<String, Object> entry = new HashMap<String, Object>();
					entry.put("id", camera.getId());
					entry.put("name", camera.getNote());
					entry.put("status", camera.isStatus());
					entry.put("longitude", camera.getLongitude());
					entry.put("latitude", camera.getLatitude());
					resultList.add(entry);
				}
				ObjectMapper mapper = new ObjectMapper();
				String json;
				try {
					json = mapper.writeValueAsString(resultList);
					return json;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					LOGGER.error(e.getMessage());
					e.printStackTrace();
				} 
			}
		}
		return "";
	}
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/csite/getWarning.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=utf-8")
	public @ResponseBody String WarningData(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String cid = request.getParameter("pid");
		
		try{
			Map<String, Object> dataResult = null;
			if(StringUtils.isNotBlank(cid)){
				dataResult = dataUtils.getWellDataByCid(Long.parseLong(cid), true);
			}
			List<Long> ids =null;
			//if (!request.isUserInRole("ADMIN") && !request.isUserInRole("SUPERADMIN")) {
				List<UnderWater> set  = pnodeUtils.getWaterCsites(request);
				if(set!=null && !set.isEmpty()){
					ids = new ArrayList<>();
					for(UnderWater s:set){
						ids.add(s.getId());
					}
				}
				
			//}
			List<UnderWater> cs = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
			dataResult =dataUtils.getWarningData(dataResult,ids,cs);
			//加载实时水位
			//dataResult =dataUtils.getStardingInfo(dataResult,ids);
			//判断用户权限
			List<Group> egroups = groupService.listGroupByPermissionName("EDIT-PROJECT",1);
			for(UnderWater c:cs){
				if(c.getId().equals(Long.parseLong(cid))){
					dataResult.put("hasright", pnodeUtils.hasProjectRight(request,egroups,c));
					break;
				}
			}
			//ConstructionSite csite = cSiteService.getByIdWithDepartment(Long.parseLong(cid));
			//dataResult.put("hasright", pnodeUtils.hasProjectRight(request,egroups,csite));
			ObjectMapper mapper = new ObjectMapper();
			mapper.getSerializationConfig().addMixInAnnotations(Basepoint.class, PointInfoFilter.class);
			mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
			String json = mapper.writeValueAsString(dataResult);
			return json;
		}catch (Exception e){
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		}
		
		
		return null;
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/csite/zones.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=utf-8")
	public @ResponseBody List<PinYin> zones(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		try{
			List<Object[]> stores = zoneService.getPinYin();
			List<PinYin> pinyin = PinyinUtils.getPinyinList(stores);
			return pinyin;
		}catch (Exception e){
			return null;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/csite/print.html", method = RequestMethod.GET)
	public String prints(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String ids =request.getParameter("ids");
		if(StringUtils.isNotBlank(ids)){
			try{
				List<Long> dids = new ArrayList<>();
				String [] is = ids.split(",");
				for(String s:is){
					dids.add(Long.parseLong(s));
				}
				List<ConstructionSite> cs = cSiteService.getByIds(dids);
				List<UnderWater> ps = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
				for (ConstructionSite csite : cs) {
					if (csite.getqCode()==null) {
						this.saveQCode(request, csite);
					}
					for(UnderWater p:ps){
						if(p.getId().equals(csite.getId())){
							csite.setName(p.getName());
							break;
						}
					}
					
					csite.setqCode(request.getContextPath() + ImageFilePathUtils.buildFilePathByConentType(csite.getqCode(), FileContentType.QCODE));
				}
				model.addAttribute("qcodes", cs);
			}catch(Exception e){
				LOGGER.error(e.getMessage());
			}
			
		}
		return "wells-print";
	}
	
	
	@RequestMapping(value = "/water/csite/projectinfo.html", method = RequestMethod.GET)
	public String projectinfo(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String cid =request.getParameter("cid");
		if(StringUtils.isNotBlank(cid)){
			try{
				ConstructionSite cs =cSiteService.getById(Long.parseLong(cid));
				if(cs!=null){
					model.addAttribute("project", cs.getProject());
					model.addAttribute("csite", cs);
				}
			}catch(Exception e){
				LOGGER.error(e.getMessage());
			}
			
		}
		return "water/projectinfo";
	}
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/csite/imgs.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=utf-8")
	public @ResponseBody List<Images> imags(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		try{
			String pid = request.getParameter("pid");
			if(StringUtils.isNotBlank(pid)){
				ConstructionSite csite = cSiteService.getByIdWithImg(Long.parseLong(pid));
				List<Images> imgs =csite.getImages();
				if(imgs!=null){
					for(Images img:imgs){
						img.setName(request.getContextPath() + ImageFilePathUtils.buildFilePathByConentType(img.getName(), FileContentType.PRODUCT_DIGITAL));
						if(StringUtils.isNotBlank(img.getJpeg())){
							img.setJpeg(request.getContextPath() + ImageFilePathUtils.buildFilePathByConentType(img.getJpeg(), FileContentType.PRODUCT_DIGITAL));
						}
					}
				}
				return imgs;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取项目每日信息
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 */
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/csite/conclusion.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=utf-8")
	public @ResponseBody Map<String, String> conclusion(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		Map<String, String> resp = new HashMap<String, String>();
		try{
			String pid = request.getParameter("pid");
			
			Calendar now = Calendar.getInstance();
			now.set(Calendar.HOUR_OF_DAY, 0);
			now.set(Calendar.MINUTE, 0);
			now.set(Calendar.SECOND, 0);
			now.set(Calendar.MILLISECOND, 0);
			
			HashMap<String, String> orderby = new HashMap<String, String>();
			orderby.put("datec", "desc");
			Entitites<Daily> list  = dailyService.getListByAttributes(new String[]{"cSite.id", "datec"}, new Object[]{Long.valueOf(pid), now.getTime()}, orderby);
			//第一次创建的情况
			if(list.getTotalCount()>0){
				Daily daily = list.getEntites().get(0);
				resp.put("conclusion", daily.getConclusion());
				resp.put("status", "0");
			}else{
				resp.put("status", "-1");
			}
			
		}catch (Exception e){
			e.printStackTrace();
			resp.put("status", "-1");
		}
		return resp;
	}
	
	/**
	 * 获取当前项目抽水井和回灌井的各个测点的当前状态，显示柱状图，读取当前表
	 * 获取当前项目抽水量和回灌量的平均值，显示饼图。（获取当天所有抽水井的流量总和，即将单井的平均流量求和，回灌井采用同样方式处理）
	 * @param request
	 * @param response
	 * @return
	 */
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/csite/getCheckpoints.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=utf-8")
	public @ResponseBody String getCheckpoints(@RequestParam(required=true, value="pid") String pid, HttpServletRequest request, HttpServletResponse response) {
		
		//Entitites<Invertedwell> iwellList = invertedwellService.getListByAttributes(new String[]{"cSite.id"}, new Object[]{Long.valueOf(pid)}, null);
		ObjectMapper mapper = new ObjectMapper();
		try {
			Map<String, Object> result = dataUtils.getCheckpoints(Long.parseLong(pid));
			String json = mapper.writeValueAsString(result);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "{status:-1}";
	}
	
	/**
	 * 获取当天测点的数据记录，提供前台页面展示曲线图
	 * @param cid
	 * @param type 测点类型，0抽水井，1观测井，2回灌井，3环境监测
	 * @param request
	 * @param response
	 * @return
	 */
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/csite/getLines.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=utf-8")
	public @ResponseBody String getLines(@RequestParam(required=true, value="cid") String cid,@RequestParam(required=true, value="type") String type, HttpServletRequest request, HttpServletResponse response) {
		/**
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		*/
		ObjectMapper mapper = new ObjectMapper();
		try {
			Map<String, Object> result = dataUtils.getLines60(Long.valueOf(cid), type);
			String json = mapper.writeValueAsString(result);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "{status:-1, message:'系统报错'}";
	}
	
	/**
	 * 读取历史表，获取当天测点的数据记录，提供前台页面展示曲线图
	 * 如果数据超过300条记录，则页面显示会间隔读取记录点
	 * @param cid
	 * @param type 测点类型，0抽水井，1观测井，2回灌井，3环境监测
	 * @param request
	 * @param response
	 * @return
	 */
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/csite/getHistoryLines.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=utf-8")
	public @ResponseBody String getHistoryLines(@RequestParam(required=true, value="cid") String cid,@RequestParam(required=true, value="type") String type, HttpServletRequest request, HttpServletResponse response) {
		String begindt = request.getParameter("begindt");
		String enddt = request.getParameter("enddt");
		String[] cids = cid.split(",");
		Date begin = null;
		Date end = null;
		try {
			if (StringUtils.isNotBlank(begindt)) {
					begin = DateUtils.parseDateStrictly(begindt, "yyyy-MM-dd");
			} 
			if (StringUtils.isNotBlank(enddt)) {
				end = DateUtils.parseDateStrictly(enddt, "yyyy-MM-dd");
				end = DateUtils.addDays(end, 1); //截止时间+1，查询包含了截止时间当天
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> result = null;
		try {
			int i = checkTime(request, begin);
			if (cids.length==1) { //单个测点历史曲线
				if(i<0) { //读取时间范围不在当前表保留范围之内，读取历史表
					result = dataUtils.getHistoryLines(Long.valueOf(cids[0]), type, begin, end);
				} else { //读取当前表
					result = dataUtils.getLines(Long.valueOf(cids[0]), type, begin, end);
				}
			} else { //多测点历史曲线比较
				if(i<0) { //读取时间范围不在当前表保留范围之内，读取历史表
					result = dataUtils.getHistoryLines(cids, type, begin, end);
				} else { //读取当前表
					result = dataUtils.getLines(cids, type, begin, end);
				}
			}
			String json = mapper.writeValueAsString(result);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "{status:-1}";
	}

	/**
	 * 判断读取测点数据，比较是否在当前表保存时间范围之内，如果是，则读取当前表，不是，则读取历史表
	 * @param request
	 * @param begin，读取起始时间
	 * @return <0，读取历史表，>=0，读取当前表
	 */
	private int checkTime(HttpServletRequest request, Date begin) {
		@SuppressWarnings("unchecked")
		List<BasedataType> keeptimeList = (List<BasedataType>) request.getSession().getServletContext().getAttribute(Constants.KEEPTIME); //数据保留天数
		//比较是否在当前表保存时间范围之内，如果是，则读取当前表，不是，则读取历史表
		Integer keeptime = Integer.valueOf(keeptimeList.get(0).getValue());
		Date tmp = DateUtils.addDays(begin, keeptime+1);
		Date now = new Date();
		int i = tmp.compareTo(now);
		return i;
	}
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
				Map<String,String> activeMenus = new HashMap<String,String>();
				activeMenus.put("projects", "projects");

				
//				@SuppressWarnings("unchecked")
//				Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("WATERMENUMAP");
				
//				Menu currentMenu = (Menu)menus.get("csite");
//				model.addAttribute("currentMenu",currentMenu);
				model.addAttribute("activeMenus",activeMenus);
		//
		
	}
	
	private void saveQCode(HttpServletRequest request,ConstructionSite csite) throws WriterException, IOException, ServiceException{
		String domainUrl = ImageFilePathUtils.buildHttp(request);
		String src =domainUrl+"/water/csite/projectinfo.html?cid="+csite.getId();
		InputStream istram = qCodeUtils.createQrCodeWithLogo(src,1000,"JPEG");
		csite.setDigital(istram);
		String filename = RadomSixNumber.getImageName(csite.getId().toString(), Constants.IMAGE_JPG);
		csite.setqCode(filename);
		cSiteService.saveQcode(csite);
	}
	
	private void setWaterMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("csite", "csite");

		
//		@SuppressWarnings("unchecked")
//		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("WATERMENUMAP");
		
//		Menu currentMenu = (Menu)menus.get("csite");
//		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}

}
