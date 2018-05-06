package com.kekeinfo.web.admin.controller.gonproblem;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.kekeinfo.core.business.attach.model.Attach;
import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.gonproblem.model.GonProblem;
import com.kekeinfo.core.business.gonproblem.service.GonProblemService;
import com.kekeinfo.core.business.project.model.Project;
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
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.RadomSixNumber;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;;

@Controller
public class GonProblemController {
	private static final Logger LOGGER = LoggerFactory.getLogger(GonProblemController.class);
	@Autowired
	GonProblemService gonproblemService;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@Autowired
	private PNodeUtils pnodeUtils;
	@Autowired
	private GroupService groupService;

	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/gonproblem/list.html", method = RequestMethod.GET)
	public String listgonproblems(Model model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		setMenu(model, request);
		GonProblem gonproblem = new GonProblem();
		model.addAttribute("gonproblem", gonproblem);
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
			
			request.setAttribute("activeCode", "gnoproblem"); // 指定项目
			model.addAttribute("hasRight", hasRight);
			model.addAttribute("gid", gid);
		return "admin-gonproblem";
	}

	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/gonproblem/server_processing.shtml", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = " application/json; charset=utf-8")
	public @ResponseBody String getGonProblem(@RequestParam String aoData, HttpServletRequest request,
			HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		AjaxResponse resp = new AjaxResponse();
		DataTableParameter dataTableParam = new DataTableParameter(aoData);
		DataTable<GonProblem> dt = new DataTable<GonProblem>();
		String gid=request.getParameter("gid");
		String filter =request.getParameter("monStatus");
		if(StringUtils.isNotBlank(gid)){
			try { // 指定根据什么条件进行模糊查询
				List<String> attributes = new ArrayList<String>();
				attributes.add("monRank"); 
				attributes.add("monType"); 
				attributes.add("owner");
				HashMap<String, String> orderby = new HashMap<String, String>();
				orderby.put("auditSection.dateModified", "desc");
				List<Object[]> where = new ArrayList<>();
				where.add(new Object[] { "guard.id", gid });
				if(StringUtils.isNotBlank(filter)){
					where.add(new Object[] { "monStatus", filter });
				}
				Entitites<GonProblem> list = gonproblemService.getPageListByAttributesLike(attributes,
						dataTableParam.getsSearch(), Long.valueOf(dataTableParam.getiDisplayLength()).intValue(),
						Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby, where, null, true);
				dt.setsEcho(dataTableParam.getsEcho() + 1);
				dt.setiTotalDisplayRecords(list.getTotalCount());
				dt.setiTotalRecords(list.getTotalCount());
				dt.setAaData(list.getEntites());
			} catch (Exception e) {
				LOGGER.error("Error while paging gonproblems", e);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}
		}
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:MM:SS"));
		String json = mapper.writeValueAsString(dt);
		return json;
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/gonproblem/imges.shtml", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = " application/json; charset=utf-8")
	public @ResponseBody String getimages(HttpServletRequest request,
			HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		
		String guid =request.getParameter("guid");
		if(StringUtils.isNotBlank(guid)){
			try{
				GonProblem gon =gonproblemService.withAttach(Long.parseLong(guid));
				if(gon.getAttach()!=null && gon.getAttach().size()>0){
					ObjectMapper mapper = new ObjectMapper();
					String json = mapper.writeValueAsString(gon.getAttach());
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
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/water/gonproblem/preview.html", method = RequestMethod.GET)
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
			GonProblem gd=gonproblemService.withAttach(Long.parseLong(did));
			model.addAttribute("gon", gd);
		}catch (Exception e){
			
		}
		return "guard-gonpre";
	}

	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/gonproblem/save.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String saveGonProblem(@ModelAttribute("gonproblem") GonProblem gonproblem,@RequestParam(required = false, value = "fileupload") List<CommonsMultipartFile> uploadfiles,
			HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse resp = new AjaxResponse();
		try {
			String delImageIds = request.getParameter("delids");
			//项目负责人
			if(gonproblem.getId()==null){
				List<GuardEntity> listen = (List<GuardEntity>) webCacheUtils.getFromCache(Constants.PROEJCT_GUARD);
				for (GuardEntity m : listen) {
					if (m.getId().equals(gonproblem.getGuard().getId())) {
						gonproblem.setOwner(m.getOwner());
						break;
					}
				}
			}
			// 处理新增加的日志图片
				List<Attach> newImages = new ArrayList<Attach>();
				if (uploadfiles != null && !uploadfiles.isEmpty()) {
					try {
						for (int i = 0; i < uploadfiles.size(); i++) {
							MultipartFile img = uploadfiles.get(i);
							if (img.getSize() <= 0)
								continue; // 如果图片大小<=0，则图片不作处理
							String imageName = img.getOriginalFilename();
							String suffix = imageName.substring(imageName.lastIndexOf(".")+1);
							
							Attach attach = new Attach();
							String in = RadomSixNumber.getImageName(imageName, suffix).toLowerCase();
							for (String filetype : FileType.FILE_TYPE) {
								if (in.endsWith(filetype)) {
									attach.setFileType(true);
									break;
								}
							}
							attach.setDigital(img.getInputStream());
							attach.setFileName(in);
							newImages.add(attach);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					gonproblem.setAttach(newImages);
				}
			//传递推送相关的信息
				List<GuardEntity> listen = (List<GuardEntity>) webCacheUtils.getFromCache(Constants.PROEJCT_GUARD);
				if(listen!=null && listen.size()>0){
					for(GuardEntity ge:listen){
						if(ge.getId().equals(gonproblem.getGuard().getId())){
							Project pr =new Project();
							pr.setId(ge.getPid());
							pr.setProjectOwnerid(ge.getUid());
							pr.setName(ge.getName());
							gonproblem.getGuard().setProject(pr);
						}
					}
				}
			gonproblemService.saveUpdate(gonproblem, delImageIds);
		} catch (ServiceException e) {
			LOGGER.error("Error while save GonProblem", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		String returnString = resp.toJSONString();
		return returnString;
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/gonproblem/remove.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody List<AjaxResponse> deleteGonProblem(@RequestParam String[] listId, HttpServletRequest request,
			Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId != null && listId.length > 0) {
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.parseLong(listId[i]);
				try {
					gonproblemService.deletewithattach(id);
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
	
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/gonproblem/doman.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String doman( HttpServletRequest request,
			Locale locale) throws Exception {
		AjaxResponse resp =new AjaxResponse();
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		String guid =request.getParameter("guid");
		if(StringUtils.isNotBlank(guid)){
			try{
				GonProblem gon =gonproblemService.getById(Long.parseLong(guid));
				gon.setMonStatus(1);
				User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
				gon.setDidman(user.getFirstName());
				String su =request.getParameter("suggest");
				if(StringUtils.isNotBlank(su)){
					gon.setSuggest(su);
				}
				gonproblemService.update(gon);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			}catch (Exception e){
				
			}
		}
		
		return resp.toJSONString();
	}

	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("gonproblem", "gonproblem");
		activeMenus.put("gonproblem-list", "gonproblem-list");
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");
		Menu currentMenu = (Menu) menus.get("gonproblem");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
	}
}
