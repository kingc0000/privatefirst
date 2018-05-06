package com.kekeinfo.web.admin.controller.pumpdata;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
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

import com.kekeinfo.core.business.common.model.BasedataType;
import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.constructionsite.model.ConstructionSite;
import com.kekeinfo.core.business.content.model.FileContentType;
import com.kekeinfo.core.business.csite.service.CSiteService;
import com.kekeinfo.core.business.data.service.HpWellDataService;
import com.kekeinfo.core.business.data.service.PWellDataService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.model.PointEnumType;
import com.kekeinfo.core.business.image.model.Wellimages;
import com.kekeinfo.core.business.monitordata.model.HpwellData;
import com.kekeinfo.core.business.pumpwell.model.Pumpwell;
import com.kekeinfo.core.business.pumpwell.service.PumpwellService;
import com.kekeinfo.core.business.user.model.AppUser;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.DataTable;
import com.kekeinfo.web.entity.UnderWater;
import com.kekeinfo.web.event.SpringContextUtils;
import com.kekeinfo.web.utils.DataTableParameter;
import com.kekeinfo.web.utils.DateConverter;
import com.kekeinfo.web.utils.ImageFilePathUtils;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.TransExcelView;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;

@Controller
public class PWellDataController {
	private static final Logger LOGGER = LoggerFactory.getLogger(PWellDataController.class);
	@Autowired private PNodeUtils pnodeUtils;
	@Autowired PWellDataService pWellDataService;
	@Autowired HpWellDataService hpWellDataService;
	@Autowired PumpwellService pumpwellService;
	@Autowired GroupService groupService;
	@Autowired  TransExcelView transExcelView;
	@Autowired CSiteService cSiteService;
	@Autowired PNodeUtils pNodeUtils;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/pdata/list.html", method=RequestMethod.GET)
	public String display(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		String pID = request.getParameter("pid");
		Object users =request.getSession().getAttribute(Constants.ADMIN_USER);
		if(users!=null){
			setMenu(model,request);
			String ptype =request.getParameter("ptype");
			User user = (User)users;
			String useragent =user.getuAgent();
			
			boolean hasRight=false;
			if(!StringUtils.isBlank(pID)){
				try{
					Pumpwell pwell = pumpwellService.getByIdWithCSite(Long.parseLong(pID));
					List<UnderWater> cs = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
					UnderWater csite=null;
					for(UnderWater c:cs){
						if(c.getId().equals(pwell.getcSite().getId())){
							csite=c;
							break;
						}
					} 
					if(csite==null)csite=pnodeUtils.getByCid(pwell.getcSite().getId());
					model.addAttribute("csite", csite);
					 if(!request.isUserInRole("ADMIN") && !request.isUserInRole("SUPERADMIN") ){
				        	if(request.isUserInRole("EDIT-PROJECT")){
				        		List<Group> egroups = groupService.listGroupByPermissionName("EDIT-PROJECT",1);
				        		hasRight=pnodeUtils.hasProjectRight(request, egroups, csite);
				        	}
						}else{
							hasRight=true;
						}
					model.addAttribute("hasRight", hasRight);
					model.addAttribute("csite", csite);
					model.addAttribute("pName", pwell.getName());
					model.addAttribute("pid", pwell.getId());
					request.setAttribute("activeFun", "pwell");  //指定当前操作功能
					HpwellData pData = new HpwellData();
					model.addAttribute("pData", pData);
					if(useragent!=null && StringUtils.isNotBlank(useragent) ){
						if(hasRight){
							//新增是否要打开
							model.addAttribute("ptype", ptype);
						}
						
						return "pwell-edit";
					}
					
					return "water-pdatas";
				}catch (Exception e){
					LOGGER.debug(e.getMessage());
				}
				
			}
				return "csite-wlist";
		}else{
			if(!StringUtils.isBlank(pID)){
				try{
					model.addAttribute("welltype", "抽水井");
					Pumpwell pwell = pumpwellService.getByIdWithCSite(Long.parseLong(pID));
					ConstructionSite cs =cSiteService.getBypId(pwell.getcSite().getId());
					model.addAttribute("wellname", pwell.getName());
					model.addAttribute("project", cs.getProject());
					model.addAttribute("csite", cs);
					StringBuffer str=new StringBuffer();
					if(StringUtils.isNotBlank(pwell.getPointInfo().getPrecipitation())){
						String[] ps = pwell.getPointInfo().getPrecipitation().split(",");
						if(ps!=null && ps.length>0){
							str.append("<span class=\"list-group-item \" >降水目的层：");
							List<BasedataType> precipitations = (List<BasedataType>) SpringContextUtils.getServletContext().getAttribute(Constants.BD_PRECIPITATION);
							for(String s:ps){
								for(BasedataType bp:precipitations){
									if(bp.getValue().equalsIgnoreCase(s)){
										str.append(bp.getName()).append(",");
									}
								}
							}
							str.deleteCharAt(str.lastIndexOf(","));
							str.append("</span>");
						}
					}
					String str1=pwell.getrFlow()==null?"":pwell.getrFlow().toString();
					String str2=pwell.getrWater()==null?"":pwell.getrWater().toString();
					String str3=pwell.getFlow()==null?"":pwell.getFlow().toString();
					String str4=pwell.getWater()==null?"":pwell.getWater().toString();
					String str5=pwell.getWaterDwon()==null?"":pwell.getWaterDwon().toString();
					str.append("<span class=\"list-group-item \" >水位上限:"+str4+"</span>");
					str.append("<span class=\"list-group-item \" >水位下限:"+str5+"</span>");
					str.append("<span class=\"list-group-item \" >实时水位:"+str2+"</span>");
					str.append("<span class=\"list-group-item \" >流量阈值:"+str3+"</span>");
					str.append("<span class=\"list-group-item \" >实时流量:"+str1+"</span>");
					model.addAttribute("detail", str.toString());
					if(pwell.getPointInfo().getImages()!=null && pwell.getPointInfo().getImages().size()>0){
						List<String> imgs =new ArrayList<>();
						for(Wellimages im:pwell.getPointInfo().getImages()){
							imgs.add(request.getContextPath() + ImageFilePathUtils.buildFilePathByConentType(im.getJpeg(), FileContentType.PWELL_DIGITAL));
						}
						model.addAttribute("images", imgs);
					}
				}catch (Exception e){
					
				}
			}
			return "water/info";
		}
		
		
	}
	
	
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/pdata/server_processing.shtml", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
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
				attributes.add("pWell.id");
				List<String> fieldValues = new ArrayList<String>();
				fieldValues.add(String.valueOf(Long.parseLong(csiteID)));
				HashMap<String, String> orderby = new HashMap<String, String>();
				orderby.put("auditSection.dateCreated", "desc");
				Entitites<HpwellData> list  = hpWellDataService.getPageListByAttributes(attributes, fieldValues, Long.valueOf(dataTableParam.getiDisplayLength()).intValue(), 
						Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby);
				dt.setsEcho(dataTableParam.getsEcho()+1);
				dt.setiTotalDisplayRecords(list.getTotalCount());
		        dt.setiTotalRecords(list.getTotalCount());
		        //dt.setAaData(list.getEntites());
		        if(list.getEntites()!=null && list.getEntites().size()>0) {
					for(HpwellData pd : list.getEntites()) {
						Map<String, Object> entry = new HashMap<String, Object>();
						entry.put("id", pd.getId());
						entry.put("flow", pd.getFlow());
						entry.put("water", pd.getWater());
						entry.put("flowThreshold", pd.getFlowThreshold());
						entry.put("waterThreshold", pd.getWaterThreshold());
						entry.put("waterDown", pd.getWaterDown());
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
				LOGGER.error("Error while paging hpwelldata", e);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}
		}
		
		return null;
	}
	

	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/pdata/lastAccu.shtml", method =  RequestMethod.POST , produces = "application/json; charset=utf-8")
	public @ResponseBody String getLast(HttpServletRequest request,HttpServletResponse response) {
		String csiteID = request.getParameter("pid");
		if(!StringUtils.isBlank(csiteID)){
			Pumpwell pwell = pumpwellService.getById(Long.parseLong(csiteID));
			if(pwell!=null ){
				try {
					Map<String, Object> entry = new HashMap<String, Object>();
					if(pwell.getThisAccu()!=null){
						entry.put("last", pwell.getThisAccu());
					}
					
					//获取上次的时间,计算周期
					if(pwell.getLastDate()!=null){
						DateConverter dc = new DateConverter();
						entry.put("lastdate", dc.dateFormate(pwell.getLastDate()));
					}
					ObjectMapper mapper = new ObjectMapper();
					String json;
					json = mapper.writeValueAsString(entry);
					return json;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					LOGGER.error(e.getMessage());
					//e.printStackTrace();
				}
			}
		}
		
		return "";
	}
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('EDIT-PROJECT')") 
	@RequestMapping(value="/water/pdata/save.shtml", method=RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String save(@ModelAttribute("csite") HpwellData pData, Model model, HttpServletRequest request, HttpServletResponse response)  {
		AjaxResponse resp = new AjaxResponse();
		
		try {
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			if(user!=null){
				if(pData.getAuditSection().getDateCreated()==null){
					pData.getAuditSection().setDateCreated(new Date());
				}
				pData.getAuditSection().setDateModified(new Date());
				
				pData.getAuditSection().setModifiedBy(user.getFirstName());
				//增加阈值数据
				Pumpwell pwell = pumpwellService.getByIdWithCSite(pData.getpWell().getId());
				int status = 0;
				if(pData.getFlow()!=null){
					if( pData.getFlow().compareTo(pwell.getFlow())==1){
						status=1;
					}
				}
				
				if(pData.getWater()!=null ){
					if(pData.getWater().compareTo(pwell.getWater())==1 || pData.getWater().compareTo(pwell.getWaterDwon())==-1){
						status+=2;
					}
				}
				pData.setStatus(status);
				pData.setFlowThreshold(pwell.getFlow());
				pData.setWaterThreshold(pwell.getWater());
				pData.setWaterDown(pwell.getWaterDwon());
				pData.setmAuto(false);
				
				HashMap<Long, Set<AppUser>> umaps =(HashMap<Long, Set<AppUser>>)webCacheUtils.getFromCache(Constants.USER_WARNING);
				List<UnderWater> cs = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
				UnderWater un=null;
				for(UnderWater u:cs){
					if(u.getId().equals(pwell.getcSite().getId())){
						un=u;
						break;
					}
				}
				Set<AppUser> auses=null;
				String pname="";
				if(un!=null){
					auses=umaps.get(un.getPid());
					pname=un.getName();
				}
				
				hpWellDataService.saveOrUpdate(pData, pwell,pname,auses);
				//处理自动开启事件，封井的时候不处理
				pnodeUtils.OpenWithDeep(pwell.getId(), pData,PointEnumType.PUMP);
				
				//更新缓存
				pNodeUtils.setstatusByCid(pwell.getcSite().getId());
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			}
			//
		} catch (ServiceException e) {
			LOGGER.error("Error while save hpwelldata ", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		return returnString;
	}
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('EDIT-PROJECT')") 
	@RequestMapping(value="/water/pdata/savemap.shtml", method=RequestMethod.POST, produces="application/json; charset=utf-8")
	public @ResponseBody String savemap( Model model, HttpServletRequest request, HttpServletResponse response)  {
		AjaxResponse resp = new AjaxResponse();
		String flow = request.getParameter("flow");
		String water = request.getParameter("water");
		String pid = request.getParameter("pid");
		String lastAccu = request.getParameter("lastAccu");
		String thisAccu = request.getParameter("thisAccu");
		String accuPeriod = request.getParameter("accuPeriod");
		if((!StringUtils.isBlank(water) || !StringUtils.isBlank(flow)) && StringUtils.isNotBlank(pid)){
			try {
				User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
				if(user!=null){
					HpwellData pData = new HpwellData();
					if(!StringUtils.isBlank(flow)){
						pData.setFlow(new BigDecimal(flow));
					}
					if(!StringUtils.isBlank(water)){
						pData.setWater(new BigDecimal(water));
					}
					
					if(pData.getAuditSection().getDateCreated()==null){
						pData.getAuditSection().setDateCreated(new Date());
					}
					if(!StringUtils.isBlank(lastAccu)){
						pData.setLastAccu(new BigDecimal(lastAccu));
					}
					if(!StringUtils.isBlank(thisAccu)){
						pData.setThisAccu(new BigDecimal(thisAccu));
					}
					if(!StringUtils.isBlank(accuPeriod)){
						pData.setAccuPeriod(new BigDecimal(accuPeriod));;
					}
					pData.getAuditSection().setDateModified(new Date());
					
					pData.getAuditSection().setModifiedBy(user.getFirstName());
					//增加阈值数据
					Pumpwell pwell = pumpwellService.getByIdWithCSite(Long.parseLong(pid));
					pData.setpWell(pwell);
					int status = 0;
					if(pData.getFlow()!=null){
						if( pData.getFlow().compareTo(pwell.getFlow())==1){
							status=1;
						}
					}
					
					if(pData.getWater()!=null ){
						if(pData.getWater().compareTo(pwell.getWater())==1 || pData.getWater().compareTo(pwell.getWaterDwon())==-1){
							status+=2;
						}
					}
					pData.setStatus(status);
					pData.setFlowThreshold(pwell.getFlow());
					pData.setWaterThreshold(pwell.getWater());
					pData.setWaterDown(pwell.getWaterDwon());
					pData.setmAuto(false);
					
					pwell.setrFlow(pData.getFlow());
					pwell.setrWater(pData.getWater());
					if(pData.getThisAccu()!=null){
						pwell.setThisAccu(pData.getThisAccu());
						pwell.setLastAccu(pData.getLastAccu());
						pwell.setLastDate(pData.getAuditSection().getDateCreated());
					}
					
					HashMap<Long, Set<AppUser>> umaps =(HashMap<Long, Set<AppUser>>)webCacheUtils.getFromCache(Constants.USER_WARNING);
					List<UnderWater> cs = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
					UnderWater un=null;
					for(UnderWater u:cs){
						if(u.getId().equals(pwell.getcSite().getId())){
							un=u;
							break;
						}
					}
					Set<AppUser> auses=null;
					String pname="";
					if(un!=null){
						auses=umaps.get(un.getPid());
						pname=un.getName();
					}
					
					hpWellDataService.saveOrUpdate(pData, pwell,pname,auses);
					
					//更新缓存
					pNodeUtils.setstatusByCid(pwell.getcSite().getId());
					//处理自动开启事件，封井的时候不处理
					pnodeUtils.OpenWithDeep(pwell.getId(), pData,PointEnumType.PUMP);
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
				}
				//
			} catch (ServiceException e) {
				LOGGER.error("Error while save hpwelldata ", e);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorMessage(e);
			}
		}
		
		
		String returnString = resp.toJSONString();
		return returnString;
	}
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value="/water/pdata/importCols.shtml", method=RequestMethod.POST, produces="text/html;charset=UTF-8")
	public @ResponseBody String importCols(Model model, @RequestParam(required=false, value="uploadfile") CommonsMultipartFile uploadfile, HttpServletRequest request, HttpServletResponse response, Locale locale) {
		AjaxResponse resp = new AjaxResponse();
		String cols = request.getParameter("cols");
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		if(!StringUtils.isBlank(cols)){
			try {
				String pid=request.getParameter("pid");
				String uname=request.getParameter("uname");
				Pumpwell pwell = pumpwellService.getByIdWithCSite(Long.parseLong(pid));
				
				HttpSession session = request.getSession();
				session.setAttribute("import", "0");
				String [] col= null;
				col= cols.split(",");
				List<String> returns = new ArrayList<String>();
				Workbook book=transExcelView.getBook(uploadfile.getInputStream());
				DecimalFormat df = new DecimalFormat("0.00");//格式化小数 
				MathContext mc = new MathContext(6, RoundingMode.HALF_DOWN);
		        Sheet sheet = book.getSheetAt(0);
		    	int trLength = sheet.getLastRowNum();
		    	if(trLength>transExcelView.getImpMaxLine())trLength =transExcelView.getImpMaxLine();
		    	
		    	BigDecimal last=new BigDecimal(0);
		    	Date lastDate =null;
		    	//获取数据库中最近的一条记录
		    	if(pwell.getLastAccu()!=null){
		    		last=pwell.getLastAccu();
		    		lastDate=pwell.getLastDate();
		    	}
		    	DateConverter datec = new DateConverter();
		    	
		    	for(int i=0;i<=trLength;i++){
		    		Row row = sheet.getRow(i);
		    		try{
		    			//观测者和观测时间为空不导入
		    			if((StringUtils.isBlank(uname) && StringUtils.isBlank(transExcelView.getValue(row,col[5], true))) || 
		    					StringUtils.isBlank(transExcelView.getValue(row,col[4], true))){
		    				returns.add(String.valueOf(i+1));
			    			float num= (float)i/trLength;  
			    			session.setAttribute("import", df.format(num));
		    				continue;
		    			}
		    			HpwellData pdata = new HpwellData();
		    			//流量
		    			try{
		    				BigDecimal lastAccu =transExcelView.getBigValue(row,col[0], true);
			    			BigDecimal thisAccu=transExcelView.getBigValue(row,col[1], true);
			    			BigDecimal period=null;
			    			period= transExcelView.getBigValue(row,col[2], true);
			    			
			    			Date date = transExcelView.getDateValue(row,col[4], true);
			    			if(date!=null){
			    				pdata.getAuditSection().setDateCreated(date);
			    			}else{
			    				returns.add(String.valueOf(i+1));
				    			float num= (float)i/trLength;  
				    			session.setAttribute("import", df.format(num));
				    			continue;
			    			}
			    			
			    			//周期
			    			if(period==null && lastDate!=null){
			    				period=datec.sub(date,lastDate);
			    			}
			    			
			    			if(lastAccu==null) lastAccu=last;
			    			if(thisAccu!=null ){
			    				pdata.setLastAccu(lastAccu);
			    				pdata.setThisAccu(thisAccu);
			    				pdata.setAccuPeriod(period);
			    				lastAccu = thisAccu.subtract(lastAccu);
			    				if(period==null || period.compareTo(new BigDecimal(0))==0){
			    					pdata.setFlow(lastAccu);
			    				}else{
			    					lastAccu = lastAccu.divide(period,mc);
			    					pdata.setFlow(lastAccu);
			    				}
			    				
			    				last=thisAccu;
		    				}
		    			}catch (Exception e){
		    				
		    			}
		    			
		    			//水位
		    			try{
		    				pdata.setWater(transExcelView.getBigValue(row,col[3], true));
		    			}catch (Exception e){
		    				
		    			}
		    			
		    			//观测者
		    			if(!StringUtils.isBlank(uname)){
		    				pdata.getAuditSection().setModifiedBy(uname);
		    			}else{
		    				pdata.getAuditSection().setModifiedBy(transExcelView.getValue(row,col[5], true));
		    			}
		    			
		    			pdata.getAuditSection().setDateModified(new Date());
		    			pdata.setpWell(pwell);
		    			//增加阈值数据
						int status = 0;
						if(pdata.getFlow()!=null && pdata.getFlow().compareTo(pwell.getFlow())==1){
							status=1;
						}
						if(pdata.getWater()!=null && (pdata.getWater().compareTo(pwell.getWater())==1 || pdata.getWater().compareTo(pwell.getWaterDwon())==-1)){
							status+=2;
						}
						pdata.setStatus(status);
						pdata.setFlowThreshold(pwell.getFlow());
						pdata.setWaterThreshold(pwell.getWater());
						pdata.setWaterDown(pwell.getWaterDwon());
						HashMap<Long, Set<AppUser>> umaps =(HashMap<Long, Set<AppUser>>)webCacheUtils.getFromCache(Constants.USER_WARNING);
						List<UnderWater> cs = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
						UnderWater un=null;
						for(UnderWater u:cs){
							if(u.getId().equals(pwell.getcSite().getId())){
								un=u;
								break;
							}
						}
						Set<AppUser> auses=null;
						String pname="";
						if(un!=null){
							auses=umaps.get(un.getPid());
							pname=un.getName();
						}
		    			hpWellDataService.saveOrUpdate(pdata, pwell,pname,auses); //测点的最新状态，统一上传后再处理
		    		}catch (Exception e){
		    			e.printStackTrace();
		    			returns.add(String.valueOf(i+1));
		    			float num= (float)i/trLength;  
		    			session.setAttribute("import", df.format(num));
		    			continue;
		    		}
		    	}	
		    	//更新状态
		    	pumpwellService.saveStatus(pwell);
		    	//更新缓存
				pNodeUtils.setstatusByCid(pwell.getcSite().getId());
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
	@RequestMapping(value="/water/pdata/exportCols.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=UTF-8")
	public @ResponseBody void  exportCols(Model model, @RequestParam(value="ebegindt", required=false) String begindt, @RequestParam(value="eendtm", required=false) String endtm, HttpServletRequest request, HttpServletResponse response, Locale locale) throws IOException {
		/**AjaxResponse resp = new AjaxResponse();
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);*/
			try {
				String pid=request.getParameter("eid");
				List<String> attributes = new ArrayList<String>();
				attributes.add("FLOW");
				attributes.add("WATER");
				attributes.add("date_format(DATE_CREATED, '%Y-%m-%d %H:%i:%s')");
				attributes.add("UPDT_ID");
				attributes.add("date_format(DATE_MODIFIED, '%Y-%m-%d %H:%i:%s')");
				attributes.add("FLOWTHRESHOLD");
				attributes.add("WATERTHRESHOLD");
				attributes.add("WATERTDOWN");
				attributes.add("STATUS");
				//导出截止日期，包含当天，因此日期+1
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(DateUtils.parseDate(endtm, "yyyy-MM-dd"));
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				endtm = DateFormatUtils.format(calendar, "yyyy-MM-dd");
				StringBuffer include_where = new StringBuffer().append(" where ( DATE_CREATED BETWEEN '").append(begindt).append("' AND '").append(endtm).append("' ) AND PWELL_ID = ").append(pid);
				List<Object[]> bbtype= pumpwellService.getBySql(attributes, "HPWELLDATA", include_where.toString());
				String[] title={"流量","水位","观测日期","观测者","修改日期","流量阈值","水位阈值上限","水位阈值下限","是否超限"};
				String sheet = request.getParameter("ename");
				String fileName ="Pumpwell";
				if(StringUtils.isBlank(sheet)) {
					sheet="数据";
				}else {
					fileName+= sheet;
				}
				fileName = URLEncoder.encode(fileName, "UTF-8");
				response.addHeader("Content-Disposition", "attachment;filename=" + fileName+".xlsx");
				XSSFWorkbook wb =transExcelView.export(sheet, title, bbtype,null);
				
				//写文件
				OutputStream os = response.getOutputStream();
				wb.write(os);
				os.close();
				//resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
				//return wb.getBytes();
			} catch (Exception e) {
				LOGGER.debug("fail export");
				response.sendError(404, Constants.FILE_NOT_FOUND);
			}
		//return resp.toJSONString();
		
	}
	
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value="/water/pdata/remove.shtml", method=RequestMethod.POST, produces="application/json;charset=utf-8")
	public @ResponseBody List<AjaxResponse> remove(@RequestParam String[] listId,HttpServletRequest request, Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId!=null&&listId.length>0) {
			Long csiteid =null;
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.parseLong(listId[i]);
				try {
					csiteid =hpWellDataService.deleteBydId(id);
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
					resplist.add(resp);
				} catch (Exception e) {
					e.printStackTrace();
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
					resplist.add(resp);
				}
			}
			//更新缓存
			pNodeUtils.setstatusByCid(csiteid);
		}
		return resplist;
	}
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("csite", "csite");


		model.addAttribute("activeMenus",activeMenus);
		//
		
	}

}
