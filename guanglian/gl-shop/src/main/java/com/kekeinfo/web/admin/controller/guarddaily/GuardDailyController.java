package com.kekeinfo.web.admin.controller.guarddaily;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
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

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.daily.model.GuardDaily;
import com.kekeinfo.core.business.daily.model.GuardDailyImage;
import com.kekeinfo.core.business.daily.service.GuardDailyImageService;
import com.kekeinfo.core.business.daily.service.GuardDailyService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.constants.FileType;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.admin.entity.web.Menu;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.BaseEntity;
import com.kekeinfo.web.entity.DataTable;
import com.kekeinfo.web.entity.GuardEntity;
import com.kekeinfo.web.utils.DataTableParameter;
import com.kekeinfo.web.utils.ImageUtils;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.RadomSixNumber;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;;

@Controller
public class GuardDailyController {
	private static final Logger LOGGER = LoggerFactory.getLogger(GuardDailyController.class);
	@Autowired
	GuardDailyService guarddailyService;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@Autowired
	private PNodeUtils pnodeUtils;
	@Autowired
	private GroupService groupService;
	
	@Autowired GuardDailyImageService  guardDailyImageService;
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/guarddaily/list.html", method = RequestMethod.GET)
	public String listguarddailys(Model model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		setMenu(model, request);
		
		
		String gid = request.getParameter("gid");
		// 判断用户是否有该项目的编辑权限
			boolean hasRight = false;
			GuardEntity me = null;
			if (StringUtils.isNotBlank(gid)) {
				List<GuardEntity> listen = (List<GuardEntity>) webCacheUtils.getFromCache(Constants.PROEJCT_GUARD);
				for (GuardEntity m : listen) {
					if (m.getId().equals(Long.parseLong(gid))) {
						me = m;
						model.addAttribute("gentity", m);
						break;
					}
				}
			}
			if (me != null) {
				if (!request.isUserInRole("ADMIN") && !request.isUserInRole("SUPERADMIN")) {
					if (request.isUserInRole("EDIT-PROJECT")) {
						List<Group> egroups = groupService.listGroupByPermissionName("EDIT-PROJECT", 1);
						hasRight = pnodeUtils.hasProjectRight(request, egroups, (BaseEntity) me);
					}
				} else {
					hasRight = true;
				}
			}
			request.setAttribute("activeCode", "daily"); // 指定项目
			model.addAttribute("hasRight", hasRight);
			 //查找今天是否创建过
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        Date date =new Date();
	        date =dateFormat.parse(dateFormat.format(date));
	        
	        GuardDaily guarddaily = new GuardDaily();
			model.addAttribute("guarddaily", guarddaily);
			model.addAttribute("nowdate", dateFormat.format(date));
			model.addAttribute("gid", gid);
		return "admin-guarddaily";
	}
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/guarddaily/preview.html", method = RequestMethod.GET)
	public String previe(Model model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		setMenu(model, request);
		String gid = request.getParameter("gid");
		if (StringUtils.isNotBlank(gid)) {
			List<GuardEntity> listen = (List<GuardEntity>) webCacheUtils.getFromCache(Constants.PROEJCT_GUARD);
			for (GuardEntity m : listen) {
				if (m.getId().equals(Long.parseLong(gid))) {
					model.addAttribute("projectname", m.getName());
					break;
				}
			}
		}
		String did =request.getParameter("did");
		try{
			GuardDaily gd=guarddailyService.wihtimg(Long.parseLong(did));
			model.addAttribute("daily", gd);
		}catch (Exception e){
			
		}
		return "guard-dailypre";
	}

	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/guarddaily/server_processing.shtml", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = " application/json; charset=utf-8")
	public @ResponseBody String getGuardDaily(@RequestParam String aoData, HttpServletRequest request,
			HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		AjaxResponse resp = new AjaxResponse();
		DataTableParameter dataTableParam = new DataTableParameter(aoData);
		DataTable<GuardDaily> dt = new DataTable<GuardDaily>();
		try { // 指定根据什么条件进行模糊查询
			List<String> attributes = new ArrayList<String>();
			HashMap<String, String> orderby = new HashMap<String, String>();
			orderby.put("auditSection.dateModified", "desc");
			Entitites<GuardDaily> list = guarddailyService.getPageListByAttributesLike(attributes,
					dataTableParam.getsSearch(), Long.valueOf(dataTableParam.getiDisplayLength()).intValue(),
					Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby, null, null);
			dt.setsEcho(dataTableParam.getsEcho() + 1);
			dt.setiTotalDisplayRecords(list.getTotalCount());
			dt.setiTotalRecords(list.getTotalCount());
			dt.setAaData(list.getEntites());
		} catch (Exception e) {
			LOGGER.error("Error while paging guarddailys", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
		String json = mapper.writeValueAsString(dt);
		return json;
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/guarddaily/imges.shtml", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = " application/json; charset=utf-8")
	public @ResponseBody String getimages(HttpServletRequest request,
			HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		
		String guid =request.getParameter("guid");
		if(StringUtils.isNotBlank(guid)){
			try{
				List<GuardDailyImage> gimgs=guardDailyImageService.getByGid(Long.parseLong(guid));
				if(gimgs!=null && gimgs.size()>0){
					ObjectMapper mapper = new ObjectMapper();
					String json = mapper.writeValueAsString(gimgs);
					return json;
				}
				return "0";
				
			}catch (Exception e){
				return "-1";
			}
		}
		
		return "-1";
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/guarddaily/check.shtml", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public @ResponseBody String check(HttpServletRequest request, HttpServletResponse response) {
		// 查找该天日志是否创建过
		AjaxResponse resp = new AjaxResponse();
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		try {
			String datec = request.getParameter("datec");
			String mid = request.getParameter("gid");
			
			if(StringUtils.isNotBlank(datec) && StringUtils.isNotBlank(mid)){
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				GuardDaily md = guarddailyService.byDate(dateFormat.parse(datec), Long.parseLong(mid));
				if(md==null){
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
				}
			}else if(StringUtils.isBlank(datec)){
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_VALIDATION_FAILED);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}

		return resp.toJSONString();
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/guarddaily/save.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String saveGuardDaily(@ModelAttribute("guarddaily") GuardDaily guarddaily,
			@RequestParam(required = false, value = "fileupload") List<CommonsMultipartFile> uploadfiles,HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse resp = new AjaxResponse();
		try {
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			guarddaily.setUserid(user.getId());
			guarddaily.getAuditSection().setModifiedBy(user.getFirstName());
			// 删除的日志图片ID集合，采用#作为分隔符，例如"imgId1#imgId2#"
			String delImageIds = request.getParameter("delids");
			// 处理新增加的日志图片
			Set<GuardDailyImage> newImages = new HashSet<GuardDailyImage>();
			if (uploadfiles != null && !uploadfiles.isEmpty()) {
				try {
					for (int i = 0; i < uploadfiles.size(); i++) {
						MultipartFile img = uploadfiles.get(i);
						if (img.getSize() <= 0)
							continue; // 如果图片大小<=0，则图片不作处理
						String imageName = img.getOriginalFilename();
						String suffix = RadomSixNumber.getImageSuffix(img.getContentType());
						if (suffix.equals("")) {
							suffix = Constants.IMAGE_JPG;
						}
						String in = RadomSixNumber.getImageName(imageName, suffix).toLowerCase();
						for (String filetype : FileType.FILE_TYPE) {
							if (in.endsWith(filetype)) {
								GuardDailyImage dailyImage = new GuardDailyImage();
								dailyImage.setImage(img.getInputStream());
								dailyImage.setDailyImage(in);

								dailyImage.setImageType(0);
								dailyImage.setGuardDaily(guarddaily);
								// 压缩
								dailyImage.setJpeg(RadomSixNumber.getImageName(dailyImage.getDailyImage()));
								InputStream inputStream = img.getInputStream();
								dailyImage.setJdigital(ImageUtils.ByteArrayOutputStream(inputStream));
								newImages.add(dailyImage);
								break;
							}
						}
					}
					guarddaily.setGuardDailyImages(newImages);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			guarddailyService.saveUpdate(guarddaily,delImageIds);
		} catch (ServiceException e) {
			LOGGER.error("Error while save GuardDaily", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		String returnString = resp.toJSONString();
		return returnString;
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/guarddaily/remove.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody List<AjaxResponse> deleteGuardDaily(@RequestParam String[] listId, HttpServletRequest request,
			Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId != null && listId.length > 0) {
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.parseLong(listId[i]);
				try {
					guarddailyService.deletewithimg(id);
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
					resplist.add(resp);
				} catch (Exception e) {
					e.printStackTrace();
					if (e.getMessage().startsWith("Cannot delete or update a parent row")) {
						resp.setStatus(9997);
					} else {
						resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
					}
					resplist.add(resp);
				}
			}
		}
		return resplist;
	}

	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("guarddaily", "guarddaily");
		activeMenus.put("guarddaily-list", "guarddaily-list");
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");
		Menu currentMenu = (Menu) menus.get("guarddaily");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
	}
}
