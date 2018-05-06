package com.kekeinfo.web.admin.controller.observerdata;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.PathVariable;
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
import com.kekeinfo.core.business.data.service.HoWellDataService;
import com.kekeinfo.core.business.data.service.OWellDataService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.model.PointEnumType;
import com.kekeinfo.core.business.image.model.Wellimages;
import com.kekeinfo.core.business.monitordata.model.HowellData;
import com.kekeinfo.core.business.observewell.model.Observewell;
import com.kekeinfo.core.business.observewell.service.ObservewellService;
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
import com.kekeinfo.web.utils.ImageFilePathUtils;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.TransExcelView;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;

@Controller
public class OWellDataController {
	private static final Logger LOGGER = LoggerFactory.getLogger(OWellDataController.class);
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
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/odata/list.html", method=RequestMethod.GET)
	public String display(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		String pID = request.getParameter("pid");
		Object users =request.getSession().getAttribute(Constants.ADMIN_USER);
		if(users!=null){
			setMenu(model,request);
			HowellData pData = new HowellData();
			String ptype =request.getParameter("ptype");
			User user = (User)users;
			String useragent =user.getuAgent();
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
					boolean hasRight=false;
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
					model.addAttribute("pName", owell.getName());
					model.addAttribute("pid", owell.getId());
					model.addAttribute("pData", pData);
					model.addAttribute("csite", csite);
					if(useragent!=null && StringUtils.isNotBlank(useragent) ){
						if(hasRight){
							//新增是否要打开
							model.addAttribute("ptype", ptype);
						}
						
						return "owell-edit";
					}
					request.setAttribute("activeFun", "owell");  //指定当前操作功能
					
					return "water-odatas";
				}catch (Exception e){
					LOGGER.debug(e.getMessage());
				}
				
			}
				return "csite-wlist";
		}else{
			if(!StringUtils.isBlank(pID)){
				try{
					model.addAttribute("welltype", "观测井");
					Observewell pwell = observewellService.getByIdWithCSite(Long.parseLong(pID));
					model.addAttribute("wellname", pwell.getName());
					ConstructionSite cs =cSiteService.getBypId(pwell.getcSite().getId());
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
					String str1=pwell.getrWater()==null?"":pwell.getrWater().toString();
					String str2=pwell.getrTemperature()==null?"":pwell.getrTemperature().toString();
					String str3=pwell.getWaterTemperature()==null?"":pwell.getWaterTemperature().toString();
					String str4=pwell.getWaterMeasurement()==null?"":pwell.getWaterMeasurement().toString();
					String str5=pwell.getWaterDwon()==null?"":pwell.getWaterDwon().toString();
					str.append("<span class=\"list-group-item \" >水位上限:"+str4+"</span>");
					str.append("<span class=\"list-group-item \" >水位下限:"+str5+"</span>");
					str.append("<span class=\"list-group-item \" >实时水位:"+str1+"</span>");
					str.append("<span class=\"list-group-item \" >水温阈值:"+str3+"</span>");
					str.append("<span class=\"list-group-item \" >实时水温:"+str2+"</span>");
					model.addAttribute("detail", str.toString());
					if(pwell.getPointInfo().getImages()!=null && pwell.getPointInfo().getImages().size()>0){
						List<String> imgs =new ArrayList<>();
						for(Wellimages im:pwell.getPointInfo().getImages()){
							imgs.add(request.getContextPath() + ImageFilePathUtils.buildFilePathByConentType(im.getJpeg(), FileContentType.OWELL_DIGITAL));
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
	@RequestMapping(value = "/water/odata/server_processing.shtml", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
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
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('EDIT-PROJECT')") 
	@RequestMapping(value="/water/odata/save.shtml", method=RequestMethod.POST, produces="application/json; charset=utf-8")
	public @ResponseBody String save(@ModelAttribute("csite") HowellData pData, Model model, HttpServletRequest request, HttpServletResponse response)  {
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
				Observewell owell = observewellService.getByIdWithCSite(pData.getoWell().getId());
				if(pData.getWater()!=null){
					if(pData.getWater().compareTo(owell.getWaterMeasurement())==1 || pData.getWater().compareTo(owell.getWaterDwon())==-1){
						status=1;
					}
				}
				if(pData.getTemperature()!=null){
					if(pData.getTemperature().compareTo(owell.getWaterTemperature())==1){
						status+=2;
					}
				}
				pData.setStatus(status);
				pData.setWaterThreshold(owell.getWaterMeasurement());
				pData.setWaterDwon(owell.getWaterDwon());
				pData.setTemperatureThreshold(owell.getWaterTemperature());
				pData.setmAuto(false);
				HashMap<Long, Set<AppUser>> umaps =(HashMap<Long, Set<AppUser>>)webCacheUtils.getFromCache(Constants.USER_WARNING);
				List<UnderWater> cs = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
				UnderWater un=null;
				for(UnderWater u:cs){
					if(u.getId().equals(owell.getcSite().getId())){
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
				
				hoWellDataService.saveOrUpdate(pData, owell,pname,auses);
				//处理自动开启事件，封井的时候不处理
				pnodeUtils.OpenWithDeep(owell.getId(), pData,PointEnumType.OBSERVE);
				//更新缓存
				pNodeUtils.setstatusByCid(owell.getcSite().getId());
				
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			}
			//
		} catch (ServiceException e) {
			LOGGER.error("Error while save howelldata ", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		return returnString;
	}
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('EDIT-PROJECT')") 
	@RequestMapping(value="/water/odata/savemap.shtml", method=RequestMethod.POST, produces="application/json; charset=utf-8")
	public @ResponseBody String savemap( Model model, HttpServletRequest request, HttpServletResponse response)  {
		AjaxResponse resp = new AjaxResponse();
		String temperature = request.getParameter("temperature");
		String water = request.getParameter("water");
		String pid = request.getParameter("pid");
		if((!StringUtils.isBlank(water) || !StringUtils.isBlank(temperature)) && StringUtils.isNotBlank(pid)){
			try {
				User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
				if(user!=null){
					HowellData pData = new HowellData(); 
					if(!StringUtils.isBlank(temperature)){
						pData.setTemperature(new BigDecimal(temperature));
					}
					if(!StringUtils.isBlank(water)){
						pData.setWater(new BigDecimal(water));
					}
					
					if(pData.getAuditSection().getDateCreated()==null){
						pData.getAuditSection().setDateCreated(new Date());
					}
					pData.getAuditSection().setDateModified(new Date());
					
					pData.getAuditSection().setModifiedBy(user.getFirstName());
					//增加阈值数据
					Observewell owell = observewellService.getByIdWithCSite(Long.parseLong(pid));
					pData.setoWell(owell);
					int status = 0;
					if(pData.getWater()!=null){
						if(pData.getWater().compareTo(owell.getWaterMeasurement())==1 || pData.getWater().compareTo(owell.getWaterDwon())==-1){
							status=1;
						}
					}
					if(pData.getTemperature()!=null){
						if(pData.getTemperature().compareTo(owell.getWaterTemperature())==1){
							status+=2;
						}
					}
					pData.setStatus(status);
					pData.setWaterThreshold(owell.getWaterMeasurement());
					pData.setWaterDwon(owell.getWaterDwon());
					pData.setTemperatureThreshold(owell.getWaterTemperature());
					pData.setmAuto(false);
					HashMap<Long, Set<AppUser>> umaps =(HashMap<Long, Set<AppUser>>)webCacheUtils.getFromCache(Constants.USER_WARNING);
					List<UnderWater> cs = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
					UnderWater un=null;
					for(UnderWater u:cs){
						if(u.getId().equals(owell.getcSite().getId())){
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
					
					hoWellDataService.saveOrUpdate(pData, owell,pname,auses);
					//更新缓存
					pNodeUtils.setstatusByCid(owell.getcSite().getId());
					//处理自动开启事件，封井的时候不处理
					pnodeUtils.OpenWithDeep(owell.getId(), pData,PointEnumType.OBSERVE);
					
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
	
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value="/water/odata/remove.shtml", method=RequestMethod.POST, produces="application/json;charset=utf-8")
	public @ResponseBody List<AjaxResponse> remove(@RequestParam String[] listId,HttpServletRequest request, Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId!=null&&listId.length>0) {
			Long csiteid =null;
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.parseLong(listId[i]);
				try {
					csiteid = hoWellDataService.deleteBydId(id); 
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
	@RequestMapping(value="/water/odata/importCols.shtml", method=RequestMethod.POST, produces="text/html;charset=UTF-8")
	public @ResponseBody String importCols(Model model, @RequestParam(required=false, value="uploadfile") CommonsMultipartFile uploadfile, HttpServletRequest request, HttpServletResponse response, Locale locale) {
		AjaxResponse resp = new AjaxResponse();
		String cols = request.getParameter("cols");
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		if(!StringUtils.isBlank(cols)){
			try {
				String pid=request.getParameter("pid");
				String uname=request.getParameter("uname");
				Observewell owell = observewellService.getByIdWithCSite(Long.parseLong(pid));
				
				HttpSession session = request.getSession();
				session.setAttribute("import", "0");
				String [] col= null;
				col= cols.split(",");
				List<String> returns = new ArrayList<String>();
				Workbook book=transExcelView.getBook(uploadfile.getInputStream());
				DecimalFormat df = new DecimalFormat("0.00");//格式化小数 
		        Sheet sheet = book.getSheetAt(0);
		    	int trLength = sheet.getLastRowNum();
		    	if(trLength>transExcelView.getImpMaxLine())trLength =transExcelView.getImpMaxLine();
		    	for(int i=0;i<=trLength;i++){
		    		Row row = sheet.getRow(i);
		    		try{
		    			//观测者和观测时间为空不导入
		    			if((StringUtils.isBlank(uname) && StringUtils.isBlank(transExcelView.getValue(row,col[3], true))) || StringUtils.isBlank(transExcelView.getValue(row,col[2], true))){
		    				returns.add(String.valueOf(i+1));
			    			float num= (float)i/trLength;  
			    			session.setAttribute("import", df.format(num));
		    				continue;
		    			}
		    			HowellData pdata = new HowellData();
		    			//水位
		    			try{
		    				pdata.setWater(new BigDecimal(transExcelView.getValue(row,col[0], true)));
		    			}catch (Exception e){
		    				
		    			}
		    			
		    			//水温
		    			try{
		    				pdata.setTemperature(new BigDecimal(transExcelView.getValue(row,col[1], true)));
		    			}catch(Exception e){
		    				
		    			}
		    			
		    			//观测者
		    			if(!StringUtils.isBlank(uname)){
		    				pdata.getAuditSection().setModifiedBy(uname);
		    			}else{
		    				pdata.getAuditSection().setModifiedBy(transExcelView.getValue(row,col[3], true));
		    			}
		    			Date date = transExcelView.getDateValue(row,col[2], true);
		    			if(date!=null){
		    				pdata.getAuditSection().setDateCreated(date);
		    			}else{
		    				returns.add(String.valueOf(i+1));
			    			float num= (float)i/trLength;  
			    			session.setAttribute("import", df.format(num));
			    			continue;
		    			}
		    			pdata.getAuditSection().setDateModified(new Date());
		    			pdata.setoWell(owell);
		    			
		    			//增加阈值数据
						int status=0;
						if(pdata.getWater().compareTo(owell.getWaterMeasurement())==1 || pdata.getWater().compareTo(owell.getWaterDwon())==-1){
							status=1;
						}
						if(pdata.getTemperature().compareTo(owell.getWaterTemperature())==1){
							status+=2;
						}
						pdata.setStatus(status);
						pdata.setWaterThreshold(owell.getWaterMeasurement());
						pdata.setWaterDwon(owell.getWaterDwon());
						pdata.setTemperatureThreshold(owell.getWaterTemperature());
						HashMap<Long, Set<AppUser>> umaps =(HashMap<Long, Set<AppUser>>)webCacheUtils.getFromCache(Constants.USER_WARNING);
						List<UnderWater> cs = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
						UnderWater un=null;
						for(UnderWater u:cs){
							if(u.getId().equals(owell.getcSite().getId())){
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
		    			hoWellDataService.saveOrUpdate(pdata, owell,pname,auses);
		    		}catch (Exception e){
		    			//e.printStackTrace();
		    			returns.add(String.valueOf(i+1));
		    			float num= (float)i/trLength;  
		    			session.setAttribute("import", df.format(num));
		    			continue;
		    		}
		    	}	
				//更新状态
		    	observewellService.saveStatus(owell);
		    	//更新缓存
				pNodeUtils.setstatusByCid(owell.getcSite().getId());
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
	@RequestMapping(value="/water/odata/exportCols.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=UTF-8")
	public @ResponseBody void  exportCols(Model model, @RequestParam(value="ebegindt", required=false) String begindt, @RequestParam(value="eendtm", required=false) String endtm, HttpServletRequest request, HttpServletResponse response, Locale locale) throws IOException {
		/**AjaxResponse resp = new AjaxResponse();
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);*/
			try {
				String pid=request.getParameter("eid");
				List<String> attributes = new ArrayList<String>();
				attributes.add("WATER");
				attributes.add("TEMPERATURE");
				attributes.add("date_format(DATE_CREATED, '%Y-%m-%d %H:%i:%s')");
				attributes.add("UPDT_ID");
				attributes.add("date_format(DATE_MODIFIED, '%Y-%m-%d %H:%i:%s')");
				attributes.add("WATERTHRESHOLD");
				attributes.add("WATERDOWN");
				attributes.add("TEMPERATURETHRESHOLD");
				attributes.add("STATUS");
				//导出截止日期，包含当天，因此日期+1
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(DateUtils.parseDate(endtm, "yyyy-MM-dd"));
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				endtm = DateFormatUtils.format(calendar, "yyyy-MM-dd");
				StringBuffer include_where = new StringBuffer().append(" where ( DATE_CREATED BETWEEN '").append(begindt).append("' AND '").append(endtm).append("' ) AND OWELL_ID = ").append(pid);
				List<Object[]> bbtype= observewellService.getBySql(attributes, "HOWELLDATA", include_where.toString());
				String[] title={"水位","水温","观测日期","观测者","修改日期","水位阈值上限","水位阈值下限","水温阈值","是否超限"};
				String sheet = request.getParameter("ename");
				String fileName ="Observewell";
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
	@RequestMapping(value = "/water/odata/{id}/qcodec.html", method = RequestMethod.GET)
	public String qcode(@PathVariable final long id,Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HowellData pData = new HowellData();
		try{
			Observewell owell = observewellService.getByIdWithCSite(id);
			//model.addAttribute("pwell", pwell);
			 List<Group> egroups = groupService.listGroupByPermissionName("EDIT-PROJECT",1);
			 boolean hasRight=false;
			 if(!request.isUserInRole("ADMIN") && !request.isUserInRole("SUPERADMIN") ){
		        	if(request.isUserInRole("EDIT-PROJECT")){
		        		hasRight=pnodeUtils.hasProjectRight(request, egroups, owell.getcSite().getProject());
		        	}
				}else{
					hasRight=true;
				}
			model.addAttribute("hasRight", hasRight);
			model.addAttribute("pName", owell.getName());
			model.addAttribute("pid", owell.getId());
			request.setAttribute("activePid", owell.getcSite().getId()); //指定项目
			request.setAttribute("activeZone", owell.getcSite().getProject().getZone().getName()); //指定项目
		}catch (Exception e){
			LOGGER.debug(e.getMessage());
		}
		model.addAttribute("pData", pData);
		return "owell-edit";
	}
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("csite", "csite");


		model.addAttribute("activeMenus",activeMenus);
		//
		
	}

}
