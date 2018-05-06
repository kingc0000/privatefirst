package com.kekeinfo.web.admin.controller.equip;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.kekeinfo.core.business.attach.model.Attach;
import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.equipment.model.Equip;
import com.kekeinfo.core.business.equipment.service.EquipService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.admin.entity.web.Menu;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.DataTable;
import com.kekeinfo.web.entity.PinYin;
import com.kekeinfo.web.utils.DataTableParameter;
import com.kekeinfo.web.utils.DownloadUtils;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.PinyinUtils;
import com.kekeinfo.web.utils.RadomSixNumber;;

@Controller
public class EquipController {
	private static final Logger LOGGER = LoggerFactory.getLogger(EquipController.class);
	@Autowired
	EquipService equipService;
	@Autowired private PNodeUtils pnodeUtils;

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/equip/list.html", method = RequestMethod.GET)
	public String listequips(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setMenu(model, request);
		Equip equip = new Equip();
		model.addAttribute("equip", equip);
		//判断用户是否有该项目的编辑权限
        boolean hasRight = false;
        try {
        	if (request.isUserInRole("ADMIN") || request.isUserInRole("SUPERADMIN")) {
        		hasRight = true;
        	} else {
        		if (request.isUserInRole("EDIT-PROJECT")) {
        			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
        			hasRight = pnodeUtils.hasProjectRightMoiniter(user.getpNodes());
        		}
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
        model.addAttribute("hasRight", hasRight);
		return "admin-equip";
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/equip/server_processing.shtml", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = " application/json; charset=utf-8")
	public @ResponseBody String getEquip(@RequestParam String aoData, HttpServletRequest request,
			HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		AjaxResponse resp = new AjaxResponse();
		DataTableParameter dataTableParam = new DataTableParameter(aoData);
		DataTable<Equip> dt = new DataTable<Equip>();
		try { // 指定根据什么条件进行模糊查询
			List<String> attributes = new ArrayList<String>();
			HashMap<String, String> orderby = new HashMap<String, String>();
			orderby.put("auditSection.dateModified", "desc");
			Entitites<Equip> list = equipService.getPageListByAttributesLike(attributes, dataTableParam.getsSearch(),
					Long.valueOf(dataTableParam.getiDisplayLength()).intValue(),
					Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby, null, null);
			dt.setsEcho(dataTableParam.getsEcho() + 1);
			dt.setiTotalDisplayRecords(list.getTotalCount());
			dt.setiTotalRecords(list.getTotalCount());
			dt.setAaData(list.getEntites());
		} catch (Exception e) {
			LOGGER.error("Error while paging equips", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
		String json = mapper.writeValueAsString(dt);
		return json;
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/equip/save.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String saveEquip(@ModelAttribute("equip") Equip equip, Model model,@RequestParam(required=false, value="fileupload") List<CommonsMultipartFile> uploadfiles, HttpServletRequest request,
			HttpServletResponse response) {
		AjaxResponse resp = new AjaxResponse();
		try {
			String delids= request.getParameter("delids");
			//附件处理
			if(uploadfiles!=null && !uploadfiles.isEmpty()){
				List<Attach> imgs= new ArrayList<>();
				for(CommonsMultipartFile uploadfile:uploadfiles){
					Attach dDoc = new Attach();
					String originalName = uploadfile.getOriginalFilename();
	        		String name = RadomSixNumber.getImageName(originalName);
	        		String uploadContentType = uploadfile.getContentType();
	        		//文件类型是否可以预览
	        		dDoc.setFileType(DownloadUtils.canPreview(uploadContentType));
	        		
	        		dDoc.setDigital(uploadfile.getInputStream());
	        		dDoc.setFileName(name);
					
					imgs.add(dDoc);
				}
				equip.setAttach(imgs);
			}
			equipService.saveOrUpdate(equip,delids);
		} catch (ServiceException | IOException e) {
			LOGGER.error("Error while save Equip", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		String returnString = resp.toJSONString();
		return returnString;
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/equip/remove.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody List<AjaxResponse> deleteEquip(@RequestParam String[] listId, HttpServletRequest request,
			Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId != null && listId.length > 0) {
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.parseLong(listId[i]);
				try {
					Equip equip = equipService.getById(id);
					if (equip != null) {
						equipService.delete(equip);
						resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
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
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/equip/images.shtml", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String images(HttpServletRequest request, Locale locale) throws Exception {
		
		String sUserId = request.getParameter("cid");
		
		try {
			Equip csite = equipService.getByIdWithImg(Long.parseLong(sUserId));
			if(csite!=null){
				if(csite.getAttach()!=null && csite.getAttach().size()>0){
					ObjectMapper mapper = new ObjectMapper();
					String json = mapper.writeValueAsString(csite.getAttach());
					return json;
				}
				
				
			}
		
		} catch (Exception e) {
			LOGGER.error("Error while deleting product price", e);
			return "-1";
			
		}
		
		return "0";
		
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/monitoreqip/equips.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=utf-8")
	public @ResponseBody List<PinYin> users(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		try{
			List<Object[]> stores = equipService.getPinYin(null);
			List<PinYin> pinyin = PinyinUtils.getPinyinList(stores);
			return pinyin;
		}catch (Exception e){
			return null;
		}
		
	}

	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("equip", "equip");
		activeMenus.put("equip-list", "equip-list");
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");
		Menu currentMenu = (Menu) menus.get("equip");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
	}
}
