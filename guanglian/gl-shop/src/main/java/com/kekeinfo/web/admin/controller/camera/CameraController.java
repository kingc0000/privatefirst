package com.kekeinfo.web.admin.controller.camera;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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

import com.kekeinfo.core.business.camera.model.Camera;
import com.kekeinfo.core.business.camera.service.CameraService;
import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.constructionsite.model.ConstructionSite;
import com.kekeinfo.core.business.csite.service.CSiteService;
import com.kekeinfo.core.business.department.service.DepartmentService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.project.model.Project;
import com.kekeinfo.core.business.project.service.ProjectService;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.admin.controller.ControllerConstants;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.DataTable;
import com.kekeinfo.web.entity.UnderWater;
import com.kekeinfo.web.utils.DataTableParameter;
import com.kekeinfo.web.utils.DateUtil;
import com.kekeinfo.web.utils.NetworkFileUtils;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;

@Controller
public class CameraController {

	@Autowired
	CameraService cameraService;
	
	@Autowired
	ProjectService projectService;

	@Autowired
	CSiteService csiteService;

	@Autowired
	DepartmentService departmentService;
	
	
	@Autowired GroupService groupService;
	@Autowired private PNodeUtils pnodeUtils;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CameraController.class);
	
	/**
	 * 摄像头同步页面
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="/water/camera/init.html", method=RequestMethod.GET)
	public String init(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setMenu(model,request);
		return "water-camera-init";
	}
	
	/**
	 * 打开原图
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/water/camera/showImg.html", method=RequestMethod.GET)
	public String showImg(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String p = request.getParameter("p");
		String url = request.getContextPath() + "/water/camera/view.html?p=" + p;
		model.addAttribute("url", url);
		return "image-view";
	}
	
	/**
	 * 摄像头同步请求处理
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="/water/camera/doSynchronize.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String doSynchronize(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Camera> list = new ArrayList<Camera>();
		NetworkFileUtils utils = new NetworkFileUtils();
		AjaxResponse resp = utils.getCamerasList(list);
		
		if (list.size()>0) { //摄像头同步
			for (Camera camera : list) {
				Entitites<Camera> result = cameraService.getListByAttributes(new String[]{"path"}, new String[]{camera.getPath()}, null);
				if (result.getTotalCount()==0) {
					cameraService.save(camera);
				}
			}
		}
		return resp.toJSONString();
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="/water/camera/list.html", method=RequestMethod.GET)
	public String displayCameras(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setMenu(model,request);
		model.addAttribute("camera", new Camera());
		model.addAttribute("hasRight", true);
		return "water-cameras";
	}
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value="/water/camera/plist.html", method=RequestMethod.GET)
	public String displaypCameras(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setMenu("csite", model);
		model.addAttribute("camera", new Camera());
		request.setAttribute("activeFun", "camera");  //指定当前操作功能
		String csiteID = request.getParameter("cid");
		if(!StringUtils.isBlank(csiteID)){
			try{
				List<UnderWater> ps = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
				for(UnderWater p:ps){
					if(p.getId().equals(Long.parseLong(csiteID))){
						model.addAttribute("csite", p);
						List<Group> egroups = groupService.listGroupByPermissionName("EDIT-PROJECT",1);
						 boolean hasRight=false;
						 if(!request.isUserInRole("ADMIN") && !request.isUserInRole("SUPERADMIN") ){
					        	if(request.isUserInRole("EDIT-PROJECT")){
					        		hasRight=pnodeUtils.hasProjectRight(request, egroups, p);
					        	}
							}else{
								hasRight=true;
							}
						model.addAttribute("hasRight", hasRight);
						String ctype = request.getParameter("ctype");
						//手机版
						if(StringUtils.isNotBlank(ctype)){
							model.addAttribute("project", p);
							return "phone-cameras";
						}
						break;
					}
				}
				 
			}catch (Exception e){
				LOGGER.debug(e.getMessage());
			}
		}
		
		return "csite-cameras";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = "/water/camera/server_processing.shtml", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getCameras(@RequestParam String aoData,HttpServletRequest request,
			HttpServletResponse response) {
		AjaxResponse resp = new AjaxResponse();
		DataTableParameter dataTableParam = new DataTableParameter(aoData);
		DataTable<Map<String, Object>> dt = new DataTable<Map<String, Object>>();
		String csiteID = request.getParameter("cid");
		List<Object[]> where =null;
		if(StringUtils.isNotBlank(csiteID) ){
			ConstructionSite csite;
			try {
				csite = csiteService.getById(Long.valueOf(csiteID).longValue());
				where = new ArrayList<>();
				where.add(new Object[]{"project.id", csite.getProject().getId()});
			} catch (NumberFormatException | ServiceException e) {
				e.printStackTrace();
			}
		}
		try {
			//指定根据什么条件进行模糊查询
			List<String> attributes = new ArrayList<String>();
			attributes.add("path"); //标题
			attributes.add("note");  //副标题
			HashMap<String, String> orderby = new HashMap<String, String>();
			orderby.put("auditSection.dateModified", "desc");
			Map<String, String> fetches = new HashMap<String, String>();
			fetches.put("project", "LEFT");
			fetches.put("department", "LEFT");
			Entitites<Camera> list = cameraService.getPageListByAttributesLike(attributes, dataTableParam.getsSearch(), Long.valueOf(dataTableParam.getiDisplayLength()).intValue(), 
					Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby, where, fetches, true);
			List<Camera> cameras = list.getEntites();
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			for (Camera camera : cameras) {
				Map<String, Object> entry = new HashMap<String, Object>();
				entry.put("id", camera.getId());
				entry.put("path", camera.getPath());
				entry.put("note", camera.getNote());
				entry.put("status", camera.isStatus());
				if (camera.getProject()!=null) { //处理工地、项目信息
					entry.put("csiteId", camera.getProject().getId());
					entry.put("csiteName", camera.getProject().getName());
					entry.put("departmentId", camera.getProject().getDepartment().getId());
					entry.put("departmentName", camera.getProject().getDepartment().getName());
					entry.put("longitude", camera.getLongitude());
					entry.put("latitude", camera.getLatitude());
				} else {
					entry.put("csiteId", "");
					entry.put("csiteName", "");
					entry.put("departmentId", "");
					entry.put("departmentName", "");
				}
				resultList.add(entry);
			}
			dt.setsEcho(dataTableParam.getsEcho()+1);
			dt.setiTotalDisplayRecords(list.getTotalCount());
	        dt.setiTotalRecords(list.getTotalCount());
	        dt.setAaData(resultList);
		} catch (Exception e) {
			LOGGER.error("Error while paging camera", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			return resp.toJSONString();
		}
		String json = dt.toJSONString();
		return json;
	}
	
	@PreAuthorize("hasRole('ADMIN')") 
	@RequestMapping(value="/water/camera/save.shtml", method=RequestMethod.POST, produces="application/json; charset=utf-8")
	public @ResponseBody String save(@ModelAttribute("camera") Camera camera, Model model, HttpServletRequest request, HttpServletResponse response)  {
		AjaxResponse resp = new AjaxResponse();
		String byuser = request.getRemoteUser();
		String path = camera.getPath().indexOf("/")==0?camera.getPath():"/"+camera.getPath();
		camera.getAuditSection().setModifiedBy(byuser);
		camera.setPath(path); 
		try {
			//判断目录路径是否已经存在
/*			Entitites<Camera> cameras = cameraService.getListByAttributes(new String[]{"path"}, new String[]{camera.getPath()}, null);
			if (cameras.getTotalCount()>0) {
				Camera o = cameras.getEntites().get(0);
				if (camera.getId()==null || !camera.getId().equals(o.getId())) {
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE_MESSAGE);
					resp.setStatusMessage("目录路径[ " + path + " ]已经存在，不允许重复添加");
					return resp.toJSONString();
				}
			}
*/			
			if (camera.getProject()!=null&&camera.getProject().getId()!=null) {
				Project cSite = projectService.getById(camera.getProject().getId());
				if (cSite!=null) {
					camera.setProject(cSite);
				}
			} else {
				camera.setProject(null);
			}
			cameraService.saveOrUpdate(camera);
			//创建成功，则在指定的服务器创建对应的目录
			//访问服务器摄像头目录，遍历目录结构
			NetworkFileUtils utils = new NetworkFileUtils();
			resp = utils.createCameraDirectory(path);
			//创建成功，如果摄像头有关联项目，则需要更新内存中的部门集合信息
			if (camera.getProject()!=null&&camera.getProject().getId()!=null) {
				pnodeUtils.reloadAllDepartments();
			}
		} catch (ServiceException e) {
			e.printStackTrace();
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE_MESSAGE);
			resp.setStatusMessage("创建失败："+e.getMessage());
			return resp.toJSONString();
		}
		String returnString = resp.toJSONString();
		return returnString;
	}
	
	/**
	 * 删除摄像头数据，不删除服务器端同步的摄像头目录
	 * @param listId
	 * @param request
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="/water/camera/remove.shtml", method=RequestMethod.POST, produces="application/json;charset=utf-8")
	public @ResponseBody List<AjaxResponse> remove(@RequestParam String[] listId,HttpServletRequest request, Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId!=null&&listId.length>0) {
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.valueOf(listId[i]);
				try {
					Camera camera = cameraService.getById(id);
					cameraService.deleteById(id);
					//删除成功，则需要更新内存中的部门集合信息
					if (camera.getProject()!=null) {
						pnodeUtils.reloadAllDepartments();
					}
					resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
					resplist.add(resp);
				} catch (Exception e) {
					e.printStackTrace();
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
					resplist.add(resp);
				}
			}
		}
		return resplist;
	}
	
	/**
	 * 加载指定日期的摄像头图片路径集合
	 * @param cid
	 * @param dt 日期
	 * @param begintm 起始时间段
	 * @param endtm 结束时间段
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@PreAuthorize("hasRole('AUTH')") 
	@RequestMapping(value="/water/camera/loadImage.shtml", method=RequestMethod.POST, produces="application/json; charset=utf-8")
	public @ResponseBody String loadImage(@RequestParam(value="cid", required=true) String cid, @RequestParam(value="dt", required=false) String dt, 
			@RequestParam(value="begintm", required=false) String begintm, @RequestParam(value="endtm", required=false) String endtm, 
			Model model, HttpServletRequest request, HttpServletResponse response)  {
		AjaxResponse resp = new AjaxResponse();
		 
		//获取摄像头
		Camera camera = cameraService.getById(Long.valueOf(cid));
		if (camera==null||!camera.isStatus()) {
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE_MESSAGE);
			resp.setStatusMessage("摄像头不存在或者不可用，请联系管理员");
			return resp.toJSONString();
		}
		if (StringUtils.isBlank(dt)) {
			dt = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
		}
		resp.addEntry("dt", dt); //设定日期，传递到前台
		dt = "/" + dt;
		resp.addEntry("cameraName", camera.getNote());
		String path = camera.getPath();
		
		//访问服务器摄像头目录，遍历目录结构
		NetworkFileUtils utils = new NetworkFileUtils();
		resp = utils.loadImagePath(path, dt, begintm, endtm);
		
		String returnString = resp.toJSONString();
		return returnString;
	}
	
	/**
	 * 获取指定图片
	 * @param path 图片路径
	 * @param request
	 * @param response
	 * @return
	 */
	@PreAuthorize("hasRole('AUTH')") 
	@RequestMapping(value="/water/camera/view.html")
	public @ResponseBody byte[] viewImage(@RequestParam(value="p", required=true) String path, 
			HttpServletRequest request, HttpServletResponse response)  {
		NetworkFileUtils utils = new NetworkFileUtils();
		return utils.viewImage(path);
	}
	
	/**
	 * 获取指定摄像头当前最新照片完整路径
	 * @param cid
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@PreAuthorize("hasRole('AUTH')") 
	@RequestMapping(value="/water/camera/monitor.shtml", method=RequestMethod.POST, produces="application/json; charset=utf-8")
	public @ResponseBody String monitor(@RequestParam(value="cid", required=true) String cid,  
			Model model, HttpServletRequest request, HttpServletResponse response)  {
		AjaxResponse resp = new AjaxResponse();
		 
		//获取摄像头
		Camera camera = cameraService.getById(Long.valueOf(cid));
		if (camera==null||!camera.isStatus()) {
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE_MESSAGE);
			resp.setStatusMessage("摄像头不存在或者不可用，请联系管理员");
			return resp.toJSONString();
		}
		String dt = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
		resp.addEntry("dt", dt); //设定日期，传递到前台
		dt = "/" + dt;
		resp.addEntry("cameraName", camera.getNote());
		String path = camera.getPath();
		
		NetworkFileUtils utils = new NetworkFileUtils();
		return utils.monitorCamera(path, dt).toJSONString();
	}
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("configuration", "configuration");
		model.addAttribute("activeMenus",activeMenus);
		//
	}
	
	private void setMenu(String active, Model model) throws Exception {
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put(active, active);
		model.addAttribute("activeMenus",activeMenus);
		//
	}
	
	@PreAuthorize("hasRole('AUTH')") 
	@RequestMapping(value={"/water/camera.html"}, method=RequestMethod.GET)
	public String cameraDashboard(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String type = request.getParameter("type");
		if (StringUtils.isBlank(type)) {
			type = "view";
		}
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("home", "home");
		User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		//未授权的访问
		if(user!=null){
			resultList = pnodeUtils.getProjectCameras(request, true);
			model.addAttribute("departments", resultList);
			model.addAttribute("activeMenus", activeMenus);
		}
		
		if (type.equals("view")) {
			return ControllerConstants.Tiles.cameraDashboard; //图像浏览
		} else {
			return ControllerConstants.Tiles.Camera.monitor; //图像监控
		}
	}
}
