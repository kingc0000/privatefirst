package com.kekeinfo.web.admin.controller.inverteddata;

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
import com.kekeinfo.core.business.data.service.HiWellDataService;
import com.kekeinfo.core.business.data.service.IWellDataService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.model.PointEnumType;
import com.kekeinfo.core.business.image.model.Wellimages;
import com.kekeinfo.core.business.invertedwell.model.Invertedwell;
import com.kekeinfo.core.business.invertedwell.service.InvertedwellService;
import com.kekeinfo.core.business.monitordata.model.HiwellData;
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
public class IWellDataController {
	private static final Logger LOGGER = LoggerFactory.getLogger(IWellDataController.class);
	@Autowired private PNodeUtils pnodeUtils;
	@Autowired IWellDataService iWellDataService;
	@Autowired HiWellDataService hiWellDataService;
	@Autowired InvertedwellService invertedwellService;
	@Autowired GroupService groupService;
	@Autowired  TransExcelView transExcelView;
	@Autowired CSiteService cSiteService;
	@Autowired PNodeUtils pNodeUtils;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/idata/list.html", method=RequestMethod.GET)
	public String display(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		String pID = request.getParameter("pid");
		Object users =request.getSession().getAttribute(Constants.ADMIN_USER);
		if(users!=null){
			setMenu(model,request);
			HiwellData pData = new HiwellData();
			String ptype =request.getParameter("ptype");
			User user = (User)users;
			String useragent =user.getuAgent();
			if(!StringUtils.isBlank(pID)){
				try{
					Invertedwell iwell = invertedwellService.getByIdWithCSite(Long.parseLong(pID));
					List<UnderWater> cs = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
					UnderWater csite =null;
					for(UnderWater c:cs){
						if(c.getId().equals(iwell.getcSite().getId())){
							csite=c;
							break;
						}
					}
					if(csite==null)csite=pnodeUtils.getByCid(iwell.getcSite().getId());
					model.addAttribute("csite", csite);
					 
					 boolean hasRight=false;
					 if(!request.isUserInRole("ADMIN") && !request.isUserInRole("SUPERADMIN") ){
				        	if(request.isUserInRole("EDIT-PROJECT")){
				        		List<Group> egroups = groupService.listGroupByPermissionName("EDIT-PROJECT",1);
				        		hasRight=pnodeUtils.hasProjectRight(request, egroups, csite);
				        	}
						}else{
							hasRight=true;
						}
					model.addAttribute("hasRight", hasRight);
					model.addAttribute("pName", iwell.getName());
					model.addAttribute("pid", iwell.getId());
					model.addAttribute("pData", pData);
					model.addAttribute("csite", csite);
					request.setAttribute("activeFun", "iwell");  //指定当前操作功能
					if(useragent!=null && StringUtils.isNotBlank(useragent) ){
						if(hasRight){
							//新增是否要打开
							model.addAttribute("ptype", ptype);
						}
						
						return "iwell-edit";
					}
					return "water-idatas";
				}catch (Exception e){
					LOGGER.debug(e.getMessage());
				}
				
			}
				return "csite-wlist";
		}else{
			if(!StringUtils.isBlank(pID)){
				try{
					model.addAttribute("welltype", "回灌井");
					Invertedwell pwell = invertedwellService.getByIdWithCSite(Long.parseLong(pID));
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
					String str2=pwell.getrPressure()==null?"":pwell.getrPressure().toString();
					String str3=pwell.getFlow()==null?"":pwell.getFlow().toString();
					String str4=pwell.getPressure()==null?"":pwell.getPressure().toString();
					str.append("<span class=\"list-group-item \" >流量:阈值"+str3+"</span>");
					str.append("<span class=\"list-group-item \" >实时流量:"+str1+"</span>");
					str.append("<span class=\"list-group-item \" >压力阈值:"+str4+"</span>");
					str.append("<span class=\"list-group-item \" >实时压力:"+str2+"</span>");
					model.addAttribute("detail", str.toString());
					if(pwell.getPointInfo().getImages()!=null && pwell.getPointInfo().getImages().size()>0){
						List<String> imgs =new ArrayList<>();
						for(Wellimages im:pwell.getPointInfo().getImages()){
							imgs.add(request.getContextPath() + ImageFilePathUtils.buildFilePathByConentType(im.getJpeg(), FileContentType.IWELL_DIGITAL));
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
	@RequestMapping(value = "/water/idata/server_processing.shtml", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getList(@RequestParam String aoData,HttpServletRequest request,
			HttpServletResponse response) {
		AjaxResponse resp = new AjaxResponse();
		String csiteID = request.getParameter("pid");
		if(!StringUtils.isBlank(csiteID)){
			
			DataTableParameter dataTableParam = new DataTableParameter(aoData);
			
			DataTable<Map<String, Object>> dt = new DataTable<Map<String, Object>>();
			try {
				
				//指定项目ID进行查询
				List<String> attributes = new ArrayList<String>();
				attributes.add("iWell.id");
				List<String> fieldValues = new ArrayList<String>();
				fieldValues.add(String.valueOf(Long.parseLong(csiteID)));
				HashMap<String, String> orderby = new HashMap<String, String>();
				orderby.put("auditSection.dateCreated", "desc");
				Entitites<HiwellData> list  = hiWellDataService.getPageListByAttributes(attributes, fieldValues, Long.valueOf(dataTableParam.getiDisplayLength()).intValue(), 
						Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby);
				dt.setsEcho(dataTableParam.getsEcho()+1);
				dt.setiTotalDisplayRecords(list.getTotalCount());
		        dt.setiTotalRecords(list.getTotalCount());
		        //dt.setAaData(list.getEntites());
		        if(list.getEntites()!=null && list.getEntites().size()>0) {
					for(HiwellData pd : list.getEntites()) {
						Map<String, Object> entry = new HashMap<String, Object>();
						entry.put("id", pd.getId());
						entry.put("flow", pd.getFlow());
						entry.put("pressure", pd.getPressure());
						entry.put("flowThreshold", pd.getFlowThreshold());
						entry.put("pressureThreshold", pd.getPressureThreshold());
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
				LOGGER.error("Error while paging HiwellData", e);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				return resp.toJSONString();
			}
		}
		
		return resp.toJSONString();
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/idata/lastAccu.shtml", method =  RequestMethod.POST , produces = "application/json; charset=utf-8")
	public @ResponseBody String getLast(HttpServletRequest request,HttpServletResponse response) {
		String csiteID = request.getParameter("pid");
		if(!StringUtils.isBlank(csiteID)){
			Invertedwell iwell = invertedwellService.getById(Long.parseLong(csiteID));
			if(iwell!=null && iwell.getThisAccu()!=null){
				try {
					Map<String, Object> entry = new HashMap<String, Object>();
					if(iwell.getThisAccu()!=null){
						entry.put("last", iwell.getThisAccu());
					}
					
					//获取上次的时间,计算周期
					if(iwell.getLastDate()!=null){
						DateConverter dc = new DateConverter();
						entry.put("lastdate", dc.dateFormate(iwell.getLastDate()));
						//LOGGER.error("iwell date:"+iwell.getLastDate().toString());
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
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('EDIT-PROJECT')") 
	@RequestMapping(value="/water/idata/save.shtml", method=RequestMethod.POST, produces="application/json; charset=utf-8")
	public @ResponseBody String save(@ModelAttribute("csite") HiwellData pData, Model model, HttpServletRequest request, HttpServletResponse response)  {
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
				int status=0;
				Invertedwell iwell = invertedwellService.getByIdWithCSite(pData.getiWell().getId());
				if(pData.getFlow()!=null){
					if(pData.getFlow().compareTo(iwell.getFlow())==1){
						status=1;
					}
				}
				if(pData.getPressure()!=null){
					if(pData.getPressure().compareTo(iwell.getPressure())==1){
						status+=2;
					}
				}
				pData.setStatus(status);
				pData.setFlowThreshold(iwell.getFlow());
				pData.setPressureThreshold(iwell.getPressure());
				pData.setmAuto(false);
				
				HashMap<Long, Set<AppUser>> umaps =(HashMap<Long, Set<AppUser>>)webCacheUtils.getFromCache(Constants.USER_WARNING);
				List<UnderWater> cs = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
				UnderWater un=null;
				for(UnderWater u:cs){
					if(u.getId().equals(iwell.getcSite().getId())){
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
				
				hiWellDataService.saveOrUpdate(pData,iwell,pname,auses);
				//处理自动开启事件，封井的时候不处理
				pnodeUtils.OpenWithDeep(iwell.getId(), pData,PointEnumType.INVERTED);
				//更新缓存
				pNodeUtils.setstatusByCid(iwell.getcSite().getId());
				
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			}
			//
		} catch (ServiceException e) {
			LOGGER.error("Error while save HiwellData ", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		return returnString;
	}
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('EDIT-PROJECT')") 
	@RequestMapping(value="/water/idata/savemap.shtml", method=RequestMethod.POST, produces="application/json; charset=utf-8")
	public @ResponseBody String savemap( Model model, HttpServletRequest request, HttpServletResponse response)  {
		AjaxResponse resp = new AjaxResponse();
		String flow = request.getParameter("flow");
		String lastAccu = request.getParameter("lastAccu");
		String thisAccu = request.getParameter("thisAccu");
		String accuPeriod = request.getParameter("accuPeriod");
		String pressure = request.getParameter("pressure");
		String pid = request.getParameter("pid");
		if((!StringUtils.isBlank(pressure) || !StringUtils.isBlank(flow)) && StringUtils.isNotBlank(pid)){
			try {
				User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
				if(user!=null){
					HiwellData pData = new HiwellData();
					if(!StringUtils.isBlank(flow)){
						pData.setFlow(new BigDecimal(flow));
					}
					if(!StringUtils.isBlank(pressure)){
						pData.setPressure(new BigDecimal(pressure));
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
					if(pData.getAuditSection().getDateCreated()==null){
						pData.getAuditSection().setDateCreated(new Date());
					}
					pData.getAuditSection().setDateModified(new Date());
					
					pData.getAuditSection().setModifiedBy(user.getFirstName());
					//增加阈值数据
					int status=0;
					Invertedwell iwell = invertedwellService.getByIdWithCSite(Long.parseLong(pid));
					pData.setiWell(iwell);
					if(pData.getFlow()!=null){
						if(pData.getFlow().compareTo(iwell.getFlow())==1){
							status=1;
						}
					}
					if(pData.getPressure()!=null){
						if(pData.getPressure().compareTo(iwell.getPressure())==1){
							status+=2;
						}
					}
					
					pData.setStatus(status);
					pData.setFlowThreshold(iwell.getFlow());
					pData.setPressureThreshold(iwell.getPressure());
					pData.setmAuto(false);

					HashMap<Long, Set<AppUser>> umaps =(HashMap<Long, Set<AppUser>>)webCacheUtils.getFromCache(Constants.USER_WARNING);
					List<UnderWater> cs = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
					UnderWater un=null;
					for(UnderWater u:cs){
						if(u.getId().equals(iwell.getcSite().getId())){
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
					
					hiWellDataService.saveOrUpdate(pData,iwell,pname,auses);
					//处理自动开启事件，封井的时候不处理
					pnodeUtils.OpenWithDeep(iwell.getId(), pData,PointEnumType.INVERTED);
					
						resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
					//更新缓存
					pNodeUtils.setstatusByCid(iwell.getcSite().getId());
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
	
	
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value="/water/idata/remove.shtml", method=RequestMethod.POST, produces="application/json;charset=utf-8")
	public @ResponseBody List<AjaxResponse> remove(@RequestParam String[] listId,HttpServletRequest request, Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId!=null&&listId.length>0) {
			Long csiteid =null;
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.parseLong(listId[i]);
				try {
					csiteid=hiWellDataService.deleteBydId(id);
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
	

	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value="/water/idata/importCols.shtml", method=RequestMethod.POST, produces="text/html;charset=UTF-8")
	public @ResponseBody String importCols(Model model, @RequestParam(required=false, value="uploadfile") CommonsMultipartFile uploadfile, HttpServletRequest request, HttpServletResponse response, Locale locale) {
		AjaxResponse resp = new AjaxResponse();
		String cols = request.getParameter("cols");
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		if(!StringUtils.isBlank(cols)){
			try {
				String pid=request.getParameter("pid");
				String uname=request.getParameter("uname");
				Invertedwell iwell = invertedwellService.getByIdWithCSite(Long.parseLong(pid));
				
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
		    	BigDecimal last=new BigDecimal(0);
		    	Date lastDate =null;
		    	//获取数据库中最近的一条记录
		    	if(iwell.getLastAccu()!=null){
		    		last=iwell.getLastAccu();
		    		lastDate=iwell.getLastDate();
		    	}
		    	DateConverter datec = new DateConverter();
		    	if(trLength>transExcelView.getImpMaxLine())trLength =transExcelView.getImpMaxLine();
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
		    			HiwellData pdata = new HiwellData();
		    			//流量
		    			try{
		    				BigDecimal lastAccu =transExcelView.getBigValue(row,col[0], true);
			    			BigDecimal thisAccu=transExcelView.getBigValue(row,col[1], true);
			    			BigDecimal period =transExcelView.getBigValue(row,col[2], true);
			    			
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
		    			try {
		    				//水位
			    			pdata.setPressure(transExcelView.getBigValue(row,col[3], true));
		    			}catch (Exception e){
		    				
		    			}
		    			
		    			//观测者
		    			if(!StringUtils.isBlank(uname)){
		    				pdata.getAuditSection().setModifiedBy(uname);
		    			}else{
		    				pdata.getAuditSection().setModifiedBy(transExcelView.getValue(row,col[5], true));
		    			}
		    			
		    			pdata.getAuditSection().setDateModified(new Date());
		    			pdata.setiWell(iwell);
		    			
		    			//增加阈值数据
						int status=0;
						if(pdata.getFlow()!=null && pdata.getFlow().compareTo(iwell.getFlow())==1){
							status=1;
						}
						if(pdata.getPressure()!=null && pdata.getPressure().compareTo(iwell.getPressure())==1){
							status+=2;
						}
						pdata.setStatus(status);
						pdata.setFlowThreshold(iwell.getFlow());
						pdata.setPressureThreshold(iwell.getPressure());
						
						HashMap<Long, Set<AppUser>> umaps =(HashMap<Long, Set<AppUser>>)webCacheUtils.getFromCache(Constants.USER_WARNING);
						List<UnderWater> cs = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
						UnderWater un=null;
						for(UnderWater u:cs){
							if(u.getId().equals(iwell.getcSite().getId())){
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
		    			hiWellDataService.saveOrUpdate(pdata, iwell,pname,auses);
		    		}catch (Exception e){
		    			//e.printStackTrace();
		    			returns.add(String.valueOf(i+1));
		    			float num= (float)i/trLength;  
		    			session.setAttribute("import", df.format(num));
		    			continue;
		    		}
		    	}	
				//更新状态
		    	invertedwellService.saveStatus(iwell);
		    	//更新缓存
				pNodeUtils.setstatusByCid(iwell.getcSite().getId());
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
			
			}
		}
		
		
		return resp.toJSONString();
	}
	
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value="/water/idata/exportCols.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=UTF-8")
	public @ResponseBody void  exportCols(Model model, @RequestParam(value="ebegindt", required=false) String begindt, @RequestParam(value="eendtm", required=false) String endtm, HttpServletRequest request, HttpServletResponse response, Locale locale) throws IOException {
		/**AjaxResponse resp = new AjaxResponse();
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);*/
			try {
				String pid=request.getParameter("eid");
				List<String> attributes = new ArrayList<String>();
				attributes.add("FLOW");
				attributes.add("PRESSURE");
				attributes.add("date_format(DATE_CREATED, '%Y-%m-%d %H:%i:%s')");
				attributes.add("UPDT_ID");
				attributes.add("date_format(DATE_MODIFIED, '%Y-%m-%d %H:%i:%s')");
				attributes.add("FLOWTHRESHOLD");
				attributes.add("PRESSURETHRESHOLD");
				attributes.add("STATUS");
				//导出截止日期，包含当天，因此日期+1
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(DateUtils.parseDate(endtm, "yyyy-MM-dd"));
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				endtm = DateFormatUtils.format(calendar, "yyyy-MM-dd");
				StringBuffer include_where = new StringBuffer().append(" where ( DATE_CREATED BETWEEN '").append(begindt).append("' AND '").append(endtm).append("' ) AND IWELL_ID = ").append(pid);
				List<Object[]> bbtype= invertedwellService.getBySql(attributes, "HIWELLDATA", include_where.toString());
				String[] title={"流量","井内水位","观测日期","观测者","修改日期","流量阈值","井内水位阈值","是否超限"};
				String sheet = request.getParameter("ename");
				String fileName ="Invertedwell";
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
	
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("csite", "csite");


		model.addAttribute("activeMenus",activeMenus);
		//
		
	}

}
