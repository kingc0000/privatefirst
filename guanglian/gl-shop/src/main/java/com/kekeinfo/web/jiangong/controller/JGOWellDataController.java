package com.kekeinfo.web.jiangong.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.csite.service.CSiteService;
import com.kekeinfo.core.business.data.service.HoWellDataService;
import com.kekeinfo.core.business.data.service.OWellDataService;
import com.kekeinfo.core.business.monitordata.model.HowellData;
import com.kekeinfo.core.business.observewell.model.Observewell;
import com.kekeinfo.core.business.observewell.service.ObservewellService;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.DataTable;
import com.kekeinfo.web.entity.UnderWater;
import com.kekeinfo.web.utils.DataTableParameter;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.TransExcelView;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;

@Controller
public class JGOWellDataController {
	private static final Logger LOGGER = LoggerFactory.getLogger(JGOWellDataController.class);
	@Autowired private PNodeUtils pnodeUtils;
	@Autowired OWellDataService oWellDataService;
	@Autowired HoWellDataService hoWellDataService;
	@Autowired ObservewellService observewellService;
	@Autowired GroupService groupService;
	@Autowired  TransExcelView transExcelView;
	@Autowired CSiteService cSiteService;
	@Autowired PNodeUtils pNodeUtils;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/jiangong/odata/list.html", method=RequestMethod.GET)
	public String display(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		String pID = request.getParameter("pid");
			setMenu(model,request);
			HowellData pData = new HowellData();
			
			if(!StringUtils.isBlank(pID)){
				try{
					Observewell owell = observewellService.getByIdWithCSite(Long.parseLong(pID));
					List<UnderWater> cs = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
					UnderWater csite=null;
					for(UnderWater c:cs){
						if(c.getId().equals(owell.getcSite().getId())){
							csite=c;
							break;
						}
					} 
					if(csite==null)csite=pnodeUtils.getByCid(owell.getcSite().getId());
					if(!csite.isJiangong()){
						return "water/404";
					}
					model.addAttribute("pName", owell.getName());
					model.addAttribute("pid", owell.getId());
					model.addAttribute("pData", pData);
					model.addAttribute("csite", csite);
					
					request.setAttribute("activeFun", "owell");  //指定当前操作功能
					
					return "jg-owelldata";
				}catch (Exception e){
					LOGGER.debug(e.getMessage());
				}
				
			}
			return "water/404";
		
	}
	
	
	@RequestMapping(value = "/jiangong/odata/server_processing.shtml", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getList(@RequestParam String aoData,HttpServletRequest request,
			HttpServletResponse response) {
		String csiteID = request.getParameter("pid");
		if(!StringUtils.isBlank(csiteID)){
			AjaxResponse resp = new AjaxResponse();
			DataTableParameter dataTableParam = new DataTableParameter(aoData);
			
			DataTable<Map<String, Object>> dt = new DataTable<Map<String, Object>>();
			try {
				
				//指定项目ID进行查询
				List<String> attributes = new ArrayList<String>();
				attributes.add("oWell.id");
				List<String> fieldValues = new ArrayList<String>();
				fieldValues.add(String.valueOf(Long.parseLong(csiteID)));
				HashMap<String, String> orderby = new HashMap<String, String>();
				orderby.put("auditSection.dateCreated", "desc");
				Entitites<HowellData> list  = hoWellDataService.getPageListByAttributes(attributes, fieldValues, Long.valueOf(dataTableParam.getiDisplayLength()).intValue(), 
						Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby);
				dt.setsEcho(dataTableParam.getsEcho()+1);
				dt.setiTotalDisplayRecords(list.getTotalCount());
		        dt.setiTotalRecords(list.getTotalCount());
		        //dt.setAaData(list.getEntites());
		        if(list.getEntites()!=null && list.getEntites().size()>0) {
					for(HowellData pd : list.getEntites()) {
						Map<String, Object> entry = new HashMap<String, Object>();
						entry.put("id", pd.getId());
						entry.put("water", pd.getWater());
						entry.put("temperature", pd.getTemperature());
						entry.put("waterThreshold", pd.getWaterThreshold());
						entry.put("waterDown", pd.getWaterDwon());
						entry.put("temperatureThreshold", pd.getTemperatureThreshold());
						entry.put("status", pd.getStatus());
						entry.put("dateCreated",DateFormatUtils.format(pd.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss") );
						entry.put("modifiedBy", pd.getAuditSection().getModifiedBy());
						entry.put("dateModified", DateFormatUtils.format(pd.getAuditSection().getDateModified(), "yyyy-MM-dd HH:mm:ss"));
						dt.getAaData().add(entry);
					}
		        }
		        ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(dt);
				return json;
			} catch (Exception e) {
				LOGGER.error("Error while paging howelldata", e);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}
		}
		
		return null;
	}
	
	
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("csite", "csite");


		model.addAttribute("activeMenus",activeMenus);
		//
		
	}

}
