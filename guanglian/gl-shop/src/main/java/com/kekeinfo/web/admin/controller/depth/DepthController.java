package com.kekeinfo.web.admin.controller.depth;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
import com.kekeinfo.core.business.monitor.model.Monitor;
import com.kekeinfo.core.business.monitor.oblique.model.Depth;
import com.kekeinfo.core.business.monitor.oblique.model.Oblique;
import com.kekeinfo.core.business.monitor.oblique.model.ObliqueData;
import com.kekeinfo.core.business.monitor.oblique.service.DepthService;
import com.kekeinfo.core.business.monitor.oblique.service.ObliqueDataService;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.admin.entity.web.Menu;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.BaseEntity;
import com.kekeinfo.web.entity.DataTable;
import com.kekeinfo.web.entity.MonitorEntity;
import com.kekeinfo.web.utils.DataTableParameter;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.TransExcelView;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;;

@Controller
public class DepthController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DepthController.class);
	@Autowired
	DepthService depthService;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@Autowired
	private PNodeUtils pnodeUtils;
	@Autowired
	private GroupService groupService;
	@Autowired  TransExcelView transExcelView;
	@Autowired ObliqueDataService obliqueDataService;

	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/depth/list.html", method = RequestMethod.GET)
	public String listdepths(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setMenu(model, request);
		String sid = request.getParameter("sid");
		String mid = request.getParameter("mid");
		Depth depth = new Depth();
		model.addAttribute("depth", depth);
		// 判断用户是否有该项目的编辑权限
		boolean hasRight = false;
		MonitorEntity me = null;
		if (StringUtils.isNotBlank(sid)) {
			@SuppressWarnings("unchecked")
			List<MonitorEntity> listen = (List<MonitorEntity>) webCacheUtils.getFromCache(Constants.PROEJCT_MONITOR);
			for (MonitorEntity m : listen) {
				if (m.getId().equals(Long.parseLong(mid))) {
					me = m;
					model.addAttribute("mentity", m);
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
		request.setAttribute("activeCode", "oblique"); // 指定项目
		model.addAttribute("hasRight", hasRight);
		model.addAttribute("sid", sid);
		model.addAttribute("mid",mid);
		return "admin-depth";
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/depth/checkDeepCode.shtml", method=RequestMethod.GET, produces="application/json;charset=UTF-8")
	public @ResponseBody String checkDeepCode(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String code = request.getParameter("deep");
		String id = request.getParameter("id");
		String oid= request.getParameter("obliqueid");

		AjaxResponse resp = new AjaxResponse();
		resp.setStatus(0);
		try {
			
			if(!StringUtils.isBlank(code) && !StringUtils.isNotBlank(oid)) {
				if(!StringUtils.isBlank(id)){
					Depth dbDepth = depthService.getById(Long.parseLong(id));
					if(dbDepth!=null){
						if(dbDepth.getDeep().toString().equalsIgnoreCase(code)){
							return resp.toJSONString();
						}else{
							Depth depth = depthService.findByDeep(new BigDecimal(code),Long.parseLong(oid));
							if(depth !=null){
								resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
								resp.setStatusMessage("该深度已经存在");
							}
						}
					}
				}
				Depth depth = depthService.findByDeep(new BigDecimal(code),Long.parseLong(oid));
				if(depth !=null){
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
					resp.setStatusMessage("该深度已经存在");
				}
			}

		} catch (Exception e) {
			LOGGER.error("Error while getting user", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			
		}
		return resp.toJSONString();
	}

	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/depth/server_processing.shtml", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = " application/json; charset=utf-8")
	public @ResponseBody String getDepth(@RequestParam String aoData, HttpServletRequest request,
			HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		AjaxResponse resp = new AjaxResponse();
		String sid=request.getParameter("sid");
		if(StringUtils.isNotBlank(sid)){
			DataTableParameter dataTableParam = new DataTableParameter(aoData);
			DataTable<Depth> dt = new DataTable<Depth>();
			try { // 指定根据什么条件进行模糊查询
				List<String> attributes = new ArrayList<String>();
				attributes.add("DEEP");
				HashMap<String, String> orderby = new HashMap<String, String>();
				orderby.put("auditSection.dateModified", "desc");
				List<Object[]> where =new ArrayList<>();
				where.add(new Object[]{"oblique.id", sid});
				Entitites<Depth> list = depthService.getPageListByAttributesLike(attributes, dataTableParam.getsSearch(), Long.valueOf(dataTableParam.getiDisplayLength()).intValue(), 
						Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby, where, null, true);
				dt.setsEcho(dataTableParam.getsEcho() + 1);
				dt.setiTotalDisplayRecords(list.getTotalCount());
				dt.setiTotalRecords(list.getTotalCount());
				dt.setAaData(list.getEntites());
			} catch (Exception e) {
				LOGGER.error("Error while paging depths", e);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(dt);
			return json;
		}else{
			return "";
		}
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/depth/save.shtml", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public @ResponseBody String saveDepth(@ModelAttribute("depth") Depth depth, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		AjaxResponse resp = new AjaxResponse();
		try {
			depthService.saveOrUpdate(depth);
		} catch (ServiceException e) {
			LOGGER.error("Error while save Depth", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		String returnString = resp.toJSONString();
		return returnString;
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/depth/remove.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody List<AjaxResponse> deleteDepth(@RequestParam String[] listId, HttpServletRequest request,
			Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId != null && listId.length > 0) {
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.parseLong(listId[i]);
				try {
					Depth depth = depthService.getById(id);
					if (depth != null) {
						depthService.delete(depth);
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
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value="/water/depth/importCols.shtml", method=RequestMethod.POST, produces="text/html;charset=UTF-8")
	public @ResponseBody String importCols(Model model, @RequestParam(required=false, value="uploadfile") CommonsMultipartFile uploadfile, HttpServletRequest request, HttpServletResponse response, Locale locale) {
		AjaxResponse resp = new AjaxResponse();
		String cols = request.getParameter("cols");
		String iid =request.getParameter("pid");
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		if(!StringUtils.isBlank(cols) && StringUtils.isNotBlank(iid)){
			try {
				
				HttpSession session = request.getSession();
				session.setAttribute("import", "0");
				String [] col= null;
				col= cols.split(",");
				List<String> returns = new ArrayList<String>();
				Workbook book=transExcelView.getBook(uploadfile.getInputStream());
				Sheet sheet = book.getSheetAt(0);
		    	int trLength = sheet.getLastRowNum();
		    	DecimalFormat df = new DecimalFormat("0.00");//格式化小数 
		    	if(trLength>transExcelView.getImpMaxLine())trLength =transExcelView.getImpMaxLine();
		    	for(int i=0;i<=trLength;i++){
		    		Row row = sheet.getRow(i);
		    		try{
		    			//0:1必须存在
		    			if(StringUtils.isBlank(transExcelView.getValue(row,col[0], true)) ||
		    					StringUtils.isBlank(transExcelView.getValue(row,col[1], true))){
		    				returns.add(String.valueOf(i+1));
			    			float num= (float)i/trLength;  
			    			session.setAttribute("import", df.format(num));
		    				continue;
		    			}
		    				//获取测点
		    				BigDecimal cedian  =transExcelView.getBigValue(row,col[0], true);
		    				if(cedian!=null){
		    					Depth depth =depthService.findByDeep(cedian, Long.parseLong(iid));
			    				if(depth!=null){
			    					ObliqueData obd=new ObliqueData();
			    					obd.setDepth(depth);
			    					BigDecimal current =transExcelView.getBigValue(row,col[1], true);
			    					if(current!=null){
			    						obd.setCurTotal(current);
			    						Date date =null;
			    						//日期是否存在，如果不存在即为当前的
					    				if(StringUtils.isBlank(transExcelView.getValue(row,col[2], true)) && transExcelView.getValue(row,col[2], true)!=null){
					    					date=transExcelView.getDateValue(row,col[4], true);
					    				}else{
					    					date =new Date();
					    				}
					    				obd.setCurDate(date);
					    				ObliqueData last =obliqueDataService.getLast(date, depth.getId());
					    				ObliqueData next =obliqueDataService.getNext(date, depth.getId());
					    				if(last!=null){
					    					obd.setLastTotal(last.getCurTotal());
					    				}
					    				if(next!=null){
					    					next.setLastTotal(current);
					    					obliqueDataService.update(next);
					    				}
					    				obliqueDataService.save(obd);
			    					}else{
			    						returns.add(String.valueOf(i+1));
						    			float num= (float)i/trLength;  
						    			session.setAttribute("import", df.format(num));
					    				continue;
			    					}
			    				}else{
			    					returns.add(String.valueOf(i+1));
					    			float num= (float)i/trLength;  
					    			session.setAttribute("import", df.format(num));
				    				continue;
			    				}
		    				}else{
		    					returns.add(String.valueOf(i+1));
				    			float num= (float)i/trLength;  
				    			session.setAttribute("import", df.format(num));
			    				continue;
		    				}
		    				
		    		}catch (Exception e){
		    			e.printStackTrace();
		    			returns.add(String.valueOf(i+1));
		    			float num= (float)i/trLength;  
		    			session.setAttribute("import", df.format(num));
		    			continue;
		    		}
		    	}	
		    	
				if(returns==null || returns.size()==0){
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
				}else{
					resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
					//全部失败
					if(returns.size()==1){
						resp.addEntry("cols",returns.get(0));
					}else{
						StringBuffer sb = new StringBuffer();
						for(String s:returns) sb.append(s).append(",");
						sb.deleteCharAt(sb.lastIndexOf(","));
						resp.addEntry("cols",sb.toString());
					}
					
				}
				session.removeAttribute("import");
			} catch (Exception e) {
			LOGGER.error(e.getMessage());
			}
		}
		
		return resp.toJSONString();
	}
	
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value="/water/depth/importPoints.shtml", method=RequestMethod.POST, produces="text/html;charset=UTF-8")
	public @ResponseBody String importPoints(Model model, @RequestParam(required=false, value="uploadfile") CommonsMultipartFile uploadfile, HttpServletRequest request, HttpServletResponse response, Locale locale) {
		AjaxResponse resp = new AjaxResponse();
		String cols = request.getParameter("cols");
		String iid =request.getParameter("pid");
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		if(!StringUtils.isBlank(cols) && StringUtils.isNotBlank(iid)){
			try {
				Oblique ob =new Oblique();
				ob.setId(Long.parseLong(iid));
				HttpSession session = request.getSession();
				session.setAttribute("import", "0");
				String [] col= null;
				col= cols.split(",");
				List<String> returns = new ArrayList<String>();
				Workbook book=transExcelView.getBook(uploadfile.getInputStream());
				Sheet sheet = book.getSheetAt(0);
		    	int trLength = sheet.getLastRowNum();
		    	DecimalFormat df = new DecimalFormat("0.00");//格式化小数 
		    	if(trLength>transExcelView.getImpMaxLine())trLength =transExcelView.getImpMaxLine();
		    	Monitor monitor =new Monitor();
		    	monitor.setId(Long.parseLong(iid));
		    	for(int i=0;i<=trLength;i++){
		    		Row row = sheet.getRow(i);
		    		try{
		    			//0必须存在
		    			if(StringUtils.isBlank(transExcelView.getValue(row,col[0], true)) ){
		    				returns.add(String.valueOf(i+1));
			    			float num= (float)i/trLength;  
			    			session.setAttribute("import", df.format(num));
		    				continue;
		    			}
		    			Depth depth =new Depth();
		    			depth.setOblique(ob);;
	    				//获取测点
	    				BigDecimal cedian = transExcelView.getBigValue(row,col[0], true);
	    				if(cedian!=null){
	    					depth.setDeep(cedian);
		    				if(!StringUtils.isBlank(transExcelView.getValue(row,col[1], true))){
		    					BigDecimal current =transExcelView.getBigValue(row,col[1], true);
		    					if(current!=null){
		    						depth.setAv(current);
		    					}
		    				}
		    				depthService.save(depth);
	    				}else{
			    			returns.add(String.valueOf(i+1));
			    			float num= (float)i/trLength;  
			    			session.setAttribute("import", df.format(num));
			    			continue;
	    				}
		    			
		    		}catch (Exception e){
		    			e.printStackTrace();
		    			returns.add(String.valueOf(i+1));
		    			float num= (float)i/trLength;  
		    			session.setAttribute("import", df.format(num));
		    			continue;
		    		}
		    	}	
		    	
				if(returns==null || returns.size()==0){
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
				}else{
					resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
					//全部失败
					if(returns.size()==1){
						resp.addEntry("cols",returns.get(0));
					}else{
						StringBuffer sb = new StringBuffer();
						for(String s:returns) sb.append(s).append(",");
						sb.deleteCharAt(sb.lastIndexOf(","));
						resp.addEntry("cols",sb.toString());
					}
					
				}
				session.removeAttribute("import");
			} catch (Exception e) {
			LOGGER.error(e.getMessage());
			}
		}
		
		return resp.toJSONString();
	}
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("depth", "depth");
		activeMenus.put("depth-list", "depth-list");
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");
		Menu currentMenu = (Menu) menus.get("depth");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
	}
}
