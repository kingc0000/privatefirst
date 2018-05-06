package com.kekeinfo.web.admin.controller.statistics;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
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

import com.kekeinfo.core.business.basedata.service.BaseDataService;
import com.kekeinfo.core.business.common.model.BasedataType;
import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.constructionsite.model.ConstructionSite;
import com.kekeinfo.core.business.csite.service.CSiteService;
import com.kekeinfo.core.business.deformmonitor.model.Deformmonitor;
import com.kekeinfo.core.business.deformmonitor.service.DeformmonitorService;
import com.kekeinfo.core.business.department.model.Department;
import com.kekeinfo.core.business.dewatering.model.Dewatering;
import com.kekeinfo.core.business.dewatering.service.DewateringService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.invertedwell.model.Invertedwell;
import com.kekeinfo.core.business.invertedwell.service.InvertedwellService;
import com.kekeinfo.core.business.observewell.model.Observewell;
import com.kekeinfo.core.business.observewell.service.ObservewellService;
import com.kekeinfo.core.business.point.model.Basepoint;
import com.kekeinfo.core.business.pumpwell.model.Pumpwell;
import com.kekeinfo.core.business.pumpwell.service.PumpwellService;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.business.zone.model.Zone;
import com.kekeinfo.core.business.zone.service.ZoneService;
import com.kekeinfo.web.admin.entity.web.PaginationData;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.PinYin;
import com.kekeinfo.web.entity.UnderWater;
import com.kekeinfo.web.event.SpringContextUtils;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.PageBuilderUtils;
import com.kekeinfo.web.utils.PinyinUtils;
import com.kekeinfo.web.utils.TransExcelView;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;

@Controller
public class StatisticsController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsController.class);
	@Autowired private PNodeUtils pnodeUtils;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@Autowired
	private ZoneService zoneService;
	@Autowired CSiteService cSiteService;
	@Autowired ObservewellService observewellService;
	@Autowired BaseDataService baseDataService;
	@Autowired  TransExcelView transExcelView;
	@Autowired GroupService groupService;
	@Autowired DewateringService dewateringService;
	@Autowired PumpwellService pumpwellService;
	@Autowired InvertedwellService invertedwellService;
	@Autowired DeformmonitorService deformmonitorService;
	
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value="/water/statistics/statistics.html", method=RequestMethod.GET)
	public String list(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!request.isUserInRole("ADMIN") && !request.isUserInRole("SUPERADMIN")) {
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			Department waterDept = (Department) webCacheUtils.getFromCache(Constants.DEPARTMENT_WATER); //地下水部门
			boolean find = pnodeUtils.hasProjectRight(user.getpNodes(), waterDept);
			if(!find){
				return "redirect:/water/home.html";
			}
		}
		setMenu(model,request);
		ConstructionSite csite = new ConstructionSite();
		model.addAttribute("csite", csite);
		List<Zone> zones =  zoneService.getAllZone();
		model.addAttribute("zones", zones);
		return "csite-statistics";
	}

	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/statistics/list.shtml", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public @ResponseBody String getBasedatas(@ModelAttribute("csite") ConstructionSite csite,HttpServletRequest request,HttpServletResponse response,Locale locale) {
		try {
			long offset=0l;
			int page=1;
			String pages =request.getParameter("page");
			if(StringUtils.isNotBlank(pages)){
				page=Integer.parseInt(pages);
				offset = new Long((page-1)*10);
			};
			
			Entitites<ConstructionSite> css = cSiteService.getByCsite(csite,offset);
			Map<String, Object> entry_camera = new HashMap<String, Object>();
			StringBuffer out = new StringBuffer().append("<tr>");
			if(css!=null ){
				List<ConstructionSite> cs= css.getEntites();
				int sopen=0;
				int sclose=0;
				int swarning=0;
				int stotal=0;
				int index=1;
				for(ConstructionSite site:cs){
					//ConstructionSite site =cSiteService.getByCidWithALLWell(site1.getId());
					//site.setPbase(site1.getPbase());
					//Set<Pumpwell> pwells = site.getPwells();
					List<Pumpwell> pwells=pumpwellService.getByCid(site.getId());
					int open=0;
					int close=0;
					int warning=0;
					int total=pwells.size();
					
					for(Pumpwell pwell:pwells){
						switch (pwell.getPowerStatus().intValue()) {
						case 0:
							open++;
							break;
						case 1:
							close++;
							break;
						default:
							break;
						}
						switch (pwell.getDataStatus().intValue()) {
						case 0:
							break;
						default:
							warning++;
							break;
						}
					}
					stotal += total;
					sopen += open;
					sclose += close;
					swarning += warning;
					out.append("<td data-title=\"编号\" rowspan=\"5\">").append(index).append("</td><td data-title=\"项目名称\" rowspan=\"5\">").append(site.getProject().getName()).append("</td><td data-title=\"降水目的层\" rowspan=\"5\">").append(this.getPre(site.getPbase().getPrecipitation()));
					out.append("</td><td data-title=\"开启数\">").append(open).append("</td><td data-title=\"关闭数\">").append(close).append("</td><td data-title=\"警告数\">").append(warning).append("</td><td data-title=\"小计\">").append(total).append("</td><td data-title=\"测点类型\">降水井");
					out.append(" <a href=\"javascript:;\" class=\"fa fa-adjust fa-font-size\" onclick=\"showbin(\'").append(site.getProject().getName()).append("降水井状态风险图\',").append(warning).append(",").append(open).append(",").append(close).append(")\"></a>").append("</td></tr><tr>");
					
					index++;
					
					//Set<Dewatering> dewells = site.getDewells();
					List<Dewatering> dewells = dewateringService.getByCid(site.getId());
					open=0;
					close=0;
					warning=0;
					total=dewells.size();
					for(Dewatering dewell:dewells){
						switch (dewell.getPowerStatus().intValue()) {
						case 0:
							open++;
							break;
						case 1:
							close++;
							break;
						default:
							break;
						}
						switch (dewell.getDataStatus().intValue()) {
						case 0:
							break;
						default:
							warning++;
							break;
						}
					}
					stotal += total;
					sopen += open;
					sclose += close;
					swarning += warning;
					out.append("<td data-title=\"开启数\">").append(open).append("</td><td data-title=\"关闭数\">").append(close).append("</td><td data-title=\"警告数\">").append(warning).append("</td><td data-title=\"小计\" id=\"dewell_").append(site.getId()).append("\">").append(total).append("</td><td data-title=\"测点类型\">疏干井");
					out.append(" <a href=\"javascript:;\" class=\"fa fa-adjust fa-font-size\" onclick=\"showbin(\'").append(site.getProject().getName()).append("疏干井状态风险图\',").append(warning).append(",").append(open).append(",").append(close).append(")\"></a>").append("</td></tr><tr>");
					
					//Set<Observewell> owells = site.getOwells();
					List<Observewell> owells = observewellService.getBycid(site.getId());
					open=0;
					close=0;
					warning=0;
					total=owells.size();
					for(Observewell owell:owells){
						switch (owell.getPowerStatus().intValue()) {
						case 0:
							open++;
							break;
						case 1:
							close++;
							break;
						default:
							break;
						}
						switch (owell.getDataStatus().intValue()) {
						case 0:
							break;
						default:
							warning++;
							break;
						}
					}
					stotal += total;
					sopen += open;
					sclose += close;
					swarning += warning;
					out.append("<td data-title=\"开启数\">").append(open).append("</td><td data-title=\"关闭数\">").append(close).append("</td><td data-title=\"警告数\">").append(warning).append("</td><td data-title=\"小计\" id=\"owell_").append(site.getId()).append("\">").append(total).append("</td><td data-title=\"测点类型\">观测井");
					out.append(" <a href=\"javascript:;\" class=\"fa fa-adjust fa-font-size\" onclick=\"showbin(\'").append(site.getProject().getName()).append("观测井状态风险图\',").append(warning).append(",").append(open).append(",").append(close).append(")\"></a>");
					out.append("&nbsp;&nbsp;<a href=\"javascript:;\" class=\"fa fa-bar-chart-o fa-font-size\" onclick=\"showline($(this),").append(site.getId()).append(")\"></a>").append("</td></tr><tr>");

					//Set<Invertedwell> iwells = site.getIwells();
					List<Invertedwell> iwells = invertedwellService.getBycid(site.getId());
					open=0;
					close=0;
					warning=0;
					total=iwells.size();
					for(Invertedwell iwell:iwells){
						switch (iwell.getPowerStatus().intValue()) {
						case 0:
							open++;
							break;
						case 1:
							close++;
							break;
						default:
							break;
						}
						switch (iwell.getDataStatus().intValue()) {
						case 0:
							break;
						default:
							warning++;
							break;
						}
					}
					stotal += total;
					sopen += open;
					sclose += close;
					swarning += warning;
					out.append("<td data-title=\"开启数\">").append(open).append("</td><td data-title=\"关闭数\">").append(close).append("</td><td data-title=\"警告数\">").append(warning).append("</td><td data-title=\"小计\" id=\"iwell_").append(site.getId()).append("\">").append(total).append("</td><td data-title=\"测点类型\">回灌井");
					out.append(" <a href=\"javascript:;\" class=\"fa fa-adjust fa-font-size\" onclick=\"showbin(\'").append(site.getProject().getName()).append("回灌井状态风险图\',").append(warning).append(",").append(open).append(",").append(close).append(")\"></a>").append("</td></tr><tr>");
					
					//Set<Deformmonitor> ewells = site.getEwells();
					List<Deformmonitor> ewells = deformmonitorService.getBycid(site.getId());
					open=0;
					close=0;
					warning=0;
					total=ewells.size();
					for(Deformmonitor ewell:ewells){
						switch (ewell.getPowerStatus().intValue()) {
						case 0:
							open++;
							break;
						case 1:
							close++;
							break;
						default:
							break;
						}
						switch (ewell.getDataStatus().intValue()) {
						case 0:
							break;
						default:
							warning++;
							break;
						}
					}
					stotal += total;
					sopen += open;
					sclose += close;
					swarning += warning;
					out.append("<td data-title=\"开启数\">").append(open).append("</td><td data-title=\"关闭数\">").append(close).append("</td><td data-title=\"警告数\">").append(warning).append("</td><td data-title=\"小计\" id=\"ewell_").append(site.getId()).append("\">").append(total).append("</td><td data-title=\"测点类型\">环境监测");
					out.append(" <a href=\"javascript:;\" class=\"fa fa-adjust fa-font-size\" onclick=\"showbin(\'").append(site.getProject().getName()).append("环境监测状态风险图\',").append(warning).append(",").append(open).append(",").append(close).append(")\"></a>").append("</td></tr><tr>");
				}
				out.append("<td class=\"hidden-xs\">总计</td><td class=\"hidden-xs\"></td><td class=\"hidden-xs\"></td><td data-title=\"开启数总计\">").append(sopen).append("</td><td data-title=\"关闭数总计\">").append(sclose).append("</td><td data-title=\"警告数总计\">").append(swarning).append("</td><td data-title=\"总计\">").append(stotal).append("</td><td></td></tr><tr>");
				entry_camera.put("out", out.toString());
				//写分页
				if(css.getTotalCount()>cs.size()){
					PaginationData paginationData = new PaginationData(10, page);
					paginationData = PageBuilderUtils.calculatePaginaionData(paginationData, Constants.MAX_ORDERS_PAGE, css.getTotalCount());
					entry_camera.put("page",paginationData);
				}
			}
			
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(entry_camera);
			return json;
		} catch (ServiceException | IOException e) {
			LOGGER.error(e.getMessage());
		}
		
        return null;
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/statistics/zones.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=utf-8")
	public @ResponseBody List<PinYin> zones(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		try{
			List<Object[]> stores = zoneService.getPinYin();
			List<PinYin> pinyin = PinyinUtils.getPinyinList(stores);
			return pinyin;
		}catch (Exception e){
			return null;
		}
		
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/statistics/lines.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=utf-8")
	public @ResponseBody String line(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		try{
			String pid = request.getParameter("pid");
			Entitites<Observewell> dataList = observewellService.getPageListByAttributes(Arrays.asList("cSite.id"), Arrays.asList(Long.parseLong(pid)),null,null,null);
			if(dataList!=null && dataList.getEntites()!=null && dataList.getEntites().size()>0){
				List<String> labels = new ArrayList<String>();
				List<BigDecimal> dataset1 = new ArrayList<BigDecimal>();
				List<BigDecimal> dataset2 = new ArrayList<BigDecimal>();
				List<BigDecimal> dataset3 = new ArrayList<BigDecimal>();
				for(Observewell owell:dataList.getEntites()){
					labels.add(owell.getName());
					dataset1.add(owell.getWaterMeasurement());
					dataset2.add(owell.getrWater());
					dataset3.add(owell.getWaterDwon());
				}
				Map<String, Object> linesMap = new HashMap<String, Object>();
				linesMap.put("label", labels);
				linesMap.put("data1", dataset1);
				linesMap.put("data2", dataset2);
				linesMap.put("data3", dataset3);
				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(linesMap);
				return json;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/statistics/welldaily.html", method=RequestMethod.GET)
	public String listdaliy(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setWaterMenu(model,request);
		String cid= request.getParameter("cid");
		request.setAttribute("activeFun", "welldaily");  //指定当前操作功能
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
				model.addAttribute("cname", csite.getName());
				List<BasedataType> precipitations = (List<BasedataType>) SpringContextUtils.getServletContext().getAttribute(Constants.BD_PRECIPITATION);
				//Entitites<BasedataType> precipitations = baseDataService.getListByAttributes(new String[]{"type"}, new String[]{Constants.BD_PRECIPITATION}, null);
				model.addAttribute("pres", precipitations);
				//model.addAttribute("csite", csite);
				HashMap<String, String> orderby = new HashMap<String, String>();
				orderby.put("auditSection.dateModified", "desc");
				List<String> where = new ArrayList<>();
				where.add("cSite.id");
				where.add(cid);
				List<String> join =new ArrayList<String>();
				join.add("pointInfo");
				Entitites<Dewatering> delist  = dewateringService.getPageListByAttributesLike(null, null,null, null, orderby,where,join);
				Entitites<Pumpwell> plist  = pumpwellService.getPageListByAttributesLike(null, null,null, null, orderby,where,join);
				Entitites<Invertedwell> ilist  = invertedwellService.getPageListByAttributesLike(null, null,null, null, orderby,where,join);
				Entitites<Observewell> olist  = observewellService.getPageListByAttributesLike(null, null,null, null, orderby,where,join);
				model.addAttribute("pwells", plist.getEntites());
				model.addAttribute("dewells", delist.getEntites());
				model.addAttribute("owells", olist.getEntites());
				model.addAttribute("iwells", ilist.getEntites());
				/**
				List<Group> egroups = groupService.listGroupByPermissionName("EDIT-PROJECT",1);
				 boolean hasRight=false;
				 if(!request.isUserInRole("ADMIN") && !request.isUserInRole("SUPERADMIN") ){
			        	if(request.isUserInRole("EDIT-PROJECT")){
			        		hasRight=pnodeUtils.hasProjectRight(request, egroups, csite);
			        	}
					}else{
						hasRight=true;
					}*/

				//权限判断
		        boolean hasRight = false;
		        try {
		        	if (!request.isUserInRole("ADMIN") && !request.isUserInRole("SUPERADMIN")) {
		        		if (request.isUserInRole("EDIT-PROJECT")) {
		        			List<Group> egroups = groupService.listGroupByPermissionName("EDIT-PROJECT",1);
		        			hasRight = pnodeUtils.hasProjectRight(request, egroups, csite);
		        		}
		        	} else {
		        		hasRight = true;
		        	}
				} catch (Exception e) {
					e.printStackTrace();
				}
				model.addAttribute("hasRight", hasRight);
				String ctype = request.getParameter("ctype");
				//手机版
				if(StringUtils.isNotBlank(ctype)){
					model.addAttribute("project", csite);
					return "phone-welldaily";
				}
				model.addAttribute("csite", csite);
			}catch (Exception e){
				LOGGER.error(e.getMessage());
			}
		}
		return "csite-welldaily";
	}
	
	@SuppressWarnings("rawtypes")
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/statistics/exportCols.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=UTF-8")
	public @ResponseBody void  exportCols(Model model, @RequestParam(value="ebegindt", required=false) String begindt, @RequestParam(value="eendtm", required=false) String endtm, HttpServletRequest request, HttpServletResponse response, Locale locale) throws IOException {
		/**AjaxResponse resp = new AjaxResponse();
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);*/
			
	try {
			String cid=request.getParameter("cid");
			ConstructionSite csite = cSiteService.getById(Long.parseLong(cid));
			
			List<Basepoint> wells = new ArrayList<>();
			HashMap<String, String> orderby = new HashMap<String, String>();
			orderby.put("auditSection.dateModified", "desc");
			List<String> where = new ArrayList<>();
			where.add("cSite.id");
			where.add(cid);
			List<String> join =new ArrayList<String>();
			join.add("pointInfo");
			int plists=0,delists=0,ilists=0,olists=0;
			Entitites<Dewatering> delist  = dewateringService.getPageListByAttributesLike(null, null,null, null, orderby,where,join);
			if(delist!=null && delist.getEntites()!=null && delist.getEntites().size()>0){
				plists=delist.getEntites().size();
				for(Dewatering d:delist.getEntites()){
					wells.add(d);
				}
			}
			Entitites<Pumpwell> plist  = pumpwellService.getPageListByAttributesLike(null, null,null, null, orderby,where,join);
			if(plist!=null && plist.getEntites()!=null && plist.getEntites().size()>0){
				delists=plist.getEntites().size();
				for(Pumpwell d:plist.getEntites()){
					wells.add(d);
				}
			}
			Entitites<Invertedwell> ilist  = invertedwellService.getPageListByAttributesLike(null, null,null, null, orderby,where,join);
			if(ilist!=null && ilist.getEntites()!=null && ilist.getEntites().size()>0){
				ilists=ilist.getEntites().size();
				for(Invertedwell d:ilist.getEntites()){
					wells.add(d);
				}
			}
			Entitites<Observewell> olist  = observewellService.getPageListByAttributesLike(null, null,null, null, orderby,where,join);
			if(olist!=null && olist.getEntites()!=null && olist.getEntites().size()>0){
				olists=olist.getEntites().size();
				for(Observewell d:olist.getEntites()){
					wells.add(d);
				}
			}
			
			XSSFWorkbook wb=null;
			if(wells!=null ){
				wb = new XSSFWorkbook();
				XSSFSheet sheet = wb.createSheet("data");
				// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
		        XSSFRow row = sheet.createRow(0);  
		        // 第四步，创建单元格，并设置值表头 设置表头居中  
		        XSSFCellStyle style = wb.createCellStyle();  
		        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式 
		        XSSFCell cell = null; 
		        //类型
		        cell = row.createCell(0);
		        cell.setCellValue("类型");
		        cell.setCellStyle(style);
		        //梳干井
		        cell = row.createCell(1);
		        cell.setCellValue("疏干井");
		        cell.setCellStyle(style);
		      //降水井
		        cell = row.createCell(delists+1);
		        cell.setCellValue("降水井");
		        cell.setCellStyle(style);
		        //回灌井
		        cell = row.createCell(1+delists+plists);
		        cell.setCellValue("回灌井");
		        cell.setCellStyle(style);
		        //观测井
		        cell = row.createCell(1+delists+plists+ilists);
		        cell.setCellValue("观测井");
		        cell.setCellStyle(style);
		        
		        row = sheet.createRow(1);
		        int i=1;
		        for(Basepoint base:wells){
		        	cell=row.createCell(i);
		        	cell.setCellValue(base.getName());
		        	i++;
		        }
				
		        //合并单元格
		        CellRangeAddress region1 = new CellRangeAddress(0, 1, 0,0);   
		        CellRangeAddress region2 = new CellRangeAddress(0,0,1,delists); 
		        CellRangeAddress region3 = new CellRangeAddress(0,0,delists+1,delists+plists);
		        CellRangeAddress region4 = new CellRangeAddress(0,0,delists+plists+1,delists+plists+ilists);
		        CellRangeAddress region5 = new CellRangeAddress(0,0,delists+plists+ilists+1,delists+plists+ilists+olists);
		        sheet.addMergedRegion(region1);   
		        sheet.addMergedRegion(region2);   
		        sheet.addMergedRegion(region3);   
		        sheet.addMergedRegion(region4);   
		        sheet.addMergedRegion(region5);
		        
		        int j=2;
		        //降水目的层
		        row = sheet.createRow(j);
		        cell=row.createCell(0);
		        cell.setCellValue("降水目的层");
		        i=1;
		        
		        if(delist!=null && delist.getEntites()!=null && delist.getEntites().size()>0){
		        	 for(Dewatering p:delist.getEntites()){
				        	cell=row.createCell(i);
				        	cell.setCellValue(this.getPre(p.getPointInfo().getPrecipitation()));
				        	i++;
				        }
		        }
		       
		        if(plist!=null && plist.getEntites()!=null && plist.getEntites().size()>0){
		        	for(Pumpwell p:plist.getEntites()){
			        	cell=row.createCell(i);
			        	cell.setCellValue(this.getPre(p.getPointInfo().getPrecipitation()));
			        	i++;
			        }
		        }
		        if(ilist!=null && ilist.getEntites()!=null && ilist.getEntites().size()>0){
		        	for(Invertedwell p:ilist.getEntites()){
			        	cell=row.createCell(i);
			        	cell.setCellValue(this.getPre(p.getPointInfo().getPrecipitation()));
			        	i++;
			        }
		        }
		        
		        if(olist!=null && olist.getEntites()!=null && olist.getEntites().size()>0){
		        	for(Observewell p:olist.getEntites()){
			        	cell=row.createCell(i);
			        	cell.setCellValue(this.getPre(p.getPointInfo().getPrecipitation()));
			        	i++;
			        }
		        }
		        
		        
		        j++;
		        
		        //孔径/mm
		        row = sheet.createRow(j);
		        cell=row.createCell(0);
		        cell.setCellValue("孔径/mm");
		        i=1;
		        for(Basepoint base:wells){
		        	cell=row.createCell(i);
		        	if(base.getPointInfo()!=null){
		        		cell.setCellValue(this.getValue(base.getPointInfo().getAperture()));
		        	}else{
		        		cell.setCellValue("");
		        	}
		        	i++;
		        }
		        
		        j++;
		      //管井/mm
		        row = sheet.createRow(j);
		        cell=row.createCell(0);
		        cell.setCellValue("管井/mm");
		        i=1;
		        for(Basepoint base:wells){
		        	cell=row.createCell(i);
		        	if(base.getPointInfo()!=null){
		        		cell.setCellValue(this.getValue(base.getPointInfo().getTubeWell()));
		        	}else{
		        		cell.setCellValue("");
		        	}
		        	i++;
		        }
		       
		        j++;
		      //井深/m
		        row = sheet.createRow(j);
		        cell=row.createCell(0);
		        cell.setCellValue("井深/m");
		        i=1;
		        for(Basepoint base:wells){
		        	cell=row.createCell(i);
		        	if(base.getPointInfo()!=null){
		        		cell.setCellValue(this.getValue(base.getPointInfo().getDeepWell()));
		        	}else{
		        		cell.setCellValue("");
		        	}
		        	i++;
		        }
		       
		        j++;
		        //滤管长度
		        row = sheet.createRow(j);
		        cell=row.createCell(0);
		        cell.setCellValue("滤管长度");
		        i=1;
		        for(Basepoint base:wells){
		        	cell=row.createCell(i);
		        	if(base.getPointInfo()!=null){
		        		cell.setCellValue(this.getValue(base.getPointInfo().getfTubleLgn()));
		        	}else{
		        		cell.setCellValue("");
		        	}
		        	i++;
		        }
		        
		        j++;
		      //滤料回填量/t
		        row = sheet.createRow(j);
		        cell=row.createCell(0);
		        cell.setCellValue("滤料回填量/t");
		        i=1;
		        for(Basepoint base:wells){
		        	cell=row.createCell(i);
		        	if(base.getPointInfo()!=null){
			        	cell.setCellValue(this.getValue(base.getPointInfo().getBackfillVol()));
		        	}else{
		        		cell.setCellValue("");
		        	}
		        	i++;
		        }
		        
		        j++;
		        //粘土球回填量/t
		        row = sheet.createRow(j);
		        cell=row.createCell(0);
		        cell.setCellValue("粘土球回填量/t");
		        i=1;
		        for(Basepoint base:wells){
		        	cell=row.createCell(i);
		        	if(base.getPointInfo()!=null){
			        	cell.setCellValue(this.getValue(base.getPointInfo().getClayBackfill()));
		        	}else{
		        		cell.setCellValue("");
		        	}
		        	i++;
		        }
		      
		        j++;
		      //井口是否回填
		        row = sheet.createRow(j);
		        cell=row.createCell(0);
		        cell.setCellValue("井口是否回填");
		        i=1;
		        for(Basepoint base:wells){
		        	cell=row.createCell(i);
		        	if(base.getPointInfo()!=null){
			        	cell.setCellValue(this.getBoolean(base.getPointInfo().getBackFill()));
		        	}else{
		        		cell.setCellValue("");
		        	}
		        	i++;
		        }
		       
		        j++;
		      //洗井周期
		        row = sheet.createRow(j);
		        cell=row.createCell(0);
		        cell.setCellValue("洗井周期");
		        i=1;
		        for(Basepoint base:wells){
		        	cell=row.createCell(i);
		        	if(base.getPointInfo()!=null){
			        	cell.setCellValue(base.getPointInfo().getWashWC());
		        	}else{
		        		cell.setCellValue("");
		        	}
		        	i++;
		        }
		        
		        j++;
		      //单井涌水量/m3/h
		        row = sheet.createRow(j);
		        cell=row.createCell(0);
		        cell.setCellValue("单井涌水量/m3/h");
		        i=1;
		        for(Basepoint base:wells){
		        	cell=row.createCell(i);
		        	if(base.getPointInfo()!=null){
			        	cell.setCellValue(this.getValue(base.getPointInfo().getsWellFlow()));
		        	}else{
		        		cell.setCellValue("");
		        	}
		        	i++;
		        }
		       
		        j++;
		      //动水位/m
		        row = sheet.createRow(j);
		        cell=row.createCell(0);
		        cell.setCellValue("动水位/m");
		        i=1;
		        for(Basepoint base:wells){
		        	cell=row.createCell(i);
		        	if(base.getPointInfo()!=null){
			        	cell.setCellValue(this.getValue(base.getPointInfo().getMoveingWater()));
		        	}else{
		        		cell.setCellValue("");
		        	}
		        	i++;
		        }
		        
		        j++;
		      //初始水位/m
		        row = sheet.createRow(j);
		        cell=row.createCell(0);
		        cell.setCellValue("初始水位/m");
		        i=1;
		        for(Basepoint base:wells){
		        	cell=row.createCell(i);
		        	if(base.getPointInfo()!=null){
			        	cell.setCellValue(this.getValue(base.getPointInfo().getInitialWater()));
		        	}else{
		        		cell.setCellValue("");
		        	}
		        	i++;
		        }
		        
		        j++;
		      //井号现场标识
		        row = sheet.createRow(j);
		        cell=row.createCell(0);
		        cell.setCellValue("初始水位/m");
		        i=1;
		        for(Basepoint base:wells){
		        	cell=row.createCell(i);
		        	if(base.getPointInfo()!=null){
			        	cell.setCellValue(base.getPointInfo().getPoundSite());
		        	}else{
		        		cell.setCellValue("");
		        	}
		        	i++;
		        }
		       
		        j++;
		      //是否有异常
		        row = sheet.createRow(j);
		        cell=row.createCell(0);
		        cell.setCellValue("是否有异常");
		        i=1;
		        for(Basepoint base:wells){
		        	cell=row.createCell(i);
		        	if(base.getPointInfo()!=null){
			        	cell.setCellValue(this.getBoolean(base.getPointInfo().getException()));
		        	}else{
		        		cell.setCellValue("");
		        	}
		        	i++;
		        }
		        
		        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		        j++;
		      //成井时间
		        row = sheet.createRow(j);
		        cell=row.createCell(0);
		        cell.setCellValue("成井时间");
		        i=1;
		        for(Basepoint base:wells){
		        	cell=row.createCell(i);
		        	if(base.getPointInfo()!=null && base.getPointInfo().getWellTime()!=null){
			        	cell.setCellValue(formatter.format(base.getPointInfo().getWellTime()));
		        	}else{
		        		cell.setCellValue("");
		        	}
		        	i++;
		        }
		        
		        j++;
		      //成井负责人
		        row = sheet.createRow(j);
		        cell=row.createCell(0);
		        cell.setCellValue("成井负责人");
		        i=1;
		        for(Basepoint base:wells){
		        	cell=row.createCell(i);
		        	if(base.getPointInfo()!=null){
			        	cell.setCellValue(base.getPointInfo().getcPerson());
		        	}else{
		        		cell.setCellValue("");
		        	}
		        	i++;
		        }
		        
		        j++;
		      //验收管理人员
		        row = sheet.createRow(j);
		        cell=row.createCell(0);
		        cell.setCellValue("验收管理人员");
		        i=1;
		        for(Basepoint base:wells){
		        	cell=row.createCell(i);
		        	if(base.getPointInfo()!=null){
			        	cell.setCellValue(base.getPointInfo().getAcceptance());
		        	}else{
		        		cell.setCellValue("");
		        	}
		        	i++;
		        }
		        
		        j++;
		      //封井时间
		        row = sheet.createRow(j);
		        cell=row.createCell(0);
		        cell.setCellValue("封井时间");
		        i=1;
		        for(Basepoint base:wells){
		        	cell=row.createCell(i);
		        	if(base.getPointInfo()!=null && base.getPointInfo().getClosure()!=null){
			        	cell.setCellValue(formatter.format(base.getPointInfo().getClosure()));
		        	}else{
		        		cell.setCellValue("");
		        	}
		        	i++;
		        }
		        
		        j++;
		      //封井措施
		        row = sheet.createRow(j);
		        cell=row.createCell(0);
		        cell.setCellValue("封井措施");
		        i=1;
		        for(Basepoint base:wells){
		        	cell=row.createCell(i);
		        	if(base.getPointInfo()!=null){
			        	cell.setCellValue(base.getPointInfo().getCmeasures());
		        	}else{
		        		cell.setCellValue("");
		        	}
		        	i++;
		        }
		        
		        j++;
		        //封井验收人
		        row = sheet.createRow(j);
		        cell=row.createCell(0);
		        cell.setCellValue("封井验收人");
		        i=1;
		        for(Basepoint base:wells){
		        	cell=row.createCell(i);
		        	if(base.getPointInfo()!=null){
			        	cell.setCellValue(base.getPointInfo().getsAcceptance());
		        	}else{
		        		cell.setCellValue("");
		        	}
		        	i++;
		        }
		        
		        
		        //调整宽度
		        for( j=0;j<delists+plists+ilists+olists;j++){
		        	sheet.autoSizeColumn((short)j);
		        }
				String fileName =csite.getProject().getName()+"成井日志";
				
				fileName = URLEncoder.encode(fileName, "UTF-8");
				response.addHeader("Content-Disposition", "attachment;filename=" + fileName+".xlsx");
				//写文件
				OutputStream os = response.getOutputStream();
				wb.write(os);
				os.close();
			}
		
				//resbase.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
				//return wb.getBytes();
			} catch (Exception e) {
				LOGGER.debug("fail export");
				response.sendError(404, Constants.FILE_NOT_FOUND);
			}
		//return resp.toJSONString();
		
	}
	
	private String getValue(Object o){
		if (o!=null){
			return o.toString();
		}
		return "";
	}
	
	private String getBoolean(int status){
		if(status==0){
			return "否";
		}else{
			return "是";
		}
	}
	
	//降水目的层
	@SuppressWarnings("unchecked")
	private String getPre(String pre){
		if(StringUtils.isNotBlank(pre)){
			List<BasedataType> precipitations = (List<BasedataType>) SpringContextUtils.getServletContext().getAttribute(Constants.BD_PRECIPITATION);
			//Entitites<BasedataType> precipitations = baseDataService.getListByAttributes(new String[]{"type"}, new String[]{Constants.BD_PRECIPITATION}, null);
			String[] ps =pre.split(",");
			StringBuffer res = new StringBuffer();
			for(String p:ps){
				for(BasedataType btyp:precipitations){
					if(btyp.getValue().equalsIgnoreCase(p.trim())){
						res.append(btyp.getName()).append(",");
						break;
					}
				}
			}
			res.deleteCharAt(res.lastIndexOf(","));
			return res.toString();
		}
		
		return "";
	}
	
	private void setWaterMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("csite", "csite");


		model.addAttribute("activeMenus",activeMenus);
		//
		
	}
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("statistics", "statistics");
		model.addAttribute("activeMenus",activeMenus);
	}
}
