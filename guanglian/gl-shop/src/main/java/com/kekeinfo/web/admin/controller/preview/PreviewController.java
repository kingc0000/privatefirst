package com.kekeinfo.web.admin.controller.preview;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.constructionsite.model.ConstructionSite;
import com.kekeinfo.core.business.content.model.FileContentType;
import com.kekeinfo.core.business.csite.service.CSiteService;
import com.kekeinfo.core.business.department.model.Department;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.preview.model.Preview;
import com.kekeinfo.core.business.preview.model.PreviewImage;
import com.kekeinfo.core.business.preview.service.PreviewService;
import com.kekeinfo.core.business.user.model.AppUser;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.utils.BaiduPushUtitls;
import com.kekeinfo.core.utils.IosPushUtils;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.DataTable;
import com.kekeinfo.web.entity.UnderWater;
import com.kekeinfo.web.utils.DataTableParameter;
import com.kekeinfo.web.utils.DateConverter;
import com.kekeinfo.web.utils.DateUtil;
import com.kekeinfo.web.utils.ImageFilePathUtils;
import com.kekeinfo.web.utils.ImageUtils;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.RadomSixNumber;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;

@Controller
public class PreviewController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PreviewController.class);
	@Autowired private PNodeUtils pnodeUtils;
	@Autowired private PreviewService previewService;
	
	@Autowired private CSiteService cSiteService;
	@Autowired private GroupService groupService;
	@Autowired private IosPushUtils iosPushUtils;
	@Autowired private BaiduPushUtitls baiduPushUtitls;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/preview/review.html", method=RequestMethod.GET)
	public String review(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setMenu(model,request, "csite");
		
		String cid = request.getParameter("cid");
		
		List<UnderWater> cs = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
		UnderWater csite=null;
		for(UnderWater c:cs){
			if(c.getId().equals(Long.parseLong(cid))){
				csite=c;
				break;
			}
		}
		//if(csite==null)csite=pnodeUtils.getByCid(Long.parseLong(cid), cSiteService);
		
		request.setAttribute("activeFun", "preview");  //指定当前操作功能
		request.setAttribute("cid", cid);
		request.setAttribute("projectId", csite.getPid());
		//请求来源，如果来自全局地图，则后退时需要回退至对应的项目
		request.setAttribute("from", request.getParameter("from"));
		//获取用户的评价信息
		User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
		List<Object[]> wheres = new ArrayList<Object[]>();
		wheres.add(new Object[]{"project.id", csite.getPid(), "0"});
		wheres.add(new Object[]{"user.id", user.getId(), "0"});
		Map<String, String> fetches = new HashMap<String, String>();
		fetches.put("previewImages", "LEFT");
		HashMap<String, String> orderby = new HashMap<String, String>();
		orderby.put("auditSection.dateCreated", "desc");
		Entitites<Preview> reviews = previewService.getPageListByAttributesLike(null, null, null, null, orderby, wheres, fetches, true);
				//.getListByAttributes(new String[]{"cSite.id", "user.id"}, new Long[]{Long.valueOf(cid), user.getId()}, null);
		if (reviews.getTotalCount()>0) {
			for(Preview review :reviews.getEntites()){
				Set<PreviewImage> imgs = review.getPreviewImages();
				if(imgs!=null){
					for(PreviewImage img:imgs){
						img.setImageUrl(request.getContextPath() + ImageFilePathUtils.buildFilePathByConentType(img.getReviewImage(), FileContentType.PREVIEW));
						
					}
				}
			}
			//第一条是不是当天的
			Preview  preview  =reviews.getEntites().get(0);
			Date now = new Date();
			DateConverter datec = new DateConverter();
			//是当天的
			if(datec.DateEq(now, preview.getAuditSection().getDateCreated())){
				model.addAttribute("preview", preview);
				//历史记录去掉第一条
				reviews.getEntites().remove(0);
			}else{
				model.addAttribute("preview", new Preview());
			}
			request.setAttribute("previews", reviews.getEntites());
		}else {
			model.addAttribute("preview", new Preview());
		}
		//String ctype = request.getParameter("ctype");
		//手机版
		String app =(String)request.getSession().getAttribute("app");
		if(StringUtils.isNotBlank(app)){
			model.addAttribute("project", csite);
			return "phone-preview";
		}
		model.addAttribute("csite", csite);
		return "csite-preview";
	}
	
	/**
	 * 查看项目评论汇总列表
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/preview/list.html", method=RequestMethod.GET)
	public String list(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setMenu(model,request, "statistics");
		
		if (!request.isUserInRole("ADMIN") && !request.isUserInRole("SUPERADMIN")) {
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			Department waterDept = (Department) webCacheUtils.getFromCache(Constants.DEPARTMENT_WATER); //地下水部门
			boolean find = pnodeUtils.hasProjectRight(user.getpNodes(), waterDept);
			if(!find){
				return "redirect:/water/home.html";
			}
		}
		return "csite-preview-list";
	}
	
	/**
	 * 查看用户评论列表
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/preview/detaillist.html", method=RequestMethod.GET)
	public String detaillist(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setMenu(model,request, "statistics");
		String cid = request.getParameter("cid");
		if (!request.isUserInRole("ADMIN") && !request.isUserInRole("SUPERADMIN")) {
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			Department waterDept = (Department) webCacheUtils.getFromCache(Constants.DEPARTMENT_WATER); //地下水部门
			boolean find = pnodeUtils.hasProjectRight(user.getpNodes(), waterDept);
			if(!find){
				return "redirect:/water/home.html";
			}
		}
		model.addAttribute("cid", cid);
		return "csite-preview-detaillist";
	}
	
	/**
	 * 获取项目评论汇总情况
	 * 项目总分，总评论数，各个评分维度平均分
	 * @param request
	 * @param response
	 * @return
	 */
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/preview/getTotal.shtml", method = { RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public @ResponseBody String getTotal(HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse resp = new AjaxResponse();
		String projectId = request.getParameter("projectId");
		if (StringUtils.isBlank(projectId)) {
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setStatusMessage("项目ID不允许为空");
			return resp.toJSONString();
		}
		try {
			List<Object[]> result = previewService.getTotalInfo(Long.valueOf(projectId));
			if (result.size()>0) {
				Object[] r = (Object[]) result.get(0);
				resp.addEntry("total", String.valueOf(r[2]));
				resp.addEntry("score1", String.valueOf(r[3]));
				resp.addEntry("score2", String.valueOf(r[4]));
				resp.addEntry("score3", String.valueOf(r[5]));
				resp.addEntry("score4", String.valueOf(r[6]));
			} else {
				resp.addEntry("total", "0");
			}
		} catch (NumberFormatException | ServiceException e) {
			e.printStackTrace();
		}
		return resp.toJSONString();
	}
	
	/**
	 * 获取项目评论汇总列表
	 * @param aoData
	 * @param request
	 * @param response
	 * @return
	 */
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/preview/server_processing.shtml", method = { RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getReveiwTotalList(@RequestParam String aoData,HttpServletRequest request,
			HttpServletResponse response) {
		DataTableParameter dataTableParam = new DataTableParameter(aoData);
		
		List<Object[]> list = null;
		try {
			if ((request.isUserInRole("ADMIN") || request.isUserInRole("SUPERADMIN"))) {
				list = previewService.getTotalInfo(null, Long.valueOf(dataTableParam.getiDisplayLength()).intValue(), 
						Long.valueOf(dataTableParam.getiDisplayStart()).intValue());
			} else {
				if (request.isUserInRole("EDIT-PROJECT")) {
						List<Group> egroups = groupService.listGroupByPermissionName("EDIT-PROJECT",1);
						//获取该用户具有编辑权限的所有地下水项目集合
						List<ConstructionSite> csites = pnodeUtils.getWaterCsites(request, egroups);
						if (csites.size()>0) {
							Set<Long> cids = new HashSet<Long>(); 
							for (ConstructionSite constructionSite : csites) {
								cids.add(constructionSite.getId());
							}
							list = previewService.getTotalInfo(cids, Long.valueOf(dataTableParam.getiDisplayLength()).intValue(), 
									Long.valueOf(dataTableParam.getiDisplayStart()).intValue());
						}
	    		}
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
		DataTable<Map<String, Object>> dt = new DataTable<Map<String, Object>>();
		List<Map<String, Object>> aaData = new ArrayList<Map<String, Object>>();
		dt.setsEcho(dataTableParam.getsEcho()+1);
		if (list!=null&&list.size()>0) {
			dt.setiTotalDisplayRecords(list.size());
			dt.setiTotalRecords(list.size());
			for (Object[] result : list) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", result[0]);
				map.put("name", result[1]);
				map.put("total", result[2]);
				map.put("score1", result[3]);
				map.put("score2", result[4]);
				map.put("score3", result[5]);
				map.put("score4", result[6]);
				aaData.add(map);
			}
		} else {
			dt.setiTotalDisplayRecords(0);
			dt.setiTotalRecords(0);
		}
		dt.setAaData(aaData);
		try {
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(dt);
			return json;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取用户评论列表
	 * @param aoData
	 * @param request
	 * @param response
	 * @return
	 */
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/preview/detail_list.shtml", method = { RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getDetailList(@RequestParam String aoData,HttpServletRequest request,
			HttpServletResponse response) {
		DataTableParameter dataTableParam = new DataTableParameter(aoData);
		String cid = request.getParameter("cid");
		Entitites<Preview> list = null;
		try {
			//指定根据什么条件进行模糊查询
			HashMap<String, String> orderby = new HashMap<String, String>();
			orderby.put("auditSection.dateModified", "desc");
			List<Object[]> where = new ArrayList<Object[]>();
			where.add(new Object[]{"project.id", Long.valueOf(cid), "0"});
			Map<String, String> fetches = new HashMap<String, String>();
			fetches.put("project", "LEFT");
			//fetches.put("previewImages", "LEFT");
			fetches.put("user", "LEFT");
			fetches.put("feedbackUser", "LEFT");
			
			if ((request.isUserInRole("ADMIN") || request.isUserInRole("SUPERADMIN"))) {
				list = previewService.getPageListByAttributesLike(null, null, Long.valueOf(dataTableParam.getiDisplayLength()).intValue(), 
						Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby, where, fetches, false);
			} else {
				if (request.isUserInRole("EDIT-PROJECT")) {
						ConstructionSite csite =cSiteService.getByCid(Long.parseLong(cid));
						List<Group> egroups = groupService.listGroupByPermissionName("EDIT-PROJECT",1);
						boolean hasRight = pnodeUtils.hasProjectRight(request, egroups, csite.getProject());
						if (hasRight) { //没有权限
							list = previewService.getPageListByAttributesLike(null, null, Long.valueOf(dataTableParam.getiDisplayLength()).intValue(), 
									Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby, where, fetches, true);
						}
	    		}
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
		DataTable<Map<String, Object>> dt = new DataTable<Map<String, Object>>();
		List<Map<String, Object>> aaData = new ArrayList<Map<String, Object>>();
		dt.setsEcho(dataTableParam.getsEcho()+1);
		if (list!=null&&list.getEntites().size()>0) {
			dt.setiTotalDisplayRecords(list.getTotalCount());
			dt.setiTotalRecords(list.getTotalCount());
			for (Preview p : list.getEntites()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", p.getId());
				map.put("csiteName", p.getProject().getName());
				map.put("cid", p.getProject().getId());
				map.put("userName", p.getUser().getAdminName());
				map.put("comment", p.getComment());
				if (p.getFeedbackUser()!=null) {
					map.put("feedbackName", p.getFeedbackUser().getAdminName());
				} else {
					map.put("feedbackName", "");
				}
				map.put("feedback", p.getFeedback());
				map.put("score1", p.getScore1());
				map.put("score2", p.getScore2());
				map.put("score3", p.getScore3());
				map.put("score4", p.getScore4());
				map.put("dateCreated", DateUtil.formatDate(p.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
				map.put("dateModified", DateUtil.formatDate(p.getAuditSection().getDateModified(), "yyyy-MM-dd HH:mm:ss"));
				/**
				Set<PreviewImage> imgs = p.getPreviewImages();
				if(imgs!=null){
					for(PreviewImage img:imgs){
						img.setImageUrl(request.getContextPath() + ImageFilePathUtils.buildFilePathByConentType(img.getReviewImage(), FileContentType.PREVIEW));
					}
				}
				map.put("imgs", imgs);*/
				aaData.add(map);
			}
		} else {
			dt.setiTotalDisplayRecords(0);
			dt.setiTotalRecords(0);
		}
		dt.setAaData(aaData);
		try {
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(dt);
			return json;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('VIEW-PROJECT')") 
	@RequestMapping(value="/water/preview/save.shtml", method=RequestMethod.POST, produces="application/json; charset=utf-8")
	public @ResponseBody String save(@ModelAttribute("preview") Preview review, @RequestParam(required=false, value="fileupload") List<CommonsMultipartFile> uploadfiles, Model model, HttpServletRequest request, HttpServletResponse response)  {
		//删除的日志图片ID集合，采用#作为分隔符，例如"imgId1#imgId2#"
		String delImageIds = request.getParameter("delids");
		LOGGER.info("review image will delete, id=" + delImageIds);
		AjaxResponse resp = new AjaxResponse();
		try {
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			
			review.getAuditSection().setModifiedBy(user.getFirstName());
			review.setUser(user);
			//处理新增加的图片
			Set<PreviewImage> newImages = new HashSet<PreviewImage>();
			if(uploadfiles!=null && !uploadfiles.isEmpty()) {
				try {
					for (int i = 0; i < uploadfiles.size(); i++) {
						MultipartFile img = uploadfiles.get(i);
						if(img.getSize()<=0) continue; //如果图片大小<=0，则图片不作处理
						String imageName = img.getOriginalFilename();
						String suffix = RadomSixNumber.getImageSuffix(img.getContentType());
						if (suffix.equals("")) {
							suffix = Constants.IMAGE_JPG;
						}
						String in = RadomSixNumber.getImageName(imageName, suffix);
						PreviewImage previewImage = new PreviewImage();
						//压缩
						InputStream inputStream = img.getInputStream();
						previewImage.setImage(ImageUtils.ByteArrayOutputStream(inputStream));
						previewImage.setReviewImage(in);
						
						previewImage.setImageType(0);
						previewImage.setPreview(review);
						newImages.add(previewImage);
					}
					review.setPreviewImages(newImages);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			 Set<AppUser> users =null;
			List<Long> uids =null;
			String pname=null;
			HashMap<Long, Set<AppUser>>  cmaps =(HashMap<Long, Set<AppUser>>)webCacheUtils.getFromCache(Constants.USER_COMMENT);
			if(cmaps!=null){
				List<UnderWater> cs = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
				for(UnderWater c:cs){
					if(c.getPid().equals(review.getProject().getId())){
						pname=c.getName();
						break;
					}
				}
				users = cmaps.get(review.getProject().getId());
				if(users!=null && users.size()>0){
					uids=new ArrayList<>();
					for(AppUser u:users){
						uids.add(u.getId());
					}
				}
			}
			previewService.save(review, delImageIds,uids,pname);
			//推送到处理人
			if(users!=null && !users.isEmpty()){
					for(AppUser cuser:users){
						try{
							String uAgent = cuser.getuAgent();
							if(uAgent!=null){
								String message=pname+"项目有新的评论，请处理！";
								if(uAgent.equalsIgnoreCase("iOS")){
									iosPushUtils.setPushToken(cuser.getDevice_token());
									iosPushUtils.setTitle(message);
									iosPushUtils.iPush();
								}else if(uAgent.equalsIgnoreCase("android")){
									baiduPushUtitls.setTitle("广联通知");
									baiduPushUtitls.setChannelID(cuser.getDevice_token());
									baiduPushUtitls.setDeviceType(3);
									baiduPushUtitls.setMessage(message);
									baiduPushUtitls.pushMessage();
								}
							}
						}catch (Exception e){
							LOGGER.error("Error while push ", e);
						}
					}
				}
		} catch (ServiceException e ) {
			LOGGER.error("Error while save review ", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		return returnString;
	}
	
	/**
	 * 保存反馈信息
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@PreAuthorize("hasRole('EDIT-PROJECT')") 
	@RequestMapping(value="/water/preview/feedback.shtml", method=RequestMethod.POST, produces="application/json; charset=utf-8")
	public @ResponseBody String feedback(Model model, HttpServletRequest request, HttpServletResponse response)  {
		AjaxResponse resp = new AjaxResponse();
		try {
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			String id = request.getParameter("id");
			String feedback = request.getParameter("feedback");
			if (StringUtils.isNotBlank(id)) {
				Preview review = previewService.getById(Long.valueOf(id));
				review.getAuditSection().setModifiedBy(user.getFirstName());
				review.setFeedback(feedback);
				review.setFeedbackUser(user);
				previewService.update(review);
			} else {
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				resp.setStatusMessage("评论信息不存在");
				return resp.toJSONString();
			}
		} catch (ServiceException e ) {
			LOGGER.error("Error while save review ", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
			return resp.toJSONString();
		}
		
		String returnString = resp.toJSONString();
		return returnString;
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/preview/imgages.html", method=RequestMethod.GET)
	public String img(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setMenu(model,request, "statistics");
		String pid = request.getParameter("id");
		if(StringUtils.isNotBlank(pid)){
			Preview review = previewService.getBypid(Long.valueOf(pid));
			
			Set<PreviewImage> imgs = review.getPreviewImages();
			if(imgs!=null){
				for(PreviewImage img:imgs){
					img.setImageUrl(request.getContextPath() + ImageFilePathUtils.buildFilePathByConentType(img.getReviewImage(), FileContentType.PREVIEW));
				}
			}
			model.addAttribute("preview", imgs);
		}
		
		return "preview-imgs";
	}
	
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value="/water/preview/remove.shtml", method=RequestMethod.POST, produces="application/json;charset=utf-8")
	public @ResponseBody List<AjaxResponse> remove(@RequestParam String[] listId,HttpServletRequest request) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId!=null&&listId.length>0) {
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.valueOf(listId[i]);
				try {
					previewService.deleteById(id);
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
	
	private void setMenu(Model model, HttpServletRequest request, String menu) throws Exception {
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put(menu, menu);
		model.addAttribute("activeMenus",activeMenus);
	}
	
}
