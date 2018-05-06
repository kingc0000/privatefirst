package com.kekeinfo.web.admin.controller.gateway;

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

import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.csite.service.CSiteService;
import com.kekeinfo.core.business.gateway.model.Gateway;
import com.kekeinfo.core.business.gateway.service.GatewayService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.DataTable;
import com.kekeinfo.web.entity.UnderWater;
import com.kekeinfo.web.utils.DataTableParameter;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;

@Controller
public class GatewayController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayController.class);
	@Autowired private PNodeUtils pnodeUtils;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@Autowired
	private GatewayService gatewayService;
	@Autowired CSiteService cSiteService;
	@Autowired GroupService groupService;
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/gateway/list.html", method=RequestMethod.GET)
	public String list(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setMenu(model,request);
		Gateway gateway = new Gateway();
		model.addAttribute("gateway", gateway);
		String csiteID = request.getParameter("cid");
		request.setAttribute("activeFun", "gateway");  //指定当前操作功能
		if(!StringUtils.isBlank(csiteID)){
			try{
				List<UnderWater> cs = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
				UnderWater csite=null;
				for(UnderWater c:cs){
					if(c.getId().equals(Long.parseLong(csiteID))){
						csite=c;
						break;
					}
				}
				if(csite==null) csite =pnodeUtils.getByCid(Long.parseLong(csiteID));
				model.addAttribute("csite", csite);
				List<Group> egroups = groupService.listGroupByPermissionName("EDIT-PROJECT",1);
				boolean hasRight=false;
				if(!request.isUserInRole("ADMIN") && !request.isUserInRole("SUPERADMIN") ){
		        	if(request.isUserInRole("EDIT-PROJECT")){
		        		hasRight=pnodeUtils.hasProjectRight(request, egroups, csite);
		        	}
				} else{
					hasRight=true;
				}
				model.addAttribute("hasRight", hasRight);
				String ctype = request.getParameter("ctype");
				//手机版
				if(StringUtils.isNotBlank(ctype)){
					model.addAttribute("project", csite);
					return "phone-gateway";
				}
			}catch (Exception e){
				LOGGER.debug(e.getMessage());
			}
			
		}else{
			return "csite-wlist";
		}
		return "water-gateway";
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/gateway/server_processing.shtml", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getGateways(@RequestParam String aoData,HttpServletRequest request,
			HttpServletResponse response) {
		String csiteID = request.getParameter("cid");
		if(!StringUtils.isBlank(csiteID)){
			AjaxResponse resp = new AjaxResponse();
			DataTableParameter dataTableParam = new DataTableParameter(aoData);
			
			DataTable<Gateway> dt = new DataTable<Gateway>();
			try {
				
				//指定项目ID进行查询
				List<String> attributes = new ArrayList<String>();
				attributes.add("cSite.id");
				List<String> fieldValues = new ArrayList<String>();
				fieldValues.add(String.valueOf(Long.parseLong(csiteID)));
				HashMap<String, String> orderby = new HashMap<String, String>();
				orderby.put("auditSection.dateModified", "desc");
				Entitites<Gateway> list  = gatewayService.getPageListByAttributes(attributes, fieldValues, Long.valueOf(dataTableParam.getiDisplayLength()).intValue(), 
						Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby);
				dt.setsEcho(dataTableParam.getsEcho()+1);
				dt.setiTotalDisplayRecords(list.getTotalCount());
		        dt.setiTotalRecords(list.getTotalCount());
		        dt.setAaData(list.getEntites());
		        ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(dt);
				return json;
			} catch (Exception e) {
				LOGGER.error("Error while paging Gateway", e);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}
		}
		return null;
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')") 
	@RequestMapping(value="/water/gateway/save.shtml", method=RequestMethod.POST, produces="application/json; charset=utf-8")
	public @ResponseBody String save(@ModelAttribute("gateway") Gateway gateway, 
			Model model, HttpServletRequest request, HttpServletResponse response)  {
		AjaxResponse resp = new AjaxResponse();
		
		try {
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			if(user!=null){
				gateway.getAuditSection().setModifiedBy(user.getId().toString());
//				ConstructionSite cSite = cSiteService.getById(gateway.getcSite().getId());
//				gateway.setcSite(cSite);
				gatewayService.saveOrUpdate(gateway);
			}
			//
		} catch (ServiceException e) {
			LOGGER.error("Error while save gateway ", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
			String returnString = resp.toJSONString();
			return returnString;
		}
		
		String returnString = resp.toJSONString();
		return returnString;
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value="/water/gateway/remove.shtml", method=RequestMethod.POST, produces="application/json;charset=utf-8")
	public @ResponseBody List<AjaxResponse> remove(@RequestParam String[] listId,HttpServletRequest request) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId!=null&&listId.length>0) {
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.valueOf(listId[i]);
				try {
					gatewayService.deleteById(id);
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
	
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value="/water/gateway/checkValidCode.shtml", method=RequestMethod.GET, produces="application/json;charset=UTF-8")
	public @ResponseBody String checkValidCode(@RequestParam String id, @RequestParam String serialno, HttpServletRequest request, HttpServletResponse response, Locale locale) {
		
		AjaxResponse resp = new AjaxResponse();
		resp.setErrorString("");
		if(StringUtils.isBlank(serialno)) {
			return resp.toJSONString();
		}
		try {
			Entitites<Gateway> result = gatewayService.getListByAttributes(new String[]{"serialno"}, new String[]{serialno}, null);
			if (result.getTotalCount()>0) {
				if (StringUtils.isNotBlank(id)) {
					if (result.getEntites().get(0).getSerialno().equalsIgnoreCase(serialno)&&!result.getEntites().get(0).getId().equals(Long.valueOf(id))) {
						resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
					}
				} else if (result.getEntites().get(0).getSerialno().equalsIgnoreCase(serialno)) {
					resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error while getting category", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
			
		String returnString = resp.toJSONString();
		return returnString;
	}
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("csite", "csite");
		model.addAttribute("activeMenus",activeMenus);
	}
}
