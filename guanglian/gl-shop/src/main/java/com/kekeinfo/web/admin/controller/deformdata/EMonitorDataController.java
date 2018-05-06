package com.kekeinfo.web.admin.controller.deformdata;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.constructionsite.model.ConstructionSite;
import com.kekeinfo.core.business.csite.service.CSiteService;
import com.kekeinfo.core.business.data.service.EMonitorDataService;
import com.kekeinfo.core.business.data.service.HeMonitorDataService;
import com.kekeinfo.core.business.deformmonitor.model.Deformmonitor;
import com.kekeinfo.core.business.deformmonitor.service.DeformmonitorService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.monitordata.model.HemonitorData;
import com.kekeinfo.core.business.user.model.AppUser;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.DataTable;
import com.kekeinfo.web.entity.UnderWater;
import com.kekeinfo.web.utils.DataTableParameter;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.TransExcelView;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;

@Controller
public class EMonitorDataController {
	private static final Logger LOGGER = LoggerFactory.getLogger(EMonitorDataController.class);
	
	@Autowired EMonitorDataService eMonitorDataService;
	@Autowired HeMonitorDataService heMonitorDataService;
	@Autowired DeformmonitorService deformmonitorService;
	@Autowired GroupService groupService;
	@Autowired  TransExcelView transExcelView;
	@Autowired CSiteService cSiteService;
	@Autowired private PNodeUtils pnodeUtils;
	@Autowired PNodeUtils pNodeUtils;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/edata/list.html", method=RequestMethod.GET)
	public String display(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		String pID = request.getParameter("pid");
		Object users =request.getSession().getAttribute(Constants.ADMIN_USER);
		if(users!=null){
			setMenu(model,request);
			HemonitorData pData = new HemonitorData();
			
			User user = (User)users;
			String useragent =user.getuAgent();
			boolean hasRight=false;
			String ptype =request.getParameter("ptype");
			if(!StringUtils.isBlank(pID)){
				try{
					Deformmonitor emointor = deformmonitorService.getByIdWithCSite(Long.parseLong(pID));
					List<UnderWater> cs = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
					UnderWater csite=null;
					for(UnderWater c:cs){
						if(c.getId().equals(emointor.getcSite().getId())){
							csite=c;
							break;
						}
					} 
					if(csite==null)csite=pnodeUtils.getByCid(emointor.getcSite().getId());
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
					
					model.addAttribute("pName", emointor.getName());
					model.addAttribute("pid", emointor.getId());
					request.setAttribute("activeFun", "dmonitor");  //指定当前操作功能
					model.addAttribute("pData", pData);
					if(useragent!=null && StringUtils.isNotBlank(useragent) ){
						if(hasRight){
							//新增是否要打开
							model.addAttribute("ptype", ptype);
						}
						
						return "ewell-edit";
					}
					return "water-emonitors";
				}catch (Exception e){
					LOGGER.debug(e.getMessage());
				}
				
			}
				return "csite-wlist";
		}else{
			if(!StringUtils.isBlank(pID)){
				try{
					model.addAttribute("welltype", "环境监测");
					Deformmonitor pwell = deformmonitorService.getByIdWithCSite(Long.parseLong(pID));
					model.addAttribute("wellname", pwell.getName());
					ConstructionSite cs =cSiteService.getBypId(pwell.getcSite().getId());
					model.addAttribute("project", cs.getProject());
					model.addAttribute("csite", pwell.getcSite());
					StringBuffer str=new StringBuffer();
					String str1=pwell.getrData()==null?"":pwell.getrData().toString();
					String str2 =pwell.getDeformData()==null?"":pwell.getDeformData().toString();
					str.append("<span class=\"list-group-item \" >数据阈值："+str2+"</span>");
					str.append("<span class=\"list-group-item \" >实时变形数据:"+str1+"</span>");
					model.addAttribute("detail", str.toString());
				}catch (Exception e){
					
				}
			}
			return "water/info";
		}
		
	}
	
	
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/edata/server_processing.shtml", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
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
				attributes.add("emonitor.id");
				List<String> fieldValues = new ArrayList<String>();
				fieldValues.add(String.valueOf(Long.parseLong(csiteID)));
				HashMap<String, String> orderby = new HashMap<String, String>();
				orderby.put("auditSection.dateCreated", "desc");
				Entitites<HemonitorData> list  = heMonitorDataService.getPageListByAttributes(attributes, fieldValues, Long.valueOf(dataTableParam.getiDisplayLength()).intValue(), 
						Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby);
				dt.setsEcho(dataTableParam.getsEcho()+1);
				dt.setiTotalDisplayRecords(list.getTotalCount());
		        dt.setiTotalRecords(list.getTotalCount());
		        //dt.setAaData(list.getEntites());
		        if(list.getEntites()!=null && list.getEntites().size()>0) {
					for(HemonitorData pd : list.getEntites()) {
						Map<String, Object> entry = new HashMap<String, Object>();
						entry.put("id", pd.getId());
						entry.put("data", pd.getData());
						entry.put("threshold", pd.getThreshold());
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
				LOGGER.error("Error while paging HemonitorData", e);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('EDIT-PROJECT')") 
	@RequestMapping(value="/water/edata/save.shtml", method=RequestMethod.POST, produces="application/json; charset=utf-8")
	public @ResponseBody String save(@ModelAttribute("csite") HemonitorData pData, Model model, HttpServletRequest request, HttpServletResponse response)  {
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
				Deformmonitor df = deformmonitorService.getByIdWithCSite(pData.getEmonitor().getId());
				int status=0;
				if(pData.getData().compareTo(df.getDeformData())==1){
					status=3;
				}
				pData.setStatus(status);
				pData.setThreshold(df.getDeformData());
				HashMap<Long, Set<AppUser>> umaps =(HashMap<Long, Set<AppUser>>)webCacheUtils.getFromCache(Constants.USER_WARNING);
				List<UnderWater> cs = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
				UnderWater un=null;
				for(UnderWater u:cs){
					if(u.getId().equals(df.getcSite().getId())){
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
				heMonitorDataService.saveOrUpdate(pData,df,pname,auses);
				//更新缓存
				pNodeUtils.setstatusByCid(df.getcSite().getId());
				
			}
			//
		} catch (ServiceException e) {
			LOGGER.error("Error while save HemonitorData ", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		return returnString;
	}
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('EDIT-PROJECT')") 
	@RequestMapping(value="/water/edata/savemap.shtml", method=RequestMethod.POST, produces="application/json; charset=utf-8")
	public @ResponseBody String savemap( Model model, HttpServletRequest request, HttpServletResponse response)  {
		AjaxResponse resp = new AjaxResponse();
		String data = request.getParameter("data");;
		String pid = request.getParameter("pid");
		if(!StringUtils.isBlank(data) && StringUtils.isNotBlank(pid)){
			try {
				User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
				if(user!=null){
					HemonitorData pData = new HemonitorData();
					pData.setData(new BigDecimal(data));
					if(pData.getAuditSection().getDateCreated()==null){
						pData.getAuditSection().setDateCreated(new Date());
					}
					pData.getAuditSection().setDateModified(new Date());
					
					pData.getAuditSection().setModifiedBy(user.getFirstName());
					//增加阈值数据
					Deformmonitor df = deformmonitorService.getByIdWithCSite(Long.parseLong(pid));
					pData.setEmonitor(df);
					int status = 0;
					if(pData.getData().compareTo(df.getDeformData())==1){
						status=3;
					}
					pData.setStatus(status);
					pData.setThreshold(df.getDeformData());
					HashMap<Long, Set<AppUser>> umaps =(HashMap<Long, Set<AppUser>>)webCacheUtils.getFromCache(Constants.USER_WARNING);
					List<UnderWater> cs = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
					UnderWater un=null;
					for(UnderWater u:cs){
						if(u.getId().equals(df.getcSite().getId())){
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
					heMonitorDataService.saveOrUpdate(pData,df,pname,auses);
					//更新缓存
					pNodeUtils.setstatusByCid(df.getcSite().getId());
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
	@RequestMapping(value="/water/edata/remove.shtml", method=RequestMethod.POST, produces="application/json;charset=utf-8")
	public @ResponseBody List<AjaxResponse> remove(@RequestParam String[] listId,HttpServletRequest request, Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId!=null&&listId.length>0) {
			Long csiteid =null;
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.parseLong(listId[i]);
				try {
					csiteid =heMonitorDataService.deleteBydId(id);
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
	@RequestMapping(value="/water/edata/importCols.shtml", method=RequestMethod.POST, produces="text/html;charset=UTF-8")
	public @ResponseBody String importCols(Model model, @RequestParam(required=false, value="uploadfile") CommonsMultipartFile uploadfile, HttpServletRequest request, HttpServletResponse response, Locale locale) {
		AjaxResponse resp = new AjaxResponse();
		String cols = request.getParameter("cols");
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		if(!StringUtils.isBlank(cols)){
			try {
				String pid=request.getParameter("pid");
				String uname=request.getParameter("uname");
				Deformmonitor dmonitor = deformmonitorService.getByIdWithCSite(Long.parseLong(pid));
				
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
		    			if((StringUtils.isBlank(uname) && StringUtils.isBlank(transExcelView.getValue(row,col[2], true))) || StringUtils.isBlank(transExcelView.getValue(row,col[1], true))){
		    				returns.add(String.valueOf(i+1));
			    			float num= (float)i/trLength;  
			    			session.setAttribute("import", df.format(num));
		    				continue;
		    			}
		    			HemonitorData pdata = new HemonitorData();
		    			//流量
		    			pdata.setData(transExcelView.getBigValue(row, col[0], true));
		    			//观测者
		    			if(!StringUtils.isBlank(uname)){
		    				pdata.getAuditSection().setModifiedBy(uname);
		    			}else{
		    				pdata.getAuditSection().setModifiedBy(transExcelView.getValue(row,col[2], true));
		    			}
		    			Date date = transExcelView.getDateValue(row,col[1], true);
		    			if(date!=null){
		    				pdata.getAuditSection().setDateCreated(date);
		    			}else{
		    				returns.add(String.valueOf(i+1));
			    			float num= (float)i/trLength;  
			    			session.setAttribute("import", df.format(num));
			    			continue;
		    			}
		    			pdata.getAuditSection().setDateModified(new Date());
		    			pdata.setEmonitor(dmonitor);
		    			
		    			//增加阈值数据
						int status=0;
						if(pdata.getData().compareTo(dmonitor.getDeformData())==1){
							status=3;
						}
						pdata.setStatus(status);
						pdata.setThreshold(dmonitor.getDeformData());
						HashMap<Long, Set<AppUser>> umaps =(HashMap<Long, Set<AppUser>>)webCacheUtils.getFromCache(Constants.USER_WARNING);
						List<UnderWater> cs = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
						UnderWater un=null;
						for(UnderWater u:cs){
							if(u.getId().equals(dmonitor.getcSite().getId())){
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
		    			heMonitorDataService.saveOrUpdate(pdata, dmonitor,pname,auses);
		    		}catch (Exception e){
		    			//e.printStackTrace();
		    			returns.add(String.valueOf(i+1));
		    			float num= (float)i/trLength;  
		    			session.setAttribute("import", df.format(num));
		    			continue;
		    		}
		    	}	
		    	//更新状态
    			deformmonitorService.saveStatus(dmonitor);
    			//更新缓存
				pNodeUtils.setstatusByCid(dmonitor.getcSite().getId());
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
	@RequestMapping(value="/water/edata/exportCols.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=UTF-8")
	public @ResponseBody void  exportCols(Model model, @RequestParam(value="ebegindt", required=false) String begindt, @RequestParam(value="eendtm", required=false) String endtm, HttpServletRequest request, HttpServletResponse response, Locale locale) throws IOException {
		/**AjaxResponse resp = new AjaxResponse();
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);*/
			try {
				String pid=request.getParameter("eid");
				List<String> attributes = new ArrayList<String>();
				attributes.add("DATA");
				attributes.add("date_format(DATE_CREATED, '%Y-%m-%d %H:%i:%s')");
				attributes.add("UPDT_ID");
				attributes.add("date_format(DATE_MODIFIED, '%Y-%m-%d %H:%i:%s')");
				attributes.add("THRESHOLD");
				attributes.add("STATUS");
				//导出截止日期，包含当天，因此日期+1
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(DateUtils.parseDate(endtm, "yyyy-MM-dd"));
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				endtm = DateFormatUtils.format(calendar, "yyyy-MM-dd");
				StringBuffer include_where = new StringBuffer().append(" where ( DATE_CREATED BETWEEN '").append(begindt).append("' AND '").append(endtm).append("' ) AND EMONITOR_ID = ").append(pid);
				List<Object[]> bbtype= deformmonitorService.getBySql(attributes, "HEMONITORDATA", include_where.toString());
				String[] title={"监测值","观测日期","观测者","修改日期","阈值","是否超限"};
				String sheet = request.getParameter("ename");
				String fileName ="Environment";
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
