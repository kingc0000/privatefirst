package com.kekeinfo.web.admin.controller.rmreport;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.RenderingHints;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.codehaus.jackson.map.ObjectMapper;
import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.kekeinfo.core.business.common.model.BasedataType;
import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.monitor.model.MPointEnumType;
import com.kekeinfo.core.business.monitor.report.service.RSupAxialDataService;
import com.kekeinfo.core.business.monitor.report.service.RSurfaceDataService;
import com.kekeinfo.core.business.monitor.statistical.model.PointManager;
import com.kekeinfo.core.business.monitor.surface.model.Surface;
import com.kekeinfo.core.business.mreport.model.PointInfo;
import com.kekeinfo.core.business.mreport.model.RHiddenLineData;
import com.kekeinfo.core.business.mreport.model.RMBaseData;
import com.kekeinfo.core.business.mreport.model.RMreport;
import com.kekeinfo.core.business.mreport.model.RSupAxialData;
import com.kekeinfo.core.business.mreport.model.RSurfaceData;
import com.kekeinfo.core.business.mreport.service.RMreportService;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.admin.entity.web.Menu;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.BaseEntity;
import com.kekeinfo.web.entity.DataTable;
import com.kekeinfo.web.entity.MonitorEntity;
import com.kekeinfo.web.event.SpringContextUtils;
import com.kekeinfo.web.utils.DataTableParameter;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;;

@Controller
public class RMreportController {
	private static final Logger LOGGER = LoggerFactory.getLogger(RMreportController.class);
	@Autowired
	RMreportService rmreportService;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@Autowired private PNodeUtils pnodeUtils;
	@Autowired private GroupService groupService;
	@Autowired RSurfaceDataService rSurfaceDataService;
	@Autowired RSupAxialDataService rSupAxialDataService;

	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/rmreport/list.html", method = RequestMethod.GET)
	public String listrmreports(Model model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		setMenu(model, request);
		RMreport rmreport = new RMreport();
		model.addAttribute("rmreport", rmreport);
		String mid = request.getParameter("mid");
		setMenu(model, request);
		Surface surface = new Surface();
		model.addAttribute("surface", surface);
		//判断用户是否有该项目的编辑权限
        boolean hasRight = false;
        MonitorEntity me=null;
		if(StringUtils.isNotBlank(mid)){
			List<MonitorEntity> listen = (List<MonitorEntity>) webCacheUtils.getFromCache(Constants.PROEJCT_MONITOR);
			for(MonitorEntity m:listen){
				if(m.getId().equals(Long.parseLong(mid))){
					me=m;
					model.addAttribute("mentity", m);
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
		request.setAttribute("activeCode", "mreport"); //指定项目
		model.addAttribute("hasRight", hasRight);
		model.addAttribute("mid", mid);
		List<BasedataType> blist = (List<BasedataType>) SpringContextUtils.getServletContext().getAttribute(Constants.RP_COMPANY);
		model.addAttribute("bc", blist);
		return "admin-rmreport";
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/rmreport/server_processing.shtml", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = " application/json; charset=utf-8")
	public @ResponseBody String getRMreport(@RequestParam String aoData, HttpServletRequest request,
			HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		AjaxResponse resp = new AjaxResponse();
		DataTableParameter dataTableParam = new DataTableParameter(aoData);
		String mid = request.getParameter("mid");
		DataTable<RMreport> dt = new DataTable<RMreport>();
		try { // 指定根据什么条件进行模糊查询
			List<String> attributes = new ArrayList<String>();
			attributes.add("eName"); 
			attributes.add("eNo");
			attributes.add("rNo"); 
			attributes.add("monitor");
			attributes.add("computer");
			attributes.add("verifier");
			List<Object[]> where =new ArrayList<>();
			where.add(new Object[]{"monitor.id", mid});
			HashMap<String, String> orderby = new HashMap<String, String>();
			orderby.put("auditSection.dateModified", "desc");
			Entitites<RMreport> list = rmreportService.getPageListByAttributesLike(attributes, dataTableParam.getsSearch(), Long.valueOf(dataTableParam.getiDisplayLength()).intValue(), 
					Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby, where, null, true);
			dt.setsEcho(dataTableParam.getsEcho() + 1);
			dt.setiTotalDisplayRecords(list.getTotalCount());
			dt.setiTotalRecords(list.getTotalCount());
			dt.setAaData(list.getEntites());
		} catch (Exception e) {
			LOGGER.error("Error while paging rmreports", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		String json = mapper.writeValueAsString(dt);
		return json;
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/rmreport/save.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String saveRMreport(@ModelAttribute("rmreport") RMreport rmreport, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse resp = new AjaxResponse();
		try {
			String[] rs =request.getParameterValues("rcompany");
			if(rs!=null && rs.length>0){
				List<BasedataType> bts =new ArrayList<>();
				for(String s:rs){
					BasedataType bt =new BasedataType();
					bt.setId(Long.parseLong(s));
					bts.add(bt);
				}
				rmreport.setRcompanys(bts);
			}
			int re= rmreportService.createReport(rmreport);
			if(re==-1){
				resp.setStatus(-4);
				resp.setStatusMessage("请先创建所选择日期的统计报表");
			}else{
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			}
		} catch (ServiceException e) {
			LOGGER.error("Error while save RMreport", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		String returnString = resp.toJSONString();
		return returnString;
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/rmreport/remove.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody List<AjaxResponse> deleteRMreport(@RequestParam String[] listId, HttpServletRequest request,
			Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId != null && listId.length > 0) {
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.parseLong(listId[i]);
				try {
					RMreport rmreport = rmreportService.getById(id);
					if (rmreport != null) {
						rmreportService.delete(rmreport);
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
	
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping("/water/rmreport/downloads/{fileName}.html")
	public @ResponseBody void downloadProduct(@PathVariable final String fileName, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(fileName!=null){
			RMreport rmreport = rmreportService.getByRid(Long.parseLong(fileName));
			if(rmreport!=null){
				String pName="";
				String mid = request.getParameter("mid");
				//项目名称
				if(StringUtils.isNotBlank(mid)){
					List<MonitorEntity> listen = (List<MonitorEntity>) webCacheUtils.getFromCache(Constants.PROEJCT_MONITOR);
					for(MonitorEntity m:listen){
						if(m.getId().equals(Long.parseLong(mid))){
							pName=m.getName();
							break;
						}
					}
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
				String filen =pName+"项目沉降报表-"+rmreport.getrNo()+"-"+sdf.format(rmreport.getThiser());
				String cpath =this.getClass().getResource("/").getPath().replace("/WEB-INF/classes", "")+"/resources/mmodel/cjmodel.xls";
				Workbook book = WorkbookFactory.create(new File(cpath));//new XSSFWorkbook(in);
				//sheet的变化
				int sheetNum=0;
				Sheet sheet = book.getSheetAt(0);
				
				
				Row row = sheet.getRow(0);
				Cell hssfCel = row.getCell(0);
				hssfCel.setCellValue(pName);
				//编号
				row = sheet.getRow(4);
				hssfCel = row.getCell(1);
				hssfCel.setCellValue(rmreport.getrNo());
				for(int i=8;i<18;i++){
					row = sheet.getRow(i);
					hssfCel = row.getCell(8);
					hssfCel.setCellValue(rmreport.getrNo());
				}
				//监测时间
				row = sheet.getRow(5);
				hssfCel = row.getCell(2);
				hssfCel.setCellValue(sdf.format(rmreport.getThiser()));
				//监测者
				row = sheet.getRow(20);
				hssfCel = row.getCell(2);
				hssfCel.setCellValue(rmreport.getRmonitor());
				//计算者
				hssfCel = row.getCell(5);
				hssfCel.setCellValue(rmreport.getComputer());
				//校核者
				hssfCel = row.getCell(9);
				hssfCel.setCellValue(rmreport.getVerifier());
				//报送
				row = sheet.getRow(23);
				row.setHeightInPoints(20);
				List<BasedataType> com=rmreport.getRcompanys();
				int line=25;
				if(com!=null && com.size()>0){
					for(BasedataType bt:com){
						row = sheet.getRow(line);
						hssfCel = row.getCell(3);
						hssfCel.setCellValue(bt.getName());
						line++;
					}
				}
				//写总评表
				CellStyle style = book.createCellStyle();
				//设置边框样式
			     style.setBorderTop(HSSFCellStyle.BORDER_THIN);
			     style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			     style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			     style.setBorderRight(HSSFCellStyle.BORDER_THIN);
			     style.setTopBorderColor(HSSFColor.BLACK.index);
			     style.setBottomBorderColor(HSSFColor.BLACK.index);
			     style.setLeftBorderColor(HSSFColor.BLACK.index);
			     style.setRightBorderColor(HSSFColor.BLACK.index);
			     style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//左右居中      
			     style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//上下居中 
				//sheetNum++;
				Sheet zp =book.getSheetAt(1);
				//写巡视
		         sheet = book.getSheetAt(9);
		       //气温
		 		row = sheet.getRow(5);
		 		hssfCel = row.getCell(2);
		 		hssfCel.setCellStyle(style);
		 		hssfCel.setCellValue(rmreport.getMstatistical().getmDaily().getTemperature());
		 		//天气
		 		row = sheet.getRow(6);
		 		hssfCel = row.getCell(2);
		 		hssfCel.setCellStyle(style);
		 		hssfCel.setCellValue(rmreport.getMstatistical().getmDaily().getWeather());
		 		//风级
		 		row = sheet.getRow(7);
		 		hssfCel = row.getCell(2);
		 		hssfCel.setCellStyle(style);
		 		hssfCel.setCellValue(rmreport.getMstatistical().getmDaily().getWind());
		 		//围护结构外观形态
		 		row = sheet.getRow(8);
		 		hssfCel = row.getCell(2);
		 		hssfCel.setCellStyle(style);
		 		hssfCel.setCellValue(rmreport.getMstatistical().getmDaily().getWeiHuW());
		 		//冠梁、支撑、围檩裂缝
		 		row = sheet.getRow(9);
		 		hssfCel = row.getCell(2);
		 		hssfCel.setCellStyle(style);
		 		hssfCel.setCellValue(rmreport.getMstatistical().getmDaily().getLeiFeng());
		 		//支撑、立柱变形
		 		row = sheet.getRow(10);
		 		hssfCel = row.getCell(2);
		 		hssfCel.setCellStyle(style);
		 		hssfCel.setCellValue(rmreport.getMstatistical().getmDaily().getBianXing());
		 		//止水帷幕开裂、渗漏
		 		row = sheet.getRow(11);
		 		hssfCel = row.getCell(2);
		 		hssfCel.setCellStyle(style);
		 		hssfCel.setCellValue(rmreport.getMstatistical().getmDaily().getSenLou());
		 		//墙后土体沉陷、裂缝及滑移
		 		row = sheet.getRow(12);
		 		hssfCel = row.getCell(2);
		 		hssfCel.setCellStyle(style);
		 		hssfCel.setCellValue(rmreport.getMstatistical().getmDaily().getHuaYi());
		 		//基坑涌土、流砂、管涌
		 		row = sheet.getRow(13);
		 		hssfCel = row.getCell(2);
		 		hssfCel.setCellStyle(style);
		 		hssfCel.setCellValue(rmreport.getMstatistical().getmDaily().getGuanYong());
		 		//支护结构其他
		 		row = sheet.getRow(14);
		 		hssfCel = row.getCell(2);
		 		hssfCel.setCellStyle(style);
		 		hssfCel.setCellValue(rmreport.getMstatistical().getmDaily().getZhiHuOther());
		 		//开挖区域土质情况
		 		row = sheet.getRow(15);
		 		hssfCel = row.getCell(2);
		 		hssfCel.setCellStyle(style);
		 		hssfCel.setCellValue(rmreport.getMstatistical().getmDaily().getTuZhi());
		 		//基坑开挖分段长度及分层厚度
		 		row = sheet.getRow(16);
		 		hssfCel = row.getCell(2);
		 		hssfCel.setCellStyle(style);
		 		hssfCel.setCellValue(rmreport.getMstatistical().getmDaily().getHouDu());
		 		//地表水、地下水状况
		 		row = sheet.getRow(17);
		 		hssfCel = row.getCell(2);
		 		hssfCel.setCellStyle(style);
		 		hssfCel.setCellValue(rmreport.getMstatistical().getmDaily().getShuiStatus());
		 		//基坑降水(回灌)设施运转情况
		 		row = sheet.getRow(18);
		 		hssfCel = row.getCell(2);
		 		hssfCel.setCellStyle(style);
		 		hssfCel.setCellValue(rmreport.getMstatistical().getmDaily().getHuiGuan());
		 		//基坑周边地面堆载情况
		 		row = sheet.getRow(19);
		 		hssfCel = row.getCell(2);
		 		hssfCel.setCellStyle(style);
		 		hssfCel.setCellValue(rmreport.getMstatistical().getmDaily().getDuiZhai());
		 		//施工工况其他
		 		row = sheet.getRow(20);
		 		hssfCel = row.getCell(2);
		 		hssfCel.setCellStyle(style);
		 		hssfCel.setCellValue(rmreport.getMstatistical().getmDaily().getShiGongOther());
		 		//管道破损、泄漏情况
		 		row = sheet.getRow(21);
		 		hssfCel = row.getCell(2);
		 		hssfCel.setCellStyle(style);
		 		hssfCel.setCellValue(rmreport.getMstatistical().getmDaily().getXieLou());
		 		//周边建筑裂缝
		 		row = sheet.getRow(22);
		 		hssfCel = row.getCell(2);
		 		hssfCel.setCellStyle(style);
		 		hssfCel.setCellValue(rmreport.getMstatistical().getmDaily().getJianLFENG());
		 		//周边道路（地面）裂缝、沉陷
		 		row = sheet.getRow(23);
		 		hssfCel = row.getCell(2);
		 		hssfCel.setCellStyle(style);
		 		hssfCel.setCellValue(rmreport.getMstatistical().getmDaily().getChenXian());
		 		//邻近施工情况
		 		row = sheet.getRow(24);
		 		hssfCel = row.getCell(2);
		 		hssfCel.setCellStyle(style);
		 		hssfCel.setCellValue(rmreport.getMstatistical().getmDaily().getNeibor());
		 		//周边环境其他
		 		row = sheet.getRow(25);
		 		hssfCel = row.getCell(2);
		 		hssfCel.setCellStyle(style);
		 		hssfCel.setCellValue(rmreport.getMstatistical().getmDaily().getZbOther());
		 		//基准点完好状况
		 		row = sheet.getRow(26);
		 		hssfCel = row.getCell(2);
		 		hssfCel.setCellStyle(style);
		 		hssfCel.setCellValue(rmreport.getMstatistical().getmDaily().getJiDian());
		 		//测点完好状况
		 		row = sheet.getRow(27);
		 		hssfCel = row.getCell(2);
		 		hssfCel.setCellStyle(style);
		 		hssfCel.setCellValue(rmreport.getMstatistical().getmDaily().getCeDian());
		 		//监测元件完好情况
		 		row = sheet.getRow(28);
		 		hssfCel = row.getCell(2);
		 		hssfCel.setCellStyle(style);
		 		hssfCel.setCellValue(rmreport.getMstatistical().getmDaily().getYuanJian());
		 		//观测工作条件
		 		row = sheet.getRow(29);
		 		hssfCel = row.getCell(2);
		 		hssfCel.setCellStyle(style);
		 		hssfCel.setCellValue(rmreport.getMstatistical().getmDaily().getTiaoJian());
				zp=this.writezp(zp,pName,rmreport,style);
				int maxline=42;
				//写管线
				List<BasedataType> blist = (List<BasedataType>) SpringContextUtils.getServletContext().getAttribute(Constants.BD_MONITOR_LINE);
				if(blist!=null && blist.size()>0){
					 List<RMBaseData<RSurfaceData>> rss = rSurfaceDataService.getByRid(rmreport.getId(), MPointEnumType.WaterLine);
					if(rss!=null && rss.size()>0){
						for(BasedataType bt:blist){
							//根据类型查找
							List<RMBaseData<RSurfaceData>> srwd=new ArrayList<RMBaseData<RSurfaceData>>();
							for(RMBaseData<RSurfaceData> rwd:rss){
								if(rwd.getPointInfo().getMarkNO().startsWith(bt.getValue())){
									srwd.add(rwd);
								}
							}
							if(srwd.size()>0){
								int time= this.gtime(srwd,maxline);
								for(int i=0;i<time;i++){
									//如果大于1需要clone一个sheet,而且不是最好一个
									Sheet sf=null;
								
										sf=book.cloneSheet(2);
										String sheetname = sf.getSheetName();
										book.setSheetOrder(sheetname, 2+sheetNum+1+i);
										if(i==0){
											book.setSheetName(2+sheetNum+1+i, bt.getName()+"管线");
										}else{
											book.setSheetName(2+sheetNum+1+i, bt.getName()+"管线("+(i+2)+")");
										}
									
									sf=this.writeMdata(i, srwd, maxline, sf, pName, style,"WaterLine",null,2,book);
									//sheet位移
									sheetNum++;
								}
							}
						}
					}
					//删除模板
					book.removeSheetAt(2);
					sheetNum--;
				}
				//写周边地表,建筑物，立柱
				for(MPointEnumType e:MPointEnumType.values()){
					if(e.toString().equalsIgnoreCase("Surface") || e.toString().equalsIgnoreCase("Building") || e.toString().equalsIgnoreCase("UpRight")){
						List<?> rss = rSurfaceDataService.getByRid(rmreport.getId(), e);
						if(rss!=null && rss.size()>0){
							int time= this.gtime(rss,maxline);
							for(int i=0;i<time;i++){
								//如果大于1需要clone一个sheet,而且不是最好一个
								Sheet sf=null;
								if(time>1 && i!=time-1){
									sf=book.cloneSheet(e.getCode()+sheetNum);
									String sheetname = sf.getSheetName();
									book.setSheetOrder(sheetname, 2+sheetNum+1);
								}else{
									sf =book.getSheetAt(e.getCode()+sheetNum);
								}
								sf=this.writeMdata(i, rss, maxline, sf, pName, style,e.toString(),null,0,book);
							}
							if(time>1){
								//sheet便宜
								sheetNum=sheetNum+(time-1);
								int sindex =book.getSheetIndex(e.getName());
					        	book.setSheetName(sindex,e.getName()+"("+(time+1)+")");
					        	book.setSheetName(e.getCode()+(time+sheetNum-1), e.getName());
					        	book.setSheetName(sindex, e.getName()+"("+(time)+")");
					        	book.setSheetOrder(e.getName(), e.getCode()+sheetNum);
							}
						}
					}
				}
				
				//写圈梁
				List<?> rss = rSurfaceDataService.getByRid(rmreport.getId(), MPointEnumType.RingBeam);
				List<?> rss1 = rSurfaceDataService.getByRid(rmreport.getId(), MPointEnumType.Displacement);
				if(rss!=null && rss.size()>0){
					int time= this.gtime(rss,maxline);
					for(int i=0;i<time;i++){
						//如果大于1需要clone一个sheet,而且不是最好一个
						Sheet sf=null;
						if(time>1 && i!=time-1){
							sf=book.cloneSheet(MPointEnumType.RingBeam.getCode()+sheetNum);
							String sheetname = sf.getSheetName();
							book.setSheetOrder(sheetname, MPointEnumType.RingBeam.getCode()+sheetNum+1);
						}else{
							sf =book.getSheetAt(MPointEnumType.RingBeam.getCode()+sheetNum);
						}
						sf=this.writeMdata(i, rss, maxline, sf, pName, style,MPointEnumType.RingBeam.toString(),rss1,0,book);
					}
					if(time>1){
						//sheet便宜
						sheetNum=sheetNum+(time-1);
						int sindex =book.getSheetIndex(MPointEnumType.RingBeam.getName());
						book.setSheetName(sindex, MPointEnumType.RingBeam.getName()+"("+(time+1)+")");
						book.setSheetName(MPointEnumType.RingBeam.getCode()+(time+sheetNum-1), MPointEnumType.RingBeam.getName());
			        	book.setSheetName(MPointEnumType.RingBeam.getCode(), MPointEnumType.RingBeam.getName()+"("+(time)+")");
			        	book.setSheetOrder(MPointEnumType.RingBeam.getName(), MPointEnumType.RingBeam.getCode()+sheetNum);
					}
				}
				
				//写潜层水位
				maxline=28;
				List<?> rss2 = rSurfaceDataService.getByRid(rmreport.getId(), MPointEnumType.HiddenLine);
				if(rss2!=null && rss2.size()>0){
					int time= this.gtime(rss,maxline);
					for(int i=0;i<time;i++){
						//如果大于1需要clone一个sheet,而且不是最好一个
						Sheet sf=null;
						if(time>1 && i!=time-1){
							sf=book.cloneSheet(MPointEnumType.HiddenLine.getCode()+sheetNum);
							String sheetname = sf.getSheetName();
							book.setSheetOrder(sheetname, MPointEnumType.HiddenLine.getCode()+sheetNum+1);
						}else{
							sf =book.getSheetAt(MPointEnumType.HiddenLine.getCode()+sheetNum);
						}
						sf=this.writeMdata(i, rss2, maxline, sf, pName, style,MPointEnumType.HiddenLine.toString(),null,1,book);
					}
					if(time>1){
						//sheet便宜
						sheetNum=sheetNum+(time-1);
				        	int sindex =book.getSheetIndex(MPointEnumType.HiddenLine.getName());
				        	book.setSheetName(sindex, MPointEnumType.HiddenLine.getName()+"("+(time+1)+")");
				        	book.setSheetName(MPointEnumType.HiddenLine.getCode()+(time+sheetNum-1), MPointEnumType.SupAxial.getName());
				        	book.setSheetName(sindex, MPointEnumType.HiddenLine.getName()+"("+(time)+")");
				        	book.setSheetOrder(MPointEnumType.HiddenLine.getName(), MPointEnumType.HiddenLine.getCode()+sheetNum);
					}
				}
				
				//写支撑
				maxline=42;
				List<RMBaseData<RSupAxialData>> rss3 =rSupAxialDataService.getByRid(rmreport.getId(), MPointEnumType.SupAxial);
				if(rss3!=null && rss3.size()>0){
					HashMap<String, List<RMBaseData<RSupAxialData>>> group =new HashMap<>();
					//分组
					for(RMBaseData<RSupAxialData> rs:rss3){
						String mno = rs.getPointInfo().getMarkNO();
						int indexxx =mno.lastIndexOf("-");
						String index="100";
						if(indexxx!=-1){
							index=mno.substring(mno.lastIndexOf("-")+1);
						}
						List<RMBaseData<RSupAxialData>> slist=group.get(index);
						if(slist==null){
							slist =new ArrayList<>();
						}
						slist.add(rs);
						group.put(index, slist);
					}
					
				//获取所有的key并排序
					Object[] key_arr = group.keySet().toArray();   
					//获取是否要分页,总数加种类
					int len=rss3.size()+key_arr.length;
					int time =len/(maxline);
					if(len%(maxline)!=0){
						time++;
					}
			        Arrays.sort(key_arr); 
			        //当前使用的组的个数
			        int j=0;
			        int n=0;
			        for(int i=0;i<time;i++){
			        	//左右切换
				        int a=0;
				      //当前写的行数
				        int k=6;
						//如果大于1需要clone一个sheet,而且不是最好一个
						Sheet sf=null;
						if(time>1 && i!=time-1){
							sf=book.cloneSheet(MPointEnumType.SupAxial.getCode()+sheetNum);
							String sheetname = sf.getSheetName();
							book.setSheetOrder(sheetname, MPointEnumType.SupAxial.getCode()+sheetNum+1);
						}else{
							sf =book.getSheetAt(MPointEnumType.SupAxial.getCode()+sheetNum);
						}
						//写标题
						Row rows = sf.getRow(k);
						Cell hssfCels = rows.getCell(0);
						hssfCels.setCellValue(pName);
						boolean isberak=false;
						 //数据集合
				        DefaultCategoryDataset sdataset = new DefaultCategoryDataset();
				        // 各曲线名称
				        String series1 = "累计";
						for (; j < key_arr.length; j++) {
							List<RMBaseData<RSupAxialData>> lg=group.get(key_arr[j]);
				        	rows = sf.getRow(k);
				        	String  title="第"+this.toChinese(key_arr[j].toString())+"道支撑";
				        	for(;n<lg.size();n++){
				        		RSupAxialData rsd =(RSupAxialData)lg.get(n);
				        		
				        		//本页结束
				        		if(k==49){
				        			isberak=true;
				        			k=6;
				        			break;
				        		}
				        		sf =writeSupAxial(sf,style,rsd,a,k,title,sdataset,series1);
				        		k++;
				        	}
				        	//重新开始计数
				        	if(isberak==false){
				        		n=0;
				        	}else{
				        		break;
				        	}
				        }
						//写图
				 		
				 		HSSFClientAnchor anchor = new HSSFClientAnchor(5, 5, 5, 5, (short) 1, 50, (short) 8, 57);
				 		Drawing patriarch = sf.createDrawingPatriarch();
				 		patriarch.createPicture(anchor, book.addPicture(this.getLineChartImage(sdataset), Workbook.PICTURE_TYPE_PNG));
					}
			        //调整sheet
			        if(time>1){
			        	sheetNum=sheetNum+(time-1);
			        	int sindex =book.getSheetIndex(MPointEnumType.SupAxial.getName());
			        	book.setSheetName(sindex, MPointEnumType.SupAxial.getName()+"("+(time+1)+")");
			        	book.setSheetName(MPointEnumType.SupAxial.getCode()+(time+sheetNum-1), MPointEnumType.SupAxial.getName());
			        	book.setSheetName(sindex, MPointEnumType.SupAxial.getName()+"("+(time)+")");
			        	book.setSheetOrder(MPointEnumType.SupAxial.getName(), MPointEnumType.SupAxial.getCode()+sheetNum);
			        }
			        
				}
				filen = URLEncoder.encode(filen, "UTF-8");
				response.addHeader("Content-Disposition", "attachment;filename=" + filen+".xls");
				//写文件
				OutputStream os = response.getOutputStream();
				book.write(os);
				os.close();
			}
		}
		
		
	}
	
	private Sheet writeSupAxial(Sheet sf,CellStyle style,RSupAxialData rs,int a,int line,String title,DefaultCategoryDataset sdataset,String ser){
		Row row = sf.getRow(line);
		//点号
		Cell hssfCel = row.getCell(a);
		hssfCel.setCellStyle(style);
		hssfCel.setCellValue(rs.getPointInfo().getMarkNO());
		
		if(rs.getCurtHeight()!=null){
			//本次累计
			BigDecimal sum=null;
			hssfCel = row.getCell(a+1);
			hssfCel.setCellStyle(style);
			if(rs.getPointInfo().getfHeight()!=null){
				sum=rs.getCurtHeight().subtract(rs.getPointInfo().getfHeight());
			}else{
				sum=rs.getCurtHeight();
			}
			hssfCel.setCellValue(sum.toString());
			//上次累计
			hssfCel = row.getCell(a+2);
			hssfCel.setCellStyle(style);
			hssfCel.setCellValue(sum.subtract(rs.getCurtHeight()).toString());
			//本次变化量
			hssfCel = row.getCell(a+3);
			hssfCel.setCellStyle(style);
			hssfCel.setCellValue(rs.getCurtHeight().toString());
			sdataset.addValue(rs.getCurtHeight()==null?0:rs.getCurtHeight(), ser, rs.getPointInfo().getMarkNO());
			//支撑类型
			hssfCel = row.getCell(a+4);
			
			if(hssfCel==null)hssfCel=row.createCell(a+4);
			hssfCel.setCellStyle(style);
			hssfCel.setCellValue(rs.getZhiCheng()==null?"":rs.getZhiCheng());
			//报警值
			hssfCel = row.getCell(a+5);
			if(hssfCel==null)hssfCel=row.createCell(a+5);
			hssfCel.setCellStyle(style);
			hssfCel.setCellValue(rs.getAlarmV()==null?"":rs.getAlarmV().toString());
			//顺序
			hssfCel = row.getCell(a+6);
			if(hssfCel==null)hssfCel=row.createCell(a+6);
			hssfCel.setCellStyle(style);
			hssfCel.setCellValue(title);
			//备注
			hssfCel = row.getCell(a+7);
			if(hssfCel==null)hssfCel=row.createCell(a+7);
			hssfCel.setCellStyle(style);
			hssfCel.setCellValue(rs.getPointInfo().getMemo());
			
		}
		
		return sf;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Sheet writeMdata(int i,List<?> rss, int maxline,Sheet sf,String pName,CellStyle style,String etype,List<?> rss1,int flag,Workbook book) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException{
		int len =(i+1)*(maxline);
		if((i+1)*(maxline)>rss.size()){
			len=rss.size();
		}
		//按100分割
		List ldata =new ArrayList();
		for(int j=i*(maxline);j<len;j++){
			ldata.add(rss.get(j));
		}
		
		Class catClass = Class.forName("com.kekeinfo.core.business.mreport.model.R"+etype+"Data");
		//List data =this.getSet(ldata, catClass);
		if(flag==0){
			sf =this.writeData(sf, pName, ldata, style, catClass, maxline,rss1,"",book);
		//管线
		}else if(flag==2){
			sf =this.writeData(sf, pName, ldata, style, catClass, maxline,rss1,sf.getSheetName(),book);
		//水位
		}
		else if(flag==1){
			sf =this.writeData(sf, pName, ldata, style,book);
		}
		
		return sf;
	}
	
	private int gtime(List<?> rss,int maxline){
		int time =rss.size()/(maxline);
		if(rss.size()%(maxline)!=0){
			time++;
		}
		return time;
	}

	/**
	 * 排序，把前30个设置为有数据的
	 * @param rs
	 * @return
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	/**
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<?> getSet(List rs,Class cpClass) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		List es =new ArrayList();
		List ds =new ArrayList();
		for(Object ss:rs){
			Method[] mes=cpClass.getMethods();
			BigDecimal c=null;
			for(Method me:mes){
				if(me.getName().equalsIgnoreCase("getCurtHeight")){
					c=(BigDecimal) me.invoke(ss);
					break;
				}
			}
			if(c!=null){
				ds.add(ss);
			}else{
				es.add(ss);
			}
		}
		if(es.size()>0){
			for(Object ss:es){
				ds.add(ss);
			}
		}
		return ds;
	}*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Sheet writeData(Sheet sheet,String pName,List rsds,CellStyle style,Class cpClass,int maxline,List<?> rss1,String sname,Workbook book) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		Row row = sheet.getRow(0);
		Cell hssfCel = row.getCell(0);
		hssfCel.setCellValue(pName);
		//强制转换
		List<RMBaseData<?>> setData =(List<RMBaseData<?>>)  rsds;
		List<RMBaseData<?>> sData=null;
		if(rss1!=null){
			sData=(List<RMBaseData<?>>)  rss1;
		}
		String ename="";
		String eno="";
		int line =7;
		if(StringUtils.isNotBlank(sname)){
			row = sheet.getRow(6);
			hssfCel = row.getCell(0);
			hssfCel.setCellStyle(style);
			hssfCel.setCellValue(sname);
		}
		 //横向左边
		int a=0;
		 //数据集合
        DefaultCategoryDataset sdataset = new DefaultCategoryDataset();
        // 各曲线名称
        String series1 = "总累计";
		for(RMBaseData<?> md:setData){
			PointInfo pinfo=null;
			Method[] mes =cpClass.getMethods();
			for(Method me:mes){
				if(me.getName().equalsIgnoreCase("getPointInfo")){
					pinfo=(PointInfo) me.invoke(md);
					break;
				}
			}
			if(ename==""){
				ename =pinfo.geteName();
				eno=pinfo.geteName();
			}
			row = sheet.getRow(line);
			//点号
			hssfCel = row.getCell(a);
			hssfCel.setCellStyle(style);
			hssfCel.setCellValue(pinfo.getMarkNO());
			//初始
			hssfCel = row.getCell(a+1);
			hssfCel.setCellStyle(style);
			hssfCel.setCellValue(pinfo.getfHeight()==null?"":pinfo.getfHeight().toString());
			//本次
			hssfCel = row.getCell(a+2);
			hssfCel.setCellStyle(style);
			hssfCel.setCellValue(md.getCurtHeight()==null?"":md.getCurtHeight().toString());
			//本次变化
			if(md.getCurtHeight()!=null){
				hssfCel = row.getCell(a+3);
				hssfCel.setCellStyle(style);
				if(md.getInitHeight()!=null){
					hssfCel.setCellValue(md.getCurtHeight().subtract(md.getInitHeight()).toString());
				}else{
					hssfCel.setCellValue(md.getCurtHeight().toString());
				}
				
				//总累计
				BigDecimal to =new BigDecimal(0);
				hssfCel = row.getCell(a+4);
				hssfCel.setCellStyle(style);
				if(pinfo.getfHeight()!=null){
					to=md.getCurtHeight().subtract(pinfo.getfHeight());
				}else{
					to=md.getCurtHeight();
				}
				hssfCel.setCellValue(to.toString());
				sdataset.addValue(to==null?0:to, series1, pinfo.getMarkNO());
			}
			
			//圈梁，找到水平位移
			if(rss1!=null){
				for(RMBaseData<?> md1:sData){
					if(md1.getPointInfo().getMarkNO().equalsIgnoreCase(md.getPointInfo().getMarkNO())){
						//水平累计
						hssfCel = row.getCell(a+6);
						hssfCel.setCellStyle(style);
						if(md1.getCurtHeight()!=null){
							//水平变化
							hssfCel = row.getCell(a+5);
							hssfCel.setCellStyle(style);
							if(md1.getInitHeight()!=null){
								hssfCel.setCellValue(md1.getCurtHeight().subtract(md1.getInitHeight()).toString());
							}else{
								hssfCel.setCellValue(md1.getCurtHeight().toString());
							}
							hssfCel = row.getCell(a+6);
							hssfCel.setCellStyle(style);
							if(pinfo.getfHeight()!=null){
								hssfCel.setCellValue(md1.getCurtHeight().subtract(md1.getPointInfo().getfHeight()).toString());
							}else{
								hssfCel.setCellValue(md1.getCurtHeight().toString());
							}
						}
						//备注
						hssfCel = row.getCell(a+7);
						hssfCel.setCellStyle(style);
						hssfCel.setCellValue(pinfo.getMemo());
						break;
					}
				}
			}else{
				//开挖前
				hssfCel = row.getCell(a+5);
				hssfCel.setCellStyle(style);
				hssfCel.setCellValue(pinfo.getsHeight()==null?"":pinfo.getsHeight().toString());
				//备注
				hssfCel = row.getCell(a+6);
				hssfCel.setCellStyle(style);
				hssfCel.setCellValue(pinfo.getMemo());
			}
			line++;	
			/**
			if(line==maxline){
				line=7;
				a=a+7;
			}else{
				
			}*/
		}
		//仪器名称
		row = sheet.getRow(3);
		hssfCel = row.getCell(2);
		hssfCel.setCellValue(ename);
		hssfCel.setCellStyle(style);
		hssfCel = row.getCell(6);
		hssfCel.setCellValue(eno);
		hssfCel.setCellStyle(style);
		//写图
 		
 		HSSFClientAnchor anchor = new HSSFClientAnchor(5, 5, 5, 5, (short) 1, 50, (short) 7, 58);
 		Drawing patriarch = sheet.createDrawingPatriarch();
 		patriarch.createPicture(anchor, book.addPicture(this.getLineChartImage(sdataset), Workbook.PICTURE_TYPE_PNG));
		return sheet;
	}
	//写水位
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Sheet writeData(Sheet sheet,String pName,List rsds,CellStyle style,Workbook book) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		Row row = sheet.getRow(0);
		Cell hssfCel = row.getCell(0);
		hssfCel.setCellValue(pName);
		//强制转换
		List<RHiddenLineData> setData =(List<RHiddenLineData>)  rsds;
		
		String ename="";
		String eno="";
		int line =7;
		 //数据集合
        DefaultCategoryDataset sdataset = new DefaultCategoryDataset();
        // 各曲线名称
        String series1 = "累计";
		for(RHiddenLineData md:setData){
			PointInfo pinfo=md.getPointInfo();
			if(ename==""){
				ename =pinfo.geteName();
				eno=pinfo.geteName();
			}
			row = sheet.getRow(line);
			//点号
			hssfCel = row.getCell(0);
			hssfCel.setCellStyle(style);
			hssfCel.setCellValue(pinfo.getMarkNO());
			//管口
			hssfCel = row.getCell(1);
			hssfCel.setCellStyle(style);
			hssfCel.setCellValue(md.getLineGaoChen()==null?"":md.getLineGaoChen().toString());
			//初始
			hssfCel = row.getCell(2);
			hssfCel.setCellStyle(style);
			hssfCel.setCellValue(pinfo.getfHeight()==null?"":pinfo.getfHeight().toString());
			//本次
			hssfCel = row.getCell(3);
			hssfCel.setCellStyle(style);
			hssfCel.setCellValue(md.getCurtHeight()==null?"":md.getCurtHeight().toString());
			//本次变化
			if(md.getCurtHeight()!=null){
				hssfCel = row.getCell(4);
				hssfCel.setCellStyle(style);
				if(md.getInitHeight()!=null){
					hssfCel.setCellValue(md.getCurtHeight().subtract(md.getInitHeight()).toString());
				}else{
					hssfCel.setCellValue(md.getCurtHeight().toString());
				}
			}
			
			//总累计
			BigDecimal to =new BigDecimal(0);
			if(md.getCurtHeight()!=null){
				hssfCel = row.getCell(5);
				hssfCel.setCellStyle(style);
				if(pinfo.getfHeight()!=null){
					to=md.getCurtHeight().subtract(pinfo.getfHeight());
					hssfCel.setCellValue(md.getCurtHeight().subtract(pinfo.getfHeight()).toString());
				}else{
					hssfCel.setCellValue(md.getCurtHeight().toString());
					to=md.getCurtHeight();
				}
			}
			sdataset.addValue(to==null?0:to, series1, pinfo.getMarkNO());
			
			//备注
			hssfCel = row.getCell(6);
			hssfCel.setCellStyle(style);
			hssfCel.setCellValue(pinfo.getMemo());
			line++;	
		}
		//仪器名称
		row = sheet.getRow(3);
		hssfCel = row.getCell(2);
		hssfCel.setCellValue(ename);
		hssfCel.setCellStyle(style);
		hssfCel = row.getCell(6);
		hssfCel.setCellValue(eno);
		hssfCel.setCellStyle(style);
		//写图
 		
 		HSSFClientAnchor anchor = new HSSFClientAnchor(5, 5, 5, 5, (short) 1, 34, (short) 7, 40);
 		Drawing patriarch = sheet.createDrawingPatriarch();
 		patriarch.createPicture(anchor, book.addPicture(this.getLineChartImage(sdataset), Workbook.PICTURE_TYPE_PNG));
		return sheet;
	}
	//写总评表
	@SuppressWarnings("unchecked")
	private Sheet writezp(Sheet sheet,String pName,RMreport rmreport,CellStyle style){
		
		Row row = sheet.getRow(0);
		Cell hssfCel = row.getCell(0);
		hssfCel.setCellValue(pName);
		//天气
		row = sheet.getRow(2);
		row.setHeightInPoints(25);
		hssfCel = row.getCell(5);
		hssfCel.setCellValue(rmreport.getMstatistical().getmDaily().getWeather());
		//hssfCel.setCellStyle(style);
		int line=5;
		Set<PointManager> pms=rmreport.getMstatistical().getPmanagers();
		List<BasedataType> blist = (List<BasedataType>) SpringContextUtils.getServletContext().getAttribute(Constants.BD_MONITOR_LINE);
		if(pms!=null && pms.size()>0){
			//管线
			for(PointManager pm:pms){
				if(pm.getpType().equalsIgnoreCase(MPointEnumType.WaterLine.toString())){
					for(BasedataType bt:blist){
						if(pm.getPointNo().startsWith(bt.getValue())){
							//往下移动一行
							sheet.shiftRows(line, sheet.getLastRowNum(), 1);
							row=sheet.createRow(line);
							row.setHeightInPoints(30);
							//第一次加
							if(line==5){
								hssfCel=row.createCell(0);
								hssfCel.setCellValue("周围管线\n竖向位移");
								hssfCel.setCellStyle(style);
							}else{
								hssfCel=row.createCell(0);
								hssfCel.setCellStyle(style);
							}
							hssfCel=row.createCell(1);
							hssfCel.setCellValue(bt.getName());
							hssfCel.setCellStyle(style);
							//本次最大变化量
							hssfCel=row.createCell(2);
							hssfCel.setCellValue(pm.getPointNo());
							hssfCel.setCellStyle(style);
							hssfCel=row.createCell(3);
							hssfCel.setCellValue(pm.getCurMaxVar()==null?"":pm.getCurMaxVar().toString());
							hssfCel.setCellStyle(style);
							//总累计最大变化量
							hssfCel=row.createCell(4);
							hssfCel.setCellValue(pm.getTpointNo());
							hssfCel.setCellStyle(style);
							hssfCel=row.createCell(5);
							hssfCel.setCellValue(pm.getTotalMaxVar()==null?"":pm.getTotalMaxVar().toString());
							hssfCel.setCellStyle(style);
							// 开挖前累计最大变化量
							hssfCel=row.createCell(6);
							hssfCel.setCellValue(pm.getEpointNo());
							hssfCel.setCellStyle(style);
							hssfCel=row.createCell(7);
							hssfCel.setCellValue(pm.getEarlyMaxVar()==null?"":pm.getEarlyMaxVar().toString());
							hssfCel.setCellStyle(style);
							//日变，累计变，备注
							hssfCel=row.createCell(8);
							hssfCel.setCellStyle(style);
							hssfCel.setCellValue(pm.getDailyVar()==null?"":pm.getDailyVar().toString());
							hssfCel=row.createCell(9);
							hssfCel.setCellStyle(style);
							hssfCel.setCellValue(pm.getTotalValue()==null?"":pm.getTotalValue().toString());
							hssfCel=row.createCell(10);
							hssfCel.setCellStyle(style);
							line++;
						}
					}
				}
			}
			//合并单元格
			if(line>5){
				CellRangeAddress region=new CellRangeAddress(5, line-1, 0, 0);
				sheet.addMergedRegion(region);//指定合并区域
			}
			//地表点竖向位移
			for(PointManager pm:pms){
				if(!pm.getpType().equalsIgnoreCase(MPointEnumType.WaterLine.toString()) && !pm.getpType().equalsIgnoreCase(MPointEnumType.SupAxial.toString())){
					sheet.shiftRows(line, sheet.getLastRowNum(), 1);
					row=sheet.createRow(line);
					row.setHeightInPoints(20);
					hssfCel=row.createCell(0);
					for(MPointEnumType mtp:MPointEnumType.values()){
						if(pm.getpType().equalsIgnoreCase(mtp.toString())){
							hssfCel.setCellValue(mtp.getName());
							hssfCel.setCellStyle(style);
							break;
						}
					}
					hssfCel=row.createCell(1);
					hssfCel.setCellStyle(style);
					CellRangeAddress region=new CellRangeAddress(line, line, 0, 1);
					sheet.addMergedRegion(region);
					//本次最大变化量
					hssfCel=row.createCell(2);
					hssfCel.setCellValue(pm.getPointNo());
					hssfCel.setCellStyle(style);
					hssfCel=row.createCell(3);
					hssfCel.setCellValue(pm.getCurMaxVar()==null?"":pm.getCurMaxVar().toString());
					hssfCel.setCellStyle(style);
					//总累计最大变化量
					hssfCel=row.createCell(4);
					hssfCel.setCellValue(pm.getTpointNo());
					hssfCel.setCellStyle(style);
					hssfCel=row.createCell(5);
					hssfCel.setCellValue(pm.getTotalMaxVar()==null?"":pm.getTotalMaxVar().toString());
					hssfCel.setCellStyle(style);
					// 开挖前累计最大变化量
					hssfCel=row.createCell(6);
					hssfCel.setCellValue(pm.getEpointNo());
					hssfCel.setCellStyle(style);
					hssfCel=row.createCell(7);
					hssfCel.setCellValue(pm.getEarlyMaxVar()==null?"":pm.getEarlyMaxVar().toString());
					hssfCel.setCellStyle(style);
					//日变，累计变，备注
					hssfCel=row.createCell(8);
					hssfCel.setCellStyle(style);
					hssfCel=row.createCell(9);
					hssfCel.setCellStyle(style);
					hssfCel=row.createCell(10);
					hssfCel.setCellStyle(style);
					hssfCel.setCellValue(pm.getMemo());
					line++;
				}
			}
			//轴力
			
			for(PointManager pm:pms){
				if(pm.getpType().equalsIgnoreCase(MPointEnumType.SupAxial.toString())){
					//sheet.shiftRows(line, sheet.getLastRowNum(), 1);
					//下一行
					line++;
					row=sheet.getRow(line);
					hssfCel=row.getCell(2);
					hssfCel.setCellValue(pm.getTpointNo());
					hssfCel.setCellStyle(style);
					hssfCel=row.getCell(3);
					hssfCel.setCellValue(pm.getTotalMaxVar()==null?"":pm.getTotalMaxVar().toString());
					hssfCel.setCellStyle(style);
					
					hssfCel=row.getCell(4);
					hssfCel.setCellValue(pm.getPointNo());
					hssfCel.setCellStyle(style);
					hssfCel=row.getCell(6);
					hssfCel.setCellValue(pm.getCurMaxVar()==null?"":pm.getCurMaxVar().toString());
					hssfCel.setCellStyle(style);
					
					line++;
					break;
				}
			}
		}
		//工程状况
		if(StringUtils.isNotBlank(rmreport.getMstatistical().getmDaily().getNote())){
			row=sheet.getRow(line);
			hssfCel=row.getCell(2);
			hssfCel.setCellValue(rmreport.getMstatistical().getmDaily().getNote());
		}
		line++;
		line++;
		line++;
		//监测点情况
		if(StringUtils.isNotBlank(rmreport.getMstatistical().getmDaily().getPointDesc())){
			row=sheet.getRow(line);
			hssfCel=row.getCell(2);
			hssfCel.setCellValue(rmreport.getMstatistical().getmDaily().getPointDesc());
		}
		line++;
		line++;
		//监测意见
		if(StringUtils.isNotBlank(rmreport.getMstatistical().getmDaily().getConclusion())){
			row=sheet.getRow(line);
			hssfCel=row.getCell(2);
			hssfCel.setCellValue(rmreport.getMstatistical().getmDaily().getConclusion());
		}
		//设置后两行
		int lastrow=sheet.getLastRowNum();
		row=sheet.getRow(lastrow-1);
		row.setHeightInPoints(25);
		row=sheet.getRow(lastrow);
		row.setHeightInPoints(30);
		return sheet;
	}
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("monitor", "monitor");
		activeMenus.put("monitor-list", "monitor-list");
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");
		Menu currentMenu = (Menu) menus.get("monitor");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
	}
	private String toChinese(String string) {
        String[] s1 = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
        String[] s2 = { "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千" };

        String result = "";

        int n = string.length();
        for (int i = 0; i < n; i++) {

            int num = string.charAt(i) - '0';

            if (i != n - 1 && num != 0) {
                result += s1[num] + s2[n - 2 - i];
            } else {
                result += s1[num];
            }
        }

        return result;

    }
	
private  byte[] getLineChartImage(DefaultCategoryDataset dataset) {  
        
	//ByteArrayInputStream in = null;  
    
    StandardChartTheme mChartTheme = new StandardChartTheme("CN");  
    mChartTheme.setLargeFont(new Font("宋体", Font.PLAIN, 12));  
    mChartTheme.setExtraLargeFont(new Font("宋体", Font.PLAIN, 10));  
    mChartTheme.setRegularFont(new Font("宋体", Font.PLAIN, 10));  
    ChartFactory.setChartTheme(mChartTheme);          
    //XYSeriesCollection mCollection = GetCollection(); 
    JFreeChart chart = ChartFactory.createLineChart("","","",dataset, PlotOrientation.VERTICAL, true, false, false);   
    CategoryPlot plot = chart.getCategoryPlot();
    chart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
    plot.setRangeGridlinePaint(Color.BLACK);//纵坐标格线颜色
    plot.setDomainGridlinesVisible(true);//显示横坐标格线
    plot.setBackgroundAlpha(0.3f); //设置背景透明度
    plot.setBackgroundPaint(ChartColor.WHITE);
    plot.setDomainGridlinesVisible(true);
    plot.setDomainGridlineStroke(new BasicStroke());
    plot.setRangeGridlineStroke(new BasicStroke());
    //LineAndShapeRenderer renderer = (LineAndShapeRenderer)plot.getRenderer();
    //DecimalFormat decimalformat1 = new DecimalFormat("##.##");//数据点显示数据值的格式
    //renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", decimalformat1));
                                      //上面这句是设置数据项标签的生成器
    /**renderer.setItemLabelsVisible(true);//设置项标签显示
    renderer.setBaseItemLabelsVisible(true);//基本项标签显示
             //上面这几句就决定了数据点按照设定的格式显示数据值  
    renderer.setShapesFilled(Boolean.TRUE);//在数据点显示实心的小图标
    renderer.setShapesVisible(true);//设置显示小图标*/
    NumberAxis rangeAxis = (NumberAxis)plot.getRangeAxis();
    rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
    rangeAxis.setAutoRangeIncludesZero(true);
    rangeAxis.setUpperMargin(0.20);
    /*rangeAxis.setLabelAngle(Math.PI / 2.0); */
    try {  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        ChartUtilities.writeChartAsPNG(out, chart, 800, 300);  
       return out.toByteArray();  
    } catch (Exception e) {  
        e.printStackTrace();  
    }   
    return null;   
    } 
}
