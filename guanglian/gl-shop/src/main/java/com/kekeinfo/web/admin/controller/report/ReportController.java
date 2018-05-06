package com.kekeinfo.web.admin.controller.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
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
import com.kekeinfo.core.business.constructionsite.model.ConstructionSite;
import com.kekeinfo.core.business.csite.service.CSiteService;
import com.kekeinfo.core.business.daily.model.Daily;
import com.kekeinfo.core.business.daily.model.WellCondition;
import com.kekeinfo.core.business.daily.service.DailyService;
import com.kekeinfo.core.business.deformmonitor.model.Deformmonitor;
import com.kekeinfo.core.business.dewatering.model.Dewatering;
import com.kekeinfo.core.business.invertedwell.model.Invertedwell;
import com.kekeinfo.core.business.observewell.model.Observewell;
import com.kekeinfo.core.business.pumpwell.model.Pumpwell;
import com.kekeinfo.core.business.report.model.Report;
import com.kekeinfo.core.business.report.service.ReportService;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.DataTable;
import com.kekeinfo.web.entity.UnderWater;
import com.kekeinfo.web.utils.DataTableParameter;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.ReportUtils;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;

@Controller
public class ReportController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportController.class);
	
	@Autowired private PNodeUtils pnodeUtils;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@Autowired CSiteService cSiteService;
	@Autowired ReportService reportService;
	@Autowired GroupService groupService;
	@Autowired DailyService dailyService;
	@Autowired ReportUtils reportUtils;
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/report/wellreport.html", method=RequestMethod.GET)
	public String list(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setWaterMenu(model,request);
		String cid= request.getParameter("cid");
		request.setAttribute("activeFun", "wellreport");  //指定当前操作功能
		if(StringUtils.isNotBlank(cid)){
			try{
				
				List<UnderWater> cs = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
				UnderWater csite =null;
				for(UnderWater c:cs){
					if(c.getId().equals(Long.parseLong(cid))){
						csite=c;
						break;
					}
				}
				if(csite==null)csite=pnodeUtils.getByCid(Long.parseLong(cid));
				model.addAttribute("cid", cid);
				List<Group> egroups = groupService.listGroupByPermissionName("EDIT-PROJECT",1);
				 boolean hasRight=false;
				 if(!request.isUserInRole("ADMIN") && !request.isUserInRole("SUPERADMIN") ){
			        	if(request.isUserInRole("EDIT-PROJECT")){
			        		hasRight=pnodeUtils.hasProjectRight(request, egroups, csite);
			        	}
					}else{
						hasRight=true;
					}
				model.addAttribute("hasRight", hasRight);
				String ctype = request.getParameter("ctype");
				//手机版
				if(StringUtils.isNotBlank(ctype)){
					model.addAttribute("project", csite);
					return "phone-report";
				}
				model.addAttribute("csite", csite);
			}catch (Exception e){
				LOGGER.error(e.getMessage());
			}
		}
		
		return "csite-report";
	}

	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/report/server_processing.shtml", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getBanner(@RequestParam String aoData,HttpServletRequest request,
			HttpServletResponse response) {
		String cid = request.getParameter("cid");
		if(!StringUtils.isBlank(cid)){
			AjaxResponse resp = new AjaxResponse();
			DataTableParameter dataTableParam = new DataTableParameter(aoData);
			
			try {
				
				//指定根据什么条件进行模糊查询
				List<String> attributes = new ArrayList<String>();
				attributes.add("rDate"); 
				HashMap<String, String> orderby = new HashMap<String, String>();
				orderby.put("auditSection.dateModified", "desc");
				Map<String, String> fetches = new HashMap<String, String>();
				fetches.put("cSite", "left");
				List<Object[]> where = new ArrayList<Object[]>();
				where.add(new String[]{"cSite.id", cid, "0"});
				
				Entitites<Report> list = reportService.getPageListByAttributesLike(attributes, dataTableParam.getsSearch(), Long.valueOf(dataTableParam.getiDisplayLength()).intValue(), 
						Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby, where, fetches, true);
				
				DataTable<Map<String, Object>> dt = new DataTable<Map<String, Object>>();
				dt.setsEcho(dataTableParam.getsEcho()+1);
				dt.setiTotalDisplayRecords(list.getTotalCount());
		        dt.setiTotalRecords(list.getTotalCount());
		        if(list.getEntites()!=null && list.getEntites().size()>0) {
					for(Report report : list.getEntites()) {
						Map<String, Object> entry = new HashMap<String, Object>();
						entry.put("id", report.getId());
						entry.put("rDate", DateFormatUtils.format(report.getrDate(), "yyyy-MM-dd"));
						entry.put("dateCreated", DateFormatUtils.format(report.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
						dt.getAaData().add(entry);
					}
				}
				
				dt.setsEcho(dataTableParam.getsEcho()+1);
				dt.setiTotalDisplayRecords(list.getTotalCount());
		        dt.setiTotalRecords(list.getTotalCount());
		        ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(dt);
				return json;
			} catch (Exception e) {
				LOGGER.error("Error while paging csite", e);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}
		}
		
		return null;
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/report/creport.shtml", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public @ResponseBody
	String createReport(HttpServletRequest request,HttpServletResponse response) {
		String cid = request.getParameter("cid");
		if(!StringUtils.isBlank(cid)){
			
			try {
				//reportUtils.setReportService(reportService);
				//reportUtils.setcSiteService(cSiteService);
				Report report = reportUtils.createReport(Long.parseLong(cid));
				return report.getId().toString();
				//return json;
			} catch (Exception e) {
				LOGGER.error("Error while paging csite", e);
				//resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}
		}
		
		return null;
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/report/preport.html", method=RequestMethod.GET)
	public String prepor(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setWaterMenu(model,request);
		String rid= request.getParameter("rid");
		String pid = request.getParameter("cid");
		if(StringUtils.isNotBlank(rid)){
			try{
				Report report = reportService.getIdWithWell(Long.parseLong(rid));
				Date date1=report.getrDate();
				Daily daily = dailyService.getByDate(Long.valueOf(pid), date1);
				ConstructionSite csite= cSiteService.getByCidWithALLWell(Long.parseLong(pid));
				if (daily!=null) {
					Set<WellCondition> wcs =this.setWellName(daily.getWellCon(), csite);
					daily.setWellCon(wcs);
				}
				model.addAttribute("daily", daily);
				model.addAttribute("report", report);
				model.addAttribute("now",new Date());
			}catch (Exception e){
				LOGGER.error(e.getMessage());
				e.printStackTrace();
			}
		}
		
		return "csite-preport";
	}
	
	private Set<WellCondition> setWellName(Set<WellCondition> wcs,ConstructionSite csite){
		for(WellCondition wc:wcs){
			if(StringUtils.isNotBlank(wc.getWellIds())){
				String[] ids = wc.getWellIds().split(",");
				StringBuffer names = new StringBuffer();
				//疏干井,降水井,回灌井,观测井,监测点
				switch (wc.getwType()) {
				case 0:
					for(String s:ids){
						for(Dewatering de:csite.getDewells()){
							if(de.getId().equals(Long.parseLong(s))){
								names.append(de.getName()).append(",");
							}
						}
					}
					if(names.length()>0){
						names=names.deleteCharAt(names.lastIndexOf(","));
					}
					wc.setWellnames(names.toString());
					break;
				case 1:
					for(String s:ids){
						for(Pumpwell p:csite.getPwells()){
							if(p.getId().equals(Long.parseLong(s))){
								names.append(p.getName()).append(",");
							}
						}
					}
					if(names.length()>0){
						names=names.deleteCharAt(names.lastIndexOf(","));
					}
					wc.setWellnames(names.toString());
					break;
				case 2:
					for(String s:ids){
						for(Invertedwell i:csite.getIwells()){
							if(i.getId().equals(Long.parseLong(s))){
								names.append(i.getName()).append(",");
							}
						}
					}
					if(names.length()>0){
						names=names.deleteCharAt(names.lastIndexOf(","));
					}
					wc.setWellnames(names.toString());
					break;
				case 3:
					for(String s:ids){
						for(Observewell o:csite.getOwells()){
							if(o.getId().equals(Long.parseLong(s))){
								names.append(o.getName()).append(",");
							}
						}
					}
					if(names.length()>0){
						names=names.deleteCharAt(names.lastIndexOf(","));
					}
					wc.setWellnames(names.toString());
					break;
				case 4:
					for(String s:ids){
						for(Deformmonitor e:csite.getEwells()){
							if(e.getId().equals(Long.parseLong(s))){
								names.append(e.getName()).append(",");
							}
						}
					}
					if(names.length()>0){
						names=names.deleteCharAt(names.lastIndexOf(","));
					}
					wc.setWellnames(names.toString());
					break;	
				}
				
			}
		}
		return wcs;
	}
	private void setWaterMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("csite", "csite");


		model.addAttribute("activeMenus",activeMenus);
		//
		
	}
	
}
