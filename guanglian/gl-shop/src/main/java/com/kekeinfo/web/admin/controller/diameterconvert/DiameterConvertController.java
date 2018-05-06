package com.kekeinfo.web.admin.controller.diameterconvert;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import com.kekeinfo.core.business.common.model.BasedataType;
import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.guard.data.model.DiameterConvertData;
import com.kekeinfo.core.business.guard.data.model.GDbasePoint;
import com.kekeinfo.core.business.guard.data.service.DiameterConvertDataService;
import com.kekeinfo.core.business.guard.model.GPointEnumType;
import com.kekeinfo.core.business.guard.model.Guard;
import com.kekeinfo.core.business.guard.point.model.DiameterConvert;
import com.kekeinfo.core.business.guard.point.model.GbasePoint;
import com.kekeinfo.core.business.guard.point.service.DiameterConvertService;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.admin.entity.web.Menu;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.BaseEntity;
import com.kekeinfo.web.entity.DataTable;
import com.kekeinfo.web.entity.GuardEntity;
import com.kekeinfo.web.event.SpringContextUtils;
import com.kekeinfo.web.utils.DataTableParameter;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.TransExcelView;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;;

@Controller
public class DiameterConvertController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DiameterConvertController.class);
	@Autowired
	DiameterConvertService diameterconvertService;
	@Autowired
	DiameterConvertDataService diameterconvertdataService;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@Autowired
	private PNodeUtils pnodeUtils;
	@Autowired
	private GroupService groupService;
	@Autowired
	TransExcelView transExcelView;
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/diameterconvert/list.html", method = RequestMethod.GET)
	public String listdiameterconverts(Model model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		setMenu(model, request);
		String gid = request.getParameter("gid");
		DiameterConvert diameterconvert = new DiameterConvert();
		model.addAttribute("diameterconvert", diameterconvert);
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
		List<BasedataType> blist = (List<BasedataType>) SpringContextUtils.getServletContext().getAttribute(Constants.BD_GUARD_TYPE);
		model.addAttribute("bt", blist);
		request.setAttribute("activeCode", "diameterconvert"); // 指定项目
		model.addAttribute("hasRight", hasRight);
		model.addAttribute("gid", gid);
		return "admin-diameterconvert";
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/diameterconvert/server_processing.shtml", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = " application/json; charset=utf-8")
	public @ResponseBody String getDiameterConvert(@RequestParam String aoData, HttpServletRequest request,
			HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		AjaxResponse resp = new AjaxResponse();
		DataTableParameter dataTableParam = new DataTableParameter(aoData);
		DataTable<GbasePoint<DiameterConvert>> dt = new DataTable<GbasePoint<DiameterConvert>>();
		String gid = request.getParameter("gid");
		try { // 指定根据什么条件进行模糊查询
			List<String> attributes = new ArrayList<String>();
			attributes.add("markNO");
			attributes.add("memo");
			HashMap<String, String> orderby = new HashMap<String, String>();
			orderby.put("auditSection.dateModified", "desc");
			List<Object[]> where = new ArrayList<>();
			where.add(new Object[] { "guard.id", gid });
			Entitites<GbasePoint<DiameterConvert>> list = diameterconvertService.getPageListByAttributesLike(attributes,
					dataTableParam.getsSearch(), Long.valueOf(dataTableParam.getiDisplayLength()).intValue(),
					Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby, where, null, true);
			dt.setsEcho(dataTableParam.getsEcho() + 1);
			dt.setiTotalDisplayRecords(list.getTotalCount());
			dt.setiTotalRecords(list.getTotalCount());
			dt.setAaData(list.getEntites());
		} catch (Exception e) {
			LOGGER.error("Error while paging diameterconverts", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(dt);
		return json;
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/diameterconvert/save.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String saveDiameterConvert(@ModelAttribute("diameterconvert") DiameterConvert diameterconvert,
			Model model, HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse resp = new AjaxResponse();
		try {
			diameterconvertService.saveOrUpdate(diameterconvert);
			GbasePoint<DiameterConvert> bdata =  diameterconvertService.getById(diameterconvert.getId(), GPointEnumType.DiameterConvert);
            GDbasePoint<DiameterConvertData> buildingdata = diameterconvertdataService.getEqualsHeightData(bdata.getInitHeight(), diameterconvert.getId(), GPointEnumType.DiameterConvert);
			if(buildingdata!=null) {
				buildingdata.setInitHeight(diameterconvert.getInitHeight());
				diameterconvertdataService.update(buildingdata);
			}
		} catch (ServiceException e) {
			LOGGER.error("Error while save DiameterConvert", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		String returnString = resp.toJSONString();
		return returnString;
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value="/water/diameterconvert/exportCols.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=UTF-8")
	public @ResponseBody void  exportCols(Model model, @RequestParam(value="ebegindt", required=false) String begindt, @RequestParam(value="eendtm", required=false) String endtm, 
			HttpServletRequest request, HttpServletResponse response, Locale locale) throws IOException {
		
		try {
			String eid =request.getParameter("eid");
			if (eid != null ) {
				List<String> attributes = new ArrayList<String>();
				List<String> where = new ArrayList<>();
				where.add("id");
				where.add(eid);
				Entitites<GbasePoint<DiameterConvert>> list  = diameterconvertService.getPageListByAttributesLike(null, null, null, null, null,where,null);
				
				XSSFWorkbook wb=null;
				if(list!=null && !list.getEntites().isEmpty()){
					String fileName ="直径收敛";
					
					
					for(GbasePoint<DiameterConvert> p:list.getEntites()){
						attributes = new ArrayList<String>();
						attributes.add("INITHEIGHT");
						attributes.add("CURTHEIGHT");
						attributes.add("CURTHEIGHT-INITHEIGHT");
						attributes.add("date_format(CALIBRATION, '%Y-%m-%d')");
						StringBuffer include_where = null;
						 include_where = new StringBuffer().append(" where ( CALIBRATION BETWEEN '").append(begindt+" 00:00:00").append("' AND '").append(endtm+" 23:59:59").append("' ) AND DIAMETERCONVERT_ID = ").append(p.getId());
						List<Object[]> bbtype= diameterconvertdataService.getBySql(attributes, "DIAMETERCONVERTDATA", include_where.toString());
						String[] title={"上次高程","本次高程","总累计位移","校准时间"};
						String sheet = p.getMarkNO();
						wb =transExcelView.export(sheet, title, bbtype,wb);
						
					}
					fileName = URLEncoder.encode(fileName, "UTF-8");
					response.addHeader("Content-Disposition", "attachment;filename=" + fileName+".xlsx");
					//写文件
					OutputStream os = response.getOutputStream();
					wb.write(os);
					os.close();
				} else {
					response.addHeader("Content-Disposition", "attachment;filename=nodata.txt");
					OutputStream os = response.getOutputStream();
					os.write("当前没有测点数据".getBytes());
					os.close();
				}
			}
		
		
	} catch (Exception e) {
		LOGGER.debug("fail export");
		response.sendError(404, Constants.FILE_NOT_FOUND);
	}
		
		
	}
	
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value="/water/diameterconvert/importCols.shtml", method=RequestMethod.POST, produces="text/html;charset=UTF-8")
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
		    			//0:测点 1：本次高程 2：导入
		    			//0:1必须存在
		    			if(StringUtils.isBlank(transExcelView.getValue(row,col[0], true)) ||
		    					StringUtils.isBlank(transExcelView.getValue(row,col[1], true))){
		    				returns.add(String.valueOf(i+1));
			    			float num= (float)i/trLength;  
			    			session.setAttribute("import", df.format(num));
		    				continue;
		    			}
		    			
		    			
		    				//获取测点
		    				String cedian = transExcelView.getValue(row,col[0], true);
		    				GbasePoint<DiameterConvert> msurface =diameterconvertService.getByNO(cedian, GPointEnumType.DiameterConvert,Long.parseLong(iid));
		    				DiameterConvert surface =(DiameterConvert) msurface;
		    				Date date =null;
		    				if(surface!=null){
		    					//日期是否存在，如果不存在即为当前的
			    				if(StringUtils.isNotBlank((transExcelView.getValue(row,col[2], true)))){
			    					date=transExcelView.getDateValue(row,col[2], true);
			    				}else{
			    					date =new Date();
			    				}
			    				DiameterConvertData pdata = new DiameterConvertData();
			    				pdata.getAuditSection().setDateCreated(new Date());
			    				pdata.setCalibration(date);
			    				BigDecimal current =transExcelView.getBigValue(row,col[1], true);
			    				pdata.setCurtHeight(current);
			    				pdata.setSpoint(surface);
			    				GDbasePoint<DiameterConvertData> last = diameterconvertdataService.getLast(date, GPointEnumType.DiameterConvert, surface.getId());
			    				GDbasePoint<DiameterConvertData> next = diameterconvertdataService.getNext(date, GPointEnumType.DiameterConvert, surface.getId());
			    				if(last!=null){
			    					pdata.setInitHeight(last.getCurtHeight());
			    				}
			    				if(next!=null){
			    					next.setInitHeight(current);
			    					diameterconvertdataService.update(next);
			    				}
			    				diameterconvertdataService.save(pdata);
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
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value="/water/diameterconvert/importPoints.shtml", method=RequestMethod.POST, produces="text/html;charset=UTF-8")
	public @ResponseBody String importPoints(Model model, @RequestParam(required=false, value="uploadfile") CommonsMultipartFile uploadfile, HttpServletRequest request, HttpServletResponse response, Locale locale) {
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
		    	Guard monitor =new Guard();
		    	monitor.setId(Long.parseLong(iid));
		    	for(int i=0;i<=trLength;i++){
		    		Row row = sheet.getRow(i);
		    		DiameterConvert building =new DiameterConvert();
		    		try{
		    			//0:测点 1：本次高程 2：初始高程
		    			//0:1必须存在
		    			if(StringUtils.isBlank(transExcelView.getValue(row,col[0], true)) ||
		    					StringUtils.isBlank(transExcelView.getValue(row,col[1], true))){
		    				returns.add(String.valueOf(i+1));
			    			float num= (float)i/trLength;  
			    			session.setAttribute("import", df.format(num));
		    				continue;
		    			}
		    			building.setGuard(monitor);
		    			//获取测点
	    				String cedian = transExcelView.getValue(row,col[0], true);
	    				//检查测点命名是否规范
	    				List<BasedataType> blist = (List<BasedataType>) SpringContextUtils.getServletContext().getAttribute(Constants.BD_GUARD_TYPE);
	    				boolean found =false;
	    				for(BasedataType bl:blist){
	    					if(cedian.startsWith(bl.getValue())){
	    						found=true;
	    						break;
	    					}
	    				}
	    				if(!found){
	    					returns.add(String.valueOf(i+1));
			    			float num= (float)i/trLength;  
			    			session.setAttribute("import", df.format(num));
		    				continue;
	    				}
	    				building.setMarkNO(cedian);
	    				BigDecimal current =transExcelView.getBigValue(row,col[1], true);
	    				building.setInitHeight(current);
		    			//备注
		    			try{
		    				String ri =transExcelView.getValue(row,col[2], true);
		    				building.setMemo(ri);
		    			}catch (Exception e){
		    				
		    			}
		    			
		    		}catch (Exception e){
		    			e.printStackTrace();
		    			returns.add(String.valueOf(i+1));
		    			float num= (float)i/trLength;  
		    			session.setAttribute("import", df.format(num));
		    			continue;
		    		}
		    		diameterconvertService.save(building);
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
	@RequestMapping(value = "/water/diameterconvert/remove.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody List<AjaxResponse> deleteDiameterConvert(@RequestParam String[] listId,
			HttpServletRequest request, Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId != null && listId.length > 0) {
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.parseLong(listId[i]);
				try {
					GbasePoint<DiameterConvert> diameterconvert = diameterconvertService.getById(id);
					if (diameterconvert != null) {
						diameterconvertService.delete(diameterconvert);
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

	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("diameterconvert", "diameterconvert");
		activeMenus.put("diameterconvert-list", "diameterconvert-list");
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");
		Menu currentMenu = (Menu) menus.get("diameterconvert");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
	}
}
