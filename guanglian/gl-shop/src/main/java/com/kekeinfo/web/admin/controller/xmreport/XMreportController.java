package com.kekeinfo.web.admin.controller.xmreport;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.RenderingHints;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import org.codehaus.jackson.map.ObjectMapper;
import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
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
import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.monitor.surface.model.Surface;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.business.xreport.model.ROblique;
import com.kekeinfo.core.business.xreport.model.RObliqueData;
import com.kekeinfo.core.business.xreport.model.XMreport;
import com.kekeinfo.core.business.xreport.service.RObliqueDataService;
import com.kekeinfo.core.business.xreport.service.RObliqueService;
import com.kekeinfo.core.business.xreport.service.XMreportService;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.admin.entity.web.Menu;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.BaseEntity;
import com.kekeinfo.web.entity.DataTable;
import com.kekeinfo.web.entity.MonitorEntity;
import com.kekeinfo.web.utils.DataTableParameter;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;;

@Controller
public class XMreportController {
	private static final Logger LOGGER = LoggerFactory.getLogger(XMreportController.class);
	@Autowired
	XMreportService xmreportService;
	@Autowired private GroupService groupService;
	@Autowired private PNodeUtils pnodeUtils;
	@Autowired private WebApplicationCacheUtils webCacheUtils;
	@Autowired private RObliqueService rObliqueService;
	@Autowired private RObliqueDataService rObliqueDataService;
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/xmreport/list.html", method = RequestMethod.GET)
	public String listxmreports(Model model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		setMenu(model, request);
		XMreport xmreport = new XMreport();
		model.addAttribute("xmreport", xmreport);
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
		request.setAttribute("activeCode", "xreport"); //指定项目
		model.addAttribute("hasRight", hasRight);
		model.addAttribute("mid", mid);
		return "admin-xmreport";
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/xmreport/server_processing.shtml", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = " application/json; charset=utf-8")
	public @ResponseBody String getXMreport(@RequestParam String aoData, HttpServletRequest request,
			HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		AjaxResponse resp = new AjaxResponse();
		DataTableParameter dataTableParam = new DataTableParameter(aoData);
		DataTable<XMreport> dt = new DataTable<XMreport>();
		try { // 指定根据什么条件进行模糊查询
			List<String> attributes = new ArrayList<String>();
			HashMap<String, String> orderby = new HashMap<String, String>();
			orderby.put("auditSection.dateModified", "desc");
			Entitites<XMreport> list = xmreportService.getPageListByAttributesLike(attributes,
					dataTableParam.getsSearch(), Long.valueOf(dataTableParam.getiDisplayLength()).intValue(),
					Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby, null, null);
			dt.setsEcho(dataTableParam.getsEcho() + 1);
			dt.setiTotalDisplayRecords(list.getTotalCount());
			dt.setiTotalRecords(list.getTotalCount());
			dt.setAaData(list.getEntites());
		} catch (Exception e) {
			LOGGER.error("Error while paging xmreports", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		String json = mapper.writeValueAsString(dt);
		return json;
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/xmreport/save.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String saveXMreport(@ModelAttribute("xmreport") XMreport xmreport, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse resp = new AjaxResponse();
		try {
			xmreportService.createXreport(xmreport);
		} catch (ServiceException e) {
			LOGGER.error("Error while save XMreport", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		String returnString = resp.toJSONString();
		return returnString;
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/xmreport/remove.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody List<AjaxResponse> deleteXMreport(@RequestParam String[] listId, HttpServletRequest request,
			Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId != null && listId.length > 0) {
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.parseLong(listId[i]);
				try {
					XMreport xmreport = xmreportService.getById(id);
					if (xmreport != null) {
						xmreportService.delete(xmreport);
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
	@RequestMapping("/water/xmreport/downloads/{fileName}.html")
	public @ResponseBody void downloadProduct(@PathVariable final String fileName, HttpServletRequest request, HttpServletResponse response) throws Exception {
		XMreport xm=xmreportService.getById(Long.parseLong(fileName));
		List<ROblique> robs =rObliqueService.getByXid(xm.getId());
		if(robs!=null){
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
			String filen =pName+"项目测斜报表-"+xm.getrNo()+"-"+sdf.format(xm.getThiser());
			String cpath =this.getClass().getResource("/").getPath().replace("/WEB-INF/classes", "")+"/resources/mmodel/xcmodel.xls";
			Workbook book = WorkbookFactory.create(new File(cpath));//new XSSFWorkbook(in);
			//sheet的变化
			int sheetNum=1;
			//数据最大行数
			int maxLine=69;
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
			for(ROblique rob:robs){
				List<RObliqueData> rds =rObliqueDataService.getByRid(rob.getId());
				if(rds!=null && rds.size()>0){
					int time =this.gtime(rds, maxLine);
					//数据开始
					int j=0;
					
					for(int i=0;i<time;i++){
						Sheet sf=book.cloneSheet(0);
						//重命名
						if(i==0){
							book.setSheetName(sheetNum, rob.getNamber());
						}else{
							book.setSheetName(sheetNum, rob.getNamber()+"("+(i+2)+")");
						}
						//记录最大变化，和累计变化
						BigDecimal maxleij=new BigDecimal(0);
						BigDecimal maxchanj=new BigDecimal(0);
						String indexmaxb="";
						String indexmaxl="";
						Row row = sf.getRow(0);
						Cell hssfCel = row.getCell(0);
				 		hssfCel.setCellStyle(style);
				 		hssfCel.setCellValue(pName);
				 		//编号
				 		row = sf.getRow(2);
				 		hssfCel = row.getCell(0);
				 		hssfCel.setCellStyle(style);
				 		hssfCel.setCellValue(xm.getrNo());
				 		//孔号
				 		row = sf.getRow(3);
				 		hssfCel = row.getCell(2);
				 		//hssfCel.setCellStyle(style);
				 		hssfCel.setCellValue("孔号："+rob.getNamber());
				 		hssfCel = row.getCell(4);
				 		//hssfCel.setCellStyle(style);
				 		hssfCel.setCellValue(rob.getEvaluate()==""?"评价：":"评价："+rob.getEvaluate());
				 		hssfCel = row.getCell(8);
				 		//hssfCel.setCellStyle(style);
				 		hssfCel.setCellValue(rob.getHorcorrect()==null?"":rob.getHorcorrect().toString());
				 		//时间
				 		row = sf.getRow(4);
				 		hssfCel = row.getCell(2);
				 		//hssfCel.setCellStyle(style);
				 		hssfCel.setCellValue("本次时间："+xm.getThiser());
				 		
				 		//写数据
				 		int len =(i+1)*(maxLine);
						if((i+1)*(maxLine*2)>rds.size()){
							len=rds.size();
						}
						int line=6;
						//做图
				 		 //数据集合
						XYSeriesCollection dataset = new XYSeriesCollection( );  
				        final XYSeries lseries = new XYSeries( "上次累计" ); 
				        final XYSeries cseries = new XYSeries( "本次累计" ); 
						for(;j<len;j++){
							RObliqueData obd =rds.get(j);
							row = sf.getRow(line);
					 		//深度
							hssfCel = row.getCell(0);
					 		hssfCel.setCellStyle(style);
					 		hssfCel.setCellValue(obd.getDepth().toString());
					 		
					 		BigDecimal last =obd.getLastTotal();
					 		BigDecimal cur=obd.getCurTotal();
					 		if(cur!=null){
					 			if(cur.compareTo(maxleij)==1){
					 				maxleij=cur;
					 				indexmaxl=obd.getDepth().toString();
					 			}
					 			BigDecimal chg =new BigDecimal(0);
					 			if(last==null){
						 			//上次
					 				hssfCel = row.getCell(1);
							 		hssfCel.setCellStyle(style);
						 			hssfCel.setCellValue(0.00);
					 				hssfCel = row.getCell(2);
							 		hssfCel.setCellStyle(style);
							 		hssfCel.setCellValue(obd.getCurTotal().toString());
							 		hssfCel = row.getCell(3);
							 		hssfCel.setCellStyle(style);
							 		hssfCel.setCellValue(0.00);
						 		}else{
						 			//上次
						 			hssfCel = row.getCell(1);
							 		hssfCel.setCellStyle(style);
						 			hssfCel.setCellValue(obd.getLastTotal().toString());
						 			hssfCel = row.getCell(2);
							 		hssfCel.setCellStyle(style);
							 		hssfCel.setCellValue(obd.getCurTotal().toString());
							 		hssfCel = row.getCell(3);
							 		hssfCel.setCellStyle(style);
							 		hssfCel.setCellValue(obd.getCurTotal().subtract(obd.getLastTotal()).toString());
							 		chg=obd.getCurTotal().subtract(obd.getLastTotal());
						 		}
					 			lseries.add(last==null?0:last, obd.getDepth());
					 			cseries.add(cur, obd.getDepth());
					 			if(chg.compareTo(maxchanj)==1){
					 				maxchanj=chg;
					 				indexmaxb =obd.getDepth().toString();
					 			}
					 		}
					 		
					 		line++;
						}
						//写最大值
						row = sf.getRow(75);
						hssfCel = row.getCell(1);
						hssfCel.setCellStyle(style);
				 		hssfCel.setCellValue(maxchanj.toString());
				 		hssfCel = row.getCell(3);
						hssfCel.setCellStyle(style);
				 		hssfCel.setCellValue(indexmaxb);
				 		row = sf.getRow(76);
						hssfCel = row.getCell(1);
						hssfCel.setCellStyle(style);
				 		hssfCel.setCellValue(maxleij.toString());
				 		hssfCel = row.getCell(3);
						hssfCel.setCellStyle(style);
				 		hssfCel.setCellValue(indexmaxl);
				 		dataset.addSeries(cseries);
				 		dataset.addSeries(lseries);
				 		//写图
				 		
				 		HSSFClientAnchor anchor = new HSSFClientAnchor(5, 5, 5, 5, (short) 4, 6, (short) 9, 77);
				 		Drawing patriarch = sf.createDrawingPatriarch();
				 		patriarch.createPicture(anchor, book.addPicture(this.getLineChartImage(dataset), Workbook.PICTURE_TYPE_PNG));
					}
					sheetNum++;
				}
			}
			book.removeSheetAt(0);
			filen = URLEncoder.encode(filen, "UTF-8");
			response.addHeader("Content-Disposition", "attachment;filename=" + filen+".xls");
			//写文件
			OutputStream os = response.getOutputStream();
			book.write(os);
			os.close();
		}
		
	}

	private int gtime(List<RObliqueData> rss,int maxline){
		int time =rss.size()/(maxline);
		if(rss.size()%(maxline)!=0){
			time++;
		}
		return time;
	}
	
	private  byte[] getLineChartImage(XYDataset dataset) {  
        
        StandardChartTheme mChartTheme = new StandardChartTheme("CN");  
        mChartTheme.setLargeFont(new Font("宋体", Font.PLAIN, 14));  
        mChartTheme.setExtraLargeFont(new Font("宋体", Font.PLAIN, 12));  
        mChartTheme.setRegularFont(new Font("宋体", Font.PLAIN, 12));  
        ChartFactory.setChartTheme(mChartTheme);   
		
        JFreeChart chart = ChartFactory.createXYLineChart("" ,
                "" ,
                "" ,
                dataset ,
                PlotOrientation.VERTICAL ,
                true , false , false);  
        chart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        XYPlot plot = chart.getXYPlot( );
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
        renderer.setSeriesPaint( 0 , Color.BLUE );
        renderer.setSeriesPaint( 1 , Color.GREEN );
        renderer.setSeriesStroke( 0 , new BasicStroke( 2.0f ) );
        renderer.setSeriesStroke( 1 , new BasicStroke( 2.0f ) );
        plot.setBackgroundPaint(ChartColor.WHITE);
        plot.setRenderer( renderer ); 
       
        try {  
            ByteArrayOutputStream out = new ByteArrayOutputStream();  
            ChartUtilities.writeChartAsPNG(out, chart, 600, 1000);  
            //in  = new ByteArrayInputStream(out.toByteArray());  
            return out.toByteArray();
        } catch (Exception e) {  
            e.printStackTrace();  
        }   
        return null;  
    } 
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("xmreport", "xmreport");
		activeMenus.put("xmreport-list", "xmreport-list");
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");
		Menu currentMenu = (Menu) menus.get("xmreport");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
	}
}
