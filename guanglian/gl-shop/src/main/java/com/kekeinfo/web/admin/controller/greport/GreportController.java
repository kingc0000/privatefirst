package com.kekeinfo.web.admin.controller.greport;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.codehaus.jackson.map.ObjectMapper;
import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
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
import com.kekeinfo.core.business.daily.model.GuardDaily;
import com.kekeinfo.core.business.daily.service.GuardDailyService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.greport.model.Greport;
import com.kekeinfo.core.business.greport.service.GreportService;
import com.kekeinfo.core.business.greport.service.RVerticalService;
import com.kekeinfo.core.business.guard.model.GPointEnumType;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.admin.entity.web.Menu;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.BaseEntity;
import com.kekeinfo.web.entity.DataTable;
import com.kekeinfo.web.entity.GuardEntity;
import com.kekeinfo.web.event.SpringContextUtils;
import com.kekeinfo.web.utils.CustomXWPFDocument;
import com.kekeinfo.web.utils.DataTableParameter;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;;

@Controller
public class GreportController {
	private static final Logger LOGGER = LoggerFactory.getLogger(GreportController.class);
	@Autowired
	GreportService greportService;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@Autowired
	private PNodeUtils pnodeUtils;
	@Autowired
	private GroupService groupService;
	@Autowired RVerticalService rVerticalService;
	@Autowired GuardDailyService guardDailyService;

	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/greport/list.html", method = RequestMethod.GET)
	public String listgreports(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setMenu(model, request);
		Greport greport = new Greport();
		model.addAttribute("greport", greport);
		String gid = request.getParameter("gid");
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
			request.setAttribute("activeCode", "greport"); // 指定项目
			model.addAttribute("hasRight", hasRight);
			model.addAttribute("gid", gid);
		return "admin-greport";
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/greport/server_processing.shtml", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = " application/json; charset=utf-8")
	public @ResponseBody String getGreport(@RequestParam String aoData, HttpServletRequest request,
			HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		AjaxResponse resp = new AjaxResponse();
		DataTableParameter dataTableParam = new DataTableParameter(aoData);
		DataTable<Greport> dt = new DataTable<Greport>();
		String gid = request.getParameter("gid");
		if(StringUtils.isNotBlank(gid)){
			try { // 指定根据什么条件进行模糊查询
				List<String> attributes = new ArrayList<String>();
				HashMap<String, String> orderby = new HashMap<String, String>();
				orderby.put("auditSection.dateModified", "desc");
				List<Object[]> where = new ArrayList<>();
				where.add(new Object[] { "guard.id", gid });
				Entitites<Greport> list = greportService.getPageListByAttributesLike(attributes,
						dataTableParam.getsSearch(), Long.valueOf(dataTableParam.getiDisplayLength()).intValue(),
						Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby, where, null, true);
				dt.setsEcho(dataTableParam.getsEcho() + 1);
				dt.setiTotalDisplayRecords(list.getTotalCount());
				dt.setiTotalRecords(list.getTotalCount());
				dt.setAaData(list.getEntites());
			} catch (Exception e) {
				LOGGER.error("Error while paging greports", e);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}
		}else{
			dt.setsEcho(0);
			dt.setiTotalDisplayRecords(0);
			dt.setiTotalRecords(0);
			dt.setAaData(null);
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		String json = mapper.writeValueAsString(dt);
		return json;
		
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/greport/save.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String saveGreport(@ModelAttribute("greport") Greport greport, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse resp = new AjaxResponse();
		try {
			GuardDaily gd =guardDailyService.byDate(greport.getThiser(),greport.getGuard().getId());
			if(gd==null){
				resp.setStatus(-4);
				resp.setStatusMessage("请先创建所选择日期的项目日志");
			}else{
				User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
				if(user!=null){
					greport.getAuditSection().setModifiedBy(user.getFirstName());
				}
				greport.setDialy(gd);
				greportService.createReport(greport);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			}
			
		} catch (ServiceException e) {
			LOGGER.error("Error while save Greport", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		String returnString = resp.toJSONString();
		return returnString;
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/greport/remove.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody List<AjaxResponse> deleteGreport(@RequestParam String[] listId, HttpServletRequest request,
			Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId != null && listId.length > 0) {
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.parseLong(listId[i]);
				try {
					Greport greport = greportService.getById(id);
					if (greport != null) {
						greportService.delete(greport);
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping("/water/greport/downloads/{fileName}.html")
	public @ResponseBody void downloadProduct(@PathVariable final String fileName, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Greport xm=greportService.getById(Long.parseLong(fileName));
		StringBuffer projects =new StringBuffer();
		Map<GPointEnumType,List> maps =new HashMap<>();
		for(GPointEnumType e:GPointEnumType.values()){
			List robs =rVerticalService.getByGid(xm.getId(),e);
			if(robs!=null && robs.size()>0){
				projects.append(e.getName()).append("、");
				maps.put(e, robs);
			}
		}
		
		String pName="";
		String mid = request.getParameter("gid");
		//项目名称
		if(StringUtils.isNotBlank(mid)){
			List<GuardEntity> listen = (List<GuardEntity>) webCacheUtils.getFromCache(Constants.PROEJCT_GUARD);
			for(GuardEntity m:listen){
				if(m.getId().equals(Long.parseLong(mid))){
					pName=m.getName();
					break;
				}
			}
		}
		String cpath =this.getClass().getResource("/").getPath().replace("/WEB-INF/classes", "")+"/resources/mmodel/gmodel.docx";
		XWPFDocument doc = new XWPFDocument(new FileInputStream(cpath)); 
		//XWPFTable table = doc.getTables().get(0); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日"); 
		String filen =pName+"-"+xm.getrNo()+"-监护";
		CustomXWPFDocument document= new CustomXWPFDocument();
		//添加标题  
        XWPFParagraph title = document.createParagraph();  
        //设置段落居中  
        title.setAlignment(ParagraphAlignment.CENTER);  
  
        XWPFRun run = title.createRun();  
        run.setText(pName);  
        run.setColor("000000");  
        run.setFontSize(16);  
		
        //副标题  
        title = document.createParagraph(); 
        title.setAlignment(ParagraphAlignment.CENTER);
        run = title.createRun();  
        run.setText("施工期间地铁结构安全技术监护报表");  
        run.setColor("000000");  
        run.setFontSize(16);  
      
        //换行  
        title = document.createParagraph();  
        run = title.createRun();  
        run.setText("\r");  
        
        //编号
        title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER); 
        run = title.createRun();  
        run.setText("（编号："+xm.getrNo()+"）");
        
        title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER); 
        if(projects.length()>0){
        	 projects =projects.deleteCharAt(projects.lastIndexOf("、"));
             run.setText("监测项目："+projects.toString());
        }else{
        	 run.setText("监测项目：");
        }
       
        
        //换行
        title = document.createParagraph();  
        run = title.createRun();  
        run.setText("\r\r\r\r\r\r\r\r\r");  
        
        //报表编制
        title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER); 
        run = title.createRun();  
        run.setText("报表编制："+xm.getAuditSection().getModifiedBy());
        
        title = document.createParagraph();  
        run = title.createRun();  
        run.setText("\r");  
        
        //报表审核
        title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER); 
        run = title.createRun();  
        run.setText("报表审核："+xm.getVerifier());
        
        //换行
        title = document.createParagraph();  
        run = title.createRun();  
        run.setText("\r\r\r\r\r\r\r\r\r\r");
        
        title = document.createParagraph();  
        title.setAlignment(ParagraphAlignment.CENTER); 
        run = title.createRun();  
        run.setText(sdf.format(xm.getThiser()));
        
        
        //分页//给这个段落添加一个分隔符即可。
        title = document.createParagraph();
        title.setPageBreak(true);
        int index =1;
	   if(maps.size()>0){
	        
			for(GPointEnumType e:GPointEnumType.values()){
				List list=maps.get(e);
				if(list!=null){
					title = document.createParagraph();  
			        title.setAlignment(ParagraphAlignment.CENTER); 
			        run = title.createRun();  
			        run.setText("（"+this.toChinese(String.valueOf(index))+"）、1地铁结构变形监测报表");
			        title = document.createParagraph();  
			        run = title.createRun();  
			        run.setText("\r");
			        title = document.createParagraph();  
			        title.setAlignment(ParagraphAlignment.CENTER); 
			        run = title.createRun();  
			        run.setText("测量项目："+e.getName()+"                                            编号："+xm.getrNo());
					
					Class catClass = Class.forName("com.kekeinfo.core.business.greport.model.R"+e.toString()+"Data");
					XWPFTable table = doc.getTables().get(index-1);
					createSimpleTableWithBdColor(document, list,catClass,table,index-1,e.getName());
					 //分页//给这个段落添加一个分隔符即可。
			        title = document.createParagraph();
			        title.setPageBreak(true);
					index++;
				}
			}
	   }
			//地铁结构病害调查报表
			GuardDaily gd =guardDailyService.byDate(xm.getThiser(),xm.getGuard().getId());
			if(gd!=null){
				title = document.createParagraph();
		        title.setAlignment(ParagraphAlignment.CENTER); 
		        run = title.createRun();  
		        run.setText("（"+this.toChinese(String.valueOf(index))+"）"+"地铁结构病害调查报表");
		        
		        title = document.createParagraph();
		        run = title.createRun();  
		        run.setText(gd.getWeiHuW());
		        //换行
		        title = document.createParagraph();  
		        run = title.createRun();  
		        run.setText("\r");
		        index++;
		        
		        title = document.createParagraph();
		        title.setAlignment(ParagraphAlignment.CENTER); 
		        run = title.createRun();  
		        run.setText("（"+this.toChinese(String.valueOf(index))+"）"+"现场监护意见");
		        
		        title = document.createParagraph();
		        run = title.createRun();  
		        run.setText(gd.getPointDesc());
			}
			
	        
			filen = URLEncoder.encode(filen, "UTF-8");
			response.addHeader("Content-Disposition", "attachment;filename=" + filen+".docx");
			//写文件
			OutputStream os = response.getOutputStream();
			document.write(os);
			os.close();
		
		
		
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
	
	
    
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("greport", "greport");
		activeMenus.put("greport-list", "greport-list");
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");
		Menu currentMenu = (Menu) menus.get("greport");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
	}
	
	
	//表格自定义边框 请忽略这么丑的颜色样式,主要说明可以自定义样式  
    @SuppressWarnings("rawtypes")
	public  void createSimpleTableWithBdColor(CustomXWPFDocument doc,List list,Class clazz,XWPFTable tb,int index,String name) throws Exception {  
       doc.createTable(list.size()+1,6);  
        //模板
       XWPFTableRow tmRow =tb.getRow(2);
       XWPFTableCell tmCell=tmRow.getCell(0);
       Method[] mess=clazz.getMethods();
        //上行线list
        List<String[]> slist =new ArrayList<>();
        //下行线list
        List<String[]> xlist =new ArrayList<>();
        for(Object ob:list){
        	String[] ss=new String[4];
        	for(Method me:mess){
        		if(me.getName().equalsIgnoreCase("getMarkNO")){
        			String mark =(String)me.invoke(ob);
        			ss[0]=mark;
        		}
        		if(me.getName().equalsIgnoreCase("getCurHeight")){
        			Object o=me.invoke(ob);
        			if(o!=null){
        				ss[1]=o.toString();
        			}else{
        				ss[1]="";
        			}
        		}
        		if(me.getName().equalsIgnoreCase("getSum")){
        			Object o=me.invoke(ob);
        			if(o!=null){
        				ss[2]=o.toString();
        			}else{
        				ss[2]="";
        			}
        		}
        		if(me.getName().equalsIgnoreCase("getLast")){
        			Object o=me.invoke(ob);
        			if(o!=null){
        				ss[3]=o.toString();
        			}else{
        				ss[3]="";
        			}
        		}
        	}
        	//上行线
			if(ss[0].startsWith("S")){
				slist.add(ss);
			}else if(ss[0].startsWith("X")){
				xlist.add(ss);
			}
        }
          
        int line=3;
        int len =slist.size();
        if(len<xlist.size()){
        	len=xlist.size();
        }
        slist =this.sort(slist);
        xlist =this.sort(xlist);
        for(int k=0;k<len;k++){
        	XWPFTableRow row =tb.insertNewTableRow(line);
        	
        	if(k<slist.size()){
        		String[] s=slist.get(k);
        		XWPFTableCell cell =row.createCell();
        		this.setCellText(tmCell, cell, s[0]);
        		cell =row.createCell();
        		this.setCellText(tmCell, cell, s[1]);
        		cell =row.createCell();
        		this.setCellText(tmCell, cell, s[2]);
        	}else{
        		for(int j=0;j<3;j++){
        			XWPFTableCell cell =row.createCell();
        			this.setCellText(tmCell, cell, "");
        		}
        	}
        	if(k<xlist.size()){
        		String[] s=xlist.get(k);
        		XWPFTableCell cell =row.createCell();
        		this.setCellText(tmCell, cell, s[0]);
        		cell =row.createCell();
        		this.setCellText(tmCell, cell, s[1]);
        		cell =row.createCell();
        		this.setCellText(tmCell, cell, s[2]);
        	}else{
        		for(int j=0;j<3;j++){
        			XWPFTableCell cell =row.createCell();
        			this.setCellText(tmCell, cell, "");
        		}
        	}
        	line++;
        }
        doc.setTable(index, tb);
        XWPFParagraph title = doc.createParagraph();  
        XWPFRun run = title.createRun();  
        run.setText("\r");
        //画图
        title = doc.createParagraph(); 
        title.setAlignment(ParagraphAlignment.CENTER);
        run = title.createRun();  
        run.setText("（"+this.toChinese(String.valueOf(index+1))+"）、2"+"累计曲线图（上、下行）");
        //数据集合
        DefaultCategoryDataset sdataset = new DefaultCategoryDataset();
        // 各曲线名称
        String series1 = "上次累计量";
        String series2 = "本次累计量";
        for(String[] ss:slist){
        	String type1 = ss[0];
        	type1=type1.substring(0, 1)+type1.substring(4);
        	sdataset.addValue(new BigDecimal(ss[3]), series1, type1);
        	sdataset.addValue(new BigDecimal(ss[2]), series2, type1);
        }
        ByteArrayInputStream  in =this.getLineChartImage(sdataset, name+"上行累计曲线图", "点号", "累计量/mm");
        XWPFParagraph paragraph = doc.createParagraph();  
        doc.addPictureData(in,XWPFDocument.PICTURE_TYPE_JPEG);
        doc.createPicture(paragraph,doc.getAllPictures().size()-1, 800, 600,"    ");
        //下行
        //数据集合
        DefaultCategoryDataset xdataset = new DefaultCategoryDataset();
        // 各曲线名称
        String series3 = "上次累计量";
        String series4 = "本次累计量";
        for(String[] ss:xlist){
        	String type1 = ss[0];
        	type1=type1.substring(0, 1)+type1.substring(4);
        	xdataset.addValue(new BigDecimal(ss[3]), series3, type1);
        	xdataset.addValue(new BigDecimal(ss[2]), series4, type1);
        }
        ByteArrayInputStream  in1 =this.getLineChartImage(xdataset, name+"下行累计曲线图", "点号", "累计量/mm");
        XWPFParagraph paragraph1 = doc.createParagraph();  
        doc.addPictureData(in1,XWPFDocument.PICTURE_TYPE_JPEG);
        doc.createPicture(paragraph1,doc.getAllPictures().size()-1, 800, 600,"    ");
        
        
    }  
      
    // 排序
    private List<String[]> sort(List<String[]> list){
    	//List<String[]> nlist =new ArrayList<>();
    	for(int i=0;i<list.size()-1;i++){
            for(int j=0;j<list.size()-i-1;j++){//比较两个整数
                if(this.getInt(list.get(j)[0])>this.getInt(list.get(j+1)[0])){
                    /*交换*/
                    String[] temp=list.get(j);
                    list.set(j, list.get(j+1));
                    list.set(j+1, temp);
                }
            }
        }
    	return	list;	
    }
    //S-CJ
    private int getInt(String s){
    	try{
    		String ss =s.substring(4);
        	return Integer.parseInt(ss);
    	}catch (Exception e){
    		return 0;
    	}
    	
    }
    
    private  void setCellText(XWPFTableCell tmpCell,XWPFTableCell cell,String text) throws Exception{
    	CTTc cttc2 = tmpCell.getCTTc();
		CTTcPr ctPr2=cttc2.getTcPr();
		CTTc cttc = cell.getCTTc();
		CTTcPr ctPr = cttc.addNewTcPr();
		if(ctPr2.getTcBorders()!=null){
			ctPr.setTcBorders(ctPr2.getTcBorders());
		}
		
		XWPFParagraph cellP=cell.getParagraphs().get(0);
		
		XWPFRun cellR = cellP.createRun();
		//设置字体信息
		cellR.setFontSize(11);
		cellR.setText(text);
    }
      
    @SuppressWarnings("deprecation")
	private  ByteArrayInputStream getLineChartImage(DefaultCategoryDataset dataset,String tile,String tbottom,String tleft) {  
        ByteArrayInputStream in = null;  
        
        StandardChartTheme mChartTheme = new StandardChartTheme("CN");  
        mChartTheme.setLargeFont(new Font("SansSerif", Font.PLAIN, 10));  
        mChartTheme.setExtraLargeFont(new Font("SansSerif", Font.PLAIN, 7));  
        mChartTheme.setRegularFont(new Font("SansSerif", Font.PLAIN, 7));  
        ChartFactory.setChartTheme(mChartTheme);          
        //XYSeriesCollection mCollection = GetCollection(); 
        JFreeChart chart = ChartFactory.createLineChart(tile,tbottom,tleft,dataset, PlotOrientation.VERTICAL, true, false, false);   
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setRangeGridlinePaint(Color.BLACK);//纵坐标格线颜色
        plot.setDomainGridlinesVisible(true);//显示横坐标格线
        plot.setBackgroundAlpha(0.3f); //设置背景透明度
        plot.setBackgroundPaint(ChartColor.WHITE);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlineStroke(new BasicStroke());
        plot.setRangeGridlineStroke(new BasicStroke());
        LineAndShapeRenderer renderer = (LineAndShapeRenderer)plot.getRenderer();
        DecimalFormat decimalformat1 = new DecimalFormat("##.##");//数据点显示数据值的格式
        renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", decimalformat1));
                                          //上面这句是设置数据项标签的生成器
        renderer.setItemLabelsVisible(true);//设置项标签显示
        renderer.setBaseItemLabelsVisible(true);//基本项标签显示
                 //上面这几句就决定了数据点按照设定的格式显示数据值  
        renderer.setShapesFilled(Boolean.TRUE);//在数据点显示实心的小图标
        renderer.setShapesVisible(true);//设置显示小图标
        NumberAxis rangeAxis = (NumberAxis)plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(true);
        rangeAxis.setUpperMargin(0.20);
        /*rangeAxis.setLabelAngle(Math.PI / 2.0); */
        try {  
            ByteArrayOutputStream out = new ByteArrayOutputStream();  
            ChartUtilities.writeChartAsPNG(out, chart, 400, 300);  
            in  = new ByteArrayInputStream(out.toByteArray());  
        } catch (Exception e) {  
            e.printStackTrace();  
        }   
        return in;  
    }  
   
}
