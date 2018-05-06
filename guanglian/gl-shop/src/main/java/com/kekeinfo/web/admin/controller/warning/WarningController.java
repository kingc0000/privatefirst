package com.kekeinfo.web.admin.controller.warning;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.generic.model.PointEnumType;
import com.kekeinfo.core.business.monitordata.model.WarningData;
import com.kekeinfo.core.business.point.model.Basepoint;
import com.kekeinfo.core.business.point.service.WarningService;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.DataTable;
import com.kekeinfo.web.entity.UnderWater;
import com.kekeinfo.web.entity.filter.PointFilter;
import com.kekeinfo.web.utils.DataTableParameter;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;

@Controller
public class WarningController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WarningController.class);
	@Autowired private PNodeUtils pnodeUtils;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@Autowired private WarningService warningService;
	@Autowired private GroupService groupService;
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/warning/list.html", method=RequestMethod.GET)
	public String list(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setMenu(model,request);
		String csiteID = request.getParameter("cid");
		request.setAttribute("activeFun", "warning");  //指定当前操作功能
		if(!StringUtils.isBlank(csiteID)){
			try{
				@SuppressWarnings("unchecked")
				List<UnderWater> cs = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
				UnderWater csite=null;
				for(UnderWater c:cs){
					if(c.getId().equals(Long.parseLong(csiteID))){
						csite=c;
						break;
					}
				}
				if(csite==null){
					csite=pnodeUtils.getByCid(Long.parseLong(csiteID));
				}
				model.addAttribute("csite", csite);
				List<Group> egroups = groupService.listGroupByPermissionName("EDIT-PROJECT",1);
				boolean hasRight=false;
				if(!request.isUserInRole("ADMIN") && !request.isUserInRole("SUPERADMIN") ){
		        	if(request.isUserInRole("EDIT-PROJECT")){
		        		hasRight=pnodeUtils.hasProjectRight(request, egroups, csite);
		        		model.addAttribute("hasRight", hasRight);
		        	}
				} else{
					hasRight=true;
				}
				
				model.addAttribute("hasRight", hasRight);
				String ctype = request.getParameter("ctype");
				//手机版
				if(StringUtils.isNotBlank(ctype)){
					model.addAttribute("project", csite);
					return "phone-dataconf";
				}
			}catch (Exception e){
				LOGGER.debug(e.getMessage());
			}
		}else{
			return "csite-wlist";
		}
		return "water-warning";
	}
	
	/**
	 * @param type 1: 降水井，2：疏干井；3：回灌井；4：观测井；5：环境监测
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/warning/server_processing.shtml", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getWarningdatas(@RequestParam String aoData, HttpServletRequest request, HttpServletResponse response) {
		DataTableParameter dataTableParam = new DataTableParameter(aoData);
		String csiteID = request.getParameter("cid");
		String type = request.getParameter("type"); //测点的类型
		String warningtype = request.getParameter("warningtype"); // 告警类型，数据告警或断电告警
		if(!StringUtils.isBlank(csiteID)){
			AjaxResponse resp = new AjaxResponse();
			DataTable dt = new DataTable();
			
			try {
				//指定项目ID进行查询，获取项目下的所有测点
				List<String> attributes = new ArrayList<String>();
				attributes.add("cSite.id");
				List<String> fieldValues = new ArrayList<String>();
				fieldValues.add(String.valueOf(Long.parseLong(csiteID)));
				HashMap<String, String> orderby = new HashMap<String, String>();
				orderby.put("auditSection.dateModified", "desc");
				
				PointEnumType pointType = PointEnumType.getType(Integer.valueOf(type));
				Integer warningType = null;
				if (StringUtils.isNotBlank(warningtype)) {
					warningType = Integer.valueOf(warningtype);
				}
				Entitites<WarningData> plist = warningService.getListByCid(Long.valueOf(csiteID), pointType, warningType, dataTableParam.getsSearch(), Long.valueOf(dataTableParam.getiDisplayLength()).intValue(), 
						Long.valueOf(dataTableParam.getiDisplayStart()).intValue());
				dt.setAaData(plist.getEntites());
				dt.setsEcho(dataTableParam.getsEcho()+1);
				dt.setiTotalDisplayRecords(plist.getTotalCount());
		        dt.setiTotalRecords(plist.getTotalCount());
				ObjectMapper mapper = new ObjectMapper();
		        mapper.getSerializationConfig().addMixInAnnotations(Basepoint.class, PointFilter.class);
		        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		        String json = mapper.writeValueAsString(dt);
		        return json;
			} catch (Exception e) {
				LOGGER.error("Error while paging dataconf", e);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}
		}
		return null;
	}
	
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value="/water/warning/remove.shtml", method=RequestMethod.POST, produces="application/json;charset=utf-8")
	public @ResponseBody List<AjaxResponse> remove(@RequestParam String[] listId,HttpServletRequest request, Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		String type = request.getParameter("type"); //测点的类型
		PointEnumType pointType = PointEnumType.getType(Integer.valueOf(type));
		if (listId!=null&&listId.length>0) {
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.parseLong(listId[i]);
				try {
					warningService.deleteById(id, pointType);
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESSANDEXTERN);
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

	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("csite", "csite");
		model.addAttribute("activeMenus",activeMenus);
	}
}
