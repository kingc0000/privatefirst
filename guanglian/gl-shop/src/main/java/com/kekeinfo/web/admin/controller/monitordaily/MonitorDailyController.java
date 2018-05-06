package com.kekeinfo.web.admin.controller.monitordaily;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
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
import com.kekeinfo.core.business.content.model.FileContentType;
import com.kekeinfo.core.business.daily.model.MonitorDaily;
import com.kekeinfo.core.business.daily.model.MonitorDailyImage;
import com.kekeinfo.core.business.daily.service.MonitorDailyImageService;
import com.kekeinfo.core.business.daily.service.MonitorDailyService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.constants.FileType;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.BaseEntity;
import com.kekeinfo.web.entity.DataTable;
import com.kekeinfo.web.entity.MonitorEntity;
import com.kekeinfo.web.utils.DataTableParameter;
import com.kekeinfo.web.utils.DateConverter;
import com.kekeinfo.web.utils.ImageFilePathUtils;
import com.kekeinfo.web.utils.ImageUtils;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.RadomSixNumber;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;


@Controller
public class MonitorDailyController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MonitorDailyController.class);
	@Autowired
	private MonitorDailyService monitordailyService;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@Autowired
	private GroupService groupService;
	@Autowired
	private PNodeUtils pnodeUtils;
	@Autowired MonitorDailyImageService monitorDailyImageService;

	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/monitordaily/list.html", method = RequestMethod.GET)
	public String listmonitordailys(Model model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String mid = request.getParameter("mid");
		setMenu(model, request);
		MonitorDaily monitordaily = new MonitorDaily();
		model.addAttribute("monitordaily", monitordaily);
		model.addAttribute("mid", mid);
		request.setAttribute("activeCode", "daily");
		MonitorEntity me=null;
		 boolean hasRight = false;
		if(StringUtils.isNotBlank(mid)){
			@SuppressWarnings("unchecked")
			List<MonitorEntity> listen = (List<MonitorEntity>) webCacheUtils.getFromCache(Constants.PROEJCT_MONITOR);
			for(MonitorEntity m:listen){
				if(m.getId().equals(Long.parseLong(mid))){
					model.addAttribute("mentity", m);
					me=m;
					break;
				}
			}
		}
		//判断用户是否有该项目的编辑权限
        if(me!=null){
			if(!request.isUserInRole("ADMIN") && !request.isUserInRole("SUPERADMIN") ){
	        	if(request.isUserInRole("EDIT-PROJECT")){
	        		List<Group> egroups = groupService.listGroupByPermissionName("EDIT-PROJECT",1);
	        		hasRight=pnodeUtils.hasProjectRight(request, egroups, (BaseEntity)me);
	        	}
			}else{
				hasRight=true;
			}
		}
        //查找今天是否创建过
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date =new Date();
        date =dateFormat.parse(dateFormat.format(date));
        
        model.addAttribute("nowdate", dateFormat.format(date));
		model.addAttribute("hasRight", hasRight);
		
		return "admin-monitordaily";
	}

	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/monitordaily/server_processing.shtml", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = " application/json; charset=utf-8")
	public @ResponseBody String getMonitorDaily(@RequestParam String aoData, HttpServletRequest request,
			HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		String mid = request.getParameter("mid");
		AjaxResponse resp = new AjaxResponse();
		DataTableParameter dataTableParam = new DataTableParameter(aoData);
		DataTable<MonitorDaily> dt = new DataTable<MonitorDaily>();
		try { // 指定根据什么条件进行模糊查询
				// 指定根据什么条件进行模糊查询
			List<String> attributes = new ArrayList<String>();
			attributes.add("note"); // 名称
			attributes.add("pointDesc");
			attributes.add("conclusion");
			HashMap<String, String> orderby = new HashMap<String, String>();
			orderby.put("auditSection.dateModified", "desc");
			Map<String, String> fetches = new HashMap<String, String>();
			List<Object[]> where = new ArrayList<Object[]>();
			where.add(new String[] { "monitor.id", mid, "0" });

			Entitites<MonitorDaily> list = monitordailyService.getPageListByAttributesLike(attributes,
					dataTableParam.getsSearch(), Long.valueOf(dataTableParam.getiDisplayLength()).intValue(),
					Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby, where, fetches, true);

			dt.setsEcho(dataTableParam.getsEcho() + 1);
			dt.setiTotalDisplayRecords(list.getTotalCount());
			dt.setiTotalRecords(list.getTotalCount());
			dt.setAaData(list.getEntites());
		} catch (Exception e) {
			LOGGER.error("Error while paging monitordailys", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(dt);
		return json;
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/monitordaily/save.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String save(@ModelAttribute("monitordaily") MonitorDaily monitordaily,
			@RequestParam(required = false, value = "fileupload") List<CommonsMultipartFile> uploadfiles,
			HttpServletRequest request, HttpServletResponse response) {
		// 删除的日志图片ID集合，采用#作为分隔符，例如"imgId1#imgId2#"
		String delImageIds = request.getParameter("delids");
		LOGGER.info("daily image will delete, id=" + delImageIds);
		AjaxResponse resp = new AjaxResponse();
		try {
			User user = (User) request.getSession().getAttribute(Constants.ADMIN_USER);
			monitordaily.getAuditSection().setModifiedBy(user.getFirstName());
			monitordaily.setUserId(user.getId());
			// 处理新增加的日志图片
			Set<MonitorDailyImage> newImages = new HashSet<MonitorDailyImage>();
			if (uploadfiles != null && !uploadfiles.isEmpty()) {
				try {
					for (int i = 0; i < uploadfiles.size(); i++) {
						MultipartFile img = uploadfiles.get(i);
						if (img.getSize() <= 0)
							continue; // 如果图片大小<=0，则图片不作处理
						String imageName = img.getOriginalFilename();
						String suffix = RadomSixNumber.getImageSuffix(img.getContentType()).toLowerCase();
						if (suffix.equals("")) {
							suffix = Constants.IMAGE_JPG;
						}
						String in = RadomSixNumber.getImageName(imageName, suffix);
						for (String filetype : FileType.FILE_TYPE) {
							if (suffix.contentEquals(filetype)) {
								MonitorDailyImage dailyImage = new MonitorDailyImage();
								dailyImage.setImage(img.getInputStream());
								dailyImage.setDailyImage(in);

								dailyImage.setImageType(0);
								dailyImage.setMonitorDaily(monitordaily);
								// 压缩
								dailyImage.setJpeg(RadomSixNumber.getImageName(dailyImage.getDailyImage()));
								InputStream inputStream = img.getInputStream();
								dailyImage.setJdigital(ImageUtils.ByteArrayOutputStream(inputStream));
								newImages.add(dailyImage);
							}
						}
					}
					monitordaily.setMonitorDailyImages(newImages);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			monitordailyService.save(monitordaily, delImageIds);
		} catch (ServiceException e) {
			LOGGER.error("Error while save monitordaily ", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}

		String returnString = resp.toJSONString();
		return returnString;
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/monitordaily/remove.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody List<AjaxResponse> deleteMonitorDaily(@RequestParam String[] listId,
			HttpServletRequest request, Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId != null && listId.length > 0) {
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.parseLong(listId[i]);
				try {
					monitordailyService.deleteById(id);
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

	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/monitordaily/check.shtml", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public @ResponseBody String check(HttpServletRequest request, HttpServletResponse response) {
		// 查找该天日志是否创建过
		AjaxResponse resp = new AjaxResponse();
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		try {
			String datec = request.getParameter("datec");
			String mid = request.getParameter("mid");
			
			if(StringUtils.isNotBlank(datec) && StringUtils.isNotBlank(mid)){
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				MonitorDaily md = monitordailyService.getBydate(dateFormat.parse(datec), Long.parseLong(mid));
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
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/monitordaily/today.shtml", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public @ResponseBody String getTodaydata(HttpServletRequest request, HttpServletResponse response) {
		// 查找今天的日志，没有就返回昨天的
		try {
			String mid = request.getParameter("mid");

			List<String> attributes = new ArrayList<String>();
			attributes.add("monitor.id");
			List<String> fieldValues = new ArrayList<String>();
			fieldValues.add(String.valueOf(Long.parseLong(mid)));
			HashMap<String, String> orderby = new HashMap<String, String>();
			orderby.put("datec", "desc");
			Entitites<MonitorDaily> list = monitordailyService.getPageListByAttributes(attributes, fieldValues, 1, null,
					orderby);
			MonitorDaily daily = null;
			Date now = new Date();
			DateConverter datec = new DateConverter();
			if (list.getTotalCount() > 0) {
				daily = list.getEntites().get(0);
				if (!datec.DateEq(now, daily.getDatec())) {
					daily = new MonitorDaily();
				}
			} else {
				daily = new MonitorDaily();
			}
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(daily);
			return json;
		} catch (IOException | ParseException e) {
			LOGGER.error(e.getMessage());
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/monitordaily/preview.html", method = RequestMethod.GET)
	public String preview(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setMenu(model, request);

		String mid = request.getParameter("mid");
		List<MonitorEntity> listen = (List<MonitorEntity>) webCacheUtils.getFromCache(Constants.PROEJCT_MONITOR);
		for(MonitorEntity m:listen){
			if(m.getId().equals(Long.parseLong(mid))){
				model.addAttribute("projectname", m.getName());
				break;
			}
		}
		String did = request.getParameter("did");
		MonitorDaily daily = monitordailyService.withImg(Long.parseLong(did));
		Set<MonitorDailyImage> imgs = daily.getMonitorDailyImages();
		if (imgs != null) {
			for (MonitorDailyImage img : imgs) {
				img.setDailyImage(request.getContextPath() + ImageFilePathUtils
						.buildFilePathByConentType(img.getDailyImage(), FileContentType.MONITOR_DAILY));
				if (StringUtils.isNotBlank(img.getJpeg())) {
					img.setJpeg(request.getContextPath() + ImageFilePathUtils.buildFilePathByConentType(img.getJpeg(),
							FileContentType.MONITOR_DAILY));
				}
			}
		}
		model.addAttribute("daily", daily);
		return "monitor-dailypre";
	}

	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/monitordaily/imges.shtml", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = " application/json; charset=utf-8")
	public @ResponseBody String getimages(HttpServletRequest request,
			HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		
		String guid =request.getParameter("muid");
		if(StringUtils.isNotBlank(guid)){
			try{
				List<MonitorDailyImage> gimgs=monitorDailyImageService.getByMid(Long.parseLong(guid));
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
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("monitor", "monitor");
		activeMenus.put("monitor-list", "monitor-list");
		model.addAttribute("activeMenus", activeMenus);
	}
}
