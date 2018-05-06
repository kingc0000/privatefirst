package com.kekeinfo.web.admin.controller.sign;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.guard.model.Guard;
import com.kekeinfo.core.business.image.model.Images;
import com.kekeinfo.core.business.job.model.GJob;
import com.kekeinfo.core.business.job.service.GJobService;
import com.kekeinfo.core.business.sign.model.Sign;
import com.kekeinfo.core.business.sign.service.SignService;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.business.user.service.UserService;
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
public class SignController {
	private static final Logger LOGGER = LoggerFactory.getLogger(SignController.class);
	@Autowired
	SignService signService;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@Autowired private PNodeUtils pnodeUtils;
	@Autowired private GroupService groupService;
	@Autowired GJobService gJobService;
	@Autowired UserService  userService;
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/sign/list.html", method = RequestMethod.GET)
	public String listsigns(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String gid = request.getParameter("gid");
		String uid = request.getParameter("uid");
		//判断用户是否有该项目的编辑权限
        boolean hasRight = false;
        GuardEntity me=null;
        if(StringUtils.isNotBlank(gid)){
			@SuppressWarnings("unchecked")
			List<GuardEntity> listen = (List<GuardEntity>) webCacheUtils.getFromCache(Constants.PROEJCT_GUARD);
			for(GuardEntity m:listen){
				if(m.getId().equals(Long.parseLong(gid))){
					model.addAttribute("gentity", m);
					me=m;
					break;
				}
			}
		}
		
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
		request.setAttribute("activeCode", "gsign"); //指定项目
		model.addAttribute("hasRight", hasRight);
		model.addAttribute("gid", gid);
		model.addAttribute("uid", uid);
		return "admin-sign";
	}
	
	@PreAuthorize("hasRole('GJOB')")
	@RequestMapping(value = "/water/sign/slist.html", method = RequestMethod.GET)
	public String ssigh(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setMenu(model, request);
		String utype=request.getParameter("utype");
		model.addAttribute("utype", utype);
		return "admin-ssign";
	}
	
	@RequestMapping(value = "/water/sign/startsign.html", method = RequestMethod.POST)
	public String startin(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setMenu(model, request);
		String sid = request.getParameter("sid");
		if(StringUtils.isNotBlank(sid)){
			Sign sign = signService.getById(Long.parseLong(sid));
			model.addAttribute("sign", sign);
		}
		return "admin-sign-in";
	}
	
	@RequestMapping(value = "/water/sign/signout.html", method = RequestMethod.POST)
	public String signout(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setMenu(model, request);
		String gid = request.getParameter("gid");
		String jid =request.getParameter("jid");
		Sign sign =new Sign();
		if(StringUtils.isNotBlank(gid)){
			@SuppressWarnings("unchecked")
			List<GuardEntity> listen = (List<GuardEntity>) webCacheUtils.getFromCache(Constants.PROEJCT_GUARD);
			for(GuardEntity m:listen){
				if(m.getId().equals(Long.parseLong(gid))){
					model.addAttribute("gentity", m);
					Guard guard =new Guard();
					guard.setId(m.getId());
					sign.setGuard(guard);
					break;
				}
			}
		}
		if(StringUtils.isNotBlank(jid)){
			GJob job =gJobService.getById(Long.parseLong(gid));
			sign.setGjob(job);
		}
		sign.setStype(1);
		model.addAttribute("sign", sign);
		return "admin-sign-in";
	}

	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/sign/server_processing.shtml", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = " application/json; charset=utf-8")
	public @ResponseBody String getSign(@RequestParam String aoData, HttpServletRequest request,
			HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		AjaxResponse resp = new AjaxResponse();
		DataTableParameter dataTableParam = new DataTableParameter(aoData);
		String gid = request.getParameter("gid");
		String uid =request.getParameter("uid");
		String stype=request.getParameter("stype");
		String status=request.getParameter("status");
		String utype=request.getParameter("utype");
		if(StringUtils.isNotBlank(utype)){
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			uid=user.getId().toString();
		}
		DataTable<Sign> dt = new DataTable<Sign>();
		
			try { // 指定根据什么条件进行模糊查询
				
				HashMap<String, String> orderby = new HashMap<String, String>();
				orderby.put("auditSection.dateModified", "desc");
				List<String> where =new ArrayList<>();
				if(StringUtils.isNotBlank(gid)){
					where.add("guard.id");
					where.add(gid);
				}
				if(StringUtils.isNotBlank(uid)){
					where.add("user.id");
					where.add(uid);
				}
				if(StringUtils.isNotBlank(stype)){
					where.add("stype");
					where.add(stype);
				}
				if(StringUtils.isNotBlank(status)){
					where.add("sattus");
					where.add(status);
				}
				Entitites<Sign> list = signService.getPageListByAttributesLike(null, dataTableParam.getsSearch(),
						Long.valueOf(dataTableParam.getiDisplayLength()).intValue(),
						Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby, where, null);
				dt.setsEcho(dataTableParam.getsEcho() + 1);
				dt.setiTotalDisplayRecords(list.getTotalCount());
				dt.setiTotalRecords(list.getTotalCount());
				dt.setAaData(list.getEntites());
			} catch (Exception e) {
				LOGGER.error("Error while paging signs", e);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}
			ObjectMapper mapper = new ObjectMapper();
			mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:MM:SS"));
			String json = mapper.writeValueAsString(dt);
			return json;
	}

	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/sign/save.shtml",method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String saveSign(@ModelAttribute("sign") Sign sign,@RequestParam(value="imageslist", required=false) List<CommonsMultipartFile> imagelists, Model model, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		AjaxResponse resp = new AjaxResponse();
		Sign db =signService.getById(sign.getId());
		try {
			if (imagelists!=null && imagelists.size()>0) {
				List<Images> imges =new ArrayList<>();
				for (int i = 0; i < imagelists.size(); i++) {
					CommonsMultipartFile image = imagelists.get(i);
					if(image.getSize()<=0) continue; //如果图片大小<=0，则图片不作处理
					String imageName = image.getOriginalFilename();
					String suffix = RadomSixNumber.getImageSuffix(image.getContentType());
					if (suffix.equals("")) {
						suffix = Constants.IMAGE_JPG;
					}
					String in = RadomSixNumber.getImageName(imageName, suffix);
					Images img =new Images();
					img.setDigital(image.getInputStream());
					img.setName(in);
					//压缩
					img.setJpeg(RadomSixNumber.getImageName(img.getName()));
					img.setJdigital(ImageUtils.ByteArrayOutputStream(image.getInputStream()));
					imges.add(img);
				}
				db.setImages(imges);;
			}else{
				db.setImages(null);
			}
			
			
			
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			db.setSignaddress(sign.getSignaddress());
			db.setuName(user.getFirstName());
			signService.saveNew(db);
		} catch (ServiceException e) {
			LOGGER.error("Error while save Sign", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		String returnString = resp.toJSONString();
		return returnString;
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/sign/detail.html", method = RequestMethod.GET)
	public String detail(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setMenu(model, request);
		String sid = request.getParameter("sid");
		
		if(StringUtils.isNotBlank(sid)){
			Sign sign =signService.getBySId(Long.parseLong(sid));
			model.addAttribute("sign", sign);
		}
		return "admin-sign-detail";
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/sign/remove.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody List<AjaxResponse> deleteSign(@RequestParam String[] listId, HttpServletRequest request,
			Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId != null && listId.length > 0) {
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.parseLong(listId[i]);
				try {
					//只能删除当前4个小时以内的
					Sign sign = signService.getById(id);
					if (sign != null) {
						Date now =new Date();
						long diff = now.getTime() - sign.getAuditSection().getDateCreated().getTime();  
						if(diff/(1000 * 60 * 60)>=4){
							resp.setStatus(7777);
							resp.setStatusMessage("只能删除4个小时以内的数据");
						}else{
							signService.delete(sign);
							resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
						}
						resplist.add(resp);
					}
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
		activeMenus.put("ssign", "ssign");
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");
		Menu currentMenu = (Menu) menus.get("ssign");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
	}
}
